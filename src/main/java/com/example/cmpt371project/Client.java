package com.example.cmpt371project;

import java.io.*; import java.net.*;
public class Client
{
    public static void main (String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(); // establish socket
        // prepare data to be sent
        String Message = "Time";
        byte[] Buffer_out = Message.getBytes();
        DatagramPacket packet_out = new DatagramPacket(Buffer_out, Buffer_out.length, InetAddress.getLocalHost(), 7070);
        // send data
        System.out.println("Sending to " + InetAddress.getLocalHost());
        socket.send(packet_out);
        // receive response
        byte[] Buffer_in = new byte[256];
        DatagramPacket packet_in = new DatagramPacket(Buffer_in, Buffer_in.length);
        socket.receive(packet_in);
        String Response = data(packet_in.getData()).toString();
        System.out.println("according to the server, the time is: " +Response);
        socket.close();
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
