package struct_test2;

public class Cords {

    private int x, y;

    Cords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Cords(int[] cords) {
        this.x = cords[0];
        this.y = cords[1];
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
