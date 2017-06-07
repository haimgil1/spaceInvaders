package accessories;

import biuoop.DrawSurface;
import collidables.Collidable;
import geometry.Line;
import geometry.Point;
import geometry.Rectangle;
import listeners.HitListener;
import listeners.HitNotifier;
import spaceinvaders.GameLevel;
import sprites.Background;
import sprites.Sprite;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A Block class implements Collidable, Sprite and HitNotifier.
 *
 * @author Nir Dunetz and Haim Gil.
 */
public class Block implements Collidable, Sprite, HitNotifier {
    // Members.
    private Rectangle rect;
    private int hitPoints;
    private List<HitListener> hitListeners;
    private Map<Integer, Background> background;
    private Color stroke;
    private Point startPosition;
    private Boolean isAlienBlock;

    /**
     * Construct a block given a rectangle and number of hit points.
     *
     * @param rect         is a rectangle.
     * @param hitPoints    is the number of hit points.
     * @param isAlienBlock is true if the block is alien block, false otherwise.
     */
    public Block(Rectangle rect, int hitPoints, Boolean isAlienBlock) {
        this.rect = rect;
        this.hitPoints = hitPoints;
        this.hitListeners = new ArrayList<HitListener>();
        this.startPosition = rect.getUpperLeft();
        this.isAlienBlock = isAlienBlock;
    }

    /**
     * Construct a block given a rectangle, number of hit points and background.
     *
     * @param rect         is a rectangle.
     * @param hitPoints    is the number of hit points.
     * @param background   is the background of the block.
     * @param isAlienBlock is true if the block is alien block, false otherwise.
     */
    public Block(Rectangle rect, int hitPoints, Map<Integer, Background> background, Boolean isAlienBlock) {
        this.rect = rect;
        this.hitPoints = hitPoints;
        this.hitListeners = new ArrayList<HitListener>();
        this.background = background;
        this.stroke = null;
        this.startPosition = rect.getUpperLeft();
        this.isAlienBlock = isAlienBlock;
    }

    /**
     * Construct a block given a rectangle, number of hit points, background and a stroke.
     *
     * @param rect         is a rectangle.
     * @param hitPoints    is the number of hit points.
     * @param background   is the background of the block.
     * @param strokeColor  is the frame's rectangle.
     * @param isAlienBlock is true if the block is alien block, false otherwise.
     */
    public Block(Rectangle rect, int hitPoints, Map<Integer, Background> background,
                 Color strokeColor, Boolean isAlienBlock) {
        this.rect = rect;
        this.hitPoints = hitPoints;
        this.hitListeners = new ArrayList<HitListener>();
        this.background = background;
        this.stroke = strokeColor;
        this.startPosition = rect.getUpperLeft();
        this.isAlienBlock = isAlienBlock;
    }

    /**
     * Copy Constructor.
     *
     * @param block is a block.
     */
    public Block(Block block) {
        this.rect = block.rect;
        this.hitPoints = block.hitPoints;
        this.hitListeners = block.hitListeners;
        this.background = block.background;
        this.stroke = block.stroke;
        this.startPosition = rect.getUpperLeft();
        this.isAlienBlock = block.getIsAlienBlock();
    }

    /**
     * @return the "collision shape" of the object.
     */
    public Rectangle getCollisionRectangle() {
        return this.rect;
    }

    /**
     * Notify to object that we collided with that was an hit,and check where it occurred.
     *
     * @param hitter          is a ball.
     * @param collisionPoint  is the point where collision occurred.
     * @param currentVelocity is the current velocity of a ball.
     * @return the new velocity expected after the hit (based on the force the object inflicted on us).
     */
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        double dx = currentVelocity.getDx();
        double dy = currentVelocity.getDy();
        Rectangle recHit = this.getCollisionRectangle();

        // Create the lines of the rectangle.
        Line upperLine = recHit.getUpperLine();
        Line leftLine = recHit.getLeftLine();
        Line bottomLine = recHit.getBottomLine();
        Line rightLine = recHit.getRightLine();

        // Checks if collisionPoint is on the bottom/upper side or on right/left side of the rectangle.
        if (upperLine.ifPointOnLine(collisionPoint) || bottomLine.ifPointOnLine(collisionPoint)) {
            dy = -dy;
        } else if (leftLine.ifPointOnLine(collisionPoint) || rightLine.ifPointOnLine(collisionPoint)) {
            dx = -dx;
        }

        // Reduce the hit points if there is a hit.
        if (this.hitPoints > 0) {
            this.hitPoints--;
        }

        this.notifyHit(hitter);
        return new Velocity(dx, dy);
    }

    /**
     * Draw the block on the given DrawSurface.
     *
     * @param surface is the surface.
     */
    public void drawOn(DrawSurface surface) {
        // Setting the rectangle.
        int width = (int) rect.getWidth();
        int height = (int) rect.getHeight();
        int x = (int) rect.getUpperLeft().getX();
        int y = (int) rect.getUpperLeft().getY();

        // Setting the background's block according to hit points.
        if (background.containsKey(this.hitPoints)) {
            background.get(this.hitPoints).drawOnBackground(surface, x, y, width, height);
        } else {
            background.get(-1).drawOnBackground(surface, x, y, width, height);
        }

        // Setting the frame.
        if (this.stroke != null) {
            surface.setColor(this.stroke);
            surface.drawRectangle(x, y, width, height);
        }
    }

    /**
     * Setting the background.
     *
     * @param m is a map.
     */
    public void setBackgrounds(Map<Integer, Background> m) {
        this.background = m;

    }

    /**
     * Notify the sprite that time has passed.
     *
     * @param dt is the frame per second.
     */
    public void timePassed(double dt) {

    }

    /**
     * Adding the block to the game.
     *
     * @param g is the game environment.
     */
    public void addToGame(GameLevel g) {
        g.addSprite(this);
        g.addCollidable(this);
    }

    /**
     * Removing the block from the game.
     *
     * @param game is the game environment.
     */
    public void removeFromGame(GameLevel game) {
        game.removeSprite(this);
        game.removeCollidable(this);
    }

    /**
     * @return the number of hit points.
     */
    public int getHitPoints() {
        return this.hitPoints;
    }

    /**
     * @param hitter Is the ball that collide.
     */
    private void notifyHit(Ball hitter) {
        // Make a copy of the hitListeners before iterating over them.
        List<HitListener> listeners = new ArrayList<HitListener>(this.hitListeners);
        // Notify all listeners about a hit event:
        for (HitListener hl : listeners) {
            hl.hitEvent(this, hitter);
        }
    }

    /**
     * @return the rectangle's width.
     */
    public double getWidth() {
        return this.rect.getWidth();
    }

    /**
     * @param hl is the listener.
     */
    public void addHitListener(HitListener hl) {
        this.hitListeners.add(hl);
    }

    /**
     * @param hl is the listener.
     */
    public void removeHitListener(HitListener hl) {
        this.hitListeners.remove(hl);
    }

    /**
     * Setting a new position to x coordinate rectangle.
     *
     * @param x is the new x.
     */
    public void setPositionByX(double x) {
        this.rect = new Rectangle(x, this.rect.getUpperLeft().getY(), this.rect.getWidth(), this.rect.getHeight());
    }

    /**
     * Setting a new position to y coordinate rectangle.
     *
     * @param y is the new y.
     */
    public void setPositionByY(double y) {
        this.rect.setUpperLeftByYCoordinate(y);
    }

    /**
     * @return the background.
     */
    public Map<Integer, Background> getBackground() {
        return this.background;
    }

    /**
     * @return the number of hit points.
     */
    public int getHitPoint() {
        return this.hitPoints;
    }

    /**
     * @return the original position of the block before moving.
     */
    public Point getStartPosition() {
        return this.startPosition;
    }

    /**
     * @return true if alien block, false otherwise.
     */
    public Boolean getIsAlienBlock() {
        return this.isAlienBlock;
    }

    /**
     * Setting a new block position by point.
     *
     * @param p is the new point.
     */
    public void setPositionByPoint(Point p) {
        this.rect.setUpperLeftByPoint(p);
    }
}
