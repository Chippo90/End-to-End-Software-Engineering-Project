package model;

import java.util.Collections;
import java.util.Stack;

/**Represents a standard 52 card deck.
 *This class ensures a set of 52 cards is created and shuffled every time a new instance is made.*/
public class Deck {
    //A Stack is used as it's a natural way to represent a deck of cards.
    private final Stack<Card> cards;

    /**The constructor creates a deck of 52 cards.
     *This is the core logic that prevents duplicate cards.*/
    public Deck() {
        //Initialize a new, empty stack to hold the cards.
        this.cards = new Stack<>();

        //This programmatically builds a complete set of 52 cards.
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                // Add one of each card to the deck.
                cards.push(new Card(suit, rank));
            }
        }

        //Shuffle the new deck.
        shuffle();
    }

    /**Randomizes the order of the cards in the deck.*/
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**Deals one card from the top of the deck.*/
    public Card deal() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.pop();
    }

    public int size() {
        return cards.size();
    }
}