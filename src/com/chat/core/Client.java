package com.chat.core;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;
import javax.swing.JFrame;

public class Client {
    public Client() {
        try {
            JFrame ui = new JFrame("xChat");
            ui.setSize(500, 500);
            ui.setVisible(true);

            JLabel test = new JLabel("king",JLabel.CENTER);
            ui.add(test);
            test.setVerticalAlignment(JLabel.TOP);
            test.setHorizontalTextPosition(JLabel.LEFT);

            JTextField text = new JTextField("text", JTextField.CENTER);
            ui.add(text);
            text.setHorizontalAlignment(JTextField.CENTER);

            Socket client = new Socket("217.81.193.203", 187);
            System.out.println("Connected to: " + client.getRemoteSocketAddress());

            //Eingang festlegen
            BufferedReader eingangUser = new BufferedReader(new InputStreamReader(System.in));

            // Eingang & Ausgang von Daten festlegen
            DataOutputStream ausgang = new DataOutputStream(client.getOutputStream());
            DataInputStream eingang = new DataInputStream(client.getInputStream());

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
