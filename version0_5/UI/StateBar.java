package version0_5.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * State bar class
 */
public class StateBar extends JPanel {
    
    public final JLabel currentLabel = new JLabel("Current: ");
    public final JLabel actionLabel = new JLabel("Hovering");
    public final JPanel statePanel = new JPanel();
    public final JPanel buttonPanel = new JPanel();
    public final JButton deleteButton = new JButton("Delete");

    public StateBar() {
        super();

        setLayout(new BorderLayout());

        Font f = currentLabel.getFont();
        currentLabel.setFont(f.deriveFont(f.getStyle() | Font.BOLD));

        statePanel.add(currentLabel);
        statePanel.add(actionLabel);

        add(statePanel, BorderLayout.WEST);
        add(buttonPanel, BorderLayout.EAST);
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.setBorder(new EmptyBorder(3, 5, 5, 5 ));
        buttonPanel.add(deleteButton, BorderLayout.NORTH);
    }
}
