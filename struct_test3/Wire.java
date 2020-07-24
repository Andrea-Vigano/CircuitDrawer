package struct_test3;

public class Wire extends Drawable {
    /*
     * Wire class
     * Contains the wire absolute position in the viewport
     * */

    boolean rightAngle = false;
    boolean inverted = false;

    public Wire(int x, int y) {
        super(x, y);
    }
    public Wire(int[] cords) {
        super(cords);
    }
}
