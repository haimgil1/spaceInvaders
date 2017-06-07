package read;

import accessories.Block;

/**
 * A BlockCreator interface.
 *
 * @author Nir Dunetz and Haim Gil.
 */
public interface BlockCreator {
    /**
     * Create a block at the specified location.
     *
     * @param xpos is the x position.
     * @param ypos is the y position.
     * @return the block with xpos and ypos.
     */
    Block create(int xpos, int ypos);
}