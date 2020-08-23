package version0_5;

import com.formdev.flatlaf.FlatDarculaLaf;
import version0_5.UI.App;
import version0_5.Engine.ActionBindingManager;

import javax.swing.*;

// TODO add support for dwg files export and import
// TODO solve visual bug on the top row of the viewport
// TODO solve issue with the rotate button not updating the group button when shown
// TODO add new components and a tool bar section for all the other components
// TODO implement item selection support in viewport
// TODO implement items label in the viewport
// TODO set up settings side bar
// TODO set up edit side bar and bind it with viewport selection

/**
 * Run app, create JFrame, set it up, add App panel and MenuBar
 * Set look and feel
 */
public class Main {

    public static final JFrame win = new JFrame("Circuit drawer");;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch(Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        App app = new App();
        win.setJMenuBar(app.menuBar);
        win.getContentPane().add(app);
        win.setBounds(3433, 320, 1380, 773);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        win.setVisible(true);

        ActionBindingManager manager = new ActionBindingManager(app);
        manager.bindMenuAndSideBars();
        manager.addAppMouseListeners();
        manager.addExportMouseListeners();
        manager.bindToolBarButtons();
        manager.bindStateBarButtons();
        manager.bindFileSideBarButtons();
        manager.bindExportPanelButtons();
    }

    /**
     * Change the title string shown on the top of the frame
     *
     * @param title new title string
     */
    public static void changeFrameTitle(String title) {
        win.setTitle(title);
    }
}
