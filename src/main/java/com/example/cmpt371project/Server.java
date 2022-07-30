package com.example.cmpt371project;

import java.io.*;
import java.net.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.cmpt371project.*;

public class Server implements Runnable{
    //Convert to thread eventually
    //player array variable to store players ports (only used for sending game board at the start)
    //
    private final int MAX_PLAYERS = 4;
    private DatagramPacket[] playerData = new DatagramPacket[MAX_PLAYERS];
    private int currentPlayers = 1;
    private boolean serverRunning = false;

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
            while (serverRunning) {
                socket.receive(packet_in);

                String IncomingData = new String(packet_in.getData()).trim();
                if (IncomingData.equals("Join")) {
                    System.out.println("Storing add " + packet_in.getAddress() + " with port: " + packet_in.getPort());
                    int port = packet_in.getPort();
                    InetAddress addr = packet_in.getAddress();
                    playerData[currentPlayers] = new DatagramPacket(Buffer_in,Buffer_in.length,addr,port);
                    currentPlayers++;
                }
                if (currentPlayers >= MAX_PLAYERS){
                    //Launch host game
                    // Gameboard.hostStart();
                    //Get gameBoard data
                    String[] data = {"Card","Order","here"}; //data = GameBoard.getBoard();
                    String msg = String.join(",",data);
                    byte[] buffer_out = msg.getBytes();
                    //Send Data to other players
                    for (int i = 1; i < MAX_PLAYERS; i++){
                        InetAddress  Add= playerData[i].getAddress();
                        int P = playerData[i].getPort();
                        DatagramPacket packet_out = new DatagramPacket(buffer_out, buffer_out.length, Add, P);
                        System.out.println("Sending to " + Add + " on port: " + P);
                        socket.send(packet_out); // send data
                    }
                    /*
                    while gameNOtFinished
                        wait on reply
                        recieve data
                        lock buttons
                        do game logic
                        send reply lock/unlock


                    **/
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
