package ui;

import game.Game;            // The main game engine (the Model).
import model.Card;           // The data structure for a single card.
import model.Move;           // A data object representing a single move action.
import piles.Pile;           // The abstract base class for all card piles.
import piles.TableauPile;    // The specific class for tableau piles, needed for stack-dragging logic.
import javax.swing.JOptionPane; // Used to show the "You Won!" pop-up message.
import javax.swing.JPanel;      // The Swing component this class extends to become a drawable canvas.
import java.awt.Color;          // Used for setting drawing colors.
import java.awt.Graphics;       // The object used to draw shapes and text on the screen.
import java.awt.event.MouseEvent; // An object containing information about a mouse event (like clicks or drags).
import java.awt.event.MouseListener; // An interface for handling mouse button press, release, and click events.
import java.awt.event.MouseMotionListener; // An interface for handling mouse movement and dragging events.
import java.util.Stack;         // The data structure used to hold the visually dragged cards.

/**The main panel where the game is drawn and user interaction is handled.*/
public class BoardPanel extends JPanel implements MouseListener, MouseMotionListener {
    //A reference to the main game object , which holds the true state of the game.
    private Game game;
    //State variables for managing a drag-and-drop action
    //Remembers which pile the drag started from.
    private Pile sourcePileForDrag = null;
    //A visual copy of the cards being dragged. The actual game state is not touched.
    private Stack<Card> draggedCards = null;
    //Offsets to make the card appear to be grabbed from where the mouse clicked.
    private int dragOffsetX, dragOffsetY;

    /**The constructor for the BoardPanel.*/
    public BoardPanel(Game game) {
        //Store the reference to the game object.
        this.game = game;
        //Set the background to a classic green.
        setBackground(new Color(0, 128, 0));
        //Register this panel to listen for mouse clicks and presses.
        addMouseListener(this);
        //Register this panel to listen for mouse dragging.
        addMouseMotionListener(this);
    }

    /**Allows the GameFrame to assign a new Game object to this panel, used for starting a new game.*/
    public void setGame(Game newGame) { this.game = newGame; }

    /**The core drawing method. Swing calls this automatically whenever the panel needs to be redrawn.*/
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Loop through all piles in the game object...
        for (Pile p : game.getAllPiles()) {
            p.draw(g); //Tell each pile to draw itself.
        }
        //If a visual drag is in progress...
        if (draggedCards != null && !draggedCards.isEmpty() && getMousePosition() != null) {
            //Get the current mouse coordinates for drawing.
            int currentX = getMousePosition().x - dragOffsetX;
            int currentY = getMousePosition().y - dragOffsetY;
            //Loop through the visual copy of the dragged cards...
            for (Card card : draggedCards) {
                //Draw them at the current mouse position.
                if (card.isFaceUp()) {
                    g.setColor(Color.WHITE);
                    g.fillRect(currentX, currentY, Pile.CARD_WIDTH, Pile.CARD_HEIGHT);
                    g.setColor(card.getColor());
                    g.drawString(card.getShortName(), currentX + 5, currentY + 20);
                    g.setColor(Color.BLACK);
                    g.drawRect(currentX, currentY, Pile.CARD_WIDTH, Pile.CARD_HEIGHT);
                }
                currentY += 20;
            }
        }
    }

    /**Called when the mouse button is pressed down. This is part of the Controller*/
    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX(); //Get the x-coordinate of the mouse press.
        int y = e.getY(); //Get the y-coordinate of the mouse press.

        //Check if the Stock Pile was clicked.
        if (game.getStockPile().includes(x, y)) {
            if (game.getStockPile().isEmpty()) {
                game.recycleWaste(); //If empty, recycle the waste pile.
            } else {
                game.drawFromStock(); //Otherwise, draw a card.
            }
            repaint(); //Redraw the screen to show the result.
            return; //Stop processing, as the action is complete.
        }

        //Loop through all piles to find which one was clicked to start a drag.
        for (Pile p : game.getAllPiles()) {
            if (p.includes(x, y) && !p.isEmpty()) {
                sourcePileForDrag = p; //Remember where the drag began.
                // f the source is a Tableau pile, handle multi-card dragging.
                if (p instanceof TableauPile) {
                    Card clickedCard = ((TableauPile) p).getCardAt(y);
                    if (clickedCard != null && clickedCard.isFaceUp()) {
                        draggedCards = new Stack<>(); //Create a new stack for the visual copy.
                        int index = p.getCards().indexOf(clickedCard); //Find the index of the clicked card.
                        //Copy the clicked card and all cards below it into the visual drag stack.
                        for (int i = index; i < p.getCards().size(); i++) {
                            draggedCards.push(p.getCards().get(i));
                        }
                    }
                } else { //For single card piles.
                    draggedCards = new Stack<>();
                    draggedCards.push(p.peek()); // Just copy the top card.
                }
                //If we successfully created a stack to drag.
                if (draggedCards != null && !draggedCards.isEmpty()) {
                    //Calculate the offset for smooth dragging.
                    dragOffsetX = x - p.getX();
                    dragOffsetY = y - (p.getY() + (p.size() - draggedCards.size()) * 20);
                    break; //Stop looking for other piles.
                }
            }
        }
    }

    /**Called continuously while the mouse is moved with the button held down.*/
    @Override
    public void mouseDragged(MouseEvent e) {
        if (draggedCards != null) { repaint(); } // If we are dragging, just repaint to show the movement.
    }

    /**Called when the mouse button is released, ending a click or drag.*/
    @Override
    public void mouseReleased(MouseEvent e) {
        //If we weren't dragging anything, do nothing.
        if (draggedCards == null || draggedCards.isEmpty()) {
            return;
        }
        //Find which pile the mouse was released over.
        Pile destinationPile = null;
        for (Pile p : game.getAllPiles()) {
            if (p != sourcePileForDrag && p.includes(e.getX(), e.getY())) {
                destinationPile = p;
                break;
            }
        }
        //The card that defines the move's validity is the first one in the dragged stack.
        Card cardToMove = draggedCards.firstElement();
        //Check if the move is legal by asking the destination pile's "Gatekeeper" [1].
        if (destinationPile != null && destinationPile.canAccept(cardToMove)) {
            // If the move is valid, send a command to the Model to execute it [1].
            game.executeMove(new Move(sourcePileForDrag, destinationPile, cardToMove));
        }
        //Discard the visual drag variables.
        draggedCards = null;
        sourcePileForDrag = null;
        //Repaint the screen to show the final, true state from the Model.
        repaint();
        //After the move, check if the player has won.
        if (game.checkForWin()) {
            JOptionPane.showMessageDialog(this, "Congratulations, you won!");
        }
    }

    //Unused Mouse Listener Methods
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
}