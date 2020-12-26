package com.chat.core;

import java.net.*;

public class Client {
    public Client() {
        try {
            Socket client = new Socket("localhost", 187);
            System.out.println("Connected to: " + client.getRemoteSocketAddress());
            client.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
