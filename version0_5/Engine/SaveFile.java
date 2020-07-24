package version0_5.Engine;

import version0_5.Components.Resistor;
import version0_5.Components.Transistor;
import version0_5.Components.Wire;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

/**
 * Methods storage class, file saving methods
 */
public class SaveFile {

    public static String name;
    public static String path;

    /**
     * Save the current data in a file with a specific name and a specific path
     *
     * @param name file name
     * @param path file absolute path
     * @param DO storage of the object contained in the drawing it is saving
     */
    public static void saveAs(String name, String path, DrawnObjects DO) {
        SaveFile.name = name;
        SaveFile.path = path;
        File file;
        // create new csv file
        if (path.endsWith(".csv")) {
            file = new File(path);
        } else  {
            file = new File(path + ".csv");
        }
        try {
            boolean exists = file.createNewFile();
            assert !exists;
            SaveFile.writeFile(file, DO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write current data in the selected file
     *
     * @param file selected file
     * @param DO storage of the object contained in the drawing it is saving
     */
    private static void writeFile(File file, DrawnObjects DO) {
        // prepare data lines
        LinkedList<String[]> dataLines = new LinkedList<>();
        for (Wire wire : DO.wires) {
            dataLines.add(wire.convertToDataLine());
        }
        for (Resistor resistor : DO.resistors) {
            dataLines.add(resistor.convertToDataLine());
        }
        for (Transistor transistor : DO.transistors) {
            dataLines.add(transistor.convertToDataLine());
        }
        try {
            try (PrintWriter pw = new PrintWriter(file)) {
                dataLines.stream()
                        .map(SaveFile::convertDataLineToCsv)
                        .forEach(pw::println);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String convertDataLineToCsv(String[] dataLine) {
        return String.join(",", dataLine);
    }

    /**
     * Update the file data on the last location it has been saved with
     */
    public static void save(DrawnObjects DO) {
        saveAs(name, path, DO);
    }
}
