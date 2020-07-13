package struct_test2;

import java.io.*;
import java.util.LinkedList;

public class DrawFile {

    LinkedList<Line> lines = new LinkedList<Line>();

    public DrawFile(Object[] drawnObjects) {
        for (Object object : drawnObjects) {
            if (object instanceof Line) {
                lines.add((Line) object);
            }
        }
    }

    public void saveFile(File csvOutputFile) throws IOException {
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            this.lines.stream()
                 .map(Line::convertToCSV)
                 .forEach(pw::println);
        }
        assert csvOutputFile.exists();
    }

    public static LinkedList<Line> loadFile(String fileName) throws IOException {
        String _line = "";
        LinkedList<Line> lines = new LinkedList<Line>();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        while ((_line = br.readLine()) != null) {
            String[] line_data = _line.split(",");
            lines.add(new Line(new Cords(Integer.parseInt(line_data[0]), Integer.parseInt(line_data[1]))));
            lines.getLast().setB(new Cords(Integer.parseInt(line_data[2]), Integer.parseInt(line_data[3])));
        }
        return lines;
    }
}
