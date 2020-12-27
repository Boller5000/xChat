package com.chat.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class Server {
    private ServerSocket serverSocket;
    //Standart Port für xChat
    private final int port = 187;
    private final String broadcast_string = "[BROADCAST]";
    //Liste mit allen verbundenen clients
    private ArrayList<Connection> connections = new ArrayList<>();
    public Server() {
        try {
            //Initialisierung
            serverSocket = new ServerSocket(port);
            System.out.println("Server up and running on Port: " + port);
            /*
            der while loop wartet auf neue eingehende verbindungen
            gibt es eine neue verbindung wird diese als connection gespeichert
            und der connection list hinzugefügt
             */
            while (true) {
                try {
                    Socket server = serverSocket.accept();
                    Connection conn = new Connection(server);
                    connections.add(conn);
                    conn.start();
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
        String clientName = "Leer";
        String socketAdress;
        public DataInputStream in;
        public DataOutputStream out;
        public Connection(Socket c) {
            try {
                client = c;
                in = new DataInputStream(client.getInputStream());
                out = new DataOutputStream(client.getOutputStream());
                socketAdress = client.getRemoteSocketAddress().toString();
                clientName = socketAdress;
            } catch(Exception e){}

        }
        @Override
        public void run() {
          broadcast("Just connected to " + client.getRemoteSocketAddress() + " as client");
          while(clientName == socketAdress){
              try {
                  sendMessage(this, "Bitte gib deinen Name ein");
                  String input = in.readUTF();
                  if (input.length() <= 3) {
                      continue;
                  }
                  clientName = input;
                  break;
              } catch(Exception e){}
          }
            while(true) {
                    try{
                        String message = "[" + clientName + "]: " + in.readUTF();

                        broadcast(message);
                    } catch(SocketException se) {
                        break;
                    }catch(Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                }
        }

    }

    /**
     *
     * @param message
     * broadcasted die Naricht an alle clients, serverweit
     */
    private void broadcast(String message) {
        System.out.println(broadcast_string + message);
        try {


            for (Connection s : connections) {
                s.out.writeUTF(message);

            }
        } catch(IOException e) {
            System.err.println("IOException while Broadcasting");
        }
    }
    private void sendMessage(Connection client,String message) {
        System.out.println("[->" + client.clientName + "]: " + message);
        try {
            client.out.writeUTF(message);
        } catch(IOException e) {
            System.err.println("IOException while sendingMessage");
        }
    }
}
