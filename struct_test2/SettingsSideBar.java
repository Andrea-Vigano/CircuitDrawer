package struct_test2;

import javax.swing.*;

public class SettingsSideBar extends JToolBar {

    JButton appearance = new JButton("Appearance");
    JButton keymap = new JButton("Keymap");

    public SettingsSideBar() {
        super(1);

        setVisible(false);

        this.add(appearance);
        this.add(keymap);
    }
}
