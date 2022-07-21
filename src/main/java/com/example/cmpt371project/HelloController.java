package com.example.cmpt371project;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    public void switchToHomeScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("HomeScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToServerScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ServerScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        //Launch server thread
        Thread serv = new Thread(new Server());
        serv.start();
    }

    public void switchToClientScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ClientScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        Thread cl = new Thread(new Client());
        cl.start();
    }
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHostButtonClick() {
        welcomeText.setText("Host selected!");
    }

    @FXML
    protected void onClientButtonClick() {
        welcomeText.setText("Client selected!");
    }
}