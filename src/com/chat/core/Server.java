package com.chat.core;

import java.net.*;
import java.net.SocketTimeoutException;

public class Server {
    private ServerSocket serverSocket;
    private final int port = 187;
    public Server() {
        new Connection().start();
    }
    private class Connection extends Thread {
        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(port);
                System.out.println("Server up and running");
                while(true) {
                    try{
                        Socket server = serverSocket.accept();
                        System.out.println("Just connected to " + server.getRemoteSocketAddress());
                        server.close();
                    }catch(SocketTimeoutException s) {

                    }
                }
            } catch(Exception e) {

            }
        }
    }
}
