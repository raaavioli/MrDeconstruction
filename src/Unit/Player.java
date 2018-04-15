package Unit;

import Main.GamePanel;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends DynamicObject {
    BufferedImage image;

    private int health;
    private int maxHealth;

    Item[] items;
    public int currentItem;

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

        try{
            image = ImageIO.read(getClass().getResourceAsStream("/Sprites/gubbe.png"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getNextPosition(){
        if(movingLeft){
            velocityX = -movementSpeed;
        }else if(movingRight){
            velocityX = movementSpeed;
        }else{
            velocityX = 0;
        }



        if(isJumping && !isFalling){
            velocityY = startJumpSpeed;
            isFalling = true;
        }

        if(isFalling){
            velocityY += fallingSpeed;
            if(velocityY > 0) isJumping = false;
            if(velocityY >  maxFallingSpeed) velocityY = maxFallingSpeed;
        }

        if(isClimbingUp){
            velocityY = -movementSpeed;
        }else if(isClimbingDown){
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
        g.drawImage(image, (int)(x - width / 2 + tileMap.getX()), (int)(y - height / 2 + tileMap.getY()), width, height,  null);
        g.setColor(Color.BLACK);
        g.fillRect((int)x + tileMap.getX(), (int)y + tileMap.getY(), 2,2);
    }

    public void pickup(Item item){
        if(items[currentItem] != null) {
            dropCurrentItem();
        }
        item.setInInventory(true);
        items[currentItem] = item;
    }

    public void dropCurrentItem(){
            items[currentItem].setInInventory(false);
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
}
