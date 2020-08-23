package version0_5.UI;

import javax.swing.*;

public class WidthChooser extends JComboBox<String> {

    public final static String[] widths = {"Thin", "Medium", "Large"};

    public WidthChooser() {
        super(widths);
    }
}
