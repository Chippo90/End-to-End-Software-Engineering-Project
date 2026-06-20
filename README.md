**Java Solitaire Game with Heuristic AI**

This project is a fully functional Solitaire game developed in Java, featuring a clean graphical user interface and an intelligent AI assistant to provide hints.

**Project Overview**

The primary goal of this project was to build a and maintainable Solitaire application by leveraging software architecture. The game allows a user to play a standard game of Klondike Solitaire and includes a "Hint" feature powered by a heuristic-based AI to suggest optimal moves.

**System Architecture**

*   **Presentation Layer “UI”**: Presenting the game state to the user and capturing the input. It has no control over the game rules. 
*   **Logic Layer “Game Engine”**: Acting as the brain of the application. It contains the core game logic, processes command from the Presentation Layer and manipulates the Data Layer according to the game's rules
*   **Data Layer “Foundation”)**: Representing the data structures and rules. It defines what a card is, what a deck is, and the rules for placing a card in piles. 

**Features**

*   **Full Solitaire Gameplay:** All standard rules are implemented, including drawing from the stock, building tableau piles, and moving cards to the foundation.
*   **Graphical User Interface (GUI):** A clean, intuitive interface built with Java Swing allows for easy drag-and-drop gameplay.
*   **Heuristic AI Hint System:** A smart assistant that analyzes the board and suggests the most immediately beneficial move based on a scoring system.
*   **Robust State Management:** The "New Game" feature correctly resets the board, providing a fresh and uncorrupted state every time.

**How to Run**

1.  Compile all `.java` files from the `src` directory.
2.  Run the `Main.java` file to launch the application.
3.  The game window will appear, and a new game will be dealt automatically.

**Future Work**

*   Advanced AI: Implementing a search-based algorithm that can look multiple moves ahead to provide long-term strategic advice.
*   Undo/Redo Functionality: Adding a feature to undo moves would greatly improve player experience.
*   Timer & Score: Adding timer along with scoring board, since both are measuring the best scores.
*   UI/UX Enhancements: Improving the visual assets with high-resolution card graphics and adding smooth animations for card movements.

**License**
* Name: Chehab Hany
* University: Gisma University of Applied Sciences
