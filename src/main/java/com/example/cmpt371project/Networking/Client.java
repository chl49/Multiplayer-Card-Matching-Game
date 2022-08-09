package com.example.cmpt371project.Networking;

import com.example.cmpt371project.Game.GameBoard;
import com.example.cmpt371project.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.*; import java.net.*;
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
                Application.setClientConnect(true);
            }
            if(Application.getClientConnect()){
                socket.receive(packet_in);
                Response = new String(packet_in.getData()).trim();
                //NEW HERB With player number added
                String[] playerData = Response.split(",");
                int playerNum = Integer.parseInt((playerData[playerData.length - 1]));
                String[] data = Arrays.copyOf(playerData, playerData.length - 1);

                data = Response.split(",");
                String[] finalData = data;
                //Starts game
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        game.clientStart(new Stage(), finalData, address, PORT, playerNum);

                    }
                });
                //Waits for server updates
                while (true){
                    Buffer_in = new byte[60000];
                    packet_in = new DatagramPacket(Buffer_in, Buffer_in.length);
                    socket.receive(packet_in);
                    Response = new String(packet_in.getData()).trim();
                    String[] output = Response.split(",");
                    System.out.println("from server: "+Response);
                    //Updates game based on server input
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            receiveUpdateFromServer(output);
                        }
                    });
                }
            }
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void receiveUpdateFromServer(String[] message){
        int buttonid1;
        int buttonid2;
        int playerNum = Integer.parseInt(message[2]) + 1;
        //Does specific thing depending on message received
        switch(message[0]){
            case "match":
                buttonid1 = Integer.parseInt(message[3]);
                buttonid2 = Integer.parseInt(message[4]);
                game.removeCards(buttonid1, buttonid2);
                game.updateScore(playerNum);
                break;
            case "clicked":
                buttonid1 = Integer.parseInt(message[3]);
                game.clickCard(buttonid1);
                break;
            case "locked":
                buttonid1 = Integer.parseInt(message[3]);
                game.lockCard(buttonid1);
                break;
            case "release":
                buttonid1 = Integer.parseInt(message[3]);
                buttonid2 = Integer.parseInt(message[4]);
                game.releaseCards(buttonid1, buttonid2);
                break;
        }
    }
}
