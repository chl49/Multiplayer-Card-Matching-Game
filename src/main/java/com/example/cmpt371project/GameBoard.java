package com.example.cmpt371project;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
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
    private static final String CARD_FRONT_PATH = "img/fronts/";
    private static final int GRID_PANE_VERTICAL_SPACING = 0;
    private static final int GRID_PANE_HORIZONTAL_SPACING = 0;
    private static final int GRID_PANE_BORDER_SPACING = 15;
    private static final int MAX_COLUMNS = 8;
    private Label playerOneScoreLabel = new Label("Player 1: \t0");
    private Label playerTwoScoreLabel = new Label("Player 2: \t0");
    private Label playerThreeScoreLabel = new Label("Player 3: \t0");
    private Label playerFourScoreLabel = new Label("Player 4: \t0");
    GridPane gridPane = new GridPane();

    public void start(Stage stage) {
        stage.setTitle("Match!");

        // Gets all files from resources -> img -> fronts
        String[] imageNames = getListOfFileNames(CARD_FRONT_PATH);
        Collections.shuffle(asList(imageNames));

        int buttonId = 0;
        LinkedHashMap<String, Integer> cardCount = new LinkedHashMap<String, Integer>();
        int TOTAL_NUMBER_OF_PAIRS = TOTAL_NUMBER_OF_CARDS / 2;
        for(int i = 0; i < TOTAL_NUMBER_OF_PAIRS; i++){
            cardCount.put(imageNames[i], 0);
        }

        // Add cards to the grid pane
        for(int x = 0; x < TOTAL_NUMBER_OF_CARDS; x++) {
            addButton(cardCount, MAX_COLUMNS, buttonId);
            buttonId++;
        }

        // Container that holds the game board (grid pane)


        // Sets spacing between cards
        gridPane.setHgap(GRID_PANE_HORIZONTAL_SPACING);
        gridPane.setVgap(GRID_PANE_VERTICAL_SPACING);

        // Padding so its away from the edge of the border
        // top, right, bottom, left
        gridPane.setPadding(new Insets(GRID_PANE_BORDER_SPACING, GRID_PANE_BORDER_SPACING, GRID_PANE_BORDER_SPACING, 0));

        SplitPane root = new SplitPane();
        root.setOrientation(Orientation.HORIZONTAL);
        VBox playerScores = new VBox();
        playerScores.getChildren().add(0, playerFourScoreLabel);
        playerScores.getChildren().add(0, playerThreeScoreLabel);
        playerScores.getChildren().add(0, playerTwoScoreLabel);
        playerScores.getChildren().add(0, playerOneScoreLabel);

        root.getItems().add(0, playerScores);
        root.getItems().add(1, gridPane);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        getGameBoardAsString(gridPane);
    }

    public static void main(String[] args) {
        launch();
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

        button.setId(String.valueOf(buttonId));
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

    public String getGameBoardAsString(GridPane gameBoard) {
        ObservableList<Node> cards = gridPane.getChildren();
        StringBuilder sb = new StringBuilder();

        Iterator<Node> iterator = cards.iterator();
        while (iterator.hasNext()) {
            CardButton cardButton = (CardButton) iterator.next();
            sb = iterator.hasNext() ? sb.append(cardButton.getValue() + ',') : sb.append(cardButton.getValue());
        }

        return sb.toString();
    }

}
