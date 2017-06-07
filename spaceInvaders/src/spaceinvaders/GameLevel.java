package spaceinvaders;

import accessories.Ball;
import accessories.Block;
import accessories.Paddle;
import accessories.Velocity;
import accessories.Aliens;
import accessories.Shields;
import animations.AnimationRunner;
import animations.Animation;
import animations.KeyPressStoppableAnimation;
import animations.PauseScreen;
import animations.CountdownAnimation;
import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import collidables.Collidable;
import geometry.Point;
import geometry.Rectangle;
import indicators.Counter;
import indicators.LivesIndicator;
import indicators.NameIndicator;
import indicators.ScoreIndicator;
import levels.Level;
import levels.LevelInformation;
import listeners.BallRemover;
import listeners.BlockRemover;
import listeners.ScoreTrackingListener;
import sprites.Background;
import sprites.BackgroundColor;
import sprites.Sprite;
import sprites.SpriteCollection;

import java.awt.Color;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map;
import java.util.Random;
import java.util.List;


/**
 * A GameLevel class.
 *
 * @author Nir Dunetz and Haim Gil.
 */
public class GameLevel implements Animation {

    // Finals.
    private final int widthScreen = 800;
    private final int heightScreen = 600;
    private final int heightScore = 20;
    private final int shieldsHeight = 450;
    private final int startGameSpeed = 60;
    // Members.
    private SpriteCollection sprites;
    private GameEnvironment environment;
    private Counter remainingBlocks;
    private Counter score;
    private Counter lives;
    private AnimationRunner runner;
    private boolean running;
    private biuoop.KeyboardSensor keyboard;
    private LevelInformation levelInformation;
    private List<Ball> shootingBallList;
    private double canShootFromPaddle;
    private double needToShootFromAlien;
    private Paddle paddle;
    private Aliens aliens;
    private Shields shields;

    /**
     * A Constructor.
     *
     * @param score            is the score of the game.
     * @param lives            is the lives of player.
     * @param runner           is AnimationRunner.
     * @param keyboard         is the keyboard.
     * @param levelInformation is the information of level.
     */
    public GameLevel(Counter score, Counter lives, AnimationRunner runner,
                     KeyboardSensor keyboard, LevelInformation levelInformation) {
        this.score = score;
        this.lives = lives;
        this.runner = runner;
        this.keyboard = keyboard;
        this.levelInformation = levelInformation;
    }

    /**
     * Adding collidable to environment.
     *
     * @param c is collidable object.
     */
    public void addCollidable(Collidable c) {
        if (this.environment == null) {
            this.environment = new GameEnvironment();
        }
        this.environment.addCollidable(c);
    }

    /**
     * Adding sprite to sprite collection.
     *
     * @param s is the sprite.
     */
    public void addSprite(Sprite s) {
        if (this.sprites == null) {
            this.sprites = new SpriteCollection();
        }
        if (this.environment == null) {
            this.environment = new GameEnvironment();
        }
        this.sprites.addSprite(s);
    }

    /**
     * Removing collidable.
     *
     * @param c Is a Collidable to be remove.
     */
    public void removeCollidable(Collidable c) {
        this.environment.removeCollidable(c);
    }

    /**
     * Removing Sprite.
     *
     * @param s Is a Sprite to be remove.
     */
    public void removeSprite(Sprite s) {
        this.sprites.removeSprite(s);
    }

    /**
     * Initialize a new game: create Blocks, Balls, Paddle,boundaries,Indicators and add them to the game.
     */
    public void initialize() {
        this.remainingBlocks = new Counter(this.levelInformation.numberOfBlocksToRemove());
        ScoreTrackingListener scoreTracking = new ScoreTrackingListener(this.score);
        this.shootingBallList = new ArrayList<Ball>();

        // Creating the "Aliens".
        Velocity v = new Velocity((((Level) this.levelInformation).getNumLevel() * 10) + startGameSpeed, 0);
        this.aliens = new Aliens(this.levelInformation.blocks(), this, v, shieldsHeight);
        this.addSprite(this.aliens);

        BlockRemover blockRemover = new BlockRemover(this, this.remainingBlocks, this.aliens);
        BallRemover ballsRemover = new BallRemover(this);

        // Adding the background level.
        this.addSprite(this.levelInformation.getBackground());

        // Create ScoreIndicator and add it to the game.
        ScoreIndicator scoreIndicator = new ScoreIndicator(this.score, widthScreen, heightScore);
        scoreIndicator.addToGame(this);

        // Create LivesIndicator and add it to the game.
        LivesIndicator livesIndicator = new LivesIndicator(this.lives, widthScreen, heightScore);
        livesIndicator.addToGame(this);

        // Create NameIndicator and add it to the game.
        NameIndicator nameIndicator = new NameIndicator(this.levelInformation.levelName(), widthScreen, heightScore);
        nameIndicator.addToGame(this);

        // Creating the color shields.
        BackgroundColor color = new BackgroundColor(Color.cyan);
        Map<Integer, Background> shieldsColor = new TreeMap<Integer, Background>();
        shieldsColor.put(1, color);

        // Creating the shields blocks.
        this.shields = new Shields(shieldsColor, blockRemover, ballsRemover);

        this.shields.addToGame(this);
        this.aliens.addToGame(blockRemover, scoreTracking, ballsRemover);

        // Creating the boundaries.
        this.createBoundaries();

        // Creating the paddle and add to the game.
        this.paddle = this.getPaddle();
        this.paddle.addToGame(this);
    }

    /**
     * Setting the boundaries and create them.
     */
    public void createBoundaries() {
        // Setting the boundaries.
        Rectangle up = new Rectangle(0, heightScore, widthScreen, 1);
        Rectangle left = new Rectangle(-1, heightScore, 1, heightScreen);
        Rectangle right = new Rectangle(widthScreen + 1, heightScore, 1, heightScreen);
        Rectangle down = new Rectangle(0, heightScreen + 1, widthScreen, 1);

        // Creating the boundaries.
        createBlockBoundaries(up, true);
        createBlockBoundaries(left, false);
        createBlockBoundaries(right, false);
        createBlockBoundaries(down, true);
    }

    /**
     * Creating a block boundary.
     *
     * @param boundary     is the rectangle that represent the boundary.
     * @param isDeadRegion Is indicate for dead region.
     */
    public void createBlockBoundaries(Rectangle boundary, boolean isDeadRegion) {
        BallRemover ballsRemover = new BallRemover(this);
        Background b = new BackgroundColor(Color.gray);
        // Setting the hit points to boundaries as default
        Block blockBoundary = new Block(boundary, -2, false);
        // Copy the block, setting the background and adding to game.
        Block blockCopy = new Block(blockBoundary);
        Map<Integer, Background> background = new TreeMap<Integer, Background>();
        background.put(new Integer(-2), b);
        blockCopy.setBackgrounds(background);
        blockCopy.addToGame(this);

        // Remove ball when hit the bottom boundary.
        if (isDeadRegion) {
            blockCopy.addHitListener(ballsRemover);
        }
    }

    /**
     * Doing One frame of the game according to the rules of the game.
     *
     * @param d  is the surface.
     * @param dt is the frames per second.
     */
    public void doOneFrame(DrawSurface d, double dt) {

        // Draw all the sprites.
        this.canShootFromPaddle -= dt;
        this.needToShootFromAlien -= dt;
        this.sprites.notifyAllTimePassed(dt);
        this.sprites.drawAllOn(d);

        // Paddle Shoot every 0.35 seconds.
        if (this.keyboard.isPressed("space") && this.canShootFromPaddle < 0.0) {
            this.canShootFromPaddle = 0.35;
            // Shoot from the middle of the paddle.
            Point p = this.paddle.getMiddlePaddlePoint();
            Ball paddleShoot = new Ball(p, 3, Color.white);
            paddleShoot.setVelocity(Velocity.fromAngleAndSpeed(0, 500));
            paddleShoot.addToGame(this);
            paddleShoot.setBallGameEnvironment(this.environment);
            this.shootingBallList.add(paddleShoot);
        }

        // Alien Shoot every 0.5 seconds.
        if (this.needToShootFromAlien < 0.0) {
            this.needToShootFromAlien = 0.50;
            List<Block> columnAlienBlock = null;
            do {
                Random rand = new Random();
                int n = rand.nextInt(this.aliens.getNumOfColumns());
                columnAlienBlock = this.aliens.getColumnOfAliens(n);
            } while (columnAlienBlock.isEmpty()); // If the column is empty it rand another number.

            // Shoot from the lowest alien in the column.
            Point lowestColumnLocation = this.aliens.getMiddleOfLowestAlienInAColumn(columnAlienBlock);
            Ball alienShoot = new Ball(lowestColumnLocation, 5, Color.red, true);
            alienShoot.setVelocity(Velocity.fromAngleAndSpeed(180, 500));
            alienShoot.addToGame(this);
            alienShoot.setBallGameEnvironment(this.environment);
            this.shootingBallList.add(alienShoot);
        }

        // Pausing the Screen if "p" key is pressed.
        if (this.keyboard.isPressed("p")) {
            this.runner.run(new KeyPressStoppableAnimation(this.keyboard, "space", new PauseScreen()));
        }

        // Check if there is no blocks or no balls left.
        if (this.remainingBlocks.getValue() == 0) {
            this.running = false;
        }

        // Check if the "Aliens" touch the paddle or the paddle has been shot and restart the level.
        if (this.aliens.onShields() || this.paddle.getIsHit()) {
            // Remove all the "shoot" from the game.
            for (int i = 0; i < this.shootingBallList.size(); i++) {
                this.shootingBallList.get(i).removeFromGame(this);
            }
            this.lives.decrease(1);
            // Aliens move to the original position,
            this.aliens.resetPosition();
            this.running = false;
        }
    }

    /**
     * Indicate to stop according to running member.
     *
     * @return boolean - True/False according to this.running.
     */
    public boolean shouldStop() {
        return !this.running;
    }

    /**
     * @return New paddle with the Data of the paddle that use in the game.
     */
    public Paddle getPaddle() {
        return new Paddle(this.keyboard, ((Level) this.levelInformation).getPaddleRectangle(),
                Color.yellow, this.levelInformation.paddleSpeed());
    }

    /**
     * Activates one turn of the game with all all the  data that related to the current level.
     */
    public void playOneTurn() {

        // Moving the paddle to the current location.
        this.paddle.moveTo(((Level) this.levelInformation).paddlePointLocation());
        this.paddle.setIsHit(false);

        // Reset the shooting time.
        this.canShootFromPaddle = 0;
        this.needToShootFromAlien = 0;

        // Count down from 3 to 0 at the beginning of a turn within 2 seconds.
        this.runner.run(new CountdownAnimation(2.0, 3, this.sprites));
        this.running = true;
        // Use the runner to run the current animation  which is one turn of the game.
        this.runner.run(this);

    }

    /**
     * @return the remaining blocks.
     */
    public Counter getRemainingBlocks() {
        return this.remainingBlocks;
    }

    /**
     * @return the remaining lives.
     */
    public Counter getLives() {
        return this.lives;
    }


}

