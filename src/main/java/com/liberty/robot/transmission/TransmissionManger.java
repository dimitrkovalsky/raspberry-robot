package com.liberty.robot.transmission;

import com.liberty.robot.common.ConnectionProperties;
import com.liberty.robot.helpers.EventListener;
import com.liberty.robot.helpers.JsonHelper;
import com.liberty.robot.messages.GenericRequest;
import utils.EventBus;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.function.Consumer;

import static utils.LoggingUtil.*;

/**
 * Created by Dmytro_Kovalskyi on 13.05.2015.
 */
public class TransmissionManger implements Runnable, EventListener {
    private Consumer<GenericRequest> messageConsumer;
    private Handler handler;
    // private String address = "10.24.9.41";
    private String address = "127.0.0.1";

    public TransmissionManger(Consumer<GenericRequest> messageConsumer) {
        this.messageConsumer = messageConsumer;
        EventBus.subscribe(getClass().getName(), this);
    }

    public TransmissionManger(String ip, Consumer<GenericRequest> messageConsumer) {
        address = ip;
        this.messageConsumer = messageConsumer;
        EventBus.subscribe(getClass().getName(), this);
    }

    public void send(GenericRequest message) {
        try {
            if (handler != null) {
                handler.send(JsonHelper.toJson(message));
            }
            else {
                localError(this, "[TransmissionManger] Handler is null");
            }
        }
        catch (Exception e) {
            localError(this, "[TransmissionManger] send error : " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            InetAddress ipAddress = InetAddress.getByName(address);
            localInfo(this, "Connecting to " + address + " and port " + ConnectionProperties.MESSAGE_PORT);
            Socket socket = new Socket(ipAddress, ConnectionProperties.MESSAGE_PORT);
            handler = new Handler(socket);
            info("RASPBERRY connected to server");
        }
        catch (Exception e) {
            localError(this, " error connecting to server " + e.getMessage());
        }
    }

    @Override
    public void onMessage(GenericRequest message) {
        send(message);
    }

    private class Handler {
        private Socket socket;
        private DataInputStream in;
        private PrintWriter writer = null;

        public Handler(Socket socket) {
            try {
                this.socket = socket;
                InputStream sin = socket.getInputStream();
                OutputStream sout = socket.getOutputStream();

                in = new DataInputStream(sin);
                writer = new PrintWriter(sout);
                new Thread(this::read).start();
            }
            catch (Exception e) {
                localError(this, " error : " + e.getMessage());
            }
        }

        private void read() {
            try {
                String input;
                System.out.println("Wait for messages");
                while ((input = in.readLine()) != null) {
                    info(this, "Received : " + input);
                    GenericRequest message = JsonHelper.toEntity(input, GenericRequest.class);
                    if (message != null)
                        incomingMessage(message);
                    else
                        localError(this, "Incoming message is incorrect");
                }
            }
            catch (Throwable t) {
                localError(this, "Error : " + t.getMessage());  // TODO: switch to autonomous mode
            }
        }

        private void incomingMessage(GenericRequest message) {
            messageConsumer.accept(message);
        }

        public void send(String msg) {
            try {
                System.out.println("Send : " + msg);
                writer.println(msg);
                writer.flush();
            }
            catch (Exception e) {
                System.err.println("[TransmissionManger] error : " + e.getMessage());
            }
        }
    }

}
