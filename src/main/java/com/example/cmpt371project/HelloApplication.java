package com.example.cmpt371project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
public class HelloApplication extends Application {

    private Stage stage;
    private Scene scene;
    private Parent root;

    private static boolean clientConnected = false;
    @FXML private Label hostIP;
    @FXML private TextField getIp;
    private static final Object key = new Object();
    private static String hostAddress = "";
    private static int hostPort = 0;

    public static void setHostAddress(String address){
        hostAddress = address;
    }
    public static String getHostAddress(){
        return hostAddress;
    }
    public static void setHostPort(int port){
        //key.notify();
        hostPort = port;
        synchronized(key){
            key.notify();
        }
    }
    public synchronized int getHostPort(){
        return hostPort;
    }
    public static void setClientConnect(boolean connect){
        clientConnected = connect;
    }
    public static boolean getClientConnect(){
        return clientConnected;
    }

    @Override
    public void start(Stage stage) throws IOException {
        root = FXMLLoader.load(getClass().getResource("HomeScene.fxml"));
        scene = new Scene(root);
        stage.setTitle("CMPT 371 Group 4 Matching Game");
        stage.setScene(scene);
        stage.show();
    }
    public void switchToHomeScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("HomeScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToServerScene(ActionEvent event) throws IOException, InterruptedException {
        //Get Host ip address
        String urlString = "http://checkip.amazonaws.com/";
        URL url = new URL(urlString);
        //String ip;
        String ip = InetAddress.getLocalHost().getHostAddress();

        System.out.println(ip);
        Parent newRoot = FXMLLoader.load(getClass().getResource("ServerScene.fxml"));
        hostIP = (Label) newRoot.lookup("#hostIP");
        hostIP.setText(ip);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(newRoot);
        stage.setScene(scene);
        stage.show();

        //Launch server thread
        Thread serv = new Thread(new Server(), "Server Thread");
        serv.start();

        //wait for server thread to establish
        synchronized(key){
            while (hostPort==0) {
                key.wait();
            }
        }
        //Launch host's client thread
        Thread cl;
        if(!clientConnected&&hostPort!=0){
            cl = new Thread(new Client(getHostAddress()), "Host Client Thread");
            cl.start();
        }
    }

    public void switchToClientScene(ActionEvent event) throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("ClientScene.fxml"));
        getIp = (TextField) newRoot.lookup("#getIp");
        getIp.setText("Enter Host ip here");
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(newRoot);
        stage.setScene(scene);
        stage.show();

        //Hit Enter upon typing ip address
        EventHandler<ActionEvent> onClick = e -> {
            hostIP = (Label) newRoot.lookup("#hostIP");
            hostIP.setText(getIp.getText());
            Thread cl;
            if(!clientConnected){
                cl = new Thread(new Client(getIp.getText()), "Guest Client Thread");
                cl.start();
            }

        };
        getIp.setOnAction(onClick);

    }
    public static void main(String[] args) {
        launch();
    }
}
