package com.example.cmpt371project;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;


public class GameBoard extends Application {
    private final int TOTAL_NUMBER_OF_CARDS = 8;
    private int row = 0;
    private int column = 0;
    GridPane gridPane = new GridPane();
    String cardName;
    int score = 0;
    CardButton selected1;
    CardButton selected2;

    public void start(Stage stage) {
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
        getGameBoard(gridPane);
    }

    public String[][] getGameBoard(GridPane gameBoard) {
        ObservableList<Node> cards = gridPane.getChildren();
        String[][] cardValues = new String[cards.size()][2];

        int index = 0;
        for(Node card : cards) {
            CardButton cardButton = (CardButton) card;
            cardValues[index][0] = cardButton.getId();
            cardValues[index][1] = cardButton.getValue();
            index++;
        }


        for(int i = 0; i < cardValues.length; i++)
        {
            for(int j = 0; j < cardValues[i].length; j++)
            {
                System.out.print(cardValues[i][j]);
                if(j < cardValues[i].length - 1) System.out.print(" ");
            }
            System.out.println();
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
        cardName = imageNames[rand.nextInt(imageNames.length)];
        while(cardMap.get(cardName) >= 2) {
            cardName = imageNames[rand.nextInt(imageNames.length)];
        }
        cardMap.put(cardName, cardMap.get(cardName) + 1);

        String cardValue = cardName.split("\\.")[0];
        CardButton button = new CardButton(new ImageView(getClass().getResource("/img/fronts/" + cardName).toExternalForm()), cardValue);
        button.setId(cardName);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                switch(button.getState()) {
                    case DEFAULT -> {
                        System.out.println("Flipping card");
                        button.setGraphic(button.getCardFront());
                        button.setState(CardButton.CardButtonState.FLIPPED);
                    }
                    case FLIPPED -> {
                        System.out.println("Card is already flipped select a different card.");
                    }
                    case NOT_IN_PLAY -> {
                        System.out.println("Card has already been used for a point.");
                    }
                }
                handleCardMatching(button);
                System.out.println("Card id:" + button.getId() + "\tValue:" + button.getValue());
            }

        });

        gridPane.add(button, column, row);

    }
    public void handleCardMatching(CardButton buttonClicked) {
        //System.out.println("Card id:" + buttonClicked.getId());
        if(selected1 == null && selected2 == null){
            selected1 = buttonClicked;
        }
        else if(selected2 == null){
            selected2 = buttonClicked;
            checkForMatch(buttonClicked);
        }
        System.out.println("Score: " + score);
    }
    private void checkForMatch(CardButton buttonClicked) {
        if(selected1.getId().equals(selected2.getId())){
            System.out.println("Its a match!");
            score++;
            selected1.setState(CardButton.CardButtonState.NOT_IN_PLAY);
            selected2.setState(CardButton.CardButtonState.NOT_IN_PLAY);
        }
        else {
            System.out.println("Not a match, flipping cards");
            selected1.setState(CardButton.CardButtonState.DEFAULT);
            selected2.setState(CardButton.CardButtonState.DEFAULT);
        }
        selected1 = null;
        selected2 = null;
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

    public static void main(String[] args) {
        launch();
    }


}
