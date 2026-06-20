package piles;

import model.Card;
import java.awt.Color;
import java.awt.Graphics;

/**The pile where cards from the Stock are placed face up.
 *The top card of this pile to be played.*/
public class WastePile extends Pile {

    public WastePile(int x, int y) {
        super(x, y);
    }

    /**A waste pile doesn't follow normal "canAccept" rules.
     *Cards are added to it only from the stock pile.*/
    @Override
    public boolean canAccept(Card card) {
        return false;
    }

    /**The push method works fine for adding from stock but we ensure the card is face up.*/
    @Override
    public void push(Card card) {
        if (card == null) return; // Safety check
        card.setFaceUp(true);
        super.push(card);
    }

    @Override
    public void draw(Graphics g) {
        //Contrasting color for the empty placeholder ---
        if (isEmpty()) {
            g.setColor(Color.DARK_GRAY);
            g.drawRect(x, y, CARD_WIDTH, CARD_HEIGHT);
        } else {
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