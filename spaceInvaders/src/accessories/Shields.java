package accessories;

import geometry.Rectangle;
import listeners.BallRemover;
import listeners.BlockRemover;
import spaceinvaders.GameLevel;
import sprites.Background;

import java.util.Map;

/**
 * A Shields class.
 *
 * @author Nir Dunetz and Haim Gil.
 */
public class Shields {
    // Finals.
    private static final int SHIELD_HEIGHT = 450;
    //Members.
    private Map<Integer, Background> shieldsColor;
    private BlockRemover blockRemover;
    private BallRemover ballsRemover;

    /**
     * A constructor.
     *
     * @param shieldsColor is the shield color.
     * @param blockRemover is the block remover.
     * @param ballsRemover is the ball remover.
     */
    public Shields(Map<Integer, Background> shieldsColor, BlockRemover blockRemover, BallRemover ballsRemover) {
        this.shieldsColor = shieldsColor;
        this.blockRemover = blockRemover;
        this.ballsRemover = ballsRemover;
    }

    /**
     * Creating a block shield.
     *
     * @param x         is the x position.
     * @param y         is the y position.
     * @param gameLevel is the game level.
     */
    public void createShieldBlock(double x, double y, GameLevel gameLevel) {

        int counter = 1;
        double tempX = x;
        for (int i = 1; i <= 90; i++) {

            // New line shield.
            if (i % 30 == 0) {
                y += 5;
                x = tempX;
                counter = 1;
                continue;
            }
            // Adding the block to the game.
            Rectangle rectangle = new Rectangle(x + (counter * 5), y, 5, 5);
            Block block = new Block(rectangle, 1, this.shieldsColor, false);
            block.addHitListener(this.blockRemover);
            block.addHitListener(this.ballsRemover);
            block.addToGame(gameLevel);
            counter++;
        }
    }

    /**
     * Add all the shields to the game.
     *
     * @param gameLevel is the game level.
     */
    public void addToGame(GameLevel gameLevel) {
        // Creating 3 block shield.
        for (int i = 50; i <= 550; i += 250) {
            this.createShieldBlock(i, SHIELD_HEIGHT, gameLevel);
        }
    }
}
