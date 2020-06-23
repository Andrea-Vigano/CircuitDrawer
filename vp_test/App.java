package vp_test;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {
    /*
     * App class
     * */

    Container contentPane = this.getContentPane();
    Viewport viewport = new Viewport();

    public static void main(String[] args) {
        // run app
        App app = new App();
        app.setVisible(true);
    }

    App() {
        // build grid panel
        super("CircuitBuilder");
        this.setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.contentPane.add(this.viewport);
    }
}
