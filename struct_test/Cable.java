package struct_test;

import java.util.LinkedList;

public class Cable {
    /*
     * Cable class, cable object in the draw
     * */

    protected LinkedList<Cords> joints = new LinkedList<Cords>();

    Cable(Cords start, Cords finish) {
        this.joints.add(start);
        this.joints.add(finish);
    }
    Cable(Cords start) {
        this.joints.add(start);
    }

    public void addJoint(Cords joint) {
        this.joints.add(joint);
    }
}
