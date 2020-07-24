package version0_5.UI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;

/**
 * Main UI panel
 */
public class App extends JPanel {
    public final MenuBar menuBar = new MenuBar();
    public final ToolBar toolBar = new ToolBar();
    public final JSplitPane horizontalSplitPane = new JSplitPane(1);
    public final JSplitPane verticalSplitPane = new JSplitPane(0);
    public final SideBar sideBar = new SideBar();
    public final Viewport viewport = new Viewport();
    public final StateBar stateBar = new StateBar();

    public App() {
        super();
        setLayout(new BorderLayout());

        add(toolBar, BorderLayout.NORTH);
        viewport.setMinimumSize(new Dimension(600, 300));
        horizontalSplitPane.setResizeWeight(1.0);
        horizontalSplitPane.add(viewport);
        horizontalSplitPane.add(sideBar);
        horizontalSplitPane.setDividerSize(20);
        horizontalSplitPane.setUI(new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    public void setBorder(Border b) {

                    }

                    @Override
                    public void paint(Graphics g) {
                        g.setColor(menuBar.getBackground());
                        g.fillRect(0, 0, getSize().width, getSize().height);
                        super.paint(g);
                    }
                };
            }
        });
        horizontalSplitPane.setBorder(BorderFactory.createMatteBorder(20, 5, 5, 0,
                menuBar.getBackground()));
        verticalSplitPane.setResizeWeight(1.0);
        verticalSplitPane.add(horizontalSplitPane);
        verticalSplitPane.add(stateBar);
        add(verticalSplitPane, BorderLayout.CENTER);

        setVisible(true);
    }

}
