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