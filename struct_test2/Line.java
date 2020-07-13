package struct_test2;

public class Line {

    private Cords A;
    private Cords B;

    Line(Cords A) {
        this.A = A;
    }

    public void setB(Cords B) {
        this.B = B;
    }

    public void setA(Cords a) {
        A = a;
    }

    public Cords getA() {
        return A;
    }

    public Cords getB() {
        return B;
    }

    public static String convertToCSV(Line line) {
        return String.format("%d,%d,%d,%d", line.A.getX(), line.A.getY(), line.B.getX(), line.B.getY());
    }
}
