package piles;

import model.Card;
import java.awt.Graphics;
import java.util.Stack;

/**An abstract base class for all piles of cards in the game.
 *It uses a Stack to hold cards, as this is Last in  First out structure.*/
public abstract class Pile {
    // he physical location and size of the pile.
    protected int x;
    protected int y;
    public static final int CARD_WIDTH = 71;
    public static final int CARD_HEIGHT = 96;

    //Inside the Pile class, add these two methods:
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    //The stack holding the cards. 'protected' so subclasses can access it.
    protected final Stack<Card> cards = new Stack<>();

    public Pile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //Core Stack Operations
    public Card peek() { return cards.isEmpty() ? null : cards.peek(); }
    public Card pop() { return cards.isEmpty() ? null : cards.pop(); }
    public void push(Card card) { cards.push(card); }
    public boolean isEmpty() { return cards.isEmpty(); }
    public int size() { return cards.size(); }
    public Stack<Card> getCards() { return cards; }

    //GUI and Rule Logic
    /**Checks if a mouse click is inside the bounds of pile.*/
    public boolean includes(int pX, int pY) {
        return pX >= x && pX <= x + CARD_WIDTH && pY >= y && pY <= y + CARD_HEIGHT;
    }

    /**The core rule checking method. Each type of pile will implement this differently.*/
    public abstract boolean canAccept(Card card);

    /**Draws the pile on the screen.*/
    public abstract void draw(Graphics g);
}