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
            //Beginn des Threads für die connection
          broadcast("Just connected to " + client.getRemoteSocketAddress() + " as client");
          while(clientName == socketAdress){
              try {
                      sendMessage(this, "Bitte gib deinen Namen ein");
                      String input = in.readUTF();
                      if (input.length() < 3) {
                          sendMessage(this, "Dein Name ist zu kurz/lang oder ist schon vergeben!");
                          continue;
                      }
                      clientName = input;
                      broadcast(clientName + " has joined xChat");
                      break;

              } catch(Exception e){
                  //Bei einem fehler wird der client gekickt
                  try {
                      client.close();
                      connections.remove(this);
                      serverBroadcast("Da konnte jemand seinen Namen nicht eingeben - Unbekannter disconnected");
                  } catch(Exception er){}
                  break;
              }
          }
          //Der Chat wird nur betreten wenn der client auch noch drinne ist, sollte dies nicht der falls sein, wird er aus der connections liste entfernt
          if(connections.contains(this)) {
              while (true) {
                  try {
                      String message = in.readUTF();
                      String[] messageA = message.split(" ");
                      switch (messageA[0]){
                          case "/msg": {
                              message = message.replace(messageA[0],"");
                              message = message.replace(messageA[1],"");
                              sendMessage(getConnectionByClientName(messageA[1]), "[flüstern von " + clientName + "]: " + message);
                              continue;
                          }
                          case "/name" : sendMessage(this,this.clientName);continue;
                          case "/help": sendMessage(this,"Command List: help,msg,name,ping");continue;
                          case "/ping" : sendMessage(this,"pong");continue;


                          default: {
                              message = "[" + clientName + "]: " + message.replaceAll(":\\D","☻").replaceAll(":\\)","☺");
                              broadcast(message);

                          }
                      }//


                  }catch(ArrayIndexOutOfBoundsException aobe) {
                      try {
                          sendServerMessage(this, "fehlerhafte Nachricht");
                      } catch(Exception e){}
                  } catch (Exception se) {
                      try {
                          client.close();
                          connections.remove(this);
                          serverBroadcast(clientName + " hat den Chat verlassen");
                      } catch (Exception e) {}
                      break;
                  }
              }
          }
        }

    }

    /**
     *
     * @param message
     * broadcasted die Nachricht an alle clients, serverweit
     */
    private void broadcast(String message) {
        System.out.println(broadcast_string + message);


            for (Connection s : connections) {
                try {
                    s.out.writeUTF(message);
                }catch(IOException e) {
                        System.err.println("IOException while Broadcasting(Connections lenght: " + connections.size() + ")");
                }

        }
    }
    private void sendMessage(Connection client,String message) throws IOException {
            System.out.println("[->" + client.clientName + "]: " + message);
                client.out.writeUTF(message);

    }

    public void serverBroadcast(String message) {
        broadcast("[Server]: " + message);
    }
    public void sendServerMessage(Connection client,String message) throws IOException{
        sendMessage(client,"[Server]: " + message);
    }

    public Connection getConnectionByClientName(String name) throws Exception{
        for(Connection c : connections) {
            if(c.clientName.contains(name) ) {
                return c;
            }
        }
        throw new Exception();
    }
}