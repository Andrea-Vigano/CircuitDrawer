package version0_5.Components;

import static java.lang.Math.pow;

/**
 * The selectable area is drawn from the origin points of a Drawable object
 * as a rectangle of a specific width and that follows the inclination of the object in the viewport.
 */
public class SelectableArea {

    // will store the selectable area poly as an array of cords with which you can re create the poly by looping
    // it and drawing a line between the current cord and the previous one
    public int[][][] vertices;

    public SelectableArea(Wire wire) {
        // get key distances
        // get actual points
        switch (wire.angle) {
            case 1 -> vertices = new int[][][]{getWireSelectableAreaVertices(wire.origin,
                    new int[]{wire.origin[0], wire.end[1]}),
                    getWireSelectableAreaVertices(new int[]{wire.origin[0], wire.end[1]}, wire.end)};
            case 2 -> vertices = new int[][][]{getWireSelectableAreaVertices(wire.origin,
                    new int[]{wire.end[0], wire.origin[1]}),
                    getWireSelectableAreaVertices(new int[]{wire.end[0], wire.origin[1]}, wire.end)};
            default -> vertices = new int[][][]{getWireSelectableAreaVertices(wire.origin, wire.end)};
        }
    }
    private int[][] getWireSelectableAreaVertices(int[] origin, int[] end) {
        if (origin[0] == end[0]) {
            // vertical wire
            return new int[][]{
                    new int[]{origin[0] - Wire.SELECTABLE_AREA_WIDTH, origin[1]},
                    new int[]{end[0] - Wire.SELECTABLE_AREA_WIDTH, end[1]},
                    new int[]{end[0] + Wire.SELECTABLE_AREA_WIDTH, end[1]},
                    new int[]{origin[0] + Wire.SELECTABLE_AREA_WIDTH, origin[1]}
            };
        } else if (origin[1] == end[1]) {
            // horizontal wire
            return new int[][]{
                    new int[]{origin[0], origin[1] - Wire.SELECTABLE_AREA_WIDTH},
                    new int[]{end[0], end[1] - Wire.SELECTABLE_AREA_WIDTH},
                    new int[]{end[0], end[1] + Wire.SELECTABLE_AREA_WIDTH},
                    new int[]{origin[0], origin[1] + Wire.SELECTABLE_AREA_WIDTH}
            };
        } else {
            // oblique wire
            int x = origin[0] - end[0],
                    y = origin[1] - end[1];
            double k = Wire.SELECTABLE_AREA_WIDTH / pow(pow(x, 2) + pow(y, 2), 0.5);
            int selectable_x = (int) (x * k),
                    selectable_y = (int) (y * k);
            return new int[][]{
                    new int[]{origin[0] + selectable_y, origin[1] - selectable_x},
                    new int[]{end[0] + selectable_y, end[1] - selectable_x},
                    new int[]{end[0] - selectable_y, end[1] + selectable_x},
                    new int[]{origin[0] - selectable_y, origin[1] + selectable_x},
            };
        }
    }

    public SelectableArea(Resistor resistor) {

    }
    public SelectableArea(Transistor transistor) {

    }

    public boolean isInside(int x, int y) {
        return false;
    }
    public boolean isInside(int[] cords) {
        return isInside(cords[0], cords[1]);
    }
}
