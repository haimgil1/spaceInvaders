import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.List;

import biuoop.GUI;
import biuoop.KeyboardSensor;
/**
 * A SubMenuRead class.
 *
 * @author Shurgil and barisya
 */
public class SubMenuRead {

    private Menu<Task<Void>> menu2;
/**
 * @param reader reader
 * @param sensor keyboard
 * @param r runner
 * @param gui gui
 * @param scoresTable score table
 * @param file file to read form
 * @throws IOException throw
 */
    public SubMenuRead(java.io.Reader reader, KeyboardSensor sensor,
            AnimationRunner r, GUI gui, HighScoresTable scoresTable, File file)
            throws IOException {

        this.menu2 = new SubMenuAnimation<Task<Void>>(sensor, r);

        LineNumberReader br = (LineNumberReader) reader;

        String line = br.readLine();

        while (line != null) {
            if (br.getLineNumber() % 2 == 1) {
                String c = line.substring(0, 1);
                String name = line.substring(2);
                line = br.readLine();

                InputStream is = null;
                is = ClassLoader.getSystemClassLoader().getResourceAsStream(line);
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(is));
                LevelSpecificationReader levelReader = new LevelSpecificationReader(
                        (java.io.Reader) reader2);

                final AnimationRunner r2 = r;
                final KeyboardSensor sensor2 = sensor;
                final GUI gui2 = gui;
                final List<LevelInformation> levels2 = levelReader
                        .getListlevel();
                final HighScoresTable scoresTable2 = scoresTable;
                final File file2 = file;

                Task<Void> rungame = new Task<Void>() {
                    public Void run() {
                        GameFlow theGame = new GameFlow(r2, sensor2, gui2,
                                scoresTable2, file2);
                        try {
                            theGame.runLevels(levels2);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void newstop() {
                        // TODO Auto-generated method stub

                    }
                };
                menu2.addSelection(c, name, rungame);
            }
            line = br.readLine();
        }
        br.close();
    }
/**
 * @return menu
 */
    @SuppressWarnings("unchecked")
	public Task<Void> getTask() {
        return (Task<Void>) this.menu2.getMenuList().get(0).getReturnVal();
    }
/**
 * @return menu
 */
    public Menu<Task<Void>> getmenu() {
        return this.menu2;
    }

}
