import animations.AnimationRunner;
import animations.HighScoresAnimation;
import animations.KeyPressStoppableAnimation;
import animations.MenuAnimation;
import biuoop.GUI;
import biuoop.KeyboardSensor;
import highscores.HighScoresTable;
import levels.LevelInformation;
import menu.Menu;
import menu.Task;
import read.LevelSetsInfo;
import read.LevelSetsReader;
import read.LevelSpecificationReader;
import spaceinvaders.GameFlow;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * An Ass7Game class.
 *
 * @author Nir Dunetz and Haim Gil.
 */
public class Ass7Game {
    // Finals.
    private static final int GUI_WIDTH = 800;
    private static final int GUI_HEIGHT = 600;
    // Members.
    private String[] args;

    /**
     * Constructor.
     *
     * @param args is String array from the command line.
     */
    public Ass7Game(String[] args) {
        this.args = args;
    }

    /**
     * Ass7Game game main method.
     *
     * @param args is getting a path.
     *             <p/>
     *             Example usage:
     *             java -cp biuoop-1.4.jar:. Ass7Game level_sets.txt
     */
    public static void main(String[] args) {

        Ass7Game game = new Ass7Game(args);
        try {
            game.startGame();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Trying to load a file table txt.
     *
     * @param file is the file.
     * @return the high scores table.
     * @throws Exception when failed to create file.
     */
    public HighScoresTable getHighScoresTable(File file) throws Exception {
        HighScoresTable scoresTable = new HighScoresTable(10);

        if (file.exists() && !file.isDirectory()) {
            scoresTable.load(file);
        } else {
            scoresTable.save(file);
        }
        return scoresTable;

    }

    /**
     * @return @return a path if getting in the command line and default path otherwise.
     */
    public String getPath() {
        String path;
        if (this.args.length > 0) {
            path = this.args[0];
        } else {
            path = "level_sets.txt";
        }
        return path;
    }

    /**
     * @param path is a path to a file.
     * @return list of the subMenu info.
     * @throws Exception when failed to open or closing file.
     */
    public LevelSetsInfo getLevelSetsInfo(String path) throws Exception {
        InputStreamReader is = null;
        LevelSetsInfo levelsInfo = null;
        LevelSetsReader levelSet = new LevelSetsReader();
        try {
            is = new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream((path)));
            levelsInfo = levelSet.fromReader(is);
        } catch (RuntimeException o) {
            System.err.println("Failed open file");
            o.printStackTrace(System.err);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                System.err.println("Failed closing file");
            }
        }
        return levelsInfo;
    }

    /**
     * Adding the sub menu.
     *
     * @param levelsInfo is a class contain all the information of the levels.
     * @return List of LevelInformation.
     * @throws Exception when failed to open or closing file.
     */
    public List<LevelInformation> readingLevelInformation(LevelSetsInfo levelsInfo)
            throws Exception {

        InputStreamReader is = null;
        String fileName = levelsInfo.getPath();
        List<LevelInformation> levelInformation = null;
        try {
            is = new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream((fileName)));
            levelInformation = new LevelSpecificationReader().fromReader(is);
        } catch (RuntimeException o) {
            System.err.println("Failed open file");
            o.printStackTrace(System.err);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                System.err.println("Failed closing file");
            }
        }
        return levelInformation;
    }

    /**
     * Running the menu.
     *
     * @param l               is a list of all information level.
     * @param keyboardSensor  is the sensor.
     * @param animationRunner is the runner.
     * @param highScoresTable is the high score table.
     */

    /**
     * Starting the game.
     *
     * @throws Exception when failed to open or closing file.
     */
    public void startGame() throws Exception {
        // Create the GUI.
        final GUI gui = new GUI("Space Invaders", GUI_WIDTH, GUI_HEIGHT);
        final File file = new File("highscores.txt");
        final AnimationRunner animationRunner = new AnimationRunner(gui);
        final KeyboardSensor keyboardSensor = gui.getKeyboardSensor();
        final HighScoresTable highScoresTable = this.getHighScoresTable(file);

        String path = getPath();
        LevelSetsInfo levelInformation = getLevelSetsInfo(path);

        List<LevelInformation> l = readingLevelInformation(levelInformation);
        runningMenu(l, keyboardSensor, animationRunner, highScoresTable, gui, file);

    }

    /**
     * @param l               is a list of all information level.
     * @param keyboardSensor  is the sensor.
     * @param animationRunner is the runner.
     * @param highScoresTable is the high score table.
     * @param gui             Is a gui screen.
     * @param file            Is a file.
     */
    public void runningMenu(final List<LevelInformation> l, final KeyboardSensor keyboardSensor,
                            final AnimationRunner animationRunner, final HighScoresTable highScoresTable,
                            final GUI gui, final File file) {
        Menu<Task<Void>> menu = new MenuAnimation<Task<Void>>("Space Invaders", keyboardSensor, animationRunner);
        menu.addSelection("s", "Start Game", new Task<Void>() {

            @Override
            public Void run() {
                GameFlow game = new GameFlow(animationRunner, keyboardSensor, gui, highScoresTable, file);
                //Running the game according to levels list.
                try {
                    game.runLevels(l);
                } catch (IOException io) {
                    System.err.println("Failed playing levels");
                    io.printStackTrace(System.err);
                }
                return null;
            }
        });

        menu.addSelection("h", "High Scores", new Task<Void>() {

            /**
             *  Running the the animations.
             * @return null.
             */
            @Override
            public Void run() {
                animationRunner.run(new KeyPressStoppableAnimation(keyboardSensor, "space",
                        new HighScoresAnimation(highScoresTable)));
                return null;
            }
        });

        menu.addSelection("q", "Quit", new Task<Void>() {

            @Override
            public Void run() {
                System.exit(0);
                return null;
            }
        });
        while (true) {
            animationRunner.run(menu);
            // wait for user selection
            Task<Void> task = menu.getStatus();
            task.run();
            menu.setAnimation();
        }
    }

}
