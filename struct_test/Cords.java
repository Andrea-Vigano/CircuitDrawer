package struct_test;

public class Cords {
    /*
    * Cartesian plane cords
    * */

    protected int x, y;

    Cords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean areEqual(Cords other) {
        // checks if this and other represents the same point
        return this.x == other.x && this.y == other.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
