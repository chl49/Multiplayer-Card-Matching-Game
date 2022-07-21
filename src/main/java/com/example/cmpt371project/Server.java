package com.example.cmpt371project;

import java.io.*; import java.net.*;

public class Server {
    //Convert to thread eventually
    public static void main (String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(7070); // establish socket and listen

        byte[] Buffer_in = new byte[256];
        DatagramPacket packet_in = new DatagramPacket(Buffer_in, Buffer_in.length);
        // wait for incoming data
        System.out.println("Setting up Server");
        while (true) {
            socket.receive(packet_in);

            String IncomingData = data(packet_in.getData()).toString();
            System.out.println("Msg received: " + IncomingData);
            if (IncomingData.equals("Time")) {
                System.out.println("Msg received: " + IncomingData);
                // prepare data to be sent
                String TheTime = Long.toString(System.currentTimeMillis( ));
                byte[] Buffer_out = TheTime.getBytes();
                InetAddress Add = packet_in.getAddress(); int P = packet_in.getPort();
                DatagramPacket packet_out = new DatagramPacket(Buffer_out, Buffer_out.length, Add, P);
                socket.send(packet_out); // send data
            }
        }

    }
    //For Truncating the excess buffer taken from https://www.geeksforgeeks.org/working-udp-datagramsockets-java/
    public static StringBuilder data(byte[] a)
    {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0)
        {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }
}
