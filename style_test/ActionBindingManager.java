package style_test;

import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.*;

/**
 * Bind and hooks all the GUI pieces together to interact with the back end
 */
public class ActionBindingManager {

    public App app;
    private final Viewport viewport;
    private final ViewportSpecs vs;

    public ActionBindingManager(App app) {
        this.app = app;
        viewport = app.viewport;
        vs = viewport.vs;
    }

    /**
     * Bind menu buttons to the side bars
     */
    public void bindMenuAndSideBars() {
        app.menuBar.file.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                boolean isVisible = app.sideBar.sideBars[0].isVisible();
                app.sideBar.clear();
                app.sideBar.sideBars[0].setVisible(!isVisible);
                app.horizontalSplitPane.resetToPreferredSizes();
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });
    }

    /**
     * Add mouseListeners to app, implementing viewport functionalities
     * and component drawing logic
     */
    public void addAppMouseListeners() {
        app.viewport.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (e.getModifiersEx() == InputEvent.BUTTON2_DOWN_MASK)
                    vs.offset = new int[] {vs.offset[0] + e.getX() - vs.direction[0],
                            vs.offset[1] + e.getY() - vs.direction[1]};
                vs.direction = new int[] {e.getX(), e.getY()};
                vs.CGI = vs.getCGI(e.getX(), e.getY());
                viewport.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                vs.CGI = vs.getCGI(e.getX(), e.getY());
                viewport.repaint();
            }
        });
        app.viewport.addMouseWheelListener(e -> {
            if (e.getWheelRotation() < 0) {
                vs.zoomFactor /= vs.ZOOM_INCREASE;
            } else {
                vs.zoomFactor *= vs.ZOOM_INCREASE;
            }
            vs.gridSize = (int) (vs.DEFAULT_GRID_SIZE / vs.zoomFactor);
            vs.CGI = vs.getCGI(e.getX(), e.getY());
            viewport.repaint();
        });
        app.viewport.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                // prepare for pad
                vs.direction = new int[] {e.getX(), e.getY()};
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }
}
