package struct_test2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOError;
import java.io.IOException;

// TODO menu bar feature (with settings opening on a side bar)
// TODO tool bar feature
// TODO add other drawable elements

public class App extends JFrame {

    Viewport viewport = new Viewport();
    MenuBar menuBar = new MenuBar();
    ToolBar toolBar = new ToolBar();
    JPanel sideBar = new JPanel();
    JToolBar[] sideBars = {new FileSideBar(), new SettingsSideBar()};
    Container contentPane = this.getContentPane();

    public static void main(String[] args) {
        // Init and run app
        new App();
    }

    App() {
        super("CircuitBuilder");
        // set frame dimensions
        this.setSize(600, 400);
        // this.setName("App");
        // set CLOSE action
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        this.addMouseListener();

        this.menuBar.file.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean visible = sideBars[0].isVisible();
                clearSideBar();
                sideBars[0].setVisible(!visible);
            }
        });
        this.menuBar.settings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean visible = sideBars[1].isVisible();
                clearSideBar();
                sideBars[1].setVisible(!visible);
            }
        });
        ((FileSideBar) this.sideBars[0]).save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DrawFile fileManager = new DrawFile(viewport.getDrawnObjects());
                try {
                    fileManager.saveFile(new File("draw.csv"));
                } catch (IOException exc) {
                    System.out.println(exc.getMessage());
            }
        }
        });
        ((FileSideBar) this.sideBars[0]).open.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                   viewport.setDrawnObjects(DrawFile.loadFile("draw.csv"));
                } catch (IOException exc) {
                    System.out.println(exc.getMessage());
                }
            }
        });

        this.contentPane.add(toolBar, BorderLayout.NORTH);
        this.contentPane.add(viewport, BorderLayout.CENTER);
        this.contentPane.add(sideBar, BorderLayout.EAST);
        for (JToolBar _sideBar : this.sideBars) {
            this.sideBar.add(_sideBar);
        }
        this.setJMenuBar(menuBar);

        this.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                viewport.requestFocusInWindow();

                System.out.println(DefaultFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner());
            }

            @Override
            public void windowLostFocus(WindowEvent e) {

            }
        });

        this.setVisible(true);
    }

    private void clearSideBar() {
        for (JToolBar _sideBar : this.sideBars) {
            _sideBar.setVisible(false);
        }
    }
}
