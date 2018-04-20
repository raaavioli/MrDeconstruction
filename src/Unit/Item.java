package Unit;

import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Item extends DynamicObject {
    private BufferedImage image;

    private boolean inInventory;
    private boolean droppedAndInAir;

    private int type;

    public static final int NO_ITEM = 0;
    public static final int HAMMER = 1;
    public static final int PICKAXE = 2;
    public static final int CHAINSAW = 3;
    public static final int SPADE = 4;

    public Item(TileMap tileMap, int type) {
        super(tileMap, 32, 32, 32, 32);
        this.type = type;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/HUD/Items/item" + type + ".png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDroppedAndInAir(boolean droppedAndInAir) {
        this.droppedAndInAir = droppedAndInAir;
    }

    public boolean isDroppedAndInAir() {
        return droppedAndInAir;
    }

    public void setInInventory(boolean inInventory) {
        this.inInventory = inInventory;
    }

    public boolean isInInventory() {
        return inInventory;
    }

    public int getType() {
        return type;
    }

    protected void getNextPosition() {
        if (droppedAndInAir && !isFalling) {
            velocityY = startJumpSpeed;
            isFalling = true;
        }

        if (isFalling) {
            velocityY += fallingSpeed;
            if (velocityY > 0) droppedAndInAir = false;
            if (velocityY > maxFallingSpeed) velocityY = maxFallingSpeed;
        }
    }

    @Override
    double getMovementSpeed() {
        return 2;
    }

    @Override
    double getFallingSpeed() {
        return 0.25;
    }

    @Override
    double getMaxFallingSpeed() {
        return 5;
    }

    @Override
    double getStartJumpSpeed() {
        return -5;
    }

    @Override
    double getMaxJumpHeight() {
        return 50;
    }

    @Override
    public void draw(Graphics2D g) {
        if (!inInventory) {
            g.drawImage(image, (int) x - width / 2 + tileMap.getX(), (int) y - height / 2 + tileMap.getY(), width, height, null);
        }
    }
}
