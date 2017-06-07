package levels;

import accessories.Ball;
import accessories.Block;
import geometry.Point;
import geometry.Rectangle;
import sprites.Background;
import sprites.Sprite;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * A Level class.
 *
 * @author Nir Dunetz and Haim Gil.
 */
public class Level implements LevelInformation {
    // Members.
    private String levelName;
    private int paddleSpeed;
    private int paddleWidth;
    private Background background;
    private List<Block> blocks;
    private int numBlocks;
    private int levelNum;
    // Finals.
    private static final int PADDLE_HEIGHT = 15;
    private static final int GUI_WIDTH = 800;
    private static final int GUI_HEIGHT = 600;

    /**
     * A constructor.
     *
     * @param name        is the level's name to be set.
     * @param paddleSpeed is the speed of the paddle to be set.
     * @param paddleWidth is the width of the paddle to be set.
     * @param numBlocks   is the number of blocks to be set.
     * @param background  is the background's level to be set.
     */
    public Level(String name, int paddleSpeed, int paddleWidth, int numBlocks,
                 Background background) {
        this.levelName = name;
        this.paddleWidth = paddleWidth;
        this.paddleSpeed = paddleSpeed;
        this.numBlocks = numBlocks;
        this.blocks = new ArrayList<Block>();
        this.background = background;

    }

    /**
     * @return the speed of the paddle.
     */
    public int paddleSpeed() {
        return paddleSpeed;
    }

    /**
     * @return the width of the paddle.
     */
    public int paddleWidth() {
        return paddleWidth;
    }

    /**
     * @return the height of the paddle.
     */
    public int paddleHeight() {
        return PADDLE_HEIGHT;
    }

    /**
     * The level name will be displayed at the top of the screen.
     *
     * @return the name of the level.
     */
    public String levelName() {
        return levelName;
    }

    /**
     * @return a Background with the background of the level
     */
    public Sprite getBackground() {
        return background;
    }

    /**
     * The Blocks that make up this level, each block contains
     * its size, color and location.
     *
     * @return a list of blocks.
     */
    public List<Block> blocks() {
        return this.blocks;
    }

    /**
     * Number of levels that should be removed
     * before the level is considered to be "cleared".
     *
     * @return the number of balls.
     */
    public int numberOfBlocksToRemove() {
        return this.numBlocks;
    }

    /**
     * Adding block list to the level.
     *
     * @param blocksList is a block list.
     */
    public void addBlocks(List<Block> blocksList) {
        this.blocks.addAll(blocksList);
    }

    /**
     * @return the point's paddle.
     */
    public Point paddlePointLocation() {
        return new Point((GUI_WIDTH - this.paddleWidth()) / 2, GUI_HEIGHT - this.paddleHeight() - 10);
    }

    /**
     * @return the rectangle's paddle.
     */
    public Rectangle getPaddleRectangle() {
        return new Rectangle(this.paddlePointLocation(), this.paddleWidth(), this.paddleHeight());
    }

    /**
     * Adding the balls to list and return it.
     *
     * @param numberOfBalls is the number of balls.
     * @return list of balls.
     */
    public List<Ball> getBallsOfLevel(int numberOfBalls) {
        List<Ball> balls = new ArrayList<Ball>();
        for (int i = 0; i < numberOfBalls; i++) {
            Ball ball = new Ball(400, GUI_HEIGHT - this.paddleHeight() - 20, 3, Color.WHITE, true);
            balls.add(ball);
        }
        return balls;
    }

    /**
     * Setting the level's name.
     *
     * @param nameByNumber is the new name.
     */
    public void setNameLevel(String nameByNumber) {
        this.levelName = "Battle No. " + nameByNumber;
    }

    /**
     * Setting the level's number.
     *
     * @param num is the new number's level.
     */
    public void setNumLevel(int num) {
        this.levelNum = num;
    }

    /**
     * @return the level's number.
     */
    public int getNumLevel() {
        return this.levelNum;
    }
}
