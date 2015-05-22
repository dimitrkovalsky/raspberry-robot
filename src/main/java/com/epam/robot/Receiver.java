package com.epam.robot;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Dmytro_Kovalskyi on 13.05.2015.
 */
public class Receiver implements Runnable {
    private static final int PORT = 5555;
    private Handler handler;
    private String address = "10.24.9.41";

    public Receiver() {

    }

    public Receiver(String ip) {
        address = ip;
    }

    @Override
    public void run() {

        try {

            InetAddress ipAddress = InetAddress.getByName(address);
            System.out.println("Connecting to " + address + " and port " + PORT);
            Socket socket = new Socket(ipAddress, PORT);
            handler = new Handler(socket);
        } catch(Exception e) {
            System.err.println("[Receiver] error connecting to server " + e.getMessage());
        }
    }

    private class Handler {
        private Socket socket;
        private DataInputStream in;
        DataOutputStream out;

        public Handler(Socket socket) {
            try {

                this.socket = socket;
                InputStream sin = socket.getInputStream();
                OutputStream sout = socket.getOutputStream();

                in = new DataInputStream(sin);
                out = new DataOutputStream(sout);
                new Thread(this::read).start();
            } catch(Exception e) {
                System.err.println("[Receiver] error : " + e.getMessage());
            }
        }

        private void read() {
            try {
                String input;
                System.out.println("Wait for messages");
                while((input = in.readLine()) != null) {
                    System.out.println("Received : " + input);
                }
            } catch(Throwable t) {
                System.err.println("Error : " + t.getMessage());
            }
        }
    }

}
