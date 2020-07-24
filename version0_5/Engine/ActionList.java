package version0_5.Engine;

import java.util.LinkedList;

public class ActionList {

    private final LinkedList<Integer> actions = new LinkedList<>();
    private final LinkedList<Integer> undone = new LinkedList<>();
    // maximum number of actions stored
    public int STORAGE_SIZE = 50;
    private final DrawnObjects DO;

    public ActionList(DrawnObjects DO) {
        this.DO = DO;
    }

    private void checkActionsSize() {
        // remove latest action when len greater than STORAGE_SIZE
        if (actions.size() > STORAGE_SIZE) {
            actions.removeFirst();
        }
    }
    private void checkUndoneSize() {
        // remove latest undone when len greater than STORAGE_SIZE
        if (undone.size() > STORAGE_SIZE) {
            undone.removeFirst();
        }
    }

    public int getLastAction() {
        return actions.getLast();
    }

    public void add(int actionID) {
        actions.addLast(actionID);
        checkActionsSize();
    }

    public void undo() {
        if (actions.size() > 0) {
            if (actions.getLast() == 2) {
                DO.deleteWire();
            } else if (actions.getLast() == 4) {
                DO.deleteResistor();
            } else if (actions.getLast() == 5) {
                DO.deleteTransistor();
            }
            undone.addLast(actions.getLast());
            checkUndoneSize();
            actions.removeLast();
        }
    }

    public void redo() {
        if (undone.size() > 0) {
            actions.addLast(undone.getLast());
            checkActionsSize();
            undone.removeLast();
            DO.restore();
        }
    }
}
