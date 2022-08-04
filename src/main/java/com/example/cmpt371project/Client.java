package com.example.cmpt371project;

import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.Arrays;

public class Client implements Runnable
{
    private String getIP;
    private final int PORT = 7070;
    public GameBoard game = new GameBoard();
    public Client(String getIP){
        this.getIP = getIP;
    }
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(); // establish socket
            // prepare data to be sent
            String Message = "Join";
            byte[] Buffer_out = Message.getBytes();

            InetAddress address = InetAddress.getByName(getIP); //Change to Input from string field
            DatagramPacket packet_out = new DatagramPacket(Buffer_out, Buffer_out.length, address, PORT);
            // send data
            System.out.println("Sending to " + InetAddress.getLocalHost());
            socket.send(packet_out);
            // receive response
            byte[] Buffer_in = new byte[60000];
            DatagramPacket packet_in = new DatagramPacket(Buffer_in, Buffer_in.length);


            //NEW ###Herb: Client waits to receive acknowledgment from server to conform connection,
            // which lets HelloApplication stop EventHandler from creating new client ports on enter
            socket.receive(packet_in);
            String Response = new String(packet_in.getData()).trim();
            if(Response.equals("Connected")){
                System.out.println("Client is connected");
                HelloApplication.setClientConnect(true);
            }

            //NEW ###Herb: After being connected, Client waits for cards

            if(HelloApplication.getClientConnect()){
                socket.receive(packet_in);
                Response = new String(packet_in.getData()).trim();
                String[] data = Response.split(",");
                String[] gameboard = Arrays.copyOfRange(data, 1, data.length);
//                for (int i = 0; i < data.length; i++){
//                    System.out.println("data:" +data[i]);
//                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        game.clientStart(new Stage(), gameboard, address, PORT, Integer.parseInt(data[0]));

                    }
                });
                while (true){
                    socket.receive(packet_in);
                    Response = new String(packet_in.getData()).trim();
                    System.out.println(Response);
                }
            }
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
