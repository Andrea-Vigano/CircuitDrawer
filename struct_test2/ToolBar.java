package struct_test2;

import javax.swing.*;

public class ToolBar extends JToolBar {
    // Tool bar class

    JButton addWire = new JButton("Add wire");
    JButton addNode = new JButton("Add node");
    JButton addResistor = new JButton("Add resistor");

    public ToolBar() {
        super();

        this.add(addWire);
        this.add(addNode);
        this.add(addResistor);
    }
}
