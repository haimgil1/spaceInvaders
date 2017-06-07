package listeners;

import accessories.Ball;
import accessories.Block;
import spaceinvaders.GameLevel;

/**
 * A BallRemover class.
 * A BallRemover is in charge of removing balls from the game, as well as keeping count
 * of the number of balls that remain.
 *
 * @author Nir Dunetz and Haim Gil.
 */
public class BallRemover implements HitListener {
    private GameLevel game;

    /**
     * Construct a BallRemover given the game and a counter represent the number of balls to remove.
     *
     * @param game is the game.
     */
    public BallRemover(GameLevel game) {
        this.game = game;
    }

    /**
     * Remove the ball when hit the block.
     *
     * @param beingHit is the block.
     * @param hitter   is the Ball that's doing the hitting.
     */
    public void hitEvent(Block beingHit, Ball hitter) {
        hitter.removeFromGame(this.game);
    }
}
