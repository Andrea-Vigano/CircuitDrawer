package version0_5.Components;

public class Transistor extends Drawable {
    public int rotation = 0;
    public static int type = 2;

    public Transistor(int[] origin) {
        super(origin);
    }

    public Transistor(int x, int y) {
        super(x, y);
    }

    public void setEnd(int[] cords) {
        end = cords;
        origin = cords;
    }

    @Override
    public String[] convertToDataLine() {
        return new String[] {Integer.toString(type),
                Integer.toString(origin[0]), Integer.toString(origin[1]),
                Integer.toString(end[0]), Integer.toString(end[1]),
                "N", Integer.toString(rotation), "N"};
    }
}
