package model;

import java.awt.Color;

/**Represents a single playing card. It has a suit, a rank, and a state.*/
public class Card {
    private final Suit suit;
    private final Rank rank;
    private boolean isFaceUp;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
        this.isFaceUp = false; // Cards start face-down by default.
    }
    //Standard getter methods
    public Suit getSuit() { return suit; }
    public Rank getRank() { return rank; }
    public boolean isFaceUp() { return isFaceUp; }

    //Method to flip the card
    public void setFaceUp(boolean faceUp) {
        isFaceUp = faceUp;
    }

    /**Determines the color of the card based on its suit.
     * Used for rule-checking (alternating colors in the tableau) and for drawing.*/
    public Color getColor() {
        if (suit == Suit.HEARTS || suit == Suit.DIAMONDS) {
            return Color.RED;
        }
        return Color.BLACK;
    }
    /**A short string representation for the card used for drawing the card on the screen.*/
    public String getShortName() {
        String rankString;
        switch(rank) {
            case ACE: rankString = "A"; break;
            case JACK: rankString = "J"; break;
            case QUEEN: rankString = "Q"; break;
            case KING: rankString = "K"; break;
            default: rankString = String.valueOf(rank.getValue());
        }
        String suitString;
        switch(suit) {
            case HEARTS: suitString = "♥"; break;
            case DIAMONDS: suitString = "♦"; break;
            case CLUBS: suitString = "♣"; break;
            case SPADES: suitString = "♠"; break;
            default: suitString = "?";
        }
        return rankString + suitString;
    }

    @Override
    public String toString() {
        return rank + "_OF_" + suit;
    }
}