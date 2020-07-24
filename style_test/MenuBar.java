package style_test;

import javax.swing.*;

public class MenuBar extends JMenuBar {

    JMenu file = new JMenu("File"),
            settings = new JMenu("Settings"),
            edit = new JMenu("Edit");

    public MenuBar() {
        super();

        add(file);
        add(settings);
        add(edit);
    }

}
