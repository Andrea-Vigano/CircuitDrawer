package version0_5.Engine;

import version0_5.Components.Resistor;
import version0_5.Components.Transistor;
import version0_5.Components.Wire;
import version0_5.Main;

import java.io.*;
import java.util.LinkedList;

/**
 * Methods storage class, file opening and loading methods
 */
public class OpenFile {

    public final static String RECENTLY_OPENED_STORAGE_NAME = "recent.txt";
    public static String current = "";

    /**
     * Open selected .csv file
     *
     * @param path file path
     * @param DO object storage where the file data will be loaded
     */
    public static void openFile(String path, DrawnObjects DO) {
        String line;
        LinkedList<String[]> dataLines = new LinkedList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null) {
                dataLines.addLast(line.split(","));
            }
            // set as currently open file
            OpenFile.current = path;
            SaveFile.path = path;
            String[] temp = path.split("/");
            SaveFile.name = temp[temp.length - 1];
            // Show file name in frame title
            Main.changeFrameTitle( "Circuit drawer  -  " + SaveFile.name);
            // Set as a recently opened file
            OpenFile.storeOpenedFileAsRecentlyOpened(path);
            // Load the file data in the editor
            OpenFile.loadDataLines(dataLines, DO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load the data lines into the list of the drawn object storage
     *
     * @param dataLines String arrays containing the necessary data to draw every object
     * @param DO object storage where the file data will be loaded
     */
    private static void loadDataLines(LinkedList<String[]> dataLines, DrawnObjects DO) {
        // parse data lines
        DO.clearDrawnObjects();
        for (String[] dataLine : dataLines) {
            int i = Integer.parseInt(dataLine[0]);
            if (i == Wire.type) {
                // wire
                DO.wires.addLast(new Wire(Integer.parseInt(dataLine[1]), Integer.parseInt(dataLine[2])));
                DO.wires.getLast().setEnd(Integer.parseInt(dataLine[3]), Integer.parseInt(dataLine[4]));
                DO.wires.getLast().angle = Integer.parseInt(dataLine[5]);
            } else if (i == Resistor.type) {
                // resistor
                DO.resistors.addLast(new Resistor(Integer.parseInt(dataLine[1]), Integer.parseInt(dataLine[2])));
                DO.resistors.getLast().setEnd(Integer.parseInt(dataLine[3]), Integer.parseInt(dataLine[4]));
                DO.resistors.getLast().resistance = Integer.parseInt(dataLine[7]);
            } else if (i == Transistor.type) {
                // transistor
                DO.transistors.addLast(new Transistor(Integer.parseInt(dataLine[1]), Integer.parseInt(dataLine[2])));
                DO.transistors.getLast().setEnd(Integer.parseInt(dataLine[3]), Integer.parseInt(dataLine[4]));
                DO.transistors.getLast().rotation = Integer.parseInt(dataLine[6]);
            }
        }
    }

    /**
     * Store in an external file the recently opened files,
     * add the file it has just opened as one of the recently opened files.
     * Update the list every time a new file is opened
     *
     * @param path Opened file absolute path
     */
    private static void storeOpenedFileAsRecentlyOpened(String path) {
        // open storage file
        String line;
        LinkedList<String> lines = new LinkedList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(OpenFile.RECENTLY_OPENED_STORAGE_NAME));
            while ((line = br.readLine()) != null) {
                lines.addLast(line);
            }
            lines.addFirst(path);
            try (PrintWriter pw = new PrintWriter(new File(OpenFile.RECENTLY_OPENED_STORAGE_NAME))) {
                lines.forEach(pw::println);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
