package struct_test2;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class App extends JFrame{

    Viewport viewport = new Viewport();
    Container contentPane = this.getContentPane();

    public static void main(String[] args) {
        // Init and run app
        App app = new App();
    }

    App() {
        super("CircuitBuilder");
        // set frame dimensions
        this.setSize(600, 400);
        this.setVisible(true);
        // set CLOSE action
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.contentPane.add(viewport);
    }
}
