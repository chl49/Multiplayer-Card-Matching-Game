package com.example.cmpt371project;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
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


public class GameBoard  {
    private final int TOTAL_NUMBER_OF_CARDS = 40;
    private int row = 0;
    private int column = 0;
    GridPane gridPane = new GridPane();

    private String[] cardValuesOrdered;
    public void start() {
        //stage.setTitle("Match!");

        // Gets all files from resources -> img -> fronts
        String[] imageNames = getListOfFileNames("img/fronts/");
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
//        Pane root = new Pane();

        // Sets spacing between cards
//        gridPane.setHgap(15);
//        gridPane.setVgap(15);

//        root.getChildren().add(gridPane);

//        Scene scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
        cardValuesOrdered = getGameBoard(gridPane);
    }

    public void clientStart(Stage stage,String[] data) {
        stage.setTitle("Match!");

        // Gets all files from resources -> img -> fronts
        String[] imageNames = getListOfFileNames("img/fronts/");
        Collections.shuffle(asList(imageNames));

        int buttonId = 0;
        LinkedHashMap<String, Integer> cardCount = new LinkedHashMap<String, Integer>();
        int TOTAL_NUMBER_OF_PAIRS = TOTAL_NUMBER_OF_CARDS / 2;
        for(int i = 0; i < TOTAL_NUMBER_OF_PAIRS; i++){
            cardCount.put(imageNames[i], 0);
        }
        for(int x = 0; x < TOTAL_NUMBER_OF_CARDS; x++) {
            column++;
            if(column > 8) {
                column = 1;
                row++;
            }
            String card = data[x] + ".png";
            CardButton button = new CardButton(new ImageView(getClass().getResource("/img/fronts/" + card).toExternalForm()), data[x]);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    switch(button.getState()) {

                        case DEFAULT -> {
                            System.out.println("Flipping card");
                            button.setGraphic(button.getCardFront());
                            button.setState(CardButton.CardButtonSate.FLIPPED);
                        }
                        case FLIPPED -> {
                            System.out.println("Card is already flipped select a different card.");
                        }
                        case NOT_IN_PLAY -> {
                            System.out.println("Card has already been used for a point.");
                        }
                    }
                    System.out.println("Card id:" + button.getId() + "\tValue:" + button.getValue());
                }

            });

            button.setId("" + buttonId);
            gridPane.add(button, column, row);
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
        getGameBoard(gridPane);

    }

    public String[] getGameBoard(GridPane gameBoard) {
        ObservableList<Node> cards = gridPane.getChildren();
        String[] cardValues = new String[cards.size()];

        int index = 0;
        for(Node card : cards) {
            CardButton cardButton = (CardButton) card;
            cardValues[index] = cardButton.getValue();
            index++;
        }
        return cardValues;
    }

    private void addButton(LinkedHashMap<String, Integer> cardMap, int numOfColumns, int buttonId) {
        column++;
        if(column > numOfColumns) {
            column = 1;
            row++;
        }

        Random rand = new Random();
        String[] imageNames = cardMap.keySet().toArray(new String[cardMap.size()]);

        // Makes sure cards are placed in pairs
        String cardName = imageNames[rand.nextInt(imageNames.length)];
        while(cardMap.get(cardName) >= 2) {
            cardName = imageNames[rand.nextInt(imageNames.length)];
        }
        cardMap.put(cardName, cardMap.get(cardName) + 1);

        String cardValue = cardName.split("\\.")[0];
        CardButton button = new CardButton(new ImageView(getClass().getResource("/img/fronts/" + cardName).toExternalForm()), cardValue);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                switch(button.getState()) {

                    case DEFAULT -> {
                        System.out.println("Flipping card");
                        button.setGraphic(button.getCardFront());
                        button.setState(CardButton.CardButtonSate.FLIPPED);
                    }
                    case FLIPPED -> {
                        System.out.println("Card is already flipped select a different card.");
                    }
                    case NOT_IN_PLAY -> {
                        System.out.println("Card has already been used for a point.");
                    }
                }
                System.out.println("Card id:" + button.getId() + "\tValue:" + button.getValue());
            }

        });

        button.setId("" + buttonId);
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
        File file = getFileFromURL(path);
        String[] imageNames = file.list();
        return imageNames;
    }

    public String[] getCardValues() {
        return cardValuesOrdered;
    }

}
