package Unit;

import Main.GamePanel;
import TileMap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {
    //Tilemap
    protected TileMap tileMap;
    protected int tileSize;
    protected BufferedImage image;

    //Dimensions and position
    protected double x;
    protected double y;
    protected int currentCol;
    protected int currentRow;
    protected int leftCol;
    protected int rightCol;
    protected int topRow;
    protected int bottomRow;
    protected int width;
    protected int height;

    //Collision
    protected boolean isCollidable;

    public GameObject(boolean isCollidable, int width, int height) {
        this.isCollidable = isCollidable;
        this.width = width;
        this.height = height;
    }

    public GameObject(TileMap tileMap, boolean isCollidable, int width, int height) {
        this.isCollidable = isCollidable;
        this.width = width;
        this.height = height;
        this.tileMap = tileMap;
        tileSize = tileMap.getTileSize();
    }

    abstract public void update();

    abstract public void draw(Graphics2D g);

    public void setTileMap(TileMap tileMap) {
        this.tileMap = tileMap;
        tileSize = tileMap.getTileSize();
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getRightCol() {
        return rightCol;
    }

    public int getLeftCol() {
        return leftCol;
    }

    public int getTopRow() {
        return topRow;
    }

    public int getBottomRow() {
        return bottomRow;
    }

    public int getCurrentCol() {
        return currentCol;
    }

    public int getCurrentRow() {
        return currentRow;
    }

    public boolean collidesWithObject(GameObject o) {
        boolean leftOf;
        boolean rightOf;
        boolean above;
        boolean below;
        if (isCollidable && o.isCollidable) {
            leftOf = x + width < o.x;
            rightOf = x > o.x + o.width;
            above = y + height < o.y;
            below = y > o.y + o.height;

            return !leftOf && !above && !rightOf && !below;
        }
        return false;
    }

    public boolean isOnScreen() {
        return x + width / 2 + tileMap.getX() > 0
                && x - width / 2 + tileMap.getX() < GamePanel.WIDTH
                && y + height / 2 + tileMap.getY() > 0
                && y - height / 2 + tileMap.getY() < GamePanel.HEIGHT;
    }
}
