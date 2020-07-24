package version0_5.Engine;

import version0_5.Components.Resistor;
import version0_5.Components.Transistor;
import version0_5.Components.Wire;
import version0_5.UI.App;
import version0_5.UI.FileSideBar;
import version0_5.UI.OpenFileChooser;
import version0_5.UI.Viewport;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.*;
import java.io.File;

/**
 * Bind and hooks all the GUI pieces together to interact with the back end.
 */
public class ActionBindingManager {

    public App app;
    private final Viewport viewport;
    private final ViewportSpecs vs;

    public ActionBindingManager(App app) {
        this.app = app;
        viewport = app.viewport;
        vs = viewport.vs;
    }

    private void changeSideBar(int index) {
        boolean isVisible = app.sideBar.sideBars[index].isVisible();
        app.sideBar.clear();
        app.sideBar.sideBars[index].setVisible(!isVisible);
        app.horizontalSplitPane.resetToPreferredSizes();
    }

    private void updateWire() {
        vs.DO.wires.getLast().setEnd(vs.getAbsCords(vs.CGI));
        vs.DO.wires.getLast().angle = vs.wireAngle;
    }
    private void updateResistor() {
        vs.DO.resistors.getLast().setEnd(vs.getAbsCords(vs.CGI));
    }
    private void updateTransistor() {
        int rotation = vs.transistorRotation;
        vs.DO.transistors.getLast().rotation = vs.transistorRotation;
        int[] cgi = vs.getAbsCords(vs.CGI);
        if (rotation == 0) {
            vs.DO.transistors.getLast().origin = new int[] {cgi[0] - vs.DEFAULT_GRID_SIZE, cgi[1] - vs.DEFAULT_GRID_SIZE};
            vs.DO.transistors.getLast().setEnd(cgi[0] + vs.DEFAULT_GRID_SIZE, cgi[1] - vs.DEFAULT_GRID_SIZE);
        } else if (rotation == 1) {
            vs.DO.transistors.getLast().origin = new int[] {cgi[0] + vs.DEFAULT_GRID_SIZE, cgi[1] + vs.DEFAULT_GRID_SIZE};
            vs.DO.transistors.getLast().setEnd(cgi[0] + vs.DEFAULT_GRID_SIZE, cgi[1] - vs.DEFAULT_GRID_SIZE);
        } else if (rotation == 2) {
            vs.DO.transistors.getLast().origin = new int[] {cgi[0] - vs.DEFAULT_GRID_SIZE, cgi[1] + vs.DEFAULT_GRID_SIZE};
            vs.DO.transistors.getLast().setEnd(cgi[0] + vs.DEFAULT_GRID_SIZE, cgi[1] + vs.DEFAULT_GRID_SIZE);
        } else {
            vs.DO.transistors.getLast().origin = new int[] {cgi[0] - vs.DEFAULT_GRID_SIZE, cgi[1] - vs.DEFAULT_GRID_SIZE};
            vs.DO.transistors.getLast().setEnd(cgi[0] - vs.DEFAULT_GRID_SIZE, cgi[1] + vs.DEFAULT_GRID_SIZE);
        }
    }
    private void saveAsAction(ActionEvent e) {
        boolean exit;
        JFileChooser fileChooser = new JFileChooser();
        System.out.println("Opening save as dialog");
        do {
            int option = fileChooser.showSaveDialog(app);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                // check if file already exists
                if (file.exists()) {
                    int overwrite = JOptionPane.showConfirmDialog(fileChooser, "The selected file already exists," +
                                    "would you like to overwrite it?", "Overwrite",
                            JOptionPane.YES_NO_CANCEL_OPTION);
                    exit = overwrite != JOptionPane.NO_OPTION;
                } else {
                    exit = true;
                }
                if (exit) {
                    System.out.println("File saved at: " + file.getAbsolutePath());
                    // save file
                    SaveFile.saveAs(file.getName(), file.getAbsolutePath(), vs.DO);
                }
            } else {
                System.out.println("Save command canceled");
                exit = true;
            }
        } while (!exit);
    }

    /**
     * Bind menu buttons to the side bars.
     */
    public void bindMenuAndSideBars() {
        app.menuBar.file.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                changeSideBar(0);
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });
        app.menuBar.settings.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                changeSideBar(1);
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });
        app.menuBar.edit.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                changeSideBar(2);
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });
    }

    /**
     * Add mouseListeners to app, implementing viewport functionalities
     * and component drawing logic.
     */
    public void addAppMouseListeners() {
        app.viewport.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (e.getModifiersEx() == InputEvent.BUTTON2_DOWN_MASK)
                    vs.offset = new int[] {vs.offset[0] + e.getX() - vs.direction[0],
                            vs.offset[1] + e.getY() - vs.direction[1]};
                vs.direction = new int[] {e.getX(), e.getY()};
                vs.CGI = vs.getCGI(e.getX(), e.getY());
                viewport.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                vs.CGI = vs.getCGI(e.getX(), e.getY());
                if (vs.action == 2) {
                    // update visual wire end
                    updateWire();
                } else if (vs.action == 4) {
                    // update visual resistor end
                    updateResistor();
                } else if (vs.action == 5) {
                    // update transistor end
                    updateTransistor();
                }
                viewport.repaint();
            }
        });
        app.viewport.addMouseWheelListener(e -> {
            if (e.getWheelRotation() < 0) {
                vs.zoomFactor /= vs.ZOOM_INCREASE;
            } else {
                vs.zoomFactor *= vs.ZOOM_INCREASE;
            }
            vs.gridSize = (int) (vs.DEFAULT_GRID_SIZE / vs.zoomFactor);
            vs.CGI = vs.getCGI(e.getX(), e.getY());
            viewport.repaint();
        });
        app.viewport.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (vs.action == 1) {
                    // if vs.action == 1: add wire origin
                    vs.DO.wires.addLast(new Wire(vs.getAbsCords(vs.CGI)));
                    app.toolBar.changeWireAngle.setVisible(true);
                    System.out.println("Action 2 selected");
                    vs.action = 2;
                    updateWire();
                } else if (vs.action == 2) {
                    // if vs.action == 2: add wire end
                    updateWire();
                    vs.actionList.add(2);
                    // continue drawing wires (do not change action)
                    vs.DO.wires.addLast(new Wire(vs.getAbsCords(vs.CGI)));
                    updateWire();
                } else if (vs.action == 3) {
                    // if vs.action == 3: add resistor origin
                    vs.DO.resistors.addLast(new Resistor(vs.getAbsCords(vs.CGI)));
                    System.out.println("Action 4 selected");
                    vs.action = 4;
                    updateResistor();
                } else if (vs.action == 4) {
                    // if vs.action == 4: add resistor end
                    updateResistor();
                    vs.actionList.add(4);
                    System.out.println("Action 0 selected");
                    app.stateBar.actionLabel.setText("Hovering");
                    vs.action = 0;
                } else if (vs.action == 5) {
                    // if vs.action == 5: add transistor
                    updateTransistor();
                    vs.actionList.add(5);
                    app.toolBar.rotateTransistor.setVisible(false);
                    System.out.println("Action 0 selected");
                    app.stateBar.actionLabel.setText("Hovering");
                    vs.action = 0;
                }
                viewport.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // prepare for pad
                vs.direction = new int[] {e.getX(), e.getY()};
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    /**
     * Bind tool bar button to their action in the viewport specs.
     * Hook hotkeys for each action + add icons + update action label in state bar.
     */
    public void bindToolBarButtons() {
        Action action;

        // addWire button: action = 1, hotkey = "Alt + W"
        action = new AbstractAction(app.toolBar.addWire.getText()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Action 1 selected");
                app.stateBar.actionLabel.setText("Drawing wire");
                vs.action = 1;
            }
        };
        app.toolBar.addWire.setAction(action);
        app.toolBar.addWire.setIcon(new ImageIcon("wire.png"));
        action.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_W);
        // changeWireAngle button: action == 2, hotkey = "Alt + I"
        action = new AbstractAction(app.toolBar.changeWireAngle.getText()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (vs.action == 2) {
                    if (vs.wireAngle == 2) {
                        vs.wireAngle = 0;
                    } else {
                        vs.wireAngle++;
                    }
                    System.out.println("Change wire angle to: " + vs.wireAngle);
                    updateWire();
                    app.viewport.repaint();
                }
            }
        };
        app.toolBar.changeWireAngle.setAction(action);
        app.toolBar.changeWireAngle.setIcon(new ImageIcon("ruler.png"));
        app.toolBar.changeWireAngle.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), app.toolBar.changeWireAngle.getText());
        app.toolBar.changeWireAngle.getActionMap().put(app.toolBar.changeWireAngle.getText(), action);
        // addResistor button: action = 3, hotkey = "Alt + R"
        action = new AbstractAction(app.toolBar.addResistor.getText()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Action 3 selected");
                app.stateBar.actionLabel.setText("Drawing resistor");
                vs.action = 3;
            }
        };
        app.toolBar.addResistor.setAction(action);
        app.toolBar.addResistor.setIcon(new ImageIcon("resistor.png"));
        action.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
        // addTransistor button: action = 5, hotkey = "Alt + T"
        action = new AbstractAction(app.toolBar.addTransistor.getText()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Action 5 selected");
                app.stateBar.actionLabel.setText("Drawing transistor");
                vs.action = 5;
                vs.DO.transistors.addLast(new Transistor(vs.getAbsCords(vs.CGI)));
                app.toolBar.rotateTransistor.setVisible(true);
                updateTransistor();
            }
        };
        app.toolBar.addTransistor.setAction(action);
        app.toolBar.addTransistor.setIcon(new ImageIcon("transistor.png"));
        action.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);
        // rotateTransistor button: action = 5, hotkey = "R"
        action = new AbstractAction(app.toolBar.rotateTransistor.getText()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (vs.transistorRotation < 3) {
                    vs.transistorRotation++;
                } else {
                    vs.transistorRotation = 0;
                }
                System.out.println("Change transistor rotation to: " + vs.transistorRotation);
                updateTransistor();
                app.viewport.repaint();
            }
        };
        app.toolBar.rotateTransistor.setAction(action);
        app.toolBar.rotateTransistor.setIcon(new ImageIcon("refresh.png"));
        app.toolBar.rotateTransistor.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), app.toolBar.rotateTransistor.getText());
        app.toolBar.rotateTransistor.getActionMap().put(app.toolBar.rotateTransistor.getText(), action);
        // undoButton: undo last action, hotkey = "Ctrl + Z"
        action = new AbstractAction(app.toolBar.undoButton.getText()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                vs.actionList.undo();
                app.viewport.repaint();
            }
        };
        app.toolBar.undoButton.setAction(action);
        app.toolBar.undoButton.setIcon(new ImageIcon("undo.png"));
        app.toolBar.undoButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK), app.toolBar.undoButton.getText());
        app.toolBar.undoButton.getActionMap().put(app.toolBar.undoButton.getText(), action);
        // redoButton: redo last action, hotkey = "Ctrl + Y"
        action = new AbstractAction(app.toolBar.redoButton.getText()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                vs.actionList.redo();
                app.viewport.repaint();
            }
        };
        app.toolBar.redoButton.setAction(action);
        app.toolBar.redoButton.setIcon(new ImageIcon("redo.png"));
        app.toolBar.redoButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK), app.toolBar.redoButton.getText());
        app.toolBar.redoButton.getActionMap().put(app.toolBar.redoButton.getText(), action);
    }

    /**
     * Bind state bar buttons to their actions in the viewport specs.
     */
    public void bindStateBarButtons() {
        Action action;

        // Delete button: action = 0, hotkey = "Esc";
        action = new AbstractAction(app.stateBar.deleteButton.getText()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (vs.action == 2) {
                    vs.DO.wires.removeLast();
                    app.toolBar.changeWireAngle.setVisible(false);
                } else if (vs.action == 4) {
                    vs.DO.resistors.removeLast();
                } else if (vs.action == 5) {
                    vs.DO.transistors.removeLast();
                    app.toolBar.rotateTransistor.setVisible(false);
                }
                System.out.println("Reset action to 0");
                app.stateBar.actionLabel.setText("Hovering");
                vs.action = 0;
                app.viewport.repaint();
            }
        };
        app.stateBar.deleteButton.setAction(action);
        app.stateBar.deleteButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), app.stateBar.deleteButton.getText());
        app.stateBar.deleteButton.getActionMap().put(app.stateBar.deleteButton.getText(), action);
    }

    /**
     * Bind file side bar buttons to their actions
     */
    public void bindFileSideBarButtons() {
        Action action;

        // saveAs button: action = showSaveDialog, hotkey = "Ctrl + Shift + S"
        action = new AbstractAction(((FileSideBar) (app.sideBar.sideBars[0])).saveAs.getText()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAsAction(e);
            }
        };
        ((FileSideBar) (app.sideBar.sideBars[0])).saveAs.setAction(action);
        ((FileSideBar) (app.sideBar.sideBars[0])).saveAs.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK + KeyEvent.SHIFT_DOWN_MASK),
                ((FileSideBar) (app.sideBar.sideBars[0])).saveAs.getText());
        ((FileSideBar) (app.sideBar.sideBars[0])).saveAs.getActionMap().put(((FileSideBar) (app.sideBar.sideBars[0])).saveAs.getText(), action);
        // save button: action = SaveFile.save, hotkey = "Ctrl + S"
        action = new AbstractAction(((FileSideBar) (app.sideBar.sideBars[0])).save.getText()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SaveFile.save(vs.DO);
                } catch (NullPointerException exc) {
                    // name and path not defined
                    saveAsAction(e);
                }
            }
        };
        ((FileSideBar) (app.sideBar.sideBars[0])).save.setAction(action);
        ((FileSideBar) (app.sideBar.sideBars[0])).save.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK),
                ((FileSideBar) (app.sideBar.sideBars[0])).save.getText());
        ((FileSideBar) (app.sideBar.sideBars[0])).save.getActionMap().put(((FileSideBar) (app.sideBar.sideBars[0])).save.getText(), action);
        // open button: action = showOpenDialog, hotkey = "Ctrl + O"
        action = new AbstractAction(((FileSideBar) (app.sideBar.sideBars[0])).open.getText()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (OpenFile.current.length() > 0) {
                    SaveFile.save(vs.DO);
                }
                OpenFileChooser fileChooser = new OpenFileChooser();
                // Get recently opened files up to date
                fileChooser.updateRecent();
                System.out.println("Opening open dialog");
                int option = fileChooser.showOpenDialog(app);
                if (option == JFileChooser.APPROVE_OPTION) {
                    // get selected file
                    File file = fileChooser.getSelectedFile();
                    OpenFile.openFile(file.getAbsolutePath(), vs.DO);
                    app.viewport.repaint();
                }
            }
        };
        ((FileSideBar) (app.sideBar.sideBars[0])).open.setAction(action);
        ((FileSideBar) (app.sideBar.sideBars[0])).open.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK),
                ((FileSideBar) (app.sideBar.sideBars[0])).open.getText());
        ((FileSideBar) (app.sideBar.sideBars[0])).open.getActionMap().put(((FileSideBar) (app.sideBar.sideBars[0])).open.getText(), action);
    }
}
