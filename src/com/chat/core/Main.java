package com.chat.core;

public class Main {
    public static void main(String args[]) {
        //new Thread(()->new Server()).start();
        new Thread(()->new Client()).start();
        //new Client();
    }
}
