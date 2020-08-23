package version0_5.UI;

import javax.swing.*;

public class StyleChooser extends JComboBox<String> {

    public final static String[] styles = {"1", "2", "3", "4"};

    public StyleChooser() {
        super(styles);
    }
}
