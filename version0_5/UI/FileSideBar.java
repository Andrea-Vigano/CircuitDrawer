package version0_5.UI;

import javax.swing.*;
import java.awt.*;

/**
 * File side bar class, only shown when the menu button "File" is pressed
 */
public class FileSideBar extends JPanel {

    // will use a panel to have the nicer buttons on the side bar
    JPanel containerPane = new JPanel();
    public JButton newCircuit = new JButton("New circuit");
    public JButton open = new JButton("Open");
    public JButton save = new JButton("Save");
    public JButton saveAs = new JButton("Save as...");
    public JButton export = new JButton("Export");

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
        g.insets = new Insets(0, 0, 0, 0);
        newCircuit.setMargin(new Insets(3, 50, 3, 50));
        containerPane.add(newCircuit, g);
        g.weightx = 0.5;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridy = 2;
        g.gridx = 0;
        g.insets = new Insets(0, 0, 20, 0);
        open.setMargin(new Insets(3, 50, 3, 50));
        containerPane.add(open, g);

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

        g.weightx = 0.5;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridy = 7;
        g.gridx = 0;
        g.insets = new Insets(0, 0, 20, 0);
        export.setMargin(new Insets(3, 50, 3, 50));
        containerPane.add(export, g);

        add(containerPane, BorderLayout.NORTH);
    }

}
