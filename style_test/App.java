package style_test;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;

/**
 * Style test
 */
public class App extends JFrame {

    public final MenuBar menuBar = new MenuBar();
    public final ToolBar toolBar = new ToolBar();
    public final JSplitPane horizontalSplitPane = new JSplitPane(1);
    public final JSplitPane verticalSplitPane = new JSplitPane(0);
    public final SideBar sideBar = new SideBar();
    public final Viewport viewport = new Viewport();
    public final JPanel stateBar = new JPanel();

    public App() {
        super("Style test");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(3433, 320, 1380, 773);
        Container contentPane = getContentPane();
        setLayout(new BorderLayout());

        setJMenuBar(menuBar);
        contentPane.add(toolBar, BorderLayout.NORTH);
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
        stateBar.add(new Label("State bar info point..."));
        verticalSplitPane.add(stateBar);
        contentPane.add(verticalSplitPane, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel( new FlatDarculaLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }

        ActionBindingManager manager = new ActionBindingManager(new App());
        // hook back end to UI
        manager.bindMenuAndSideBars();
        manager.addAppMouseListeners();
    }

}
