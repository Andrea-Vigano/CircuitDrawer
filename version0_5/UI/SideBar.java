package version0_5.UI;

import javax.swing.*;
import java.awt.*;

/**
 * SideBar panel, all the sidebars are made visible one at the time depending on the button that has been pressed
 */
public class SideBar extends JPanel {

    public final JPanel[] sideBars = {new FileSideBar(), new SettingsSideBar(), new EditSideBar()};

    public SideBar() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(sideBars[0], BorderLayout.CENTER);
    }

    public void clear() {
        for (JPanel sideBar : sideBars) {
            sideBar.setVisible(false);
        }
    }
}