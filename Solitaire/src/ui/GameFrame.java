
package ui;

import ai.HintAI;      // The AI class used to generate hints.
import game.Game;      // The main game engine (the Model).
import model.Move;     // A data object representing a single move action.
import javax.swing.*;  // Imports all classes from the Swing library for the GUI.
import java.awt.*;     // Imports classes for basic AWT functionality like layout managers.

/**The main window frame for the Solitaire application.It holds the BoardPanel and control buttons.*/
public class GameFrame extends JFrame {
    //A reference to the main game object, which holds the true state of the game.
    private Game game;
    //A reference to the panel where the game is drawn.
    private BoardPanel boardPanel;
    //A reference to the AI engine used for providing hints.
    private final HintAI hintAI;
    //A label at the bottom of the screen to display hints and status messages.
    private JLabel statusLabel;

    /**The constructor for the GameFrame. It initializes and assembles all UI components.*/
    public GameFrame() {
        //Create an instance of the game engine.
        game = new Game();
        //Create an instance of the AI hint provider.
        hintAI = new HintAI();
        //Create the main drawing panel, passing it a reference to the game model so it knows what to draw.
        boardPanel = new BoardPanel(game);

        //Window Setup
        setTitle("Solitaire"); //Sets the text in the window's title bar.
        setSize(800, 600); //Sets the initial width and height of the window.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Ensures the application closes when the window is closed.
        setLocationRelativeTo(null); //Centers the window on the screen on startup.

        //Create the status label with a welcome message.
        statusLabel = new JLabel("Welcome to Solitaire! Good luck.");

        //Create the "New Game" button.
        JButton newGameButton = new JButton("New Game");
        //Add an action listener to define what happens when the button is clicked.
        newGameButton.addActionListener(e -> {
            //Create a brand new, clean Game object.
            game = new Game();
            //Tell the BoardPanel to use this new Game object.
            boardPanel.setGame(game);
            //Update the status label for the new game.
            statusLabel.setText("New game started. Good luck!");
            //Force the board panel to redraw itself to show the new game state.
            boardPanel.repaint();
        });

        //reate the "Hint" button.
        JButton hintButton = new JButton("Hint");
        //Add an action listener for the hint button.
        hintButton.addActionListener(e -> {
            //Ask the AI to find the best move based on the current game state.
            Move hint = hintAI.findBestMove(game);
            //If the AI found a valid move...
            if (hint != null) {
                //Display the hint in the status label.
                statusLabel.setText("Hint: " + hint.toString());
            } else {
                //Otherwise, inform the user that no moves are available.
                statusLabel.setText("No moves found. You may need to draw or recycle.");
            }
        });

        //Layout Management
        //Create a new panel to hold the controls at the bottom of the window.
        JPanel controlPanel = new JPanel();
        //Add the buttons and status label to this control panel.
        controlPanel.add(newGameButton);
        controlPanel.add(hintButton);
        controlPanel.add(statusLabel);

        //Set the layout manager for the main frame to BorderLayout.
        setLayout(new BorderLayout());
        //Add the main game board to the center of the frame, where it will take up most of the space.
        add(boardPanel, BorderLayout.CENTER);
        //Add the control panel to the bottom (south) of the frame.
        add(controlPanel, BorderLayout.SOUTH);

        //Make the window and all its components visible to the user.
        setVisible(true);
    }
}