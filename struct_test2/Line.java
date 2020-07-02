package struct_test2;

import java.awt.*;

public class Line {

    private final Cords A;
    private Cords B;

    Line(Cords A) {
        this.A = A;
    }

    public void setB(Cords B) {
        this.B = B;
    }

    public Cords getA() {
        return A;
    }

    public Cords getB() {
        return B;
    }
}
