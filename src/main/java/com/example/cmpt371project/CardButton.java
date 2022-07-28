package com.example.cmpt371project;

/**
 * @author Steve
 * @created 2022-07-28
 * @project cmpt371-team04
 */

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class CardButton extends Button {
    public enum CardButtonSate {
        DEFAULT,
        FLIPPED,
        NOT_IN_PLAY
    }

    /**
     * The value of the card e.g. 2C for 2 of clubs, AH for ace of hearts
     */
    private String value;
    private ImageView cardFront;
    private ImageView cardBack;
    private CardButtonSate state;
    private static final String CARD_BACK_FILE_IMAGE = "/img/black_joker.png";

    public CardButton(ImageView cardFront, String value) {
        super();
        this.value = value;

        setOnAction(this);
        this.cardBack = new ImageView(getClass().getResource(CARD_BACK_FILE_IMAGE).toExternalForm());;
        cardBack.setFitHeight(80);
        cardBack.setPreserveRatio(true);
        this.setGraphic(cardBack);
        this.state = CardButtonSate.DEFAULT;

        this.cardFront = cardFront;
        cardFront.setFitHeight(80);
        cardFront.setPreserveRatio(true);
    }

    public CardButton(String s, Node node, String value, ImageView cardFront, CardButtonSate state) {
        super(s, node);
        this.value = value;


    }

    private void setOnAction(Button clickedButton) {
        clickedButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                switch(state) {

                    case DEFAULT -> {
                        System.out.println("Flipping card");
                        clickedButton.setGraphic(cardFront);
                        state = CardButtonSate.FLIPPED;
                    }
                    case FLIPPED -> {
                        System.out.println("Card is already flipped select a different card.");
                    }
                    case NOT_IN_PLAY -> {
                        System.out.println("Card has already been used for a point.");
                    }
                }
                System.out.println("Card id:" + clickedButton.getId() + "\tValue:" + value);
            }

        });
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ImageView getCardFront() {
        return cardFront;
    }

    public void setCardFront(ImageView cardFront) {
        this.cardFront = cardFront;
    }

    public ImageView getCardBack() {
        return cardBack;
    }

    public void setCardBack(ImageView cardBack) {
        this.cardBack = cardBack;
    }

    public CardButtonSate getState() {
        return state;
    }

    public void setState(CardButtonSate state) {
        this.state = state;
    }

}
