package struct_test2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileSideBar extends JToolBar {

    JButton newCircuit = new JButton("New circuit");
    JButton open = new JButton("Open");
    JButton openRecent = new JButton("Open recent");
    JButton save = new JButton("Save");
    JButton saveAs = new JButton("Save as...");

    public FileSideBar() {
        super(1);

        setVisible(false);

        this.add(newCircuit);
        this.add(open);
        this.add(openRecent);
        this.add(save);
        this.add(saveAs);
    }
}
