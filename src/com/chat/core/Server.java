package com.chat.core;

import java.net.ServerSocket;

public class Server {
    private ServerSocket socket;
    private final int port = 187;
    public Server() {
        try {
            socket = new ServerSocket(port);
        } catch (Exception e) {

        }
    }
}
