package app;

import javax.swing.SwingUtilities;
import view.Tool;

public class Run {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Tool t = new Tool();
                t.createAndShowGUI();
            }
        });
    }
}
