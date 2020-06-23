package struct_test;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseWheelSpy implements MouseWheelListener {
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        // zoom in and zoom out
        Viewport source = (Viewport) e.getSource();
        int rotation = e.getWheelRotation();

        if (rotation < 0) {
            source.zoomIn();
        } else {
            source.zoomOut();
        }
    }
}
