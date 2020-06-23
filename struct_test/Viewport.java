package struct_test;

import javax.swing.*;
import java.awt.*;

public class Viewport extends JPanel {

    // Listeners
    static protected MouseSpy mouseSpy = new MouseSpy();
    static protected MouseMotionSpy mouseMotionSpy = new MouseMotionSpy();
    static protected MouseWheelSpy mouseWheelSpy = new MouseWheelSpy();

    // Grid data
    protected int GRID_SIZE = 60;
    protected Color BACKGROUND_COLOR = Color.LIGHT_GRAY;
    protected Color GRID_COLOR = Color.GRAY;

    // Zoom
    // zoom factor is stored as a precise number to reduce loss in precision when zooming in and out for a long time
    protected double zoomFactor = 1.000000;
    protected double zoomOffsetting = 1.1;

    // Pad
    protected int XOffset = 0, YOffset = 0;
    protected int relativeXOffset = 0, relativeYOffset = 0;

    public Viewport() {
        super();

        // add mouse spy
        this.addMouseListener(mouseSpy);
        this.addMouseMotionListener(mouseMotionSpy);
        this.addMouseWheelListener(mouseWheelSpy);
        this.setBackground(this.BACKGROUND_COLOR);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int WIDTH = getWidth(), HEIGHT = getHeight();
        g.setColor(this.GRID_COLOR);

        // Draw grid
        for (int i = relativeXOffset - GRID_SIZE; i < WIDTH; i += GRID_SIZE) {
            g.drawLine(i, 0, i, HEIGHT);
        }
        for (int i = relativeYOffset - GRID_SIZE; i < HEIGHT; i += GRID_SIZE) {
            g.drawLine(0, i, WIDTH, i);
        }

        g.setColor(Color.BLUE);
        int[] cordsA = getRelCords(100, 100);
        int[] cordsB = getRelCords(200, 200);
        g.drawLine(cordsA[0], cordsA[1], cordsB[1], cordsB[1]);
    }

    public int getGRID_SIZE() {
        return GRID_SIZE;
    }

    public void setGRID_SIZE(int GRID_SIZE) {
        this.GRID_SIZE = GRID_SIZE;
    }

    public Color getBACKGROUND_COLOR() {
        return BACKGROUND_COLOR;
    }

    public void setBACKGROUND_COLOR(Color BACKGROUND_COLOR) {
        this.BACKGROUND_COLOR = BACKGROUND_COLOR;
    }

    public Color getGRID_COLOR() {
        return GRID_COLOR;
    }

    public void setGRID_COLOR(Color GRID_COLOR) {
        this.GRID_COLOR = GRID_COLOR;
    }

    public void zoomIn() {
        if (this.zoomFactor < 20.0) {
            this.zoomFactor *= zoomOffsetting;
            this.GRID_SIZE *= zoomOffsetting;
            // adjust offset
            this.increaseOffset((int) (WIDTH - (WIDTH / this.zoomOffsetting)) / 2,
                    (int) (HEIGHT - (HEIGHT / this.zoomOffsetting)) / 2);
            repaint();
        }
    }

    public void zoomOut() {
        if (this.zoomFactor > 0.1) {
            this.zoomFactor /= zoomOffsetting;
            this.GRID_SIZE /= zoomOffsetting;
            // adjust offset
            this.increaseOffset((int) (WIDTH - (WIDTH * this.zoomOffsetting)) / 2,
                    (int) (HEIGHT - (HEIGHT * this.zoomOffsetting)) / 2);
            repaint();
        }
    }

    public void increaseOffset(int x, int y) {
        this.XOffset += x;
        this.YOffset += y;

        this.relativeXOffset = this.GRID_SIZE - (XOffset % GRID_SIZE);
        this.relativeYOffset = this.GRID_SIZE - (YOffset % GRID_SIZE);
        repaint();
        System.out.println(Integer.toString(relativeXOffset) + " " + Integer.toString(relativeYOffset));
    }

    public int[] getAbsCord(int x, int y) {
        // get absolute cords from relative cords
        // relative -> add offset, divide zoom
        return new int[]{(int) (x * zoomFactor) + this.XOffset, (int) (x * zoomFactor) + this.YOffset};
    }

    public int[] getRelCords(int x, int y) {
        // get relative cords from absolute cords
        // absolute -> remove offset, multiply zoom
        return new int[]{(int) (x / zoomFactor) - this.XOffset, (int) (x / zoomFactor) - this.YOffset};
    }
}
