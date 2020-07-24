package struct_test3.style;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Menu button class.
 * Define the style of the Buttons of the MenuBar.
 */
public class MenuButton extends JButton {

    public MenuButton(String _text) {
        // Add HTML styling to text
        super(_text);

        // change border
        setBorder(new EmptyBorder(1, 10, 1, 10));
        // set BG color and text color
        setBackground(Color.GRAY);
        setForeground(Color.WHITE);
    }

}
