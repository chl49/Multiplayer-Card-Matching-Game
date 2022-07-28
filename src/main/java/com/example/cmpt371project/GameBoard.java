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
import java.util.*;

import static java.util.Arrays.asList;


public class GameBoard extends Application {
    private final int TOTAL_NUMBER_OF_CARDS = 40;
    private int row = 0;
    private int column = 0;
    GridPane gridPane = new GridPane();

    public void start(Stage stage) {
        stage.setTitle("Match!");

        // Gets all files from resources -> img
        String[] imageNames = getListOfFileNames("img");
        Collections.shuffle(asList(imageNames));

        int buttonId = 0;
        LinkedHashMap<String, Integer> cardCount = new LinkedHashMap<String, Integer>();
        int TOTAL_NUMBER_OF_PAIRS = TOTAL_NUMBER_OF_CARDS / 2;
        for(int i = 0; i < TOTAL_NUMBER_OF_PAIRS; i++){
            cardCount.put(imageNames[i], 0);
        }
        for(int x = 0; x < TOTAL_NUMBER_OF_CARDS; x++) {
            addButton(cardCount, 8, buttonId);
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

    private void addButton(LinkedHashMap<String, Integer> cardMap, int numOfColumns, int buttonId) {
        column++;
        if(column > numOfColumns) {
            column = 1;
            row++;
        }

        // Make a new button and give it a random card image

        Random rand = new Random();

        String[] imageNames = cardMap.keySet().toArray(new String[cardMap.size()]);
        // Makes sure cards are placed in pairs
        String cardName = imageNames[rand.nextInt(imageNames.length)];
        while(cardMap.get(cardName) >= 2){
            cardName = imageNames[rand.nextInt(imageNames.length)];
        }
        cardMap.put(cardName, cardMap.get(cardName) + 1);

        String cardFileName = "/img/" + cardName;
        String cardValue = cardName.split("\\.")[0];
        Button button = new CardButton(new ImageView(getClass().getResource(cardFileName).toExternalForm()), cardValue);


//        ImageView view = new ImageView(getClass().getResource("/img/" + cardName).toExternalForm());
//        view.setFitHeight(80);
//        view.setPreserveRatio(true);
//        button.setGraphic(view);

        button.setId("" + buttonId);
//        button.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent e) {
//
//            }
//        });
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
