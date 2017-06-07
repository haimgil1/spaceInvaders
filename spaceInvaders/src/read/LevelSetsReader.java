package read;

import java.io.IOException;
import java.io.LineNumberReader;

/**
 * A LevelSetsReader class.
 *
 * @author Nir Dunetz and Haim Gil.
 */
public class LevelSetsReader {
    /**
     * read lines and create list of level sets information.
     *
     * @param reader Is a java.io.Reader.
     * @return List of level sets information.
     */
    public LevelSetsInfo fromReader(java.io.Reader reader) {
        LineNumberReader lineNumberReader = new LineNumberReader(reader);
        //Representing line that read.
        String line;
        String[] splitLine = null;
        LevelSetsInfo levelSetInfo = null;
        try {
            // Splitting the line in accordance to odd/even row.
            while ((line = lineNumberReader.readLine()) != null) {
                if (lineNumberReader.getLineNumber() % 2 == 1) {
                    splitLine = line.split(":");
                }
                line = lineNumberReader.readLine();
                levelSetInfo = new LevelSetsInfo(splitLine[0], splitLine[1], line);
            }
        } catch (IOException io) {
            System.out.print("Failed to find path");
            io.printStackTrace(System.err);
        }
        return levelSetInfo;
    }
}
