package levels;

import accessories.Block;
import sprites.Sprite;

import java.util.List;

/**
 * A LevelInformation interface.
 *
 * @author Nir Dunetz and Haim Gil.
 */
public interface LevelInformation {

    /**
     * @return the speed of the paddle.
     */
    int paddleSpeed();

    /**
     * @return the width of the paddle.
     */
    int paddleWidth();

    /**
     * The level name will be displayed at the top of the screen.
     *
     * @return the name of the level.
     */
    String levelName();

    /**
     * @return a sprite with the background of the level
     */
    Sprite getBackground();

    /**
     * The Blocks that make up this level, each block contains
     * its size, color and location.
     *
     * @return a list of blocks.
     */
    List<Block> blocks();

    /**
     * Number of levels that should be removed
     * before the level is considered to be "cleared".
     *
     * @return the number of balls.
     */
    int numberOfBlocksToRemove();

    /**
     * Setting the level's name.
     *
     * @param string is the new name.
     */
    void setNameLevel(String string);

    /**
     * Setting the level's number.
     *
     * @param i is the new number's level.
     */
    void setNumLevel(int i);
}
