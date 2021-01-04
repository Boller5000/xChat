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

            JPanel panel = new JPanel();
            panel.setSize(500,500);
            panel.setLayout(null);
            JTextField text = new JTextField("Nachricht eingeben",25);
            text.setBounds(100,400,300,50);
            panel.add(text);
            JLabel label = new JLabel("Funktioniert es?");
            label.setBounds(200,10,100,10);
            panel.add(label);
            JTextArea text2 = new JTextArea();
            text2.setEditable(false);
            JScrollPane scroll = new JScrollPane(text2);
            scroll.setBounds(100,150,300,150);
            panel.add(scroll);

            //kleiner change

            xChat.add(panel);
            xChat.setVisible(true);
            // parallelen Thread aufrufen und gucken ob es eine Nachricht gibt
            new Thread(() -> {
                while (true) {
                    try {
                        String result = eingang.readUTF();
                        SwingUtilities.invokeLater(new Runnable(){
                            public void run() {
                                text2.append(result + "\n");
                            }
                        });
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
