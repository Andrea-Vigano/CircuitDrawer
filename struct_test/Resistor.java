package struct_test;

import java.util.LinkedList;

public class Resistor {
    /*
    * Resistor
    * */

    protected int resistance;
    protected LinkedList<Cords> joints = new LinkedList<Cords>();

    Resistor(Cords start, Cords end, int resistance) {
        this.joints.add(start);
        this.joints.add(end);
        this.resistance = resistance;
    }

    public int getResistance() {
        return resistance;
    }

    public LinkedList<Cords> getJoints() {
        return joints;
    }
}
