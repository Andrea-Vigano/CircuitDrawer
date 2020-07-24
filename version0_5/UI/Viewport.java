package version0_5.UI;

import version0_5.Components.Resistor;
import version0_5.Components.Transistor;
import version0_5.Components.Wire;
import version0_5.Engine.ViewportSpecs;

import javax.swing.*;
import java.awt.*;

import static java.lang.Math.pow;
import static java.lang.Math.round;

/**
 * Viewport class, uses ViewportSpecs data to paint itself when needed.
 */
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

        Graphics2D g2d = (Graphics2D) g;

        // draw wires
        g2d.setColor(vs.WIRE_COLOR);
        g2d.setStroke(new BasicStroke(vs.WIRE_WIDTH));
        for (Wire wire : vs.DO.wires) {
            int[] wireOrigin = vs.getCGI(vs.getRelCords(wire.origin));
            int[] wireEnd = vs.getCGI(vs.getRelCords(wire.end));
            switch (wire.angle) {
                case 0:
                    g2d.drawLine(wireOrigin[0], wireOrigin[1], wireEnd[0], wireEnd[1]);
                    break;
                case 1:
                    g2d.drawLine(wireOrigin[0], wireOrigin[1], wireOrigin[0], wireEnd[1]);
                    g2d.drawLine(wireOrigin[0], wireEnd[1], wireEnd[0], wireEnd[1]);
                    break;
                case 2:
                    g2d.drawLine(wireOrigin[0], wireOrigin[1], wireEnd[0], wireOrigin[1]);
                    g2d.drawLine(wireEnd[0], wireOrigin[1], wireEnd[0], wireEnd[1]);
                    break;
                default:
                    break;
            }
        }

        // Draw resistors
        g2d.setColor(vs.RESISTOR_COLOR);
        g2d.setStroke(new BasicStroke((vs.RESISTOR_WIDTH)));
        // Resistor origin and end relative position and triangle sizes
        int[] relOrigin;
        int[] relEnd;
        double length, width, height;
        // Horizontal step and Vertical step
        double[] step = {0, 0};
        // optimized steps number
        int n;
        // constant
        double k;
        // offset of the spike top from the intersection of normal at the resistor line passing through the top point
        int[] topOffset = {0, 0};
        // temp array to store cords and double to store step while drawing the spikes
        double[] temp = {0, 0};
        for (Resistor resistor : vs.DO.resistors) {
            // Get relative origin and end
            relOrigin = vs.getCGI(vs.getRelCords(resistor.origin));
            relEnd = vs.getCGI(vs.getRelCords(resistor.end));
            temp[0] = relOrigin[0];
            temp[1] = relOrigin[1];
            // Get sizes
            width = relOrigin[0] - relEnd[0];
            height = relOrigin[1] - relEnd[1];
            length = pow((pow(width, 2) + pow(height, 2)), 0.5);
            // steps number
            n = vs.RESISTOR_SPIKES_WIDTH_PER_GRID * ((int) (round(length / vs.gridSize)));
            // HS and VS
            step[0] = width / n / 2;
            step[1] = height / n / 2;
            // Get top offset
            k = vs.RESISTOR_SPIKES_HEIGHT / vs.zoomFactor / length;
            topOffset[0] = (int) (k * height);
            topOffset[1] = (int) (k * width);
            // draw spikes
            for (int i = 0; i < n; i++) {
                // get top point and draw first line (origin to top)
                temp[0] -= step[0];
                temp[1] -= step[1];
                g2d.drawLine(relOrigin[0], relOrigin[1], (int) (temp[0] + topOffset[0]), (int) (temp[1] - topOffset[1]));
                // get end point and draw second line (top to end)
                relOrigin[0] = (int) temp[0];
                relOrigin[1] = (int) temp[1];
                temp[0] -= step[0];
                temp[1] -= step[1];
                g2d.drawLine(relOrigin[0] + topOffset[0], relOrigin[1] - topOffset[1], (int) temp[0], (int) temp[1]);
                // prepare data for next spike
                relOrigin[0] = (int) temp[0];
                relOrigin[1] = (int) temp[1];
            }
        }

        // draw transistor
        g2d.setColor(vs.TRANSISTOR_COLOR);
        g2d.setStroke(new BasicStroke(vs.TRANSISTOR_WIDTH));
        for (Transistor transistor : vs.DO.transistors) {
            relOrigin = vs.getCGI(vs.getRelCords(transistor.origin));
            relEnd = vs.getCGI(vs.getRelCords(transistor.end));
            switch (transistor.rotation) {
                case 0:
                    g2d.drawLine(relOrigin[0], relOrigin[1], relOrigin[0] + vs.gridSize / 2, relOrigin[1] + vs.gridSize);
                    g2d.drawLine(relEnd[0], relEnd[1], relEnd[0] - vs.gridSize / 2, relEnd[1] + vs.gridSize);
                    g2d.drawLine(relOrigin[0] + vs.gridSize, relOrigin[1] + vs.gridSize,
                            relOrigin[0] + vs.gridSize, relOrigin[1] + 2 * vs.gridSize);
                    g2d.setStroke(new BasicStroke((int) (vs.TRANSISTOR_WIDTH * 1.5)));
                    g2d.drawLine(relOrigin[0], relOrigin[1] + vs.gridSize, relEnd[0], relEnd[1] + vs.gridSize);
                    break;
                case 1:
                    g2d.drawLine(relOrigin[0], relOrigin[1], relOrigin[0] - vs.gridSize, relOrigin[1] - vs.gridSize / 2);
                    g2d.drawLine(relEnd[0], relEnd[1], relEnd[0] - vs.gridSize, relEnd[1] + vs.gridSize / 2);
                    g2d.drawLine(relOrigin[0] - vs.gridSize, relOrigin[1] - vs.gridSize,
                            relOrigin[0] - 2 * vs.gridSize, relOrigin[1] - vs.gridSize);
                    g2d.setStroke(new BasicStroke((int) (vs.TRANSISTOR_WIDTH * 1.5)));
                    g2d.drawLine(relOrigin[0] - vs.gridSize, relOrigin[1], relEnd[0] - vs.gridSize, relEnd[1]);
                    break;
                case 2:
                    g2d.drawLine(relOrigin[0], relOrigin[1], relOrigin[0] + vs.gridSize / 2, relOrigin[1] - vs.gridSize);
                    g2d.drawLine(relEnd[0], relEnd[1], relEnd[0] - vs.gridSize / 2, relEnd[1] - vs.gridSize);
                    g2d.drawLine(relOrigin[0] + vs.gridSize, relOrigin[1] - vs.gridSize,
                            relOrigin[0] + vs.gridSize, relOrigin[1] - 2 * vs.gridSize);
                    g2d.setStroke(new BasicStroke((int) (vs.TRANSISTOR_WIDTH * 1.5)));
                    g2d.drawLine(relOrigin[0], relOrigin[1] - vs.gridSize, relEnd[0], relEnd[1] - vs.gridSize);
                    break;
                case 3:
                    g2d.drawLine(relOrigin[0], relOrigin[1], relOrigin[0] + vs.gridSize, relOrigin[1] + vs.gridSize / 2);
                    g2d.drawLine(relEnd[0], relEnd[1], relEnd[0] + vs.gridSize, relEnd[1] - vs.gridSize / 2);
                    g2d.drawLine(relOrigin[0] + vs.gridSize, relOrigin[1] + vs.gridSize,
                            relOrigin[0] + 2 * vs.gridSize, relOrigin[1] + vs.gridSize);
                    g2d.setStroke(new BasicStroke((int) (vs.TRANSISTOR_WIDTH * 1.5)));
                    g2d.drawLine(relOrigin[0] + vs.gridSize, relOrigin[1], relEnd[0] + vs.gridSize, relEnd[1]);
                    break;
                default:
                    break;
            }
        }

        // draw snapping circle in CGI
        g.setColor(vs.SNAPPING_CIRCLE_COLOR);
        g.fillOval(vs.CGI[0] - vs.SNAPPING_CIRCLE_RADIUS / 2, vs.CGI[1] - vs.SNAPPING_CIRCLE_RADIUS / 2,
                vs.SNAPPING_CIRCLE_RADIUS, vs.SNAPPING_CIRCLE_RADIUS);
    }
}
