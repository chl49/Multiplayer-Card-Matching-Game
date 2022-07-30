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
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import com.example.cmpt371project.*;
public class HelloApplication extends Application {

    private Stage stage;
    private Scene scene;
    private Parent root;

    private static boolean clientConnected = false;
    @FXML private Label hostIP;
    @FXML private TextField getIp;

    public static void setClientConnect(boolean connect){
        clientConnected = connect;
    }
    public static boolean getClientConnect(){
        return clientConnected;
    }

    @Override
    public void start(Stage stage) throws IOException {
        /*
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("HomeScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("CMPT 371 Group 4 Matching Game");
        stage.setScene(scene);
        stage.show();
        */
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("HomeScene.fxml"));
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

    public void switchToServerScene(ActionEvent event) throws IOException {
        //Get Host ip address
        String urlString = "http://checkip.amazonaws.com/";
        URL url = new URL(urlString);
        //String ip;
        String ip = InetAddress.getLocalHost().getHostAddress();
        /*
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            ip = br.readLine();
        }

         */
        System.out.println(ip);
        Parent newRoot = FXMLLoader.load(getClass().getResource("ServerScene.fxml"));
        hostIP = (Label) newRoot.lookup("#hostIP");
        hostIP.setText(ip);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(newRoot);
        stage.setScene(scene);
        stage.show();

        //Launch server thread
        Thread serv = new Thread(new Server());
        serv.start();
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
        EventHandler<ActionEvent> onClick = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                hostIP = (Label) newRoot.lookup("#hostIP");
                hostIP.setText(getIp.getText());
                Thread cl;
                if(!clientConnected){
                    cl = new Thread(new Client(getIp.getText()));
                    cl.start();
                }

            }
        };
        getIp.setOnAction(onClick);

    }

    public void startGame(ActionEvent event){
    }

    public static void main(String[] args) {
        launch();
    }
}
