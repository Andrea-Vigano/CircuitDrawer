package version0_5.Engine;

import version0_5.Components.*;

import java.util.LinkedList;

/**
 * List like class to store all the object drawn in the viewport at any point in time
 */
public class DrawnObjects {

    public final LinkedList<Wire> wires = new LinkedList<>();
    public final LinkedList<Resistor> resistors = new LinkedList<>();
    public final LinkedList<Transistor> transistors = new LinkedList<>();
    public final LinkedList<Drawable> removed = new LinkedList<>();

    public Drawable selected;

    /**
     * Add a new drawn object to the lists
     * @param object drawn object
     */
    public void addObject(Drawable object) {
        if (object instanceof Wire) {
            wires.addLast((Wire) object);
        } else if (object instanceof Resistor) {
            resistors.addLast((Resistor) object);
        } else if (object instanceof Transistor) {
            transistors.addLast((Transistor) object);
        }
    }

    /**
     * Clear all the object from the lists
     */
    public void clearDrawnObjects() {
        wires.clear();
        resistors.clear();
        transistors.clear();
    }

    /**
     * Delete an object
     */
    public void deleteWire() {
        removed.addLast(wires.getLast());
        wires.removeLast();
    }
    public void deleteResistor() {
        removed.addLast(resistors.getLast());
        resistors.removeLast();
    }
    public void deleteTransistor() {
        removed.addLast(transistors.getLast());
        transistors.removeLast();
    }

    /**
     * Restore an object
     */
    public void restore() {
        Drawable last = removed.getLast();
        if (last instanceof Wire) {
            wires.addLast((Wire) last);
        } else if (last instanceof Resistor) {
            resistors.addLast((Resistor) last);
        } else if (last instanceof Transistor) {
            transistors.addLast((Transistor) last);
        }
        removed.removeLast();
    }

    /**
     * Change selection
     */
    public void changeSelection(Drawable drawable) {
        Drawable oldSelection = selected;
        if (drawable instanceof Wire) {
            for (Wire wire : wires) {
                if (drawable == wire) {
                    selected = wire;
                    break;
                }
            }
        } else if (drawable instanceof Resistor) {
            for (Resistor resistor : resistors) {
                if (drawable == resistor) {
                    selected = resistor;
                    break;
                }
            }
        } else if (drawable instanceof Transistor) {
            for (Transistor transistor : transistors) {
                if (drawable == transistor) {
                    selected = transistor;
                    break;
                }
            }
        }
        if (selected == oldSelection) {
            // not found
            new Exception("Selected item not found").printStackTrace();
        }
    }
}
