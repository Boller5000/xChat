package com.chat.core;

import java.awt.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;
import javax.swing.*;


public class Client {
    public Client() {
        try {
            Socket client = new Socket("217.81.193.203", 187);
            System.out.println("Connected to: " + client.getRemoteSocketAddress());

            //Eingang festlegen
            BufferedReader eingangUser = new BufferedReader(new InputStreamReader(System.in));

            // Eingang & Ausgang von Daten festlegen
            DataOutputStream ausgang = new DataOutputStream(client.getOutputStream());
            DataInputStream eingang = new DataInputStream(client.getInputStream());

            JFrame xChat = new JFrame("xChat");
            xChat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            xChat.setSize(500, 500);
            xChat.setLayout(new BorderLayout());

            JPanel panel = new JPanel();

            JTextField text = new JTextField(eingang.readUTF(),1);
            panel.add(text, BorderLayout.CENTER);
            xChat.add(panel);

            xChat.setVisible(true);
            // parallelen Thread aufrufen und gucken ob es eine Nachricht gibt
            new Thread(() -> {
                while (true) {
                    try {
                        String result = eingang.readUTF();
                        System.out.println(result);
                    } catch (Exception e) {
                    }
                }
            }).start();
            //Zeile einlesen
            while (true) {
                System.out.println("Nachricht eingeben:");
                String s1 = eingangUser.readLine();
                if (s1.equals("/exit")) {
                    System.exit(0);
                }
                ausgang.writeUTF(s1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
