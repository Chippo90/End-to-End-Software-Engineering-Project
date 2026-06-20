package model;

import piles.Pile;

/**A simple data holding class that represents a single move in the game.
  A move consists of moving a card from a source pile to a destination pile.*/
public class Move {
    public final Pile sourcePile;
    public final Pile destinationPile;
    public final Card card; // The card being moved

    public Move(Pile source, Pile dest, Card card) {
        this.sourcePile = source;
        this.destinationPile = dest;
        this.card = card;
    }

    @Override
    public String toString() {
        //I use getClass().getSimpleName() to avoid printing the full package name.
        return "Move " + card.getShortName() + " from " + sourcePile.getClass().getSimpleName() + " to " + destinationPile.getClass().getSimpleName();
    }
}