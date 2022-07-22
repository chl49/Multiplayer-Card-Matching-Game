package com.example.cmpt371project;

import java.io.*; import java.net.*;

public class Server implements Runnable{
    //Convert to thread eventually
    public boolean serverRunning = false;
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(7070); // establish socket and listen

            byte[] Buffer_in = new byte[256];
            DatagramPacket packet_in = new DatagramPacket(Buffer_in, Buffer_in.length);
            //Get Host ip address
            String urlString = "http://checkip.amazonaws.com/";
            URL url = new URL(urlString);
            String ip;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                ip = br.readLine();
            }
            // wait for incoming data
            serverRunning = true;
            System.out.println("Setting up Server at " + ip);
            while (serverRunning) {
                socket.receive(packet_in);

                String IncomingData = new String(packet_in.getData()).trim();
                System.out.println("Msg received: " + IncomingData);
                if (IncomingData.equals("Time")) {
                    // prepare data to be sent
                    String TheTime = Long.toString(System.currentTimeMillis( ));
                    byte[] Buffer_out = TheTime.getBytes();
                    InetAddress Add = packet_in.getAddress(); int P = packet_in.getPort();
                    DatagramPacket packet_out = new DatagramPacket(Buffer_out, Buffer_out.length, Add, P);
                    socket.send(packet_out); // send data

                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
