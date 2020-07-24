package struct_test3;

public class Resistor extends Drawable {
    /*
     * Resistor class
     * Contains the resistor absolute position in the viewport
     * and its resistance
     * */

    int resistance = 0;

    public Resistor(int x, int y, int resistance) {
        super(x, y);
        this.resistance = resistance;
    }
    public Resistor(int[] cords, int resistance) {
        super(cords);
        this.resistance = resistance;
    }
}
