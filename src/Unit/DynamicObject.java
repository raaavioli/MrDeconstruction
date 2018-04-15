package Unit;

import TileMap.Tile;
import TileMap.TileMap;

import java.awt.*;

/**
 * A dynamic movable object that can be moved upon creation.
 *
 * Dynamic objects could be any movable object in the game such as NPCs, projectiles or players.
 */
public abstract class DynamicObject extends GameObject{
    //Collision
    protected boolean collidingTopLeft;
    protected boolean collidingTopRight;
    protected boolean collidingBottomLeft;
    protected boolean collidingBottomRight;
    protected int leftCol;
    protected int rightCol;
    protected int topRow;
    protected int bottomRow;
    private final static int collisionMargin = 1;

    //Directions
    protected double velocityY;
    protected double velocityX;
    protected double xDestination;
    protected double yDestination;

    //moving
    protected boolean movingLeft;
    protected boolean movingRight;
    protected boolean isJumping;
    protected boolean isClimbingUp;
    protected boolean isClimbingDown;
    protected boolean isFalling;
    protected double movementSpeed;
    protected double fallingSpeed;
    protected double maxFallingSpeed;

    protected double startJumpSpeed;
    protected double maxJumpHeight;


    public DynamicObject(int width, int height) {
        super(true, width, height);
    }

    public DynamicObject(TileMap tileMap, int width, int height){
        super(tileMap, true, width, height);
    }

    public void calculateCorners(double x, double y){
        leftCol = (int) (x - width / 2) / tileSize;
        rightCol = (int) (x + width / 2  ) / tileSize;
        topRow = (int) (y - height / 2 ) / tileSize;
        bottomRow = (int) (y + height / 2 ) / tileSize;

        int topLeft = tileMap.getType(topRow, leftCol);
        int topRight = tileMap.getType(topRow, rightCol);
        int botLeft = tileMap.getType(bottomRow, leftCol);
        int botRight = tileMap.getType(bottomRow, rightCol);

        collidingTopLeft = topLeft == Tile.STATIC || topLeft == Tile.DESTRUCTABLE;
        collidingTopRight = topRight == Tile.STATIC || topRight == Tile.DESTRUCTABLE;
        collidingBottomLeft = botLeft == Tile.STATIC || botLeft == Tile.DESTRUCTABLE;
        collidingBottomRight = botRight == Tile.STATIC || botRight == Tile.DESTRUCTABLE;


    }

    public void checkTileMapCollision(){
        currentRow = (int) (y) / tileSize;
        currentCol = (int) (x) / tileSize;
        xDestination = x + velocityX;
        yDestination = y + velocityY;

        calculateCorners(x, yDestination);
        if(velocityY > 0){
            if(collidingBottomRight || collidingBottomLeft){
                isFalling = false;
                velocityY = 0;
                yDestination = (bottomRow)*tileSize - height / 2-collisionMargin;
            }
        }

        if(velocityY < 0){
            if(collidingTopRight || collidingTopLeft){
                velocityY = 0;
                yDestination = (topRow+1)*tileSize + height / 2 + collisionMargin;
            }
        }


        calculateCorners(xDestination, y);
        if(velocityX > 0){
            if(collidingTopRight || collidingBottomRight){
                velocityX = 0;
                xDestination = (rightCol)*tileSize - width / 2 - 2*collisionMargin;
            }
        }

        if(velocityX < 0){
            if(collidingBottomLeft || collidingTopLeft){
                velocityX = 0;
                xDestination = (leftCol+1)*tileSize + width / 2 + collisionMargin;
            }
        }

        if(!isFalling && !isJumping){
            calculateCorners(x, yDestination+collisionMargin);
            if(!collidingBottomLeft && !collidingBottomRight){
                isFalling = true;
            }
        }


    }

    public void setVelocity(double vx, double vy){
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

    public void setClimbingUp(boolean climbingUp) {
        isClimbingUp = climbingUp;
    }

    public void setClimbingDown(boolean climbingDown) {
        isClimbingDown = climbingDown;
    }

    public boolean isStandingStill(){
        return !isJumping && !isFalling && !movingRight && !movingLeft && !isClimbingDown && !isClimbingUp;
    }
}
