package struct_test;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseMotionSpy implements MouseMotionListener {

    protected int oldX = 0, oldY = 0;

    @Override
    public void mouseDragged(MouseEvent e) {
        // pad viewport
        Viewport source = (Viewport) e.getSource();
        int x = e.getX(), y = e.getY();

        source.increaseOffset(oldX - x, oldY - y);

        oldX = x;
        oldY = y;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // track closest intersection to the mouse in the viewport
        // TODO add snap to intersection (after zoom and pad to viewport)
        Viewport source = (Viewport) e.getSource();
    }
}
