package struct_test3.style;

import javax.swing.*;
import java.awt.*;

/**
 * Tool Bar Button style
 */
public class ToolBarButton extends JButton {

    public ToolBarButton(String _text, Image icon) {
        super(_text);

        // change border
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        setMargin(new Insets(20, 1, 20, 1));
        // set BG color and text color
        setBackground(Color.GRAY);
        setForeground(Color.WHITE);
        setIcon(new ImageIcon(icon));
    }

    public ToolBarButton(String _text) {
        super(_text);

        // change border
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        setMargin(new Insets(20, 1, 20, 1));
        // set BG color and text color
        setBackground(Color.GRAY);
        setForeground(Color.WHITE);
    }
}
