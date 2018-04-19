package Unit;

import GameState.Infobar;
import TileMap.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Player extends DynamicObject {
    private BufferedImage image;
    private Animation walking;

    private int health;
    private int maxHealth;

    private boolean onStairs;

    private Item[] items;
    private int currentItem;
    private HashMap<Integer, Integer> materials;
    private int currentMaterial;

    public static final int WALKING = 0;

    public Player(int width, int height, int cWidth, int cHeight) {
        super(width, height, cWidth, cHeight);

        health = 3;
        maxHealth = 4;
        latestDirection = RIGHT;
        movementSpeed = 2;
        fallingSpeed = 0.25;
        maxJumpHeight = 50;
        startJumpSpeed = -5;
        maxFallingSpeed = 5;
        items = new Item[4];
        currentItem = 0;
        currentMaterial = Infobar.WOOD;

        materials = new HashMap<>();
        initMaterials();

        walking = new Animation(100);
        TileMap walkingTiles = new TileMap(32);
        walkingTiles.loadTiles("/Sprites/walking.png");

        for (Tile tile : walkingTiles.getTiles()[WALKING]) {
            walking.addImage(tile.getImage());
        }

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Sprites/gubbe.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initMaterials() {
        materials.put(Infobar.WOOD, 0);
        materials.put(Infobar.DIRT, 0);
        materials.put(Infobar.IRON, 0);
        materials.put(Infobar.STONE, 0);
    }

    public HashMap<Integer, Integer> getMaterials() {
        return materials;
    }

    public void incrementMaterialCount(int type, int amount) {
        materials.put(type, materials.get(type) + amount);
    }

    public boolean decrementMaterialCount(int type, int amount) {
        if (materials.get(type) - amount >= 0) {
            materials.put(type, materials.get(type) - amount);
            return true;
        }
        return false;
    }

    public void buildBlock(int row, int col, int type) {
        if (items[currentItem].getType() == currentMaterial && materials.get(currentMaterial) > 0 && tileMap.getType(row, col) == 0) {
            tileMap.changeType(row, col, type * 5);
            decrementMaterialCount(type, 1);
        }
    }

    public void destroyBlock(int row, int col) {
        if (items[currentItem].getType() == tileMap.getType(row, col)) {
            int material = tileMap.getType(row, col);
            tileMap.changeType(row, col, 0);
            incrementMaterialCount(material, 1);
        }
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
        if (isFalling) {
            try {
                image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Jump.png"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (isStandingStill()) {
            try {
                image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Idle.png"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (movingLeft || movingRight) {
            image = walking.getImage();
            walking.update();
        }

        if (latestDirection == LEFT) {
            g.drawImage(image, (int) (x - width / 2 + tileMap.getX() + width), (int) (y - height / 2 + tileMap.getY()), -width, height, null);
        } else if (latestDirection == RIGHT) {
            g.drawImage(image, (int) (x - width / 2 + tileMap.getX()), (int) (y - height / 2 + tileMap.getY()), null);
        }
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

    public void incrementCurrentMaterial() {
        if (currentMaterial < materials.keySet().size() - 1) {
            currentMaterial++;
        }
    }

    public void decrementCurrentMaterial() {
        if (currentMaterial > 0) {
            currentMaterial--;
        }
    }

    public int getCurrentMaterial() {
        return currentMaterial;
    }

    public int getCurrentItem() {
        return currentItem;
    }

    public void incrementCurrentItem() {
        if (currentItem < items.length - 1) {
            currentItem++;
        }
    }

    public void decrementCurrentItem() {
        if (currentItem > 0) {
            currentItem--;
        }
    }
}
