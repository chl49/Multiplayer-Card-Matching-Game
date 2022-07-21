package com.example.cmpt371project;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
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