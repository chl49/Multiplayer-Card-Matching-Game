package com.example.cmpt371project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;

public class HelloApplication extends Application {

    private Stage stage;
    private Scene scene;
    private Parent root;

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
        Parent newRoot = FXMLLoader.load(getClass().getResource("ServerScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(newRoot);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToClientScene(ActionEvent event) throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("ClientScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(newRoot);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}