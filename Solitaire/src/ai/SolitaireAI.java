package ai;

import game.Game;
import model.Move;

/**An interface for any class that can play or suggest moves in Solitaire.
 *This allows us to easily swap different AI implementations Simple vs Hint.*/
public interface SolitaireAI {
    /**Analyzes the current state of the game and finds the best possible move.*/
    Move findBestMove(Game game);
}