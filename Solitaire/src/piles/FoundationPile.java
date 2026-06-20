package piles;

import model.Card;
import model.Rank;
import java.awt.Color;
import java.awt.Graphics;

/**The goal is to build these piles up from Ace to King for each suit.*/
public class FoundationPile extends Pile {

    public FoundationPile(int x, int y) {
        super(x, y);
    }

    /**Implements the rules for placing a card on a Foundation pile.
     *Rule 1: If the pile is empty, only an ACE can be placed.
     *Rule 2: If not empty, the card must be the same suit and one rank higher.*/
    @Override
    public boolean canAccept(Card card) {
        if (isEmpty()) {
            return card.getRank() == Rank.ACE;
        }
        Card topCard = peek();
        return topCard.getSuit() == card.getSuit() &&
                topCard.getRank().getValue() + 1 == card.getRank().getValue();
    }

    @Override
    public void draw(Graphics g) {
        //Contrasting color for the empty placeholder
        if (isEmpty()) {
            g.setColor(Color.DARK_GRAY);
            g.drawRect(x, y, CARD_WIDTH, CARD_HEIGHT);
            //Draw a symbol to indicate the purpose
            g.drawString("A", x + CARD_WIDTH / 2 - 4, y + CARD_HEIGHT / 2 + 5);
        } else {
            //Draw the top card of the pile if it exists
            Card topCard = peek();
            g.setColor(Color.WHITE);
            g.fillRect(x, y, CARD_WIDTH, CARD_HEIGHT);
            g.setColor(topCard.getColor());
            g.drawString(topCard.getShortName(), x + 5, y + 20);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, CARD_WIDTH, CARD_HEIGHT);
        }
    }
}