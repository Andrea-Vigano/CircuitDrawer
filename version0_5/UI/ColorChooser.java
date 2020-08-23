package version0_5.UI;

import javax.swing.*;
import java.awt.*;

public class ColorChooser extends JComboBox<String> {

    public final static Color[] colors = {
            Color.BLACK,
            Color.GRAY,
            Color.LIGHT_GRAY,
            Color.WHITE,
            Color.PINK,
            Color.MAGENTA,
            Color.RED,
            Color.ORANGE,
            Color.YELLOW,
            Color.GREEN,
            Color.CYAN,
            Color.BLUE
    };

    public ColorChooser(String[] labels) {
        super(labels);
        setMaximumRowCount(12);
        setRenderer(new ColorRenderer(this));
    }
}

class ColorRenderer extends JLabel implements ListCellRenderer {

    private ColorChooser colorChooser;

    public ColorRenderer(ColorChooser colorChooser) {
        setOpaque(true);
        this.colorChooser = colorChooser;
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        setText(" ");
        setBorder(BorderFactory.createMatteBorder(5, 7, 5, 7, Color.DARK_GRAY));
        if (index >= 0) {
            setBackground(ColorChooser.colors[index]);
        } else {
            colorChooser.setBackground(ColorChooser.colors[list.getSelectedIndex()]);
        }
        return this;
    }
}
