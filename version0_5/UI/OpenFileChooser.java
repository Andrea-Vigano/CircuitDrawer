package version0_5.UI;

import version0_5.Engine.OpenFile;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.*;
import java.util.LinkedList;

/**
 * Open file chooser extension, add recently opened file list aside the default list
 * Get the recently opened files from the file recent.txt updated by OpenFile.storeOpenedFileAsRecentlyOpened
 */
public class OpenFileChooser extends JFileChooser implements ListSelectionListener {

    DefaultListModel<String> recent = new DefaultListModel<>();
    JList<String> list = new JList<>(recent);

    public OpenFileChooser() {
        setAccessory(list);
        list.addListSelectionListener(this);
    }

    /**
     * Update the recently opened files
     */
    public void updateRecent() {
        int i = 0;
        String line;
        LinkedList<String> lines = new LinkedList<>();
        try {
            // extract data
            BufferedReader br = new BufferedReader(new FileReader(OpenFile.RECENTLY_OPENED_STORAGE_NAME));
            while ((line = br.readLine()) != null && i < 6) {
                // update data only if it is reading different files from the current one
                if (!line.equals(OpenFile.current) && !lines.contains(line)) {
                    lines.addLast(line);
                    i++;
                }
            }
            // load data into the JList
            for (String _line : lines) {
                recent.addElement(_line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        // set selected file as the selected element
        setSelectedFile(new File(list.getSelectedValue()));
    }
}
