package style_test;

import javax.swing.plaf.ColorUIResource;
import java.awt.*;

/**
 * Viewport specs object
 * Storage of all the data related to the viewport panel
 * Put in an external object so that it is easily accessible from every point in the App class body
 */
public class ViewportSpecs {
    // std colors and sizes
    public Color GRID_COLOR = new ColorUIResource(80, 80, 80);
    public int DEFAULT_GRID_SIZE = 100;
    public int gridSize = DEFAULT_GRID_SIZE;
    public Color SNAPPING_CIRCLE_COLOR = Color.RED;
    public int SNAPPING_CIRCLE_RADIUS = 4;

    public Color WIRE_COLOR = Color.BLUE;
    public int WIRE_WIDTH = 4;
    public boolean rightAngle = false;
    public boolean inverted = false;

    public Color RESISTOR_COLOR = Color.GREEN;
    public int RESISTOR_WIDTH = 4;
    public int RESISTOR_SPIKES_WIDTH_PER_GRID = 2;
    public int RESISTOR_SPIKES_HEIGHT = 15;

    public Color TRANSISTOR_COLOR = Color.PINK;
    public int TRANSISTOR_WIDTH = 4;

    // std resistance
    public int DEFAULT_RESISTANCE = 100;

    // zoom data
    public double ZOOM_INCREASE = 1.5;
    public double zoomFactor = 1;

    // padding data
    public int[] offset = {0, 0};
    public int[] direction = {0, 0};

    // CGI (Closest Grid Intersection)
    public int[] CGI = {0, 0};

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
