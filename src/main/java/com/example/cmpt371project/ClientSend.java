package com.example.cmpt371project;

import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.*; import java.net.*;
import com.example.cmpt371project.*;
public class ClientSend implements Runnable {

    private final int SERVER_PORT = 7070;
    private final InetAddress address;
    private String msg;
    public ClientSend( InetAddress address){
        this.address =  address;
    }
    @Override
    public void run() {

    }
}
