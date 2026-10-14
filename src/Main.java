import controller.Controller;
import view.Window;

import static javax.swing.SwingUtilities.invokeLater;

/**
 * Application entry point. Creates the window and the controller on the Swing event thread.
 *
 * @author Radim Krb
 */
public class Main {

    public static void main(String[] args) {
        invokeLater(() -> {
            Window window = new Window(800, 600);
            new Controller(window.getCanvas()).init();
        });
    }

}
