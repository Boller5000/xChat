package com.chat.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.*;
import java.util.ArrayList;

public class Server {
    private ServerSocket serverSocket;
    private final int port = 187;
    private ArrayList<Socket> connections = new ArrayList<>();
    public Server() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server up and running on Port: " + port);
            while (true) {
                try {
                    Socket server = serverSocket.accept();
                    connections.add(server);
                    new Connection(server).start();
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        } catch(Exception e) {

        }
    }
    private class Connection extends Thread {
        Socket client;
        public Connection(Socket c) {
            client = c;
        }
        @Override
        public void run() {
            System.out.println("Just connected to " + client.getRemoteSocketAddress() + " as client");
            while(true) {
                    try{

                        DataInputStream in = new DataInputStream(client.getInputStream());
                        String message = "["+ client.getRemoteSocketAddress() + "]: " + in.readUTF();

                        for(Socket s : connections) {
                            DataOutputStream out = new DataOutputStream(s.getOutputStream());
                            out.writeUTF(message);
                        }
                    }catch(Exception e) {
                        continue;
                    }
                }
        }
    }
}
