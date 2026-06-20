package ai;

import game.Game;
import model.Card;
import model.Move;
import piles.FoundationPile;
import piles.TableauPile;
import java.util.ArrayList;
import java.util.List;

/**An advanced AI that finds the best move by scoring the outcome of all possible moves.
 *It simulates each move and uses a heuristic function to decide which result is best.*/
public class HintAI implements SolitaireAI {

    @Override
    public Move findBestMove(Game game) {
        List<Move> possibleMoves = findAllPossibleMoves(game);
        Move bestMove = null;
        int bestScore = -1; // Start with a very low score

        // Evaluate each possible move
        for (Move move : possibleMoves) {
            // We don't need a deep copy for this simple scoring.
            // A smarter AI might simulate multiple moves ahead.
            int score = calculateMoveScore(move, game);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    /**A heuristic function to assign a score to a potential move. Higher scores are better.*/
    private int calculateMoveScore(Move move, Game game) {
        int score = 0;

        //Highest priority: Moving any card to the foundation is always good.
        if (move.destinationPile instanceof FoundationPile) {
            score += 100;
        }
        //Uncovering a face down card in the tableau is very good.
        if (move.sourcePile instanceof TableauPile) {
            TableauPile source = (TableauPile) move.sourcePile;
            // Check if this move would uncover a face-down card
            if (!source.isEmpty() && source.peek() != move.card && !source.peek().isFaceUp()) {
                score += 50;
            }
        }
        //Moving a King to an empty tableau spot is good.
        if (move.destinationPile instanceof TableauPile) {
            if (((TableauPile) move.destinationPile).isEmpty() && move.card.getRank().getValue() == 13) {
                score += 25;
            }
        }
        //Moving from the waste pile is a standard progress move.
        if (move.destinationPile instanceof TableauPile) {
            score += 10;
        }
        //Drawing from the stock is low score.
        if (move.sourcePile == game.getStockPile()) {
            score += 1;
        }
        return score;
    }

    /**Scans the entire board to find every possible legal move from the current state.*/
    private List<Move> findAllPossibleMoves(Game game) {
        List<Move> moves = new ArrayList<>();
        Card wasteCard = game.getWastePile().peek();
        //1. Check moves from Waste to Foundation/Tableau
        if (wasteCard != null) {
            for (FoundationPile p : game.getFoundationPiles()) {
                if (p.canAccept(wasteCard)) moves.add(new Move(game.getWastePile(), p, wasteCard));
            }
            for (TableauPile p : game.getTableauPiles()) {
                if (p.canAccept(wasteCard)) moves.add(new Move(game.getWastePile(), p, wasteCard));
            }
        }
        //2. Check moves from Tableau to Foundation/other Tableau
        for (TableauPile source : game.getTableauPiles()) {
            if (!source.isEmpty()) {
                Card topCard = source.peek();
                //To Foundation
                for (FoundationPile p : game.getFoundationPiles()) {
                    if (p.canAccept(topCard)) moves.add(new Move(source, p, topCard));
                }
                //To other Tableau
                for (TableauPile dest : game.getTableauPiles()) {
                    if (source != dest && dest.canAccept(topCard)) moves.add(new Move(source, dest, topCard));
                }
                //Check for moving stacks
                for(Card card : source.getCards()) {
                    if (card.isFaceUp() && card != topCard) {
                        for (TableauPile dest : game.getTableauPiles()) {
                            if (source != dest && dest.canAccept(card)) moves.add(new Move(source, dest, card));
                        }
                    }
                }
            }
        }
        //3. Check for drawing from stock
        if (!game.getStockPile().isEmpty()) {
            moves.add(new Move(game.getStockPile(), game.getWastePile(), game.getStockPile().peek()));
        } else if (!game.getWastePile().isEmpty()) {
            moves.add(new Move(game.getWastePile(), game.getStockPile(), game.getWastePile().peek()));
        }

        return moves;
    }
}