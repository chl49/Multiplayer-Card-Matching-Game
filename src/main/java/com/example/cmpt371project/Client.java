package com.example.cmpt371project;

import java.io.*; import java.net.*;
public class Client implements Runnable
{
    private String getIP;
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
                System.out.println("connected");
                HelloApplication.setClientConnect(true);
            }
            System.out.println(HelloApplication.getClientConnect());

            //NEW ###Herb: After being connected, Client waits for cards

            if(HelloApplication.getClientConnect()){
                socket.receive(packet_in);
                Response = new String(packet_in.getData()).trim();
                String[] data = Response.split(",");
                for (int i = 0; i < data.length; i++){
                    System.out.println("data:" +data[i]);
                }
            }
            /*
            Gameboard.Clientstart(Data);
            while (gameNotFinished)
                msg = send id;
            **/
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
