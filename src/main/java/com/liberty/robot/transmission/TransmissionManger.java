package com.liberty.robot.transmission;

import com.liberty.robot.common.ConnectionProperties;
import com.liberty.robot.helpers.JsonHelper;
import com.liberty.robot.messages.GenericMessage;
import com.liberty.robot.messages.KeyPressedMessage;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * Created by Dmytro_Kovalskyi on 13.05.2015.
 */
public class TransmissionManger implements Runnable {
    private Consumer<KeyPressedMessage> keyConsumer;
    private Handler handler;
   // private String address = "10.24.9.41";
    private String address = "127.0.0.1";

    public TransmissionManger(Consumer<KeyPressedMessage> keyConsumer) {
        this.keyConsumer = keyConsumer;
    }

    public TransmissionManger(String ip, Consumer<KeyPressedMessage> keyConsumer) {
        address = ip;
        this.keyConsumer = keyConsumer;
    }


    public void send(GenericMessage message) {
        try {
            if(handler != null) {
                handler.send(JsonHelper.toJson(message));
            } else {
                System.err.println("[TransmissionManger] Handler is null");
            }
        } catch(Exception e) {
            System.err.println("[TransmissionManger] send error : " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            InetAddress ipAddress = InetAddress.getByName(address);
            System.out.println("Connecting to " + address + " and port " + ConnectionProperties.MESSAGE_PORT);
            Socket socket = new Socket(ipAddress, ConnectionProperties.MESSAGE_PORT);
            handler = new Handler(socket);
        } catch(Exception e) {
            System.err.println("[TransmissionManger] error connecting to server " + e.getMessage());
        }
    }

    private class Handler {
        private Socket socket;
        private DataInputStream in;
        private BufferedWriter writer;

        public Handler(Socket socket) {
            try {
                this.socket = socket;
                InputStream sin = socket.getInputStream();
                OutputStream sout = socket.getOutputStream();

                in = new DataInputStream(sin);
                writer = new BufferedWriter(new OutputStreamWriter(sout));
                new Thread(this::read).start();
            } catch(Exception e) {
                System.err.println("[TransmissionManger] error : " + e.getMessage());
            }
        }

        private void read() {
            try {
                String input;
                System.out.println("Wait for messages");
                while((input = in.readLine()) != null) {
                    System.out.println("Received : " + input);
                    incomingMessage(JsonHelper.toEntity(input, KeyPressedMessage.class));
                }
            } catch(Throwable t) {
                System.err.println("Error : " + t.getMessage());
            }
        }

        private void incomingMessage(KeyPressedMessage message) {
            keyConsumer.accept(message);
        }

        public void send(String msg) {
            try {
                System.out.println("Send : " + msg);
                writer.write(msg);
                writer.flush();
            } catch(IOException e) {
                System.err.println("[TransmissionManger] error : " + e.getMessage());
            }
        }
    }

}
