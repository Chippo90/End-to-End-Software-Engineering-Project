
import ui.GameFrame;
import javax.swing.SwingUtilities;

/**Main entry point for the Solitaire application.*/
public class Main {
    public static void main(String[] args) {
        //Practice in Swing to create and show the GUI on the Event Dispatch Thread  to avoid potential threading issues.
        SwingUtilities.invokeLater(() -> new GameFrame());
    }
}
