package com.example.cmpt371project;

import java.io.*;
import java.net.*;


import com.example.cmpt371project.*;
import javafx.stage.Stage;

public class Server implements Runnable{
    //Convert to thread eventually
    //player array variable to store players ports (only used for sending game board at the start)
    //
    private final int MAX_PLAYERS = 1;
    private final DatagramPacket[] playerData = new DatagramPacket[MAX_PLAYERS];
    private int currentPlayers = 0;
    private boolean serverRunning = false;
    private boolean gameNotFinished = false;

    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(7070); // establish socket and listen
            //InetAddress address = InetAddress.getByName("142.58.223.218");
            InetAddress ip = InetAddress.getLocalHost();
            //InetAddress address = getByAddress("142.58.223.218", byte[] addr)

            byte[] Buffer_in = new byte[256];
            DatagramPacket packet_in = new DatagramPacket(Buffer_in, Buffer_in.length);

            // wait for incoming data
            serverRunning = true;
            System.out.println("Setting up Server at Local address "+ ip.getHostAddress());
            HelloApplication.setHostAddress(ip.getHostAddress());
            HelloApplication.setHostPort(socket.getPort());

            while (serverRunning) {
                socket.receive(packet_in);

                String IncomingData = new String(packet_in.getData()).trim();
                if (IncomingData.equals("Join")) {

                    System.out.println("Storing add " + packet_in.getAddress() + " with port: " + packet_in.getPort());
                    int port = packet_in.getPort();
                    InetAddress addr = packet_in.getAddress();
                    playerData[currentPlayers] = new DatagramPacket(Buffer_in,Buffer_in.length,addr,port);
                    currentPlayers++;

                    //NEW ###Herb: Acknowledge users connected, locking clients to port
                    String Message = "Connected";
                    byte[] buffer_out = Message.getBytes();
                    DatagramPacket packet_out = new DatagramPacket(buffer_out, buffer_out.length, addr, port);
                    System.out.println("Sending to " + addr + " on port: " + port);
                    socket.send(packet_out); // send data
                }
                if (currentPlayers >= MAX_PLAYERS){
                    //Launch host game
                    //Gameboard.hostStart();
                    GameBoard game = new GameBoard();
                    game.start();
                    //Get gameBoard data
                    String msg = game.getGameBoard();
                    System.out.println(msg);
                    byte[] buffer_cast = msg.getBytes();
                    //Send Data to other players
                    for (int i = 0; i < MAX_PLAYERS; i++){
                        InetAddress Add = playerData[i].getAddress();
                        int P = playerData[i].getPort();
                        DatagramPacket packet_cast = new DatagramPacket(buffer_cast, buffer_cast.length, Add, P);
                        System.out.println("Sending to " + Add + " on port: " + P);
                        socket.send(packet_cast); // send data
                    }
                    gameNotFinished = true;
                    while (gameNotFinished) {
                        socket.receive(packet_in);
                        //DO SMT
                    }

                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
