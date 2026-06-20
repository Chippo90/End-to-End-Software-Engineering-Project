package piles;

import model.Card;   //The data structure for a single card.
import model.Rank;   //The enum representing card ranks (Ace, King, etc.).
import java.awt.Color;   //Used for setting drawing colors.
import java.awt.Graphics;// he object used to draw shapes and text on the screen.
import java.util.Stack;  //The data structure used to hold the cards.

/**Represents one of the seven Tableau piles where the main gameplay occurs.*/
public class TableauPile extends Pile {
    private static final int Y_OFFSET = 20;

    /**The constructor for the TableauPile.*/
    public TableauPile(int x, int y) { super(x, y); }

    /**
     Implements the Gatekeeper rules for placing a card on a Tableau pile.*/
    @Override
    public boolean canAccept(Card card) {
        //Rule 1: If the pile is empty, only a King can be placed.
        if (isEmpty()) {
            return card.getRank() == Rank.KING;
        }
        //Get the card currently on top of the pile.
        Card topCard = peek();
        //Safety check to prevent errors.
        if(topCard == null || card == null) return false;
        //Rule 2: The new card must be the opposite color AND one rank lower than the top card.
        return topCard.getColor() != card.getColor() &&
                topCard.getRank().getValue() - 1 == card.getRank().getValue();
    }

    /**
     * Overrides the default method to check if a mouse click is within the pile's bounds.*/
    @Override
    public boolean includes(int pX, int pY) {
        //If the pile is empty, use the simple check from the parent class.
        if (isEmpty()) {
            return super.includes(pX, pY);
        }
        //Calculate the total height of the pile, including the overlaps.
        int height = CARD_HEIGHT + (cards.size() - 1) * Y_OFFSET;
        //Check if the click coordinates fall within the calculated rectangular area.
        return pX >= x && pX <= x + CARD_WIDTH && pY >= y && pY <= y + height;
    }

    /**A helper method to find which specific card in the vertical stack was clicked.*/
    public Card getCardAt(int pY) {
        //Loop backwards from the top card, as it's drawn last and is visually on top.
        for (int i = cards.size() - 1; i >= 0; i--) {
            Card card = cards.get(i);
            //Calculate the y-coordinate of the top of the current card.
            int cardTopY = y + i * Y_OFFSET;
            //Calculate the y-coordinate of the bottom of the clickable area for this card.
            int cardBottomY = (i == cards.size() - 1) ? cardTopY + CARD_HEIGHT : cardTopY + Y_OFFSET;
            //If the click is within this card's vertical space...
            if (pY >= cardTopY && pY < cardBottomY) {
                //return this card.
                return card;
            }
        }
        //If no card was at that y-coordinate, return null.
        return null;
    }

    /**A safe method to split the pile. It uses standard list operations to prevent corruption.*/
    public Stack<Card> split(Card card) {
        //Find the index of the card where the split should occur.
        int splitIndex = cards.indexOf(card);
        //If the card is not found, return an empty stack to prevent errors.
        if (splitIndex == -1) {
            return new Stack<>();
        }
        //Create a new stack to hold the cards that will be moved.
        Stack<Card> movedCards = new Stack<>();
        //Get a view of the sublist of cards to be moved and add them all to the new stack.
        movedCards.addAll(cards.subList(splitIndex, cards.size()));
        //Remove that same sublist of cards from this pile.
        cards.subList(splitIndex, cards.size()).clear();
        //Return the stack of cards that were removed.
        return movedCards;
    }

    @Override
    public void draw(Graphics g) {
        //If the pile is empty, draw a dark gray placeholder rectangle.
        if (isEmpty()) {
            g.setColor(Color.DARK_GRAY);
            g.drawRect(x, y, CARD_WIDTH, CARD_HEIGHT);
        } else {
            //If the pile has cards, loop through and draw each one.
            for (int i = 0; i < cards.size(); i++) {
                Card card = cards.get(i);
                if (card == null) continue;// Safety check to prevent crashes from corrupted data.
                //Calculate the y-position for this specific card based on its index.
                int currentY = y + (i * Y_OFFSET);
                //Check if the card is face up.
                if (card.isFaceUp()) {
                    //If so, draw a white rectangle with the card's rank and suit.
                    g.setColor(Color.WHITE);
                    g.fillRect(x, currentY, CARD_WIDTH, CARD_HEIGHT);
                    g.setColor(card.getColor());
                    g.drawString(card.getShortName(), x + 5, currentY + 20);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, currentY, CARD_WIDTH, CARD_HEIGHT);
                } else {
                    //If not, draw a blue rectangle representing the back of the card.
                    g.setColor(Color.BLUE);
                    g.fillRect(x, currentY, CARD_WIDTH, CARD_HEIGHT);
                    g.setColor(Color.WHITE);
                    g.drawRect(x, currentY, CARD_WIDTH, CARD_HEIGHT);
                }
            }
        }
    }
}