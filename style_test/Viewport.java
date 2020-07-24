package style_test;

import javax.swing.*;
import java.awt.*;

public class Viewport extends JPanel {

    // data storage for viewport
    public ViewportSpecs vs = new ViewportSpecs();

    public Viewport() {
        super();

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // draw grid lines
        g.setColor(vs.GRID_COLOR);
        for (int i = vs.offset[0] % vs.gridSize; i < getWidth(); i = i + vs.gridSize) {
            g.drawLine(i, 0, i, getHeight());
        }
        for (int i = vs.offset[1] % vs.gridSize; i < getHeight(); i = i + vs.gridSize) {
            g.drawLine(0, i, getWidth(), i);
        }
        // draw snapping circle in CGI
        g.setColor(vs.SNAPPING_CIRCLE_COLOR);
        g.fillOval(vs.CGI[0] - vs.SNAPPING_CIRCLE_RADIUS / 2, vs.CGI[1] - vs.SNAPPING_CIRCLE_RADIUS / 2,
                vs.SNAPPING_CIRCLE_RADIUS, vs.SNAPPING_CIRCLE_RADIUS);
    }
}
