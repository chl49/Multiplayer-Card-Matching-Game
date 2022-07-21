package com.example.cmpt371project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;

import static javafx.application.Application.launch;

public class GameBoard extends Application {
    private int row = 0;
    private int column = 0;
    GridPane gridPane = new GridPane();

    public void start(Stage stage) throws IOException {
        Pane root = new Pane();
        stage.setTitle("Match!");

        File file = getFileFromURL("img");
        String[] imageNames = file.list();
        int buttonId = 0;


        for(int x = 0; x < 40; x++) {
            addButton(imageNames, 8, buttonId);
            buttonId++;
        }

        gridPane.setHgap(10);
        gridPane.setVgap(10);
        root.getChildren().add(gridPane);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void addButton(String[] imageNames, int numOfColumns, int buttonId) {
        column++;
        if(column > numOfColumns) {
            column = 1;
            row++;
        }
        Button button = new Button();
        Random rand = new Random();
        int index = rand.nextInt(imageNames.length);

        ImageView view = new ImageView(getClass().getResource("/img/" + imageNames[index]).toExternalForm());
        view.setFitHeight(80);
        view.setPreserveRatio(true);
        button.setGraphic(view);

//        gridPane.getChildren().add(button);
        final int numButton = buttonId;
        button.setId("" + buttonId);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("id(" + button.getId()  + ") =  " + numButton);
            }
        });
        gridPane.add(button, column, row);
    }

    private File getFileFromURL(String path) {
        URL url = this.getClass().getClassLoader().getResource(path);
        File file = null;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            file = new File(url.getPath());
        } finally {
            return file;
        }
    }

    public static void main(String[] args) {
        launch();
    }

}
