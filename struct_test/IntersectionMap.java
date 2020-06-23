package struct_test;

import java.util.Comparator;
import java.util.LinkedList;

public class IntersectionMap {
    /*
    * Intersection mapper, getter and setter class
    * */

    // Intersections are stored in a sorted list
    protected LinkedList<Intersection> intersections = new LinkedList<Intersection>();

    public Intersection getIntersection(int index) {
        try {
            return this.intersections.get(index);
        } catch (IndexOutOfBoundsException exc) {
            exc.printStackTrace();
        }
        return null;
    }

    public Intersection getClosestIntersection(Intersection intersection) {
        int index = this.intersections.size() / 2 + 1, fieldSize = this.intersections.size();
        double dis1, dis2, dist3;

        while (fieldSize > 2) {
            if (intersection.compare(this.intersections.get(index)) > 0) {
                // intersection is greater
                index = (3 * index) / 2 + 1;
            } else {
                // intersection is smaller
                index = index / 2 + 1;
            }
            fieldSize = fieldSize / 2 + 1;
        }
//        dis1 = ((intersection.getX() - this.intersections.get(index - 1).getX()) ** 2 +
//                (intersection.getY() - this.intersections.get(index - 1).getY()) ** 2) ** (0.5);
        return new Intersection(0, 0);
    }

    public void addIntersection(Intersection intersection) {
        this.intersections.add(intersection);
        this.intersections.sort(new Comparator<Intersection>() {
            @Override
            public int compare(Intersection o1, Intersection o2) {
                return o1.compare(o2);
            }
        });
    }
}

class Intersection {

    final protected int x, y;

    Intersection(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int compare(Intersection other) {
        if (this.x == other.x) {
            return this.y - other.y;
        } else {
            return this.x - other.x;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
