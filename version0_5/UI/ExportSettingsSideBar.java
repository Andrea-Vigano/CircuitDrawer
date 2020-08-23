package version0_5.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Export panel side bar with export setting for image
 */
public class ExportSettingsSideBar extends JToolBar {

    public final JPanel containerPane = new JPanel();
    public final JLabel colorLabel = new JLabel("Color");
    public final ColorChooser color;
    public final JLabel widthLabel = new JLabel("Width");
    public final WidthChooser width = new WidthChooser();
    public final JLabel lineStyleLabel = new JLabel("Style");
    public final StyleChooser lineStyle = new StyleChooser();
    public final JCheckBox showGridLines = new JCheckBox("Show grid lines");
    public final JCheckBox showLabels = new JCheckBox("Show labels");
    public final JPanel advancedPanel = new JPanel();
    public final JButton advanced = new JButton("Advanced");
    public final JLabel imageSizeLabel = new JLabel("Image sizes:");
    public final JPanel imageSizePanel = new JPanel();
    public final JTextField imageWidth = new JTextField();
    public final JLabel xLabel = new JLabel("x");
    public final JTextField imageHeight = new JTextField();

    public final JPanel exportPanel = new JPanel();
    public final JButton exportAsJPG = new JButton("Export JPG"),
                         exportAsPNG = new JButton("Export PNG"),
                         exportAsDWG = new JButton("Export DWG"),
                         exportAsPDF = new JButton("Export PDF");

    public ExportSettingsSideBar() {
        super(1);

        setLayout(new BorderLayout());

        GridBagConstraints g = new GridBagConstraints();
        containerPane.setLayout(new GridBagLayout());

        String[] labels = new String[ColorChooser.colors.length];
        for (int i = 0; i < ColorChooser.colors.length; i++) {
            labels[i] =  " ";
        }
        color = new ColorChooser(labels);

        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 0.5;
        g.gridx = 0;
        g.gridy = 0;
        colorLabel.setHorizontalAlignment(RIGHT);
        colorLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        containerPane.add(colorLabel, g);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 0.5;
        g.gridx = 1;
        g.gridy = 0;
        containerPane.add(color, g);

        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 0.5;
        g.gridx = 0;
        g.gridy = 1;
        widthLabel.setHorizontalAlignment(RIGHT);
        widthLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        containerPane.add(widthLabel, g);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 0.5;
        g.gridx = 1;
        g.gridy = 1;
        containerPane.add(width, g);

        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 0.5;
        g.gridx = 0;
        g.gridy = 2;
        lineStyleLabel.setHorizontalAlignment(RIGHT);
        lineStyleLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        containerPane.add(lineStyleLabel, g);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 0.5;
        g.gridx = 1;
        g.gridy = 2;
        containerPane.add(lineStyle, g);

        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 0.5;
        g.gridwidth = 2;
        g.gridx = 0;
        g.gridy = 3;
        showGridLines.setBorder(new EmptyBorder(10, 10, 10, 10));
        containerPane.add(showGridLines, g);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 0.5;
        g.gridwidth = 2;
        g.gridx = 0;
        g.gridy = 4;
        showLabels.setBorder(new EmptyBorder(0, 10, 20, 10));
        containerPane.add(showLabels, g);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 0.5;
        g.gridwidth = 2;
        g.gridx = 0;
        g.gridy = 5;
        advancedPanel.setLayout(new BorderLayout());
        g.insets = new Insets(0, 0, 20, 0);
        advanced.setMargin(new Insets(3, 50, 3, 50));
        advancedPanel.add(advanced);
        containerPane.add(advancedPanel, g);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 0.5;
        g.gridwidth = 2;
        g.gridx = 0;
        g.gridy = 6;
        g.insets = new Insets(0, 5, 3, 0);
        containerPane.add(imageSizeLabel, g);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 0.5;
        g.gridwidth = 2;
        g.gridx = 0;
        g.gridy = 7;
        g.insets = new Insets(0, 5, 0, 5);
        imageSizePanel.setLayout(new BorderLayout());
        xLabel.setHorizontalTextPosition(CENTER);
        imageSizePanel.add(imageWidth, BorderLayout.WEST);
        imageSizePanel.add(xLabel, BorderLayout.CENTER);
        imageSizePanel.add(imageHeight, BorderLayout.EAST);
        containerPane.add(imageSizePanel, g);

        exportPanel.setLayout(new GridLayout(4, 1));
        exportPanel.add(exportAsJPG);
        exportPanel.add(exportAsPNG);
        exportPanel.add(exportAsDWG);
        exportPanel.add(exportAsPDF);

        add(containerPane, BorderLayout.NORTH);
        add(exportPanel, BorderLayout.SOUTH);
    }
}
