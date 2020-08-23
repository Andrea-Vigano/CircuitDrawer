package version0_5.UI;

import version0_5.Engine.ViewportSpecs;
import version0_5.Main;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;

/**
 * Main UI panel for file export options
 */
public class ExportPanel extends JPanel {

    public static final ViewportSpecs vs = new ViewportSpecs();
    public final JSplitPane containerPane = new JSplitPane(1);
    public final ExportPreview preview;
    public final JPanel settingPanel = new JPanel();
    public final ExportSettingsSideBar settingsSideBar;

    public ExportPanel(ViewportSpecs _vs) {
        super();

        // load the Drawn Objects data into the new vs
        vs.loadExternalDO(_vs.DO);

        preview = new ExportPreview(vs, 100, 100);
        preview.setMinimumSize(new Dimension(600, 300));
        containerPane.setBorder(BorderFactory.createMatteBorder(20, 0, 5, 0, Main.win.getJMenuBar().getBackground()));
        preview.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, Main.win.getJMenuBar().getBackground()));
        containerPane.setDividerSize(20);
        containerPane.setUI(new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    public void setBorder(Border b) {

                    }

                    @Override
                    public void paint(Graphics g) {
                        g.setColor(Main.win.getJMenuBar().getBackground());
                        g.fillRect(0, 0, getSize().width, getSize().height);
                        super.paint(g);
                    }
                };
            }
        });
        settingsSideBar = new ExportSettingsSideBar();
        containerPane.setResizeWeight(1.0);
        setLayout(new BorderLayout());
        containerPane.add(preview);
        settingPanel.setLayout(new BorderLayout());
        settingPanel.add(settingsSideBar);
        containerPane.add(settingPanel);
        add(containerPane, BorderLayout.CENTER);

        setVisible(true);
    }
}
