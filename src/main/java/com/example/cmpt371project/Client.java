package com.example.cmpt371project;

import java.io.*; import java.net.*;
public class Client implements Runnable
{
    public void run() {
        try {
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
            String Response = new String(packet_in.getData()).trim();
            System.out.println("according to the server, the time is: " +Response);
            socket.close();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
