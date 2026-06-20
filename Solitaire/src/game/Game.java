package game;

import model.Card;
import model.Deck;
import model.Move;
import piles.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**The main engine of the Solitaire game, acting as the "Model".
 *This class manages the game state, holds all the card piles and handles the core logic like dealing and executing moves.*/
public class Game {
    //A list that holds every single pile for iteration .
    private final List<Pile> allPiles;
    //Specific references to the different types of piles for rule logic.
    private final StockPile stockPile;
    private final WastePile wastePile;
    private final List<FoundationPile> foundationPiles;
    private final List<TableauPile> tableauPiles;

    /**The constructor for the Game class. It sets up a new, complete game of Solitaire.*/
    public Game() {
        //Initialize the lists to hold the different pile objects.
        this.allPiles = new ArrayList<>();
        this.foundationPiles = new ArrayList<>();
        this.tableauPiles = new ArrayList<>();
        //Create the Stock and Waste piles at specific coordinates.
        this.stockPile = new StockPile(20, 20);
        this.wastePile = new WastePile(110, 20);
        allPiles.add(stockPile);
        allPiles.add(wastePile);
        //Loop four times to create the four Foundation piles.
        for (int i = 0; i < 4; i++) {
            //Create each pile at a new x-coordinate to put them horizontally.
            FoundationPile f = new FoundationPile(300 + i * 90, 20);
            //Add the new pile to its list and to the master list.
            foundationPiles.add(f);
            allPiles.add(f);
        }
        //Loop seven times to create the seven Tableau piles for gameplay.
        for (int i = 0; i < 7; i++) {
            //Create each pile at a new x-coordinate.
            TableauPile t = new TableauPile(20 + i * 90, 150);
            //Add the new pile to its list and to the master list.
            tableauPiles.add(t);
            allPiles.add(t);
        }
        //After all piles are created, deal the cards to start the game.
        dealCards();
    }
    /**Sets up the initial board state by creating a new deck and dealing the cards according to the rules of Solitaire.*/
    private void dealCards() {
        //Create new,52 card deck.
        Deck deck = new Deck();
        //This nested loop performs the deal.
        //The outer loop iterates through the number of cards to deal in this round.
        for (int i = 0; i < 7; i++) {
            //The inner loop deals one card to each subsequent pile.
            for (int j = i; j < 7; j++) {
                tableauPiles.get(j).push(deck.deal());
            }
        }
        //After dealing, flip the top card of each tableau pile face up.
        for (TableauPile tableau : tableauPiles) {
            if (!tableau.isEmpty()) {
                tableau.peek().setFaceUp(true);
            }
        }
        //Place all remaining cards from the deck onto the stock pile.
        while (deck.size() > 0) {
            stockPile.push(deck.deal());
        }
    }
    /**Recycles all cards from the Waste pile back to the Stock pile.*/
    public void recycleWaste() {
        //Do nothing if the stock isn't empty or the waste is empty.
        if (!stockPile.isEmpty() || wastePile.isEmpty()) return;
        //Delegate the actual recycling logic to the StockPile class.
        stockPile.recycle(wastePile.getCards());
    }
    /**Draws a single card from the Stock pile and places it onto the Waste pile.*/
    public void drawFromStock() {
        //Do nothing if the stock is empty.
        if (!stockPile.isEmpty()) {
            //Pop one card from stock and push it to waste.
            wastePile.push(stockPile.pop());
        }
    }
    /**Executes a move action described by a Move object. This is the central method for changing the game state.*/
    public void executeMove(Move move) {
        //A stack to hold the card or cards that are being moved.
        Stack<Card> cardsToMove;
        //Check if the move originates from a Tableau pile, which can move multiple cards.
        if (move.sourcePile instanceof TableauPile) {
            //If so, call the 'split' method on the tableau pile to get the stack of cards.
            cardsToMove = ((TableauPile) move.sourcePile).split(move.card);
        } else {
            //Otherwise, it's a single-card move from the Waste or a Foundation.
            cardsToMove = new Stack<>();
            Card c = move.sourcePile.pop();
            if (c != null) cardsToMove.push(c);
        }
        //Check if cards to move.
        if (cardsToMove != null && !cardsToMove.isEmpty()) {
            //Iterate through the stack of cards to move.
            for (Card card : cardsToMove) {
                //Add each card to the destination pile.
                move.destinationPile.push(card);
            }
        }
        //After a move from a Tableau pile, a new card might be revealed.
        if (move.sourcePile instanceof TableauPile) {
            Pile source = move.sourcePile;
            //If the pile is not empty and its new top card is face down.
            if (!source.isEmpty() && !source.peek().isFaceUp()) {
                source.peek().setFaceUp(true);
            }
        }
    }
    /**Checks if the game has been won.*/
    public boolean checkForWin() {
        //Loop through each of the four foundation piles.
        for (FoundationPile pile : foundationPiles) {
            //If any pile does not have exactly 13 cards (Ace to King)...
            if (pile.size() != 13) return false;
        }
        //If the loop completes, it means all four piles are full.
        return true;
    }

    //These public methods allow other parts of the application to get read-only access to the game's state without being able to modify it.
    public StockPile getStockPile() { return stockPile; }
    public WastePile getWastePile() { return wastePile; }
    public List<FoundationPile> getFoundationPiles() { return foundationPiles; }
    public List<TableauPile> getTableauPiles() { return tableauPiles; }
    public List<Pile> getAllPiles() { return allPiles; }
}