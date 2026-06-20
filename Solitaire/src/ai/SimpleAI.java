package ai;

import game.Game;
import model.Card;
import model.Move;
import piles.FoundationPile;
import piles.TableauPile;

/**A simple AI that finds moves based on a fixed set of priorities.
 *It does not look; it just finds the first valid move in its priority list.
 */
public class SimpleAI implements SolitaireAI {

    @Override
    public Move findBestMove(Game game) {
        //Priority 1: Move any possible card to a Foundation pile. This is always a good move.
        //Check the top of the waste pile.
        Card wasteCard = game.getWastePile().peek();
        if (wasteCard != null) {
            for (FoundationPile foundation : game.getFoundationPiles()) {
                if (foundation.canAccept(wasteCard)) {
                    return new Move(game.getWastePile(), foundation, wasteCard);
                }
            }
        }
        //Check the top of each tableau pile.
        for (TableauPile tableau : game.getTableauPiles()) {
            if (!tableau.isEmpty()) {
                Card tableauCard = tableau.peek();
                if (tableauCard.isFaceUp()) {
                    for (FoundationPile foundation : game.getFoundationPiles()) {
                        if (foundation.canAccept(tableauCard)) {
                            return new Move(tableau, foundation, tableauCard);
                        }
                    }
                }
            }
        }
        //Priority 2: Move cards between Tableau piles to uncover face down cards.
        //Or move a full stack to make an empty space for a King.
        for (TableauPile sourceTableau : game.getTableauPiles()) {
            for (Card card : sourceTableau.getCards()) { // Iterate through all cards to find a movable sub-stack
                if (card.isFaceUp()) {
                    for (TableauPile destTableau : game.getTableauPiles()) {
                        if (sourceTableau != destTableau && destTableau.canAccept(card)) {
                            //We will just take the first one we find.
                            return new Move(sourceTableau, destTableau, card);
                        }
                    }
                }
            }
        }
        //Priority 3: Move a card from the Waste pile to a Tableau pile.
        if (wasteCard != null) {
            for (TableauPile tableau : game.getTableauPiles()) {
                if (tableau.canAccept(wasteCard)) {
                    return new Move(game.getWastePile(), tableau, wasteCard);
                }
            }
        }
        //Priority 4: If no other moves, suggest drawing from the Stock pile.
        //We represent this as a "move" from the Stock to the Waste pile.
        if (!game.getStockPile().isEmpty()) {
            return new Move(game.getStockPile(), game.getWastePile(), game.getStockPile().peek());
        }
        //Priority 5: If the stock is empty, recycle the waste pile.
        if (!game.getWastePile().isEmpty()) {
            return new Move(game.getWastePile(), game.getStockPile(), game.getWastePile().peek());
        }
        // If no moves are possible at all.
        return null;
    }
}