package Unit;

import GameState.Infobar;
import TileMap.Tile;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;

/**
 * A dynamic movable object that can be moved upon creation.
 * <p>
 * Dynamic objects could be any movable object in the game such as NPCs, projectiles or players.
 */
public abstract class DynamicObject extends GameObject {
    //Collision
    protected boolean collidingTopLeft;
    protected boolean collidingTopRight;
    protected boolean collidingBottomLeft;
    protected boolean collidingBottomRight;
    private final static int collisionMargin = 1;
    private int cWidth;
    private int cHeight;

    //Directions
    protected double velocityY;
    protected double velocityX;
    protected double xDestination;
    protected double yDestination;

    //moving
    protected boolean movingLeft;
    protected boolean movingRight;
    protected boolean isJumping;
    protected boolean movingUp;
    protected boolean movingDown;
    protected boolean isFalling;
    int latestDirection;

    public static final int RIGHT = 1;
    public static final int UP = 2;
    public static final int LEFT = 3;
    public static final int DOWN = 4;

    protected double movementSpeed;
    protected double fallingSpeed;
    protected double maxFallingSpeed;
    protected double startJumpSpeed;
    protected double maxJumpHeight;


    public DynamicObject(int width, int height, int cWidth, int cHeight) {
        super(true, width, height);
        this.cWidth = cWidth;
        this.cHeight = cHeight;
        movementSpeed = getMovementSpeed();
        fallingSpeed = getFallingSpeed();
        maxFallingSpeed = getMaxFallingSpeed();
        startJumpSpeed = getStartJumpSpeed();
        maxJumpHeight = getMaxJumpHeight();
    }

    public DynamicObject(TileMap tileMap, int width, int height, int cWidth, int cHeight) {
        super(tileMap, true, width, height);
        this.cHeight = cHeight;
        this.cWidth = cWidth;
        movementSpeed = getMovementSpeed();
        fallingSpeed = getFallingSpeed();
        maxFallingSpeed = getMaxFallingSpeed();
        startJumpSpeed = getStartJumpSpeed();
        maxJumpHeight = getMaxJumpHeight();
    }

    protected abstract void getNextPosition();

    abstract double getMovementSpeed();

    abstract double getFallingSpeed();

    abstract double getMaxFallingSpeed();

    abstract double getStartJumpSpeed();

    abstract double getMaxJumpHeight();

    public void update() {
        getNextPosition();
        if (isOnScreen()) {
            checkTileMapCollision();
        }
        setPosition(x + velocityX, y + velocityY);
    }

    private void calculateCorners(double x, double y) {
        leftCol = (int) (x - cWidth / 2) / tileSize;
        rightCol = (int) (x + cWidth / 2) / tileSize;
        topRow = (int) (y - cHeight / 2) / tileSize;
        bottomRow = (int) (y + cHeight / 2) / tileSize;

        if (x + cWidth / 2 >= tileMap.getWidth()) {
            collidingTopRight = true;
            return;
        }

        if (x - cWidth / 2 <= 0) {
            collidingBottomLeft = true;
            leftCol--;
            return;
        }

        if (y + cHeight / 2 >= tileMap.getHeight()) {
            collidingBottomRight = true;
            return;
        }

        if (y - cHeight / 2 <= 0) {
            collidingTopRight = true;
            topRow--;
            return;
        }

        int topLeft = tileMap.getType(topRow, leftCol);
        int topRight = tileMap.getType(topRow, rightCol);
        int botLeft = tileMap.getType(bottomRow, leftCol);
        int botRight = tileMap.getType(bottomRow, rightCol);

        collidingTopLeft = topLeft != Tile.NORMAL;
        collidingTopRight = topRight != Tile.NORMAL;
        collidingBottomLeft = botLeft != Tile.NORMAL;
        collidingBottomRight = botRight != Tile.NORMAL;
    }

    private void checkTileMapCollision() {
        currentRow = (int) (y) / tileSize;
        currentCol = (int) (x) / tileSize;
        xDestination = x + velocityX;
        yDestination = y + velocityY;

        calculateCorners(x, yDestination);
        if (velocityY > 0) {
            if (collidingBottomRight || collidingBottomLeft) {
                isFalling = false;
                velocityY = 0;
                yDestination = (bottomRow) * tileSize - cHeight / 2 - collisionMargin;
            }
        }

        if (velocityY < 0) {
            if (collidingTopRight || collidingTopLeft) {
                velocityY = 0;
                yDestination = (topRow + 1) * tileSize + cHeight / 2 + collisionMargin;
            }
        }


        calculateCorners(xDestination, y);
        if (velocityX > 0) {
            if (collidingTopRight || collidingBottomRight) {
                velocityX = 0;
                xDestination = (rightCol) * tileSize - cWidth / 2 - 2 * collisionMargin;
            }
        }

        if (velocityX < 0) {
            if (collidingBottomLeft || collidingTopLeft) {
                velocityX = 0;
                xDestination = (leftCol + 1) * tileSize + cWidth / 2 + collisionMargin;
            }
        }

        if (!isFalling && !isJumping) {
            calculateCorners(x, yDestination + collisionMargin);
            if (!collidingBottomLeft && !collidingBottomRight) {
                isFalling = true;
            }
        }


    }

    public void setVelocity(double vx, double vy) {
        velocityX = vx;
        velocityY = vy;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public void setJumping(boolean jumping) {
        isJumping = jumping;
    }

    public void setMovingUp(boolean movingUp) {
        this.movingUp = movingUp;
    }

    public void setMovingDown(boolean movingDown) {
        this.movingDown = movingDown;
    }

    public boolean isMovingDown() {
        return movingDown;
    }

    public boolean isMovingUp() {
        return movingUp;
    }

    public boolean isMovingLeft() {
        return movingLeft;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public boolean isFalling() {
        return isFalling;
    }

    public void setLatestDirection(int latestDirection) {
        this.latestDirection = latestDirection;
    }

    public int getLatestDirection() {
        return latestDirection;
    }

    public boolean isStandingStill() {
        return !isJumping && !isFalling && !movingRight && !movingLeft && !movingUp && !movingUp;
    }
}
