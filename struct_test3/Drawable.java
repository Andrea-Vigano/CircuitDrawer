package struct_test3;

/*
 * Drawable abstract class
 * define the specs for all the drawable objects
 */
public abstract class Drawable {
    int[] originCords;
    int[] endCords;

    public Drawable(int x, int y) {
        originCords = new int[] {x, y};
    }
    public Drawable(int[] cords) {
        originCords = cords;
    }

    public void setEndCords(int x, int y) {
        endCords = new int[] {x, y};
    }
    public void setEndCords(int[] cords) {
        endCords = cords;
    }
}
