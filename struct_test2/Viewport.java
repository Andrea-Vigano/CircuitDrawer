package struct_test2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.LinkedList;

public class Viewport extends JPanel
        implements MouseMotionListener, MouseListener, MouseWheelListener, KeyListener{

    // lines storage
    private final LinkedList<Line> lines = new LinkedList<Line>();

    private final Color BG_COLOR = Color.WHITE;

    // snapping circle specs
    private final Color SNAPPING_CIRCLE_COLOR = Color.RED;
    private final int SNAPPING_CIRCLE_RADIUS = 4;
    private int[] closesGridIntersection = {0, 0};

    // actions tracking
    // setting line origin, setting line end
    private boolean[] actions = {false, false};
    private final Color LINE_COLOR = Color.BLUE;
    private final int LINE_WIDTH = 6;

    // implement padding
    private final Cords offset = new Cords(0, 0);
    private final Cords direction = new Cords(0, 0);

    // implement zoom
    private final double ZOOM_INCREASE = 1.5;
    private double zoomFactor = 1;

    // grid specs
    private final int GRID_SIZE = 50;
    private final Color GRID_COLOR = Color.LIGHT_GRAY;

    public Viewport() {
        super();
        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        addKeyListener(this);

        setBackground(this.BG_COLOR);
    }

    private void clearActions() {
        Arrays.fill(this.actions, false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int gridSize = (int) (this.GRID_SIZE / this.zoomFactor);

        // draw grid lines
        g.setColor(this.GRID_COLOR);
        for (int i = this.offset.getX() % gridSize; i < this.getWidth();
             i = i + gridSize) {
            g.drawLine(i, 0, i, this.getHeight());
        }
        for (int i = this.offset.getY() % gridSize; i < this.getHeight();
             i = i + gridSize) {
            g.drawLine(0, i, this.getWidth(), i);
        }

        // draw snapping circle
        g.setColor(this.SNAPPING_CIRCLE_COLOR);
        g.fillOval(this.closesGridIntersection[0] - this.SNAPPING_CIRCLE_RADIUS / 2,
                this.closesGridIntersection[1] - this.SNAPPING_CIRCLE_RADIUS / 2,
                this.SNAPPING_CIRCLE_RADIUS, this.SNAPPING_CIRCLE_RADIUS);

        // draw lines
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(this.LINE_WIDTH));
        g2d.setColor(this.LINE_COLOR);
        for (Line line : this.lines) {
            int[] relA = this.relCords(line.getA().getX(), line.getA().getY()),
                  relB = this.relCords(line.getB().getX(), line.getB().getY());
            g2d.drawLine(relA[0], relA[1], relB[0], relB[1]);
        }
    }

    // Cords converter
    public int[] relCords(int absX, int absY) {
        return new int[] {(int) ((absX / this.zoomFactor + this.offset.getX())),
                (int) ((absY / this.zoomFactor + this.offset.getY()))};
    }
    public int[] absCords(int relX, int relY) {
        return new int[] {(int) (relX * this.zoomFactor - this.offset.getX()),
                (int) (relY * this.zoomFactor - this.offset.getY())};
    }

    // Grid intersection getter
    private int[] getClosestGridIntersection(int x, int y) {
        int gridSize = (int) (this.GRID_SIZE / this.zoomFactor);
        // closest vertical grid (X)
        int gridOffsetX = this.offset.getX() % gridSize;
        int _x = ((x - gridOffsetX) / gridSize) * gridSize + gridOffsetX;
        if (x - _x > gridSize / 2) {
            _x += gridSize;
        }
        // closest horizontal grid (Y)
        int gridOffsetY = this.offset.getY() % gridSize;
        int _y = ((y - gridOffsetY) / gridSize) * gridSize + gridOffsetY;
        if (y - _y > gridSize / 2) {
            _y += gridSize;
        }
        return new int[] {_x, _y};
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (this.actions[0]) {
            int[] absEvent = this.absCords(this.closesGridIntersection[0], this.closesGridIntersection[1]);
            this.lines.add(new Line(new Cords(absEvent[0], absEvent[1])));
            // setting line end
            this.clearActions();
            this.actions[1] = true;
        } else if (this.actions[1]) {
            int[] absEvent = this.absCords(this.closesGridIntersection[0], this.closesGridIntersection[1]);
            this.lines.getLast().setB(new Cords(absEvent[0], absEvent[1]));
            this.clearActions();
        }
        this.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // pad setup
        this.direction.setX(e.getX());
        this.direction.setY(e.getY());
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

    @Override
    public void mouseDragged(MouseEvent e) {
        // pad
        this.offset.setX(this.offset.getX() + e.getX() - this.direction.getX());
        this.offset.setY(this.offset.getY() + e.getY() - this.direction.getY());
        this.direction.setX(e.getX());
        this.direction.setY(e.getY());
        this.closesGridIntersection = this.getClosestGridIntersection(e.getX(), e.getY());
        this.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.closesGridIntersection = this.getClosestGridIntersection(e.getX(), e.getY());
        if (this.actions[1]) {
            // dynamically draw line
            int[] absEvent = this.absCords(this.closesGridIntersection[0], this.closesGridIntersection[1]);
            this.lines.getLast().setB(new Cords(absEvent[0], absEvent[1]));
        }
        this.repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() < 0) {
            this.zoomFactor /= this.ZOOM_INCREASE;
        } else {
            this.zoomFactor *= this.ZOOM_INCREASE;
        }
        this.closesGridIntersection = this.getClosestGridIntersection(e.getX(), e.getY());
        this.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'l') {
            // setting line origin
            this.clearActions();
            this.actions[0] = true;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
