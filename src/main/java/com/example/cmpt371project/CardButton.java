package com.example.cmpt371project;

/**
 * @author Steve
 * @created 2022-07-28
 * @project cmpt371-team04
 */

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class CardButton extends Button {
    public enum CardButtonState {
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
    private CardButtonState state;
    private static final String CARD_BACK_FILE_IMAGE = "/img/backs/black_joker.png";

    public CardButton(ImageView cardFront, String value) {
        super();
        this.value = value;

        setOnAction(this);
        this.cardBack = new ImageView(getClass().getResource(CARD_BACK_FILE_IMAGE).toExternalForm());
        cardBack.setFitHeight(80);
        cardBack.setPreserveRatio(true);
        this.setGraphic(cardBack);
        this.state = CardButtonState.DEFAULT;

        this.cardFront = cardFront;
        cardFront.setFitHeight(80);
        cardFront.setPreserveRatio(true);
    }

    public CardButton(String s, Node node, String value, ImageView cardFront, CardButtonState state) {
        super(s, node);
        this.value = value;


    }

    private void setOnAction(Button clickedButton) {

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

    public CardButtonState getState() {
        return state;
    }

    public void setState(CardButtonState state) {
        if(state == CardButtonState.DEFAULT){
            this.setGraphic(cardBack);
        }
        else if(state == CardButtonState.NOT_IN_PLAY){
            this.setGraphic(null);
        }
        this.state = state;
    }

}
