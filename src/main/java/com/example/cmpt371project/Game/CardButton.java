package com.example.cmpt371project.Game;

/**
 * @author Steve
 * @created 2022-07-28
 * @project cmpt371-team04
 */

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;

public class CardButton extends Button {
    public enum CardButtonState {
        DEFAULT,
        FLIPPED,
        IN_USE,
        NOT_IN_PLAY
    }

    /**
     * The value of the card e.g. 2C for 2 of clubs, AH for ace of hearts
     */
    private String value;
    private ImageView cardFront;
    private ImageView cardBack;
    private CardButtonState state;
    private static final String CARD_BACK_FILE_IMAGE = "/img/backs/eecs_shervin_shirmohammadi.jpg";
    private static final int CARD_SIZE = 125;

    public CardButton(ImageView cardFront, String value) {
        super();
        this.borderProperty();
        this.value = value;
        this.setBackground(Background.EMPTY);

        this.cardBack = new ImageView(getClass().getResource(CARD_BACK_FILE_IMAGE).toExternalForm());
        cardBack.setFitHeight(CARD_SIZE);
        cardBack.setPreserveRatio(true);
        this.setGraphic(cardBack);
        this.state = CardButtonState.DEFAULT;

        this.cardFront = cardFront;
        cardFront.setFitHeight(CARD_SIZE);
        cardFront.setPreserveRatio(true);
    }

    public CardButton(String s, Node node, String value, ImageView cardFront, CardButtonState state) {
        super(s, node);
        this.value = value;
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

    //Object Locking Scheme with the objects shared between the 4 players being the cards
    //If CardButtonState is DEFAULT, the object is shared
    //If a DEFAULT CardButton is clicked by a client:
    // - the Client who clicked will have the card become FLIPPED
    // - other Clients will have the clicked card become IN_USE
    // - the clicked card becomes locked and unusable.
    // - IF a DEFAULT card clicked matches a previous card FLIPPED:
    //    - The current card and previous card becomes NOT_IN_PLAY
    //    - Points will be awarded to the matched client's score
    // - IF a DEFAULT card clicked does not match a previous card FLIPPED:
    //    - The current card and previous card revert to DEFAULT
    //    - all clients will have the two objects unlocked as shared objects
    public void setState(CardButtonState state) {
        if(state == CardButtonState.DEFAULT){
            this.setGraphic(cardBack);
            this.setMouseTransparent(false);
            this.setDisable(false);
        }

        else if(state == CardButtonState.FLIPPED){
            this.setGraphic(cardFront);
            this.setMouseTransparent(true);
        }

        else if(state == CardButtonState.NOT_IN_PLAY){
            this.setDisable(true);
            this.setVisible(false);
        }

        else if(state == CardButtonState.IN_USE) {
            this.setGraphic(cardFront);
            this.setDisable(true);
        }

        this.state = state;
    }

    @Override
    public String toString() {
        return value;
    }
}
