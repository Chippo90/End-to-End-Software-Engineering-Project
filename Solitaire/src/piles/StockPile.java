package piles;

import model.Card;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Stack;

/**The pile of face down cards from which the player draws.*/
public class StockPile extends Pile {

    public StockPile(int x, int y) {
        super(x, y);
    }

    /**A stock pile never accepts a single card. Moves to it only happen when recycling the waste pile.*/
    @Override
    public boolean canAccept(Card card) {
        return false;
    }

    /**Takes all cards from the waste pile and places them back onto the stock, face down.*/
    public void recycle(Stack<Card> wasteCards) {
        while (!wasteCards.isEmpty()) {
            Card card = wasteCards.pop();
            card.setFaceUp(false);
            this.push(card);
        }
    }

    @Override
    public void draw(Graphics g) {
        if (isEmpty()) {
            // --- FIX: Use a contrasting color for the empty placeholder ---
            g.setColor(Color.DARK_GRAY);
            g.drawRect(x, y, CARD_WIDTH, CARD_HEIGHT);
            g.drawString("R", x + CARD_WIDTH / 2 - 4, y + CARD_HEIGHT / 2 + 5);
        } else {
            // Draw the back of a card
            g.setColor(Color.BLUE);
            g.fillRect(x, y, CARD_WIDTH, CARD_HEIGHT);
            g.setColor(Color.WHITE);
            g.drawRect(x, y, CARD_WIDTH, CARD_HEIGHT);
        }
    }
}