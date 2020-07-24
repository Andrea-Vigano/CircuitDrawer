package style_test;

import javax.swing.*;
import java.awt.*;

/**
 * File side bar class, only shown when the menu button "File" is pressed
 */
public class FileSideBar extends JPanel {

    // will use a panel to have the nicer buttons on the side bar
    JPanel containerPane = new JPanel();
    JButton newCircuit = new JButton("New circuit");
    JButton open = new JButton("Open");
    JButton openRecent = new JButton("Open recent");
    JButton save = new JButton("Save");
    JButton saveAs = new JButton("Save as...");

    public FileSideBar() {
        super();

        setLayout(new BorderLayout());

        setVisible(false);
        containerPane.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();

        g.weightx = 0.5;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridy = 0;
        g.gridx = 0;
        g.insets = new Insets(0, 0, 20, 0);
        newCircuit.setMargin(new Insets(3, 50, 3, 50));
        containerPane.add(newCircuit, g);
        g.weightx = 0.5;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridy = 2;
        g.gridx = 0;
        g.insets = new Insets(0, 0, 0, 0);
        open.setMargin(new Insets(3, 50, 3, 50));
        containerPane.add(open, g);
        g.weightx = 0.5;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridy = 3;
        g.gridx = 0;
        g.insets = new Insets(0, 0, 20, 0);
        openRecent.setMargin(new Insets(3, 50, 3, 50));
        containerPane.add(openRecent, g);

        g.weightx = 0.5;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridy = 5;
        g.gridx = 0;
        g.insets = new Insets(0, 0, 0, 0);
        save.setMargin(new Insets(3, 50, 3, 50));
        containerPane.add(save, g);
        g.weightx = 0.5;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridy = 6;
        g.gridx = 0;
        g.insets = new Insets(0, 0, 20, 0);
        saveAs.setMargin(new Insets(3, 50, 3, 50));
        containerPane.add(saveAs, g);


        add(containerPane, BorderLayout.NORTH);
    }
}