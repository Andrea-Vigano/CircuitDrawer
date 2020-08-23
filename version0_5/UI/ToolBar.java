package version0_5.UI;

import javax.swing.*;
import java.awt.*;

public class ToolBar extends JToolBar {

    JPanel addPanel = new JPanel(),
            modifyPanel = new JPanel();

    public JButton addWire = new JButton("Wire", new ImageIcon("wire.png")),
            addNode = new JButton("Node", new ImageIcon("anchor-point.png")),
            addResistor = new JButton("Resistor", new ImageIcon("resistor.png")),
            addTransistor = new JButton("Transistor", new ImageIcon("transistor.png")),
            addButton = new JButton("Add"),
            undoButton = new JButton("Undo", new ImageIcon("undo.png")),
            redoButton = new JButton("Redo", new ImageIcon("redo.png")),
            modifyButton = new JButton("Modify");

    // Additional add options (only shown when the "Add" button is pressed)
    public JButton addCCG = new JButton("Generator"),
            addCapacitor = new JButton(" Capacitor"),
            addInductor = new JButton("Inductor"),
            addVoltmeter = new JButton("Voltmeter"),
            addAmperometer = new JButton("Amperometer"),
            addLamp = new JButton("Lamp");

    // Additional Buttons (only shown when some actions are being performed)
    public JButton changeWireAngle = new JButton("Orthogonal"),
            rotateTransistor = new JButton("Rotate");

    public ToolBar() {
        super();

        setLayout(new FlowLayout(FlowLayout.LEFT));
        GridBagConstraints g = new GridBagConstraints();

        // Will have different JPanels for different tool types

        // Add panel
        addPanel.setLayout(new GridBagLayout());

        // Add buttons in place
        g.weightx = 0.0;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridy = 0;
        g.gridx = 0;
        addWire.setHorizontalTextPosition(CENTER);
        addWire.setVerticalTextPosition(BOTTOM);
        addWire.setMargin(new Insets(10, 10, 5, 10));
        addPanel.add(addWire, g);
        g.weightx = 0.0;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridy = 0;
        g.gridx = 1;
        addNode.setHorizontalTextPosition(CENTER);
        addNode.setVerticalTextPosition(BOTTOM);
        addNode.setMargin(new Insets(10, 10, 5, 10));
        addPanel.add(addNode, g);
        g.weightx = 0.0;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridy = 0;
        g.gridx = 2;
        addResistor.setHorizontalTextPosition(CENTER);
        addResistor.setVerticalTextPosition(BOTTOM);
        addResistor.setMargin(new Insets(10, 10, 5, 10));
        addPanel.add(addResistor, g);
        g.weightx = 0.0;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridy = 0;
        g.gridx = 3;
        addTransistor.setHorizontalTextPosition(CENTER);
        addTransistor.setVerticalTextPosition(BOTTOM);
        addTransistor.setMargin(new Insets(10, 10, 5, 10));
        addPanel.add(addTransistor, g);

        g.fill = GridBagConstraints.HORIZONTAL;
        addButton.setBorderPainted(false);
        addButton.setBackground(getBackground());
        g.weightx = 0.0;
        g.gridwidth = 4;
        g.gridx = 0;
        g.gridy = 1;
        addPanel.add(addButton, g);

        add(addPanel);

        addSeparator();

        // Modify panel
        modifyPanel.setLayout(new GridBagLayout());
        g = new GridBagConstraints();

        g.weightx = 0.0;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridy = 0;
        g.gridx = 0;
        undoButton.setHorizontalTextPosition(CENTER);
        undoButton.setVerticalTextPosition(BOTTOM);
        undoButton.setMargin(new Insets(10, 10, 5, 10));
        modifyPanel.add(undoButton, g);
        g.weightx = 0.0;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridy = 0;
        g.gridx = 1;
        redoButton.setHorizontalTextPosition(CENTER);
        redoButton.setVerticalTextPosition(BOTTOM);
        redoButton.setMargin(new Insets(10, 10, 5, 10));
        modifyPanel.add(redoButton, g);

        g.fill = GridBagConstraints.HORIZONTAL;
        modifyButton.setBorderPainted(false);
        modifyButton.setBackground(getBackground());
        g.weightx = 0.0;
        g.gridwidth = 4;
        g.gridx = 0;
        g.gridy = 1;
        modifyPanel.add(modifyButton, g);

        g = new GridBagConstraints();

        g.weightx = 0.0;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridy = 0;
        g.gridx = 3;
        changeWireAngle.setHorizontalTextPosition(CENTER);
        changeWireAngle.setVerticalTextPosition(BOTTOM);
        changeWireAngle.setMargin(new Insets(10, 10, 5, 10));
        changeWireAngle.setVisible(false);
        modifyPanel.add(changeWireAngle, g);
        g.weightx = 0.0;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridy = 0;
        g.gridx = 4;
        rotateTransistor.setHorizontalTextPosition(CENTER);
        rotateTransistor.setVerticalTextPosition(BOTTOM);
        rotateTransistor.setMargin(new Insets(10, 10, 5, 10));
        rotateTransistor.setVisible(false);
        modifyPanel.add(rotateTransistor, g);

        add(modifyPanel);

        addSeparator();
    }

}
