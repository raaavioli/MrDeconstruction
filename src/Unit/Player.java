package Unit;

import GameState.Infobar;
import Main.GamePanel;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class Player extends DynamicObject {
    BufferedImage image;

    private int health;
    private int maxHealth;

    private boolean onStairs;

    Item[] items;
    public int currentItem;
    HashMap<Integer, Integer> materials;

    public Player(int width, int height) {
        super(width, height);

        health = 3;
        maxHealth = 4;
        movementSpeed = 2;
        fallingSpeed = 0.25;
        maxJumpHeight = 50;
        startJumpSpeed = -5;
        maxFallingSpeed = 5;
        items = new Item[4];
        currentItem = 0;

        materials = new HashMap<>();
        initMaterials();

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Sprites/gubbe.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initMaterials() {
        materials.put(Infobar.WOOD, 0);
        materials.put(Infobar.DIRT, 0);
        materials.put(Infobar.IRON, 0);
        materials.put(Infobar.STONE, 0);
    }

    public HashMap<Integer, Integer> getMaterials() {
        return materials;
    }

    public void incrementMaterial(int type) {
        materials.put(type, materials.get(type) + 1);
    }

    private void getNextPosition() {
        if (movingLeft) {
            velocityX = -movementSpeed;
        } else if (movingRight) {
            velocityX = movementSpeed;
        } else {
            velocityX = 0;
        }


        if (isJumping && !isFalling) {
            velocityY = startJumpSpeed;
            isFalling = true;
        }

        if (isFalling) {
            velocityY += fallingSpeed;
            if (velocityY > 0) isJumping = false;
            if (velocityY > maxFallingSpeed) velocityY = maxFallingSpeed;
        }

        if (movingUp && isOnStairs()) {
            velocityY = -movementSpeed;
        } else if (movingDown && isOnStairs()) {
            velocityY = movementSpeed;
        }
    }

    @Override
    public void update() {

        getNextPosition();
        checkTileMapCollision();
        setPosition(xDestination, yDestination);

    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(image, (int) (x - width / 2 + tileMap.getX()), (int) (y - height / 2 + tileMap.getY()), width, height, null);
        g.setColor(Color.BLACK);
        g.fillRect((int) x + tileMap.getX(), (int) y + tileMap.getY(), 2, 2);
    }

    public void pickup(Item item) {
        if (!item.isDroppedAndInAir()) {
            if (items[currentItem] != null) {
                dropCurrentItem();
            }
            item.setInInventory(true);
            items[currentItem] = item;
        }
    }

    public void dropCurrentItem() {
        items[currentItem].setInInventory(false);
        items[currentItem].setDroppedAndInAir(true);
        System.out.println(items[currentItem].getType());
        items[currentItem] = null;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Item[] getItems() {
        return items;
    }

    public boolean isOnStairs() {
        //Yet to be implemented when stairs are added to the game
        return false;
    }
}
