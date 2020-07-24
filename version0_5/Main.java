package version0_5;

import com.formdev.flatlaf.FlatDarculaLaf;
import version0_5.UI.App;
import version0_5.Engine.ActionBindingManager;

import javax.swing.*;

/**
 * Run app, create JFrame, set it up, add App panel and MenuBar
 * Set look and feel
 */
public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel( new FlatDarculaLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }

        JFrame win = new JFrame("Circuit drawer");
        App app = new App();
        win.setJMenuBar(app.menuBar);
        win.getContentPane().add(app);
        win.setBounds(3433, 320, 1380, 773);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        win.setVisible(true);

        ActionBindingManager manager = new ActionBindingManager(app);
        manager.bindMenuAndSideBars();
        manager.addAppMouseListeners();
        manager.bindToolBarButtons();
        manager.bindStateBarButtons();
        manager.bindFileSideBarButtons();
    }
}
