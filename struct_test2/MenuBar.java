package struct_test2;

import javax.swing.*;

public class MenuBar extends JMenuBar {
    // Menu bar class

    JButton file = new JButton("File");
    JButton settings = new JButton("Settings");
    JButton edit = new JButton("Edit");
    JButton help = new JButton("Help");

    public MenuBar() {
        this.add(file);
        this.add(settings);
        this.add(edit);
        this.add(help);
    }
}
