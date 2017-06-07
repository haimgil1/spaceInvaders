package accessories;

import biuoop.DrawSurface;
import geometry.Point;
import listeners.BallRemover;
import listeners.BlockRemover;
import listeners.ScoreTrackingListener;
import spaceinvaders.GameLevel;
import sprites.Sprite;

import java.util.ArrayList;
import java.util.List;

/**
 * A Aliens class.
 *
 * @author Nir Dunetz and Haim Gil.
 */
public class Aliens implements Sprite {

    private final int guiWidth = 800;
    private final int guiLeftBoundary = 0;
    private final int numOfColumns = 10;
    private final int spaceBetweenAliens = 10;

    //Members.
    private List<Block> aliens;
    private int bottom;
    private Velocity velocity;
    private double[] columnsOrder;
    private Velocity startLevelVelocity;
    private GameLevel game;

    /**
     * A Constructor.
     *
     * @param aliensBlocks Is list of blocks.
     * @param game         is the game level.
     * @param v            Is velocity.
     * @param bottom       Is the bottom coordinate(y) that aliens can be.
     */
    public Aliens(List<Block> aliensBlocks, GameLevel game, Velocity v, int bottom) {
        this.aliens = new ArrayList<Block>();
        this.copyList(aliensBlocks);
        this.game = game;
        this.velocity = v;
        this.bottom = bottom;
        this.setColumns();
        this.startLevelVelocity = v;
    }

    /**
     * Creating a copy list of initialize block list of aliens.
     *
     * @param aliensBlocks Is list of block to be copy.
     */
    public void copyList(List<Block> aliensBlocks) {

        for (int i = 0; i < aliensBlocks.size(); i++) {
            Block blockCopy = new Block(aliensBlocks.get(i));
            this.aliens.add(blockCopy);
        }

    }

    /**
     * Adding aliens blocks to game.
     *
     * @param blockRemove is the block remover.
     * @param sTracking   is the score tracking.
     * @param ballsRemove is the ball remover.
     */
    public void addToGame(BlockRemover blockRemove, ScoreTrackingListener sTracking, BallRemover ballsRemove) {
        // Create blocks, HitListeners and add them to the game.
        for (int i = 0; i < this.aliens.size(); i++) {
            Block block = this.aliens.get(i);
            block.addHitListener(blockRemove);
            block.addHitListener(sTracking);
            block.addHitListener(ballsRemove);
            block.addToGame(this.game);
        }
    }

    /**
     * Setting array that every index indicate for number of column and insert to index
     * x position of every column in according.
     */
    public void setColumns() {
        this.columnsOrder = new double[numOfColumns];
        for (int i = 0; i < numOfColumns; i++) {
            this.columnsOrder[i] = this.aliens.get(i).getStartPosition().getX();
        }
    }

    /**
     * finding the edge from aliens according to operator and return is x position.
     * note: if there is aliens in the same  position return the first to be found.
     *
     * @param operator is ">" or "<".
     * @return The X position of the the edge alien.
     */
    public double getEdgeAlien(String operator) {
        double edge;
        double temp;
        // Check if there is aliens.
        if (!(this.aliens.isEmpty())) {
            edge = this.aliens.get(0).getCollisionRectangle().getUpperLeft().getX();
            //Getting the first alien.
            for (int i = 0; i < this.aliens.size(); i++) {
                temp = this.aliens.get(i).getCollisionRectangle().getUpperRight().getX();

                //In case of searching for the right edge alien.
                if (operator.equals(">")) {
                    if (temp > edge) {
                        edge = temp;
                    }
                } else if (operator.equals("<")) { //In case of searching for the left edge alien.
                    if (temp < edge) {
                        edge = temp;
                    }
                }
            }
            return edge;
        }
        return -1;
    }

    /**
     * Finding the Position of the lowest alien .
     *
     * @return The x coordinate of the lowest alien
     */
    public double getLowestAlien() {
        double lowest;
        double temp;
        // Check if there is aliens.
        if (!(aliens.isEmpty())) {
            lowest = aliens.get(0).getCollisionRectangle().getBottomLeft().getY();
            // Go over all aliens.
            for (int i = 0; i < aliens.size(); i++) {
                temp = aliens.get(i).getCollisionRectangle().getBottomLeft().getY();
                if (temp > lowest) {
                    lowest = temp;
                }
            }
            return lowest;
        }
        return -1;
    }

    /**
     * Getting dt in moving aliens in according to there velocities.
     *
     * @param dt Is the frames per seconds.
     */
    public void moveTo(double dt) {
        // Check if aliens on boundaries and Update velocity and position.
        if (this.getEdgeAlien("<") <= guiLeftBoundary || this.getEdgeAlien(">") >= guiWidth) {
            double newDx = (-1.1) * this.velocity.getDx();
            this.velocity = new Velocity(newDx, 0.0);
            this.updateYPosition();
        }
        // Moving aliens.
        this.moveSideWays(dt);
    }

    /**
     * Update y coordinate of aliens + 10 every call to function.
     */
    public void updateYPosition() {
        for (int i = 0; i < this.aliens.size(); i++) {
            double currentY = this.aliens.get(i).getCollisionRectangle().getUpperLeft().getY();
            this.aliens.get(i).setPositionByY(currentY + 10);
        }
    }

    /**
     * Moving one step aliens according to velocity and dt.
     *
     * @param dt Is the frames per seconds.
     */
    public void moveSideWays(double dt) {
        for (int i = 0; i < this.aliens.size(); i++) {
            double currentX = this.aliens.get(i).getCollisionRectangle().getUpperLeft().getX();
            this.aliens.get(i).setPositionByX(currentX + (this.velocity.getDx() * dt));
        }
    }


    /**
     * Reset all blocks in aliens list to there initialize position and moving left considering empty columns.
     */
    public void resetPosition() {
        int counter = 0;
        //Checking how many left columns are missing.
        while (getColumnOfAliens(counter).isEmpty()) {
            counter++;
        }

        // Update start position.
        for (int i = 0; i < this.aliens.size(); i++) {
            double x = this.aliens.get(i).getStartPosition().getX()
                    - (counter * (this.aliens.get(i).getWidth() + spaceBetweenAliens));
            double y = this.aliens.get(i).getStartPosition().getY();
            this.aliens.get(i).setPositionByPoint(new Point(x, y));
        }
        this.velocity = this.startLevelVelocity;
    }

    /**
     * Checking if aliens got to the bottom boundary - the shields position.
     *
     * @return True if lowest alien on shields position , False if not.
     */
    public Boolean onShields() {
        if (this.getLowestAlien() >= this.bottom) {
            return true;
        }
        return false;
    }

    /**
     * Getting number of column and Create column list in accordance.
     *
     * @param column Is the number of column to create a list of aliens.
     * @return List of blocks that all in the same column.
     */
    public List<Block> getColumnOfAliens(int column) {
        List<Block> columnList = new ArrayList<Block>();
        for (int i = 0; i < aliens.size(); i++) {
            if (this.aliens.get(i).getStartPosition().getX() == columnsOrder[column]) {
                columnList.add(aliens.get(i));
            }
        }
        return columnList;
    }

    /**
     * Find the lowest block in list of blocks in a column and return the middle of his bottom line.
     *
     * @param column Is a list of blocks that are in the dame column.
     * @return Middle Point of the of bottom line of block.
     */
    public Point getMiddleOfLowestAlienInAColumn(List<Block> column) {
        if (!(column.isEmpty())) {
            Point middle = new Point(column.get(column.size() - 1).getCollisionRectangle().getBottomLine().middle());
            double y = middle.getY() + 5;
            return new Point(middle.getX(), y);
        }
        return null;
    }

    /**
     * Return the aliens blocks.
     *
     * @return List of blocks
     */
    public List<Block> getBlockAliensList() {
        return this.aliens;
    }

    /**
     * @return number of columns of aliens.
     */
    public int getNumOfColumns() {
        return this.columnsOrder.length;
    }

    /**
     * Draw the sprite to the screen.
     *
     * @param d is the surface.
     */
    public void drawOn(DrawSurface d) {

    }

    /**
     * Notify the sprite that time has passed.
     *
     * @param dt Is the frame per second.
     */
    public void timePassed(double dt) {
        this.moveTo(dt);
    }
}
