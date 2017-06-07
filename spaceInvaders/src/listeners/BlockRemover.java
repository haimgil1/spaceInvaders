package listeners;

import accessories.Aliens;
import accessories.Ball;
import accessories.Block;
import indicators.Counter;
import spaceinvaders.GameLevel;

/**
 * A BlockRemover class.
 * A BlockRemover is in charge of removing blocks from the game, as well as keeping count
 * of the number of blocks that remain.
 *
 * @author Nir Dunetz and Haim Gil.
 */
public class BlockRemover implements HitListener {
    private GameLevel game;
    private Counter remainingBlocks;
    private Aliens aliens;

    /**
     * Construct a BlockRemover given the game and a counter represent the number of block to remove.
     *
     * @param aliens        Is an aliens.
     * @param game          is the game.
     * @param removedBlocks is a counter represent the number of block to remove.
     */
    public BlockRemover(GameLevel game, Counter removedBlocks, Aliens aliens) {
        this.game = game;
        this.remainingBlocks = removedBlocks;
        this.aliens = aliens;
    }

    /**
     * Blocks that are hit and reach 0 hit-points should be removed
     * from the game.
     *
     * @param beingHit is the block.
     * @param hitter   is the Ball that's doing the hitting.
     */
    public void hitEvent(Block beingHit, Ball hitter) {

        // Check which ball and block is hits.
        if (!hitter.getIsAlienShoot()) {
            // Alien block should ignore alien's shoot.
            if (beingHit.getIsAlienBlock()) {
                this.aliens.getBlockAliensList().remove(beingHit);
                this.remainingBlocks.decrease(1);
            }
            beingHit.removeFromGame(this.game);
            beingHit.removeHitListener(this);
        } else if (hitter.getIsAlienShoot() && !beingHit.getIsAlienBlock()) {
            beingHit.removeFromGame(this.game);
            beingHit.removeHitListener(this);
        }

    }
}

