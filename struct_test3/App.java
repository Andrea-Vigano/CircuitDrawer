package struct_test3;

import struct_test3.style.MenuButton;
import struct_test3.style.ToolBarButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.LinkedList;

import static java.lang.Math.pow;
import static java.lang.Math.round;

public class App extends JFrame
    implements MouseWheelListener, MouseMotionListener, MouseListener {
    /**
     * App class, JFrame extension
     *
     * Main app class, all the components should be defined inside this object
     * so that the Event listeners can all be applied to this one object alone without the need of any kind
     * of event dispatcher
     */

    // Frame specs
    private Dimension DEFAULT_DIMENSION = new Dimension(1380, 773);
    private Container contentPane = getContentPane();

    // viewport specs data storage
    static final ViewportSpecs vs = new ViewportSpecs();

    // Drawn data storage
    static LinkedList<Wire> wires = new LinkedList<>();
    static LinkedList<Resistor> resistors = new LinkedList<>();
    static LinkedList<Transistor> transistors = new LinkedList<>();

    // Actions tracker
    // none = 0,
    // draw wire origin = 1, draw wire end = 2,
    // draw resistor origin = 3, draw resistor end = 4,
    // draw transistor = 5
    static byte action = 0;
    
    // Add components: Menu, ToolBar, SideBar Panel, SideBars, Viewport
    private final ToolBar toolBar = new ToolBar();

    private final JPanel sideBarPanel = new JPanel();

    // Will define a vertical toolBar for each Button in the menu, add them to the sidebarPanel and selectively show them
    private JToolBar[] sideBars = {new FileSideBar()};

    private final JMenuBar menu = new JMenuBar() {
        {
            // MenuBar fields
            final JButton file = new MenuButton("File");
            final JButton settings = new MenuButton("Settings");
            final JButton edit = new MenuButton("Edit");

            setBackground(Color.GRAY);
            setBorderPainted(false);

            // add actions
            file.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean visible = sideBars[0].isVisible();
                    clearSideBar();
                    sideBars[0].setVisible(!visible);
                }
            });

            add(file);
            add(settings);
            add(edit);
        }
    };
    
    private final JPanel viewport = new JPanel() {
        /**
         * Paint component
         * draw grid lines with data from viewportSpecs
         * draw snapping circle at CGI (Closest Grid Intersection)
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // draw grid lines
            g.setColor(vs.GRID_COLOR);
            for (int i = vs.offset[0] % vs.gridSize; i < getWidth(); i = i + vs.gridSize) {
                g.drawLine(i, 0, i, getHeight());
            }
            for (int i = vs.offset[1] % vs.gridSize; i < getHeight(); i = i + vs.gridSize) {
                g.drawLine(0, i, getWidth(), i);
            }
            // draw snapping circle in CGI
            g.setColor(vs.SNAPPING_CIRCLE_COLOR);
            g.fillOval(vs.CGI[0] - vs.SNAPPING_CIRCLE_RADIUS / 2, vs.CGI[1] - vs.SNAPPING_CIRCLE_RADIUS / 2,
                    vs.SNAPPING_CIRCLE_RADIUS, vs.SNAPPING_CIRCLE_RADIUS);

            Graphics2D g2d = (Graphics2D) g;

            // draw wires
            g2d.setColor(vs.WIRE_COLOR);
            g2d.setStroke(new BasicStroke(vs.WIRE_WIDTH));
            for (Wire wire : wires) {
                int[] wireOrigin = vs.getCGI(vs.getRelCords(wire.originCords));
                int[] wireEnd = vs.getCGI(vs.getRelCords(wire.endCords));
                if (!wire.rightAngle) {
                    g2d.drawLine(wireOrigin[0], wireOrigin[1], wireEnd[0], wireEnd[1]);
                } else {
                    if (wire.inverted) {
                        g2d.drawLine(wireOrigin[0], wireOrigin[1], wireOrigin[0], wireEnd[1]);
                        g2d.drawLine(wireOrigin[0], wireEnd[1], wireEnd[0], wireEnd[1]);
                    } else {
                        g2d.drawLine(wireOrigin[0], wireOrigin[1], wireEnd[0], wireOrigin[1]);
                        g2d.drawLine(wireEnd[0], wireOrigin[1], wireEnd[0], wireEnd[1]);
                    }
                }
            }

            // Draw resistors
            g2d.setColor(vs.RESISTOR_COLOR);
            g2d.setStroke(new BasicStroke((vs.RESISTOR_WIDTH)));
            // Resistor origin and end relative position and triangle sizes
            int[] relOrigin;
            int[] relEnd;
            double length, width, height;
            // Horizontal step and Vertical step
            double[] step = {0, 0};
            // optimized steps number
            int n = 1;
            // constant
            double k = 1;
            // offset of the spike top from the intersection of normal at the resistor line passing through the top point
            int[] topOffset = {0, 0};
            // temp array to store cords and double to store step while drawing the spikes
            double[] temp = {0, 0};
            for (Resistor resistor : resistors) {
                // Get relative origin and end
                relOrigin = vs.getCGI(vs.getRelCords(resistor.originCords));
                relEnd = vs.getCGI(vs.getRelCords(resistor.endCords));
                temp[0] = relOrigin[0];
                temp[1] = relOrigin[1];
                // Get sizes
                width = relOrigin[0] - relEnd[0];
                height = relOrigin[1] - relEnd[1];
                length = pow((pow(width, 2) + pow(height, 2)), 0.5);
                // steps number
                n = vs.RESISTOR_SPIKES_WIDTH_PER_GRID * ((int) (round(length / vs.gridSize)));
                // HS and VS
                step[0] = width / n / 2;
                step[1] = height / n / 2;
                // Get top offset
                k = vs.RESISTOR_SPIKES_HEIGHT / vs.zoomFactor / length;
                topOffset[0] = (int) (k * height);
                topOffset[1] = (int) (k * width);
                // draw spikes
                for (int i = 0; i < n; i++) {
                    // get top point and draw first line (origin to top)
                    temp[0] -= step[0];
                    temp[1] -= step[1];
                    g2d.drawLine(relOrigin[0], relOrigin[1], (int) (temp[0] + topOffset[0]), (int) (temp[1] - topOffset[1]));
                    // get end point and draw second line (top to end)
                    relOrigin[0] = (int) temp[0];
                    relOrigin[1] = (int) temp[1];
                    temp[0] -= step[0];
                    temp[1] -= step[1];
                    g2d.drawLine(relOrigin[0] + topOffset[0], relOrigin[1] - topOffset[1], (int) temp[0], (int) temp[1]);
                    // prepare data for next spike
                    relOrigin[0] = (int) temp[0];
                    relOrigin[1] = (int) temp[1];
                }
            }

            // Draw transistors
            g2d.setColor(vs.TRANSISTOR_COLOR);
            for (Transistor transistor : transistors) {
                g2d.setStroke(new BasicStroke(vs.TRANSISTOR_WIDTH));
                relOrigin = vs.getCGI(vs.getRelCords(transistor.originCords));
                relEnd = vs.getCGI(vs.getRelCords(transistor.endCords));
                switch (transistor.rotation) {
                    case 0:
                        g2d.drawLine(relOrigin[0], relOrigin[1], relOrigin[0] + vs.gridSize / 2, relOrigin[1] + vs.gridSize);
                        g2d.drawLine(relEnd[0], relEnd[1], relEnd[0] - vs.gridSize / 2, relEnd[1] + vs.gridSize);
                        g2d.drawLine(relOrigin[0] + vs.gridSize, relOrigin[1] + vs.gridSize,
                                relOrigin[0] + vs.gridSize, relOrigin[1] + 2 * vs.gridSize);
                        g2d.setStroke(new BasicStroke((int) (vs.TRANSISTOR_WIDTH * 1.5)));
                        g2d.drawLine(relOrigin[0], relOrigin[1] + vs.gridSize, relEnd[0], relEnd[1] + vs.gridSize);
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    default:
                        break;
                }
            }
        }
    };

    public static void main(String[] args) throws IOException {
        new App();
    }

    /**
     * Class constructor,
     * build JFrame with App name,
     * set frame specs
     * add components
     * implement event listeners
     * set visible
     */
    public App() throws IOException {
        super("Circuit Builder");
        // frame specs: size, close operation, layout
        setBounds(3433, 320, DEFAULT_DIMENSION.width, DEFAULT_DIMENSION.height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.GRAY);
        setForeground(Color.LIGHT_GRAY);
        setLayout(new BorderLayout());
        // add components
        setJMenuBar(menu);
        contentPane.add(toolBar, BorderLayout.NORTH);
        contentPane.add(sideBarPanel, BorderLayout.EAST);
        sideBarPanel.add(sideBars[0]);
        contentPane.add(viewport, BorderLayout.CENTER);
        viewport.setBackground(vs.BG_COLOR);
        // implemented event listeners
        viewport.addMouseMotionListener(this);
        viewport.addMouseWheelListener(this);
        viewport.addMouseListener(this);
        // set visible
        setVisible(true);
    }

    // Change default dimension setting
    public void changeDefaultDimension(Dimension DIMENSION) {
        DEFAULT_DIMENSION = DIMENSION;
    }

    // clear side bar visibility
    private void clearSideBar() {
        for (JToolBar sidebar : sideBars) {
            sidebar.setVisible(false);
        }
    }

    /**
     * Mouse dragged event
     * if button is mouse wheel (BUTTON2) -> PADDING
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (e.getModifiersEx() == InputEvent.BUTTON2_DOWN_MASK)
        vs.offset = new int[] {vs.offset[0] + e.getX() - vs.direction[0],
                               vs.offset[1] + e.getY() - vs.direction[1]};
        vs.direction = new int[] {e.getX(), e.getY()};
        vs.CGI = vs.getCGI(e.getX(), e.getY());
        viewport.repaint();
    }

    /**
     * Mouse moved event
     * update CGI position
     * if action is 2 -> update last wire end at current CGI
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        vs.CGI = vs.getCGI(e.getX(), e.getY());
        if (action == 2) {
            // update last wire end
            wires.getLast().setEndCords(vs.getAbsCords(vs.CGI));
            wires.getLast().rightAngle = vs.rightAngle;
            wires.getLast().inverted = vs.inverted;
        } else if (action == 4) {
            // update last resistor end
            resistors.getLast().setEndCords(vs.getAbsCords(vs.CGI));
        } else if (action == 5) {
            // update last transistor position
            int rotation = transistors.getLast().rotation;
            int[] cgi = vs.getAbsCords(vs.CGI);
            if (rotation == 0) {
                transistors.getLast().originCords = new int[] {cgi[0] - vs.DEFAULT_GRID_SIZE, cgi[1] - vs.DEFAULT_GRID_SIZE};
                transistors.getLast().setEndCords(cgi[0] + vs.DEFAULT_GRID_SIZE, cgi[1] - vs.DEFAULT_GRID_SIZE);
            } else if (rotation == 1) {
                transistors.getLast().originCords = new int[] {cgi[0] - vs.DEFAULT_GRID_SIZE, cgi[1] + vs.DEFAULT_GRID_SIZE};
                transistors.getLast().setEndCords(cgi[0] + vs.DEFAULT_GRID_SIZE, cgi[1] + vs.DEFAULT_GRID_SIZE);
            } else if (rotation == 2) {
                transistors.getLast().originCords = new int[] {cgi[0] + vs.DEFAULT_GRID_SIZE, cgi[1] + vs.DEFAULT_GRID_SIZE};
                transistors.getLast().setEndCords(cgi[0] + vs.DEFAULT_GRID_SIZE, cgi[1] - vs.DEFAULT_GRID_SIZE);
            } else {
                transistors.getLast().originCords = new int[] {cgi[0] + vs.DEFAULT_GRID_SIZE, cgi[1] - vs.DEFAULT_GRID_SIZE};
                transistors.getLast().setEndCords(cgi[0] - vs.DEFAULT_GRID_SIZE, cgi[1] - vs.DEFAULT_GRID_SIZE);
            }
        }
        viewport.repaint();
    }

    /**
     * Mouse wheel moved event
     * -> ZOOMING
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() < 0) {
            vs.zoomFactor /= vs.ZOOM_INCREASE;
        } else {
            vs.zoomFactor *= vs.ZOOM_INCREASE;
        }
        vs.gridSize = (int) (vs.DEFAULT_GRID_SIZE / vs.zoomFactor);
        vs.CGI = vs.getCGI(e.getX(), e.getY());
        viewport.repaint();
    }

    /**
     * Mouse clicked event
     * if action is 1 (draw wire origin) -> set action to 2, create new wire at current CGI
     * if action is 2 (draw wire end) -> set action to 0, set wire end at current CGI
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (action == 1) {
            // draw wire origin
            action = 2;
            toolBar.rightAngleWire.setVisible(true);
            wires.addLast(new Wire(vs.getAbsCords(vs.CGI)));
            wires.getLast().setEndCords(vs.getAbsCords(vs.CGI));
        } else if (action == 2) {
            // set wire end
            wires.getLast().setEndCords(vs.getAbsCords(vs.CGI));
            // continue drawing wires with a new wire (won't change action then)
            wires.addLast(new Wire(vs.getAbsCords(vs.CGI)));
            wires.getLast().setEndCords(vs.getAbsCords(vs.CGI));
            wires.getLast().rightAngle = vs.rightAngle;
            wires.getLast().inverted = vs.inverted;
        } else if (action == 3) {
            // set resistor origin
            action = 4;
            resistors.addLast(new Resistor(vs.getAbsCords(vs.CGI), vs.DEFAULT_RESISTANCE));
            resistors.getLast().setEndCords(vs.getAbsCords(vs.CGI));
        } else if (action == 4) {
            // set resistor end
            resistors.getLast().setEndCords(vs.getAbsCords(vs.CGI));
            // do not continue drawing resistors (change action)
            action = 0;
            toolBar.back.setVisible(false);
        } else if (action == 5) {
            // set transistor position
            action = 0;
            toolBar.back.setVisible(false);
            toolBar.rotateTransistor.setVisible(false);
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
}

/**
 * Viewport specs object
 * Storage of all the data related to the viewport panel
 * Put in an external object so that it is easily accessible from every point in the App class body
 */
class ViewportSpecs {
    // std colors and sizes
    Color BG_COLOR = Color.WHITE;
    Color GRID_COLOR = Color.LIGHT_GRAY;
    int DEFAULT_GRID_SIZE = 50;
    int gridSize = DEFAULT_GRID_SIZE;
    Color SNAPPING_CIRCLE_COLOR = Color.RED;
    int SNAPPING_CIRCLE_RADIUS = 4;

    Color WIRE_COLOR = Color.BLUE;
    int WIRE_WIDTH = 4;
    boolean rightAngle = false;
    boolean inverted = false;

    Color RESISTOR_COLOR = Color.GREEN;
    int RESISTOR_WIDTH = 4;
    int RESISTOR_SPIKES_WIDTH_PER_GRID = 2;
    int RESISTOR_SPIKES_HEIGHT = 15;

    Color TRANSISTOR_COLOR = Color.PINK;
    int TRANSISTOR_WIDTH = 4;

    // std resistance
    int DEFAULT_RESISTANCE = 100;

    // zoom data
    double ZOOM_INCREASE = 1.5;
    double zoomFactor = 1;

    // padding data
    int[] offset = {0, 0};
    int[] direction = {0, 0};

    // CGI (Closest Grid Intersection)
    int[] CGI = {0, 0};

    public int[] getCGI(int[] cords) {
        return getCGI(cords[0], cords[1]);
    }
    public int[] getCGI(int x, int y) {
        // Closest vertical grid
        int gridOffsetX = offset[0] % gridSize;
        int _x = ((x - gridOffsetX) / gridSize) * gridSize + gridOffsetX;
        if (x - _x > gridSize / 2) {
            _x += gridSize;
        }
        // Closest horizontal grid
        int gridOffsetY = this.offset[1] % gridSize;
        int _y = ((y - gridOffsetY) / gridSize) * gridSize + gridOffsetY;
        if (y - _y > gridSize / 2) {
            _y += gridSize;
        }
        return new int[] {_x, _y};
    }

    // cords converter (used to ensure that the wires drawn stay in the same places event after zooming and padding)
    public int[] getRelCords(int x, int y) {
        return new int[] {(int) ((x / zoomFactor + offset[0])), (int) ((y / zoomFactor + offset[1]))};
    }
    public int[] getRelCords(int[] cords) {
        return getRelCords(cords[0], cords[1]);
    }

    public int[] getAbsCords(int x, int y) {
        return new int[] {(int) ((x - offset[0])  * zoomFactor), (int) ((y - offset[1]) * zoomFactor)};
    }
    public int[] getAbsCords(int[] cords) {
        return getAbsCords(cords[0], cords[1]);
    }
}

/**
 * ToolBar external class
 * (needed because some fields needed to be accessed form App class body)
 */
class ToolBar extends JToolBar {

    // ToolBar Buttons
    final JButton addWire = new ToolBarButton(" Add wire ", ImageIO.read(getClass().getResource("icontest.jpg")));
    final JButton addNode = new ToolBarButton(" Add node ");
    final JButton addResistor = new ToolBarButton(" Add resistor ");
    final JButton addTransistor = new ToolBarButton(" Add transistor ");
    final JButton back = new ToolBarButton("Back");

    final JButton rightAngleWire = new ToolBarButton("right_angle_wire");
    final JButton invertRightAngleWire = new ToolBarButton("invert_right_angle_wire");

    final JButton rotateTransistor = new ToolBarButton("rotate_transistor");

    public ToolBar() throws IOException {

        setBackground(Color.GRAY);
        setBorderPainted(false);

        // set app-wide hotkey for addWire btn "Ctrl + W"
        Action addWireAction = new AbstractAction(addWire.getText()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Action 1 selected");
                App.action = 1;
                back.setVisible(true);
            }
        };
        addWire.setAction(addWireAction);
        addWireAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
        addWire.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK), addWire.getText());
        addWire.getActionMap().put(addWire.getText(), addWireAction);

        // add wire draw modifiers not shown put accessible with hotkeys "Shift" to draw only using parallel lines
        // and "Alt" to invert the orientation
        Action rightAngleWireAction = new AbstractAction(rightAngleWire.getText()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (App.action == 2) {
                    System.out.println("Setting to right angle");
                    if (App.vs.rightAngle) {
                        if (App.vs.inverted) {
                            App.vs.rightAngle = false;
                            App.vs.inverted = false;
                        } else {
                            App.vs.inverted = true;
                        }
                    } else {
                        App.vs.rightAngle = true;
                    }
                }
            }
        };
        rightAngleWire.setAction(rightAngleWireAction);
        rightAngleWireAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
        rightAngleWire.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_I, 0), rightAngleWire.getText());
        rightAngleWire.getActionMap().put(rightAngleWire.getText(), rightAngleWireAction);

        rightAngleWire.setVisible(false);

        // set up add-resistor hotkey for addResistor btn "Ctrl + R"
        Action addResistorAction = new AbstractAction(addResistor.getText()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Action 3 selected");
                App.action = 3;
                back.setVisible(true);
            }
        };
        addResistor.setAction(addResistorAction);
        addResistorAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
        addResistor.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK), addResistor.getText());
        addResistor.getActionMap().put(addResistor.getText(), addResistorAction);

        // set up add-transistor hotkey for addTransistor btn "Ctrl + T"
        Action addTransistorAction = new AbstractAction(addTransistor.getText()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Action 5 selected");
                App.action = 5;
                App.transistors.addLast(new Transistor());
                back.setVisible(true);
                rotateTransistor.setVisible(true);
            }
        };
        addTransistor.setAction(addTransistorAction);
        addTransistorAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
        addTransistor.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK), addTransistor.getText());
        addTransistor.getActionMap().put(addTransistor.getText(), addTransistorAction);

        rotateTransistor.setVisible(false);

        // set up escape hotkey for back btn "Esc"
        String backKey = "back";
        Action backAction = new AbstractAction(backKey) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (App.action == 2) {
                    App.wires.removeLast();
                } else if (App.action == 4) {
                    App.resistors.removeLast();
                } else if (App.action == 5) {
                    App.transistors.removeLast();
                }
                App.action = 0;
                rightAngleWire.setVisible(false);
                back.setVisible(false);
            }
        };
        back.setAction(backAction);
        backAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
        back.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), backKey);
        back.getActionMap().put(backKey, backAction);
        // set back to not visible (only shown when and action is being performed)
        back.setVisible(false);

        add(addWire);
        add(addNode);
        add(addResistor);
        add(addTransistor);

        add(back);

        add(rightAngleWire);
        add(rotateTransistor);
    }
}

// FileSideBar class
class FileSideBar extends JToolBar {

    JButton newCircuit = new JButton("New circuit");
    JButton open = new JButton("Open");
    JButton openRecent = new JButton("Open recent");
    JButton save = new JButton("Save");
    JButton saveAs = new JButton("Save as...");

    public FileSideBar() {
        super(1);

        setVisible(false);

        this.add(newCircuit);
        this.add(open);
        this.add(openRecent);
        this.add(save);
        this.add(saveAs);
    }
}
