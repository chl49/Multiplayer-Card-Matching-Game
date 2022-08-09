package com.example.cmpt371project;

import java.io.*;
import java.net.*;


import com.example.cmpt371project.*;
import javafx.stage.Stage;
import java.util.PriorityQueue;
import java.util.Comparator;


public class Server implements Runnable{
    //Convert to thread eventually
    //player array variable to store players ports (only used for sending game board at the start)
    private boolean hasMessage;
    //
    private final int MAX_PLAYERS = 2;
    private final DatagramPacket[] playerData = new DatagramPacket[MAX_PLAYERS];
    private int currentPlayers = 0;
    private boolean serverRunning = false;
    private boolean gameNotFinished = false;

    //TODO: Priority Queue Not Implemented YET
    /*
    static Comparator<String[]> comparator = new StringLengthComparator();
    private static PriorityQueue<String[]> messages = new PriorityQueue<>(10, comparator);

     */
    private synchronized void updateMessageQueue(String[] msg){
        while (hasMessage) {
            // no room for new message
            try {
                wait();  // release the lock of this object
            } catch (InterruptedException ignored) { }
        }
        // acquire the lock and continue
        hasMessage = true;
        System.out.println("updating message queue");
        //add packet to messages queue with priority on timestamp
        //TODO: Priority Queue Not Implemented YET
        //messages.add(msg);
        // notify processing Priority queue thread to take over
        notify();
    }

    public void addClient(DatagramSocket socket, DatagramPacket packet_in, byte[] Buffer_in) throws IOException {
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
        if (currentPlayers >= MAX_PLAYERS){
            //Launch host game
            //Gameboard.hostStart();
            GameBoard game = new GameBoard();
            game.start();
            //Get gameBoard data
            String boardMsg = game.getGameBoard();
            byte[] buffer_cast;
            System.out.println(boardMsg);
            //Send Data to other players
            for (int i = 0; i < MAX_PLAYERS; i++){
                InetAddress Add = playerData[i].getAddress();
                int P = playerData[i].getPort();
                //NEW Herb: w/ player numbers sent
                String msg = boardMsg+','+i;
                buffer_cast = msg.getBytes();
                DatagramPacket packet_cast = new DatagramPacket(buffer_cast, buffer_cast.length, Add, P);
                System.out.println("Sending to " + Add + " on port: " + P);
                socket.send(packet_cast); // send data
            }
                        /*
                        gameNotFinished = true;
                        while (gameNotFinished) {
                            socket.receive(packet_in);
                            //DO SMT
                        }

                         */
        }
    }
    public void updateClient(DatagramSocket socket, DatagramPacket packet_in, byte[] Buffer_in, String[] data) throws IOException {
        //Herb: TODO:::: Server Multicast
        //NEW ###Herb: Acknowledge users connected, locking clients to port

        //String NewMessage = "DONE";
        String message = String.join(",",data);
        //Herb: TODO:::: Queue processing NOT COMPLETED
        //updateMessageQueue(data);
        System.out.println(message);
        //Send Data to other players
        for (int i = 0; i < MAX_PLAYERS; i++){
            byte[] buffer_new;
            InetAddress Add = playerData[i].getAddress();
            int P = playerData[i].getPort();
            //Herb: TODO:::: Player unique stuff
            //EX. data = [Clicked,0,1] == Action, Grid, Player
            if(Integer.parseInt(data[2])==i || data[0].equals("release")){
                buffer_new = message.getBytes();
                DatagramPacket packet_new = new DatagramPacket(buffer_new, buffer_new.length, Add, P);
                System.out.println("Sending new to " + Add + " on port: " + P);
                String output = new String(packet_new.getData()).trim();
                System.out.println(output);
                socket.send(packet_new); // send data

            } else if(data[0].equals("match")) {
                buffer_new = message.getBytes();
                DatagramPacket packet_new = new DatagramPacket(buffer_new, buffer_new.length, Add, P);
                System.out.println("Sending new to " + Add + " on port: " + P);
                String output = new String(packet_new.getData()).trim();
                System.out.println(output);
                socket.send(packet_new); // send data
            }
            else{
                String[] newData = data;
                newData[0] = "locked";
                String otherMessage = String.join(",",newData);
                buffer_new = otherMessage.getBytes();
                DatagramPacket packet_new = new DatagramPacket(buffer_new, buffer_new.length, Add, P);
                System.out.println("Sending new to " + Add + " on port: " + P);
                String output = new String(packet_new.getData()).trim();
                System.out.println(output);
                socket.send(packet_new); // send data
            }
        }
    }

    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(7070); // establish socket and listen
            //InetAddress address = InetAddress.getByName("142.58.223.218");
            InetAddress ip = InetAddress.getLocalHost();
            //InetAddress address = getByAddress("142.58.223.218", byte[] addr)

            byte[] Buffer_in = new byte[60000];
            DatagramPacket packet_in = new DatagramPacket(Buffer_in, Buffer_in.length);

            // wait for incoming data
            serverRunning = true;
            System.out.println("Setting up Server at Local address "+ ip.getHostAddress());
            HelloApplication.setHostAddress(ip.getHostAddress());
            HelloApplication.setHostPort(socket.getPort());

            while (serverRunning) {
                Buffer_in = new byte[60000];
                packet_in = new DatagramPacket(Buffer_in, Buffer_in.length);
                socket.receive(packet_in);

                String IncomingData = new String(packet_in.getData()).trim();
                String[] data = IncomingData.split(",");
                System.out.println(IncomingData);
                switch (data[0]) {
                    case "clicked":
                    case "release":
                    case "match":
                        updateClient( socket,  packet_in, Buffer_in, data);
                        break;

                    case "Join":
                        addClient( socket,  packet_in, Buffer_in);
                        break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
