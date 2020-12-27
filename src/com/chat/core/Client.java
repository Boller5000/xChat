package com.chat.core;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;

public class Client {
    public Client() {
        try {
            Socket client = new Socket("localhost", 187);
            System.out.println("Connected to: " + client.getRemoteSocketAddress());

            //Eingang festlegen
            BufferedReader eingangUser = new BufferedReader(new InputStreamReader(System.in));

            // Eingang & Ausgang von Daten festlegen
            DataOutputStream ausgang = new DataOutputStream(client.getOutputStream());
            DataInputStream eingang = new DataInputStream(client.getInputStream());
            new Thread(()->{
                while(true) {
                    try {
                        String result = eingang.readUTF();
                        System.out.println(result);

                    } catch (Exception e) {
                    }
                }
            }).start();
            //Zeile einlesen
            while(true) {
            System.out.println("Bitte was eingeben.");
                String s1 = eingangUser.readLine();
                ausgang.writeUTF(s1);

                //Antwort vom Server einlesen
               // String result = eingang.readUTF();
                //Beenden vom Programm
                /*if(result == "logout"){
                 System.out.println("Für beenden drücke [x]")
                 char Answer;
                 Scanner keyboard = new Scanner(System.in);
                 Answer = keyboard.next().charAt(0);

                 if(Answer == "x"){
                     System.exit(0);
                 }
                }*/
               // System.out.println(result);
            }
            //client.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
