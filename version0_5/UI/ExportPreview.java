package version0_5.UI;

import version0_5.Engine.ViewportSpecs;

import java.awt.*;

public class ExportPreview extends Viewport {

    public int imageWidth;
    public int imageHeight;
    public int MIN_IMAGE_WIDTH = 100,
        MIN_IMAGE_HEIGHT = 100;
    public int rectSnap = 0;
    int[] snapCenter = {-5, -5};

    Color RECTANGLE_COLOR = Color.RED;
    BasicStroke RECTANGLE_STROKE = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);

    Color SNAPPING_EDGE_COLOR = Color.GREEN;

    public ExportPreview(ViewportSpecs _vs, int imageWidth, int imageHeight) {
        super(_vs);
        setDrawSnappingCircle(false);
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Draw image sizes rectangle
        g2d.setColor(RECTANGLE_COLOR);
        g2d.setStroke(RECTANGLE_STROKE);
        int[][] rectCords = getRectEdgesCords();
        g2d.drawRect(rectCords[0][0], rectCords[0][1], imageWidth, imageHeight);
        switch (rectSnap) {
            case 1 -> snapCenter = rectCords[0];
            case 2 -> snapCenter = rectCords[1];
            case 3 -> snapCenter = rectCords[2];
            case 4 -> snapCenter = rectCords[3];
            default -> snapCenter = new int[]{-5, -5};
        }
        g2d.setColor(SNAPPING_EDGE_COLOR);
        g2d.fillRect(snapCenter[0] - vs.SNAPPING_CIRCLE_RADIUS / 2, snapCenter[1] - vs.SNAPPING_CIRCLE_RADIUS / 2,
                vs.SNAPPING_CIRCLE_RADIUS, vs.SNAPPING_CIRCLE_RADIUS);
    }

    public int[][] getRectEdgesCords() {
        int baseX = (getWidth() - imageWidth) / 2,
                baseY = (getHeight() - imageHeight) / 2;
        return new int[][]{new int[]{baseX, baseY},
                new int[]{baseX + imageWidth, baseY},
                new int[]{baseX + imageWidth, baseY + imageHeight},
                new int[]{baseX, baseY + imageHeight}};
    }
}
