package com.chat.core;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

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
            panel.setBackground(new Color(146,94,185));
            panel.setSize(500,500);
            panel.setLayout(null);
            JTextField text = new JTextField("",25);
            EmptyBorder eBord = new EmptyBorder(2,10,2,10);
            text.setForeground(new Color(46,155,151));
            text.setBorder(eBord);
            text.setBounds(100,375,300,50);
            panel.add(text);
            JLabel label = new JLabel("xChat - Dein Messenger!");
            label.setFont(new Font("Verdana", Font.BOLD, 16));
            label.setForeground(Color.white);
            label.setBounds(135,20,230,20);
            panel.add(label);
            JTextArea text2 = new JTextArea();
            text2.setForeground(new Color(46,155,151));
            text2.setBorder(eBord);
            text2.setEditable(false);
            JScrollPane scroll = new JScrollPane(text2);
            scroll.setBounds(50,75,400,250);
            panel.add(scroll);

            xChat.add(panel);
            xChat.setVisible(true);
            text.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                        System.out.println("check2");
                        try {
                            String s1 = text.getText();
                            text.setText("");
                            if (s1.equals("/exit")) {
                                System.exit(0);
                            } else if(s1.equals("/clean")) {
                                text2.setText("");
                                text2.setText("Chat cleaned!");
                                return;
                            }
                            ausgang.writeUTF(s1);
                        }catch(Exception epsilon){}
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {

                }
            });

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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
