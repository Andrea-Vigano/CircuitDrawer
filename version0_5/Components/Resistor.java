package version0_5.Components;

public class Resistor extends Drawable {

    public int resistance = 100;
    public static int type = 1;

    public Resistor(int[] origin) {
        super(origin);
    }

    public Resistor(int x, int y) {
        super(x, y);
    }

    @Override
    public String[] convertToDataLine() {
        return new String[] {Integer.toString(type),
                Integer.toString(origin[0]), Integer.toString(origin[1]),
                Integer.toString(end[0]), Integer.toString(end[1]),
                "N", "N", Integer.toString(resistance)};
    }

    @Override
    public SelectableArea getSelectableRect() {
        return new SelectableArea(this);
    }
}
