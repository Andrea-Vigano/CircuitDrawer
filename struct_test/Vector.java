package struct_test;

import static java.lang.Math.pow;

public class Vector {
    /*
    * Vector class
    * */

    protected final Cords origin, end;

    Vector(Cords origin, Cords end) {
        this.origin = origin;
        this.end = end;
    }

    public double getModule() {
        return pow(pow((double) this.origin.getX() + this.end.getX(), 2.0)
                + pow((double) this.origin.getY() + this.end.getY(), 2.0), 0.5);
    }

    public Cords getOrigin() {
        return this.origin;
    }
    public Cords getEnd() {
        return this.end;
    }
}
