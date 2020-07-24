package version0_5.UI;

import javax.swing.*;

/**
 * Menu bar class
 */
public class MenuBar extends JMenuBar {

    public JMenu file = new JMenu("File"),
            settings = new JMenu("Settings"),
            edit = new JMenu("Edit");

    public MenuBar() {
        super();

        add(file);
        add(settings);
        add(edit);
    }

}
