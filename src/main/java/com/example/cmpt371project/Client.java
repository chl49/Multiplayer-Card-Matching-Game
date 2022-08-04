package com.example.cmpt371project;

import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.*; import java.net.*;
import java.util.Arrays;

public class Client implements Runnable
{
    private String getIP;
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
            DatagramPacket packet_out = new DatagramPacket(Buffer_out, Buffer_out.length, address, 7070);
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
            if(HelloApplication.getClientConnect()){
                socket.receive(packet_in);
                Response = new String(packet_in.getData()).trim();
                //NEW HERB With player number added
                String[] playerData = Response.split(",");
                int playerNum = Integer.parseInt((playerData[playerData.length - 1]));
                String[] data = Arrays.copyOf(playerData, playerData.length - 1);
//                for (int i = 0; i < data.length; i++){
//                    System.out.println("data:" +data[i]);
//                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        game.clientStart(new Stage(), data);
                    }
                });

            }
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
