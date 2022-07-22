package com.example.cmpt371project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;


public class GameBoard extends Application {
    private final int TOTAL_NUMBER_OF_CARDS = 40;
    private int row = 0;
    private int column = 0;
    GridPane gridPane = new GridPane();

    public void start(Stage stage) {
        stage.setTitle("Match!");

        // Gets all files from resources -> img
        String[] imageNames = getListOfFileNames("img");

        int buttonId = 0;
        for(int x = 0; x < TOTAL_NUMBER_OF_CARDS; x++) {
            addButton(imageNames, 8, buttonId);
            buttonId++;
        }

        // Container that holds the game board (grid pane)
        Pane root = new Pane();

        // Sets spacing between cards
        gridPane.setHgap(15);
        gridPane.setVgap(15);

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

        // Make a new button and give it a random card image
        Button button = new Button();

        Random rand = new Random();
        int index = rand.nextInt(imageNames.length);
        ImageView view = new ImageView(getClass().getResource("/img/" + imageNames[index]).toExternalForm());
        view.setFitHeight(80);
        view.setPreserveRatio(true);
        button.setGraphic(view);

        button.setId("" + buttonId);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("Card id:" + button.getId());
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

    private String[] getListOfFileNames(String path) {
        File file = getFileFromURL("img");
        String[] imageNames = file.list();
        return imageNames;
    }

    public static void main(String[] args) {
        launch();
    }

}
