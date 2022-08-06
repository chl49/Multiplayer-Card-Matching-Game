package com.example.cmpt371project;

import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.lang.*;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.*;

import static java.util.Arrays.asList;


public class GameBoard {
    private final int TOTAL_NUMBER_OF_CARDS = 40;
    private int row = 0;
    private int column = 0;
    private static final String CARD_FRONT_PATH = "img/fronts/";
    private static final int GRID_PANE_VERTICAL_SPACING = 0;
    private static final int GRID_PANE_HORIZONTAL_SPACING = 0;
    private static final int GRID_PANE_BORDER_SPACING = 15;
    private static final int MAX_COLUMNS = 8;
    private static final String DEFAULT_SCORE_STRING = "Player %s: \t%o          ";
    private StringProperty playerOneTextScore = new SimpleStringProperty();
    private Label playerOneScoreLabel = new Label();
    private int playerOneScore = 0;
    private StringProperty playerTwoTextScore = new SimpleStringProperty();
    private Label playerTwoScoreLabel = new Label();
    private int playerTwoScore = 0;
    private StringProperty playerThreeTextScore = new SimpleStringProperty();
    private Label playerThreeScoreLabel = new Label();
    private int playerThreeScore = 0;
    private StringProperty playerFourTextScore = new SimpleStringProperty();
    private Label playerFourScoreLabel = new Label();
    private int playerFourScore = 0;

    private String gameBoard;
    GridPane gridPane = new GridPane();
    ArrayList<CardButton> allButtons = new ArrayList<>();
    String cardName;
    CardButton selected1;
    CardButton selected2;


    //Networking
    private int playerId;
    private InetAddress hostAddress;
    private int hostPort;
    public void start() {
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
        gameBoard = getGameBoardAsString(gridPane);
    }

    public void clientStart(Stage stage, String[] data, InetAddress address, int port, int id) {
        this.hostAddress = address;
        this.hostPort = port;
        this.playerId = id;

        stage.setTitle("Match!");

        // Gets all files from resources -> img -> fronts
        String[] imageNames = getListOfFileNames(CARD_FRONT_PATH);
        Collections.shuffle(asList(imageNames));

        int buttonId = 0;

        // Add cards to the grid pane
        for(int x = 0; x < TOTAL_NUMBER_OF_CARDS; x++) {
            column++;
            if(column > 8) {
                column = 1;
                row++;
            }
            String card = data[x] + ".png";
            CardButton button = new CardButton(new ImageView(getClass().getResource("/img/fronts/" + card).toExternalForm()), data[x]);
            button.setId(Integer.toString(buttonId));
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    sendMessage(button, "clicked");
                    //may need a sleep function
                    switch(button.getState()) {
                        case DEFAULT -> {
                            //System.out.println("Flipping card");
                            if(selected1 == null || selected2 == null) {
                                button.setGraphic(button.getCardFront());
                                button.setState(CardButton.CardButtonState.FLIPPED);
                            }
                        }
                        case FLIPPED -> {
                            System.out.println("Card is already flipped select a different card.");
                        }
                        case NOT_IN_PLAY -> {
                            System.out.println("Card has already been used for a point.");
                        }
                    }
                    handleCardMatching(button);
                    //System.out.println("Card id:" + button.getId() + "\tValue:" + button.getValue());
                }

            });
            allButtons.add(button);
            gridPane.add(button, column, row);
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
        VBox playerScoresVBox = setupScoreBoard(new VBox());

        root.getItems().add(0, playerScoresVBox);
        root.getItems().add(1, gridPane);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        getGameBoardAsString(gridPane);
    }

    private VBox setupScoreBoard(VBox playerScoresVBox) {
        playerOneScoreLabel.textProperty().bind(playerOneTextScore);
        playerOneTextScore.setValue(formatStringForScoreLabel("1", playerOneScore));

        playerTwoScoreLabel.textProperty().bind(playerTwoTextScore);
        playerTwoTextScore.setValue(formatStringForScoreLabel("2", playerTwoScore));

        playerThreeScoreLabel.textProperty().bind(playerThreeTextScore);
        playerThreeTextScore.setValue(formatStringForScoreLabel("3", playerThreeScore));

        playerFourScoreLabel.textProperty().bind(playerFourTextScore);
        playerFourTextScore.setValue(formatStringForScoreLabel("4", playerFourScore));

        playerScoresVBox.getChildren().add(0, playerFourScoreLabel);
        playerScoresVBox.getChildren().add(0, playerThreeScoreLabel);
        playerScoresVBox.getChildren().add(0, playerTwoScoreLabel);
        playerScoresVBox.getChildren().add(0, playerOneScoreLabel);
        return playerScoresVBox;
    }



    private String formatStringForScoreLabel(String playerNumber, int score) {
        return String.format(DEFAULT_SCORE_STRING, playerNumber, score);
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
                        //System.out.println("Flipping card");
                        if(selected1 == null || selected2 == null) {
                            button.setState(CardButton.CardButtonState.FLIPPED);
                        }
                    }
                    case FLIPPED -> {
                        System.out.println("Card is already flipped select a different card.");
                    }
                    case NOT_IN_PLAY -> {
                        System.out.println("Card has already been used for a point.");
                    }
                }
                handleCardMatching(button);
                //System.out.println("Card id:" + button.getId() + "\tValue:" + button.getValue());
            }

        });
        allButtons.add(button);
        gridPane.add(button, column, row);
    }
    public void handleCardMatching(CardButton buttonClicked) {
        if(selected1 == null && selected2 == null){
            selected1 = buttonClicked;
        }
        else if(selected2 == null){
            selected2 = buttonClicked;
            checkForMatch();
        }
        //System.out.println("Score: " + score);
    }
    private void checkForMatch() {
        if(selected1.getValue().equals(selected2.getValue()) && selected1 != selected2){
            System.out.println("Its a match!");
            PauseTransition pause = new PauseTransition(Duration.seconds(0.7));
            pause.setOnFinished(e ->{
                selected1.setState(CardButton.CardButtonState.NOT_IN_PLAY);
                selected2.setState(CardButton.CardButtonState.NOT_IN_PLAY);
                sendMessage(selected1, "match");
                selected1 = null;
                selected2 = null;

                // Change depending on which player got the point
                playerOneScore++;
                playerOneTextScore.setValue(formatStringForScoreLabel("1", playerOneScore));
            });
            pause.play();
        }
        else {
            System.out.println("Not a match, flipping cards");
            PauseTransition pause = new PauseTransition(Duration.seconds(0.7));
            pause.setOnFinished(e ->{
                selected1.setState(CardButton.CardButtonState.DEFAULT);
                selected2.setState(CardButton.CardButtonState.DEFAULT);
                sendMessage(selected1, "release");
                selected1 = null;
                selected2 = null;
            });
            pause.play();
        }
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
    private void sendMessage(Button button, String command) {
        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        String message = null;
        switch (command) {
            case "clicked":
                message = command + "," + System.currentTimeMillis()+ "," + playerId + "," + button.getId();
                break;
            case "match":
            case "release":
                message = command + "," + System.currentTimeMillis()+ "," + playerId + "," + selected1.getId() + "," + selected2.getId() + "," ;
                break;
        }
        byte buffer[] = message.getBytes();
        DatagramPacket packet_out = new DatagramPacket(buffer, buffer.length, hostAddress, hostPort);
        //System.out.println("GameAddress" + hostAddress);
        //System.out.println("GamePort" + hostPort);
        try {
            socket.send(packet_out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        socket.close();
    }
    public String getGameBoard() {
        return gameBoard;
    }
    // functions that handle updates from server
    public void removeCards(int buttonid1, int buttonid2){
        allButtons.get(buttonid1).setState(CardButton.CardButtonState.NOT_IN_PLAY);
        allButtons.get(buttonid2).setState(CardButton.CardButtonState.NOT_IN_PLAY);
    }
    public void lockCard(int buttonid1){
        allButtons.get(buttonid1).setState(CardButton.CardButtonState.IN_USE);
    }
    public void releaseCards(int buttonid1, int buttonid2){
        allButtons.get(buttonid1).setState(CardButton.CardButtonState.DEFAULT);
        allButtons.get(buttonid2).setState(CardButton.CardButtonState.DEFAULT);
    }
    public void updateScore(int playerNum){
        switch(playerNum){
            case 1:
                playerOneScore++;
                playerOneTextScore.setValue(formatStringForScoreLabel(String.valueOf(playerNum), playerOneScore));
                break;
            case 2:
                playerTwoScore++;
                playerTwoTextScore.setValue(formatStringForScoreLabel(String.valueOf(playerNum), playerTwoScore));
                break;
            case 3:
                playerThreeScore++;
                playerThreeTextScore.setValue(formatStringForScoreLabel(String.valueOf(playerNum), playerThreeScore));
                break;
            case 4:
                playerFourScore++;
                playerFourTextScore.setValue(formatStringForScoreLabel(String.valueOf(playerNum), playerFourScore));
                break;
        }
    }

}
