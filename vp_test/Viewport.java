package vp_test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Viewport extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final ViewportState state = new ViewportState();

    public Viewport() {
        super();
        // assign mouse events listeners
        // mouse wheel spy -> zooming
        this.addMouseWheelListener(new MouseWheelSpyVP());
        // mouse motion spy -> padding (dragging) and snapping (moving)
        this.addMouseMotionListener(new MouseMotionSpyVP());
        // key spy -> fast typed commands
        this.addKeyListener(new KeySpyVP());
        this.setFocusable(true);
        // mouse spy -> wire creation
        this.addMouseListener(new MouseSpyVP());
        // dimension tracker
        int[] dimensions = {getSize().width, getSize().height};
        state.setViewportDimensions(dimensions);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int[] dimensions = {e.getComponent().getSize().width, e.getComponent().getSize().height};
                state.setViewportDimensions(dimensions);
                repaint();
            }
        });
        // set bg color
        this.setBackground(Color.GRAY);
    }

    public void paintComponent(Graphics g) {
        // custom paint JPanel
        super.paintComponent(g);
        int[] offset = state.getRelativeOffset();

        // draw grid
        g.setColor(Color.lightGray);
        // use gridSize (zoom dependant) and relativeOffset (pad dependant)
        // draw X-oriented lines
        int rowsCount = getHeight() / state.getGridsize();
        g.drawLine(0, offset[1], getWidth(), offset[1]);
        int currentX = state.getGridsize() + offset[1];
        for (int i = 0; i < rowsCount; i++) {
            g.drawLine(0, currentX, getWidth(), currentX);
            currentX = currentX + state.getGridsize();
        }
        g.drawLine(0, currentX, getWidth(), currentX);
        // draw Y-oriented lines
        int colsCount = getWidth() / state.getGridsize();
        g.drawLine(offset[0], 0, offset[0], getHeight());
        int currentY = state.getGridsize() + offset[0];
        for (int i = 0; i < colsCount; i++) {
            g.drawLine(currentY, 0, currentY, getHeight());
            currentY = currentY + state.getGridsize();
        }
        g.drawLine(currentY, 0, currentY, getHeight());
        //Grid end
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.blue);
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(4,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));

        // draw plotted components
        // Wires
        g2d.setPaint(Color.green);
        for (Wire wire : state.getWires()) {
            // get relative cords
            if (wire.getEnd() != null) {
                int[] relOri = state.getRelCoordinates(wire.getOri()), relEnd = state.getRelCoordinates(wire.getEnd());
                if (!wire.isInverted()) {
                    g2d.drawLine(relOri[0], relOri[1], relOri[0], relEnd[1]);
                    g2d.drawLine(relOri[0], relEnd[1], relEnd[0], relEnd[1]);
                } else {
                    g2d.drawLine(relOri[0], relOri[1], relEnd[0], relOri[1]);
                    g2d.drawLine(relEnd[0], relOri[1], relEnd[0], relEnd[1]);
                }
            }
        }

        // draw snapping circle
        int[] intersection = state.getClosestGridIntersection();
        if (intersection != null) {
            g2d.setPaint(Color.red);
            g2d.fillOval(intersection[0], intersection[1], 3, 3);
        }
    }

    // state getter
    public ViewportState getState() {return state;}
    // window dimensions getter
    public int[] getDimensions() {return new int[]{getWidth(), getHeight()};}

}

class ViewportState {
    // state service class
    // keep track of Positioning Offset and ZoomFactor relative to absolute Origin (0; 0)
    // Padding -> translation
    // zooming -> dilatation

    // P (x; y) in relative coordinates -> P(zoomFactor(x - totalOffsetX); zoomFactor(y - totalOffsetY))

    // Padding updates the totalOffsets
    // Zooming increases / decreases the zoomFactor

    // after every pad / zoom action is performed, the screen its repainted with the up-to-date ViewportState data

    private static final int defaultGridSize = 200;
    private static final int[] absoluteOrigin = {0, 0};
    private static final double zoomUnit = 2.0;
    private int[] viewportDimensions = {0, 0};
    private int[] totalOffset = {0, 0};
    private double zoomFactor = 1;
    private int gridSize = defaultGridSize;
    private final int[] relativeOrigin = {0, 0};
    private int[] relativeOffset = {0, 0};
    private int[] closestGridIntersection;
    private final ArrayList<Boolean> actions = new ArrayList<Boolean>();
    private final ArrayList<Wire> wires = new ArrayList<Wire>();

    ViewportState() {
        actions.add(false); // isAddWireOri
        actions.add(false); // isAddWireEnd
    }

    public static int getDefaultgridsize() {return defaultGridSize;}
    public static int[] getAbsoluteorigin() {return absoluteOrigin;}
    public static double getZoomunit() {return zoomUnit;}
    public int[] getViewportDimensions() {return viewportDimensions;}
    public void setViewportDimensions(int[] viewportDimensions) {this.viewportDimensions = viewportDimensions;}
    public int[] getTotalOffset() {return totalOffset;}
    public void addToTotalOffset(int[] vector) {totalOffset = addVectors(totalOffset, vector); updateRelativeOffset(); updateRelativeOrigin();}
    public void addToTotalOffset(int x, int y) {totalOffset[0] = totalOffset[0] + x; totalOffset[1] = totalOffset[1] + y; updateRelativeOffset(); updateRelativeOrigin();}
    public void subToTotalOffset(int[] vector) {totalOffset = subVectors(totalOffset, vector); updateRelativeOffset(); updateRelativeOrigin();}
    public void subToTotalOffset(int x, int y) {totalOffset[0] = totalOffset[0] - x; totalOffset[1] = totalOffset[1] - y; updateRelativeOffset(); updateRelativeOrigin();}
    public void multiplyTotalOffset(double factor) {totalOffset = multiplyToVector(totalOffset, factor); updateRelativeOffset(); updateRelativeOrigin();}
    public void divideTotalOffset(double factor) {totalOffset[0] = (int) (totalOffset[0] / factor); totalOffset[1] = (int) (totalOffset[1] / factor); updateRelativeOffset(); updateRelativeOrigin();}
    public double getZoomFactor() {return zoomFactor;}
    public void increaseZoomFactor() {zoomIn();}
    public void decreaseZoomFactor() {zoomOut();}
    public int getGridsize() {return gridSize;}
    public int[] getRelativeOrigin() {return relativeOrigin;}
    public int[] getRelativeOffset() {return relativeOffset;}
    public int[] getClosestGridIntersection() {return closestGridIntersection;}
    public void updateClosestGridIntersection(int x, int y) {int[] cords = {x, y}; this.updateClosestGridIntersection(cords);}
    public void resetClosestGridIntersection() {closestGridIntersection = null;}
    public ArrayList<Wire> getWires() {return wires;}
    public void addWire(Wire wire) {this.wires.add(wire);}

    // actions getters and setters
    public boolean getIsAddWireOri() {return actions.get(0);}
    public void setIsAddWireOri(boolean active) {deactivateAll(); actions.set(0, active);}
    public boolean getIsAddWireEnd() {return actions.get(1);}
    public void setIsAddWireEnd(boolean active) {deactivateAll(); actions.set(1, active);}
    public void deactivateAll() {
        for (int i = 0; i < actions.size(); i++) {
            actions.set(i, false);
        }
    }

    // update gridSize when zooming is performed
    private void updateGridSize() {
        gridSize = (int) (defaultGridSize * zoomFactor);
        if (gridSize == 0) {
            gridSize = 1;
        }
    }
    // update relative origin (not necessarely correspond to a grid intersection -> only if relativeOffset == 0)
    private void updateRelativeOrigin() {relativeOrigin[0] = totalOffset[0]; relativeOrigin[1] = totalOffset[1];}
    // update relative-to-edges offsets when padding is performed
    private void updateRelativeOffset() {
        int a, b;
        if (totalOffset[0] > 0) {
            a = gridSize - totalOffset[0] % gridSize;
        } else  {
            a = -totalOffset[0] % gridSize;
        }
        if (totalOffset[1] > 0) {
            b = gridSize - totalOffset[1] % gridSize;
        } else  {
            b = -totalOffset[1] % gridSize;
        }
        relativeOffset = new int[]{a, b};
    }
    // zoom functions
    private void zoomIn() {
        zoomFactor = (Math.round((zoomFactor * zoomUnit) * 1000) / 1000.000);
        // update offset value
        // offset = (offset * zoomUnit) + ((viewportDimension / 2) * (zoomUnit - 1))
        multiplyTotalOffset(zoomUnit);
        addToTotalOffset(multiplyToVector(viewportDimensions, 0.5 * (zoomUnit - 1)));
        updateGridSize();
        updateRelativeOffset();
    }
    private void zoomOut() {
        zoomFactor = (Math.round((zoomFactor / zoomUnit) * 100) / 100.00);
        divideTotalOffset(zoomUnit);
        subToTotalOffset(multiplyToVector(viewportDimensions, 0.5 * (1 - 1 / zoomUnit)));
        updateGridSize();
        updateRelativeOffset();
    }
    // update closest grid intersection to gaven cursor coordinates
    private void updateClosestGridIntersection(int[] coordinates) {
        int padAbsX = coordinates[0] - relativeOffset[0], padAbsY = coordinates[1] - relativeOffset[1];
        int X, Y, distance;
        // get intersectionX
        distance = padAbsX % gridSize;
        if (distance < gridSize / 2) {
            X = coordinates[0] - distance;
        } else {
            X = coordinates[0] + (gridSize - distance);
        }
        // get intersectionY
        distance = padAbsY % gridSize;
        if (distance < gridSize / 2) {
            Y = coordinates[1] - distance;
        } else {
            Y = coordinates[1] + (gridSize - distance);
        }
        closestGridIntersection = new int[]{X, Y};
    }
    // absolute coordinates getter
    public int[] getAbsCoordinates(int[] relativeCords) {
        return new int[]{(int) ((relativeCords[0] + totalOffset[0]) / zoomFactor + 0.5), (int) ((relativeCords[1] + totalOffset[1]) / zoomFactor + 0.5)};
    }
    // relative coordinates getter
    public int[] getRelCoordinates(int[] absoluteCords) {
        return new int[]{(int) (absoluteCords[0] * zoomFactor - totalOffset[0] + 0.5), (int) (absoluteCords[1] * zoomFactor - totalOffset[1] + 0.5)};
    }

    // vector ops
    private int[] addVectors (int[] a, int[] b) {
        return new int[]{a[0] + b[0], a[1] + b[1]};
    }
    private int[] subVectors (int[] a, int[] b) {
        return new int[]{a[0] - b[0], a[1] - b[1]};
    }
    private int[] multiplyToVector (int[] vector, double factor) {
        return new int[]{(int) (vector[0] * factor), (int) (vector[1] * factor)};
    }

    // service functions
    public void printState() {
        System.out.println("---- Viewport state report ----");
        System.out.println("Window: (" + viewportDimensions[0] + "; " + viewportDimensions[1] + ")");
        System.out.println("TotalOffset: (" + totalOffset[0] + "; " + totalOffset[1] + ")");
        System.out.println("RelativeOffset: (" + relativeOffset[0] + "; " + relativeOffset[1] + ")");
        System.out.println("ZoomFactor: " + zoomFactor);
        System.out.println("GridSize: " + gridSize);
    }

}


class MouseWheelSpyVP implements MouseWheelListener {

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        Viewport source = (Viewport) e.getSource();
        // zoom IN -> positive rotation, zoom OUT -> negative rotation
        if (e.getWheelRotation() < 0) {
            // zoom IN
            source.getState().increaseZoomFactor();
        } else {
            // zoom OUT
            source.getState().decreaseZoomFactor();
        }
        source.getState().resetClosestGridIntersection();
        source.repaint();
    }
}

class MouseSpyVP implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent e) {
        Viewport source = (Viewport) e.getSource();
        // if isWireAddOri -> create new Wire
        if (source.getState().getIsAddWireOri()) {
            int[] cords = source.getState().getClosestGridIntersection();
            int[] absCords = source.getState().getAbsCoordinates(cords);
            source.getState().addWire(new Wire(absCords));
            source.getState().setIsAddWireEnd(true);
            // if isWireAddEnd -> clear actions (confirm end cords)
        } else if (source.getState().getIsAddWireEnd()) {
            source.getState().deactivateAll();
        } else {
            // if no action is being performed
            // -> check if coordinates corresponds to an object selectable area
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Viewport source = (Viewport) e.getSource();
        source.getState().printState();
        System.out.println(source.getState().getAbsCoordinates(source.getState().getClosestGridIntersection())[0]
                + " " + source.getState().getAbsCoordinates(source.getState().getClosestGridIntersection())[1]);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Viewport source = (Viewport) e.getSource();
        // if wheel is released -> pad action end
        if (e.getModifiersEx() == 8) {
            ((MouseMotionSpyVP) source.getMouseMotionListeners()[0]).resetXY();
        }
    }
}

class MouseMotionSpyVP implements MouseMotionListener {
    // initial x and y coordinates
    private int x = 0, y = 0;

    @Override
    public void mouseDragged(MouseEvent e) {
        Viewport source = (Viewport) e.getSource();
        // if mouse wheel pressed -> enable pad
        if (e.getModifiersEx() == 8) {
            // pad
            if (x == 0 && y == 0) {
                x = e.getX();
                y = e.getY();
            } else {
                source.getState().addToTotalOffset(x - e.getX(), y - e.getY());
                x = e.getX();
                y = e.getY();
            }
            source.getState().resetClosestGridIntersection();
            source.repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Viewport source = (Viewport) e.getSource();
        // update closest grid intersection
        source.getState().updateClosestGridIntersection(e.getX(), e.getY());
        if (source.getState().getIsAddWireEnd()) {
            int[] cords = source.getState().getClosestGridIntersection();
            int[] absCords = source.getState().getAbsCoordinates(cords);
            source.getState().getWires().get(source.getState().getWires().size() - 1).setEnd(absCords);
        }
        source.repaint();
    }

    public void resetXY() {x = 0; y = 0;}
}

class KeySpyVP implements KeyListener {

    @Override
    public void keyPressed(KeyEvent e) {
        Viewport source = (Viewport) e.getSource();
        if (e.isControlDown()) {
            if (source.getState().getIsAddWireEnd()) {
                source.getState().getWires().get(source.getState().getWires().size() - 1).setInverted(true);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Viewport source = (Viewport) e.getSource();
        if (!e.isControlDown()) {
            if (source.getState().getIsAddWireEnd()) {
                source.getState().getWires().get(source.getState().getWires().size() - 1).setInverted(false);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        Viewport source = (Viewport) e.getSource();
        // if typed 'L' -> activate wire creation action
        if (e.getKeyChar() == 'l') {
            source.getState().setIsAddWireOri(true);
            // if typed 'S' -> return service data state
        } else if (e.getKeyChar() == 's') {
            for (Wire wire : source.getState().getWires())
            {
                System.out.println(source.getState().getRelCoordinates(wire.getEnd())[0]);
                System.out.println(source.getState().getRelCoordinates(wire.getEnd())[1]);
                System.out.println(source.getState().getRelCoordinates(wire.getOri())[0]);
                System.out.println(source.getState().getRelCoordinates(wire.getOri())[1]);
            }
        } else if (e.getKeyChar() == KeyEvent.VK_CONTROL) {

        }
        if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
            source.getState().deactivateAll();
        }
    }
}


class Wire {
    private final int[] origin;
    private int[] end = null;
    private boolean isInverted = false;

    Wire(int[] originCords) {
        origin = originCords;
    }

    public int[] getOri() {return origin;}
    public int[] getEnd() {return end;}
    public void setEnd(int[] end) {this.end = end;}
    public boolean isInverted() {return isInverted;}
    public void setInverted(boolean isIverted) {this.isInverted = isIverted;}

}
