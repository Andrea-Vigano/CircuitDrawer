package version0_5.Components;

/**
 * Define all the common elements of a drawable object,
 * like a origin cords and an end cords
 */
public abstract class Drawable {

    public int[] origin;
    public int[] end;

    public static int type;

    public Drawable(int[] origin) {
        this.origin = origin;
    }
    public Drawable(int x, int y) {
        origin = new int[] {x, y};
    }

    public void setEnd(int x, int y) {
        end = new int[] {x, y};
    }
    public void setEnd(int[] cords) {
        end = cords;
    }

    public abstract String[] convertToDataLine();
}
