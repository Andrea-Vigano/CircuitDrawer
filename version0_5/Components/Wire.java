package version0_5.Components;

public class Wire extends Drawable {

    // store the angle of the wire
    public int angle = 0;
    public static int type = 0;

    public Wire(int[] origin) {
        super(origin);
    }
    public Wire(int x, int y) {
        super(x, y);
    }

    @Override
    public String[] convertToDataLine() {
        return new String[] {Integer.toString(type),
                Integer.toString(origin[0]), Integer.toString(origin[1]),
                Integer.toString(end[0]), Integer.toString(end[1]),
                Integer.toString(angle), "N", "N"};
    }
}
