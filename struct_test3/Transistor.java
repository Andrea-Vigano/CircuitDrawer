package struct_test3;

public class Transistor extends Drawable {
    /*
     * Transistor class
     * Define object origin and end as the source and drain, while the gate position
     * is determinate by the rotation
     * */

    int rotation = 0;

    public Transistor() {
        super(-100, -100);
    }
    public Transistor(int x, int y, int rotation) {
        super(x, y);
        this.rotation = rotation;
    }
    public Transistor(int[] cords, int rotation) {
        super(cords);
        this.rotation = rotation;
    }
    public Transistor(int x, int y) {
        super(x, y);
    }
    public Transistor(int[] cords) {
        super(cords);
    }
}
