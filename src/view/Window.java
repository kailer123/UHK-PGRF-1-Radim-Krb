package view;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    private final Canvas canvas;

    public Window(int width, int height) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("FIM UHK | PGRF1 | 2026 | Radim Krb");
        setLayout(new BorderLayout());
        setResizable(false);

        canvas = new Canvas(width, height);
        add(canvas);
        pack();

        setLocationRelativeTo(null);
        canvas.setFocusable(true);
        canvas.grabFocus();
        setVisible(true);
    }

    public Canvas getCanvas() {
        return canvas;
    }

}