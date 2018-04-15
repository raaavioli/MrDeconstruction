package GameState;

import Main.GamePanel;
import Misc.BorderedBufferedImage;
import Unit.Item;
import Unit.Player;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Infobar extends GameState {

    private Player player;
    private Item[] items;
    private BorderedBufferedImage[] materialImages;
    private BorderedBufferedImage[] itemImages;
    private BorderedBufferedImage[] inventoryItems;

    private int currentMaterial;
    private int currentOption;
    private String[] options;

    private int width;
    private int height;


    public static final int WOOD = 0;
    public static final int DIRT = 1;
    public static final int STONE = 2;
    public static final int IRON = 3;

    public static final int MATERIALS = 0;
    public static final int ITEMS = 1;

    public Infobar(GameStateManager gsm, Player player) {
        width = GamePanel.WIDTH;
        height = 45;
        currentMaterial = WOOD;
        currentOption = MATERIALS;
        options = new String[]{"Materials", "Items"};

        this.gsm = gsm;
        this.player = player;
        items = player.getItems();

        materialImages = createMaterialImages();
        itemImages = createItemImages();
        inventoryItems = generateItemsImages();
    }

    private BorderedBufferedImage[] createMaterialImages() {
        BorderedBufferedImage[] images = new BorderedBufferedImage[4];
        for (int i = 0; i < 4; i++) {
            images[i] = new BorderedBufferedImage("/HUD/Materials/material" + i + ".png", 115 + (i + 1) * 25, 3 * height / 24, 17, 17, 10);
        }
        return images;
    }

    private BorderedBufferedImage[] createItemImages() {
        BorderedBufferedImage[] images = new BorderedBufferedImage[5];
        for (int i = 0; i < 5; i++) {
            //String resourcePath, double x, double y, double width, double height, int cornerRadius
            images[i] = new BorderedBufferedImage("/HUD/Items/item" + i + ".png", 0, 0, 17, 17, 10);
            images[i].setBorderColor(Color.LIGHT_GRAY);
        }

        return images;
    }

    private BorderedBufferedImage[] generateItemsImages() {
        BorderedBufferedImage[] images = new BorderedBufferedImage[4];
        for (int i = 0; i < 4; i++) {
            if (player.getItems()[i] != null) {
                switch (player.getItems()[i].getType()) {
                    case Item.HAMMER:
                        images[i] = itemImages[Item.HAMMER];
                        break;
                    case Item.PICKAXE:
                        images[i] = itemImages[Item.PICKAXE];
                        break;
                    case Item.SPADE:
                        images[i] = itemImages[Item.SPADE];
                        break;
                    case Item.CHAINSAW:
                        images[i] = itemImages[Item.CHAINSAW];
                        break;
                }
            } else {
                images[i] = itemImages[Item.NO_ITEM];
            }
        }
        return images;
    }


    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setCurrentMaterial(int currentMaterial) {
        this.currentMaterial = currentMaterial;
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {
        inventoryItems = generateItemsImages();
        //Update materials
        for (int i = 0; i < 4; i++) {
            if (i == currentMaterial) {
                materialImages[i].setBorderColor(Color.LIGHT_GRAY);
            } else {
                materialImages[i].setBorderColor(Color.DARK_GRAY);
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        //Background
        g.setColor(new Color(90, 90, 90, 140));
        g.fillRect(0, 0, width, height);

        //Line
        g.setColor(new Color(90, 90, 90, 255));
        g.drawLine(0, height, width, height);

        //Texts
        g.setColor(Color.BLACK);
        g.setFont(new Font("DEFAULT", Font.PLAIN, 12));
        g.drawString("Health", 20, (2 * height / 5));
        if (currentOption == MATERIALS) {
            g.drawString("Items", 80, (8 * height / 9));
            g.setColor(Color.LIGHT_GRAY);
            g.drawString("Materials", 80, (2 * height / 5));

        } else if (currentOption == ITEMS) {
            g.drawString("Materials", 80, (2 * height / 5));
            g.setColor(Color.LIGHT_GRAY);
            g.drawString("Items", 80, (8 * height / 9));
        }


        //Health
        for (int i = 1; i <= player.getMaxHealth(); i++) {
            //Health
            if (i <= player.getHealth()) {
                //Draw red heart
                g.setColor(Color.RED);
                g.fillRect(-5 + 15 * i, 7 * height / 12, 10, 10);
            } else {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(-5 + 15 * i, 7 * height / 12, 10, 10);
            }
        }

        //Materials
        for (int i = 0; i < materialImages.length; i++) {
            materialImages[i].draw(g);
        }

        //Items
        for (int i = 0; i < inventoryItems.length; i++) {
            BorderedBufferedImage image = inventoryItems[i];
            image.setDimensions(115 + (i + 1) * 25, 7 * height / 12, 17, 17, 10);
            if (i == player.currentItem) {
                image.setBorderColor(Color.LIGHT_GRAY);
            } else {
                image.setBorderColor(Color.DARK_GRAY);
            }
            image.draw(g);
        }
    }

    @Override
    public void keyPressed(int key) {
        switch (key) {
            case KeyEvent.VK_LEFT:
                if (currentOption == MATERIALS) {
                    currentMaterial--;
                    if (currentMaterial < 0) {
                        currentMaterial = 0;
                    }
                } else if (currentOption == ITEMS) {
                    player.currentItem--;
                    if (player.currentItem < 0) {
                        player.currentItem = 0;
                    }
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (currentOption == MATERIALS) {
                    currentMaterial++;
                    if (currentMaterial >= materialImages.length) {
                        currentMaterial = materialImages.length - 1;
                    }
                } else if (currentOption == ITEMS) {
                    player.currentItem++;
                    if (player.currentItem >= inventoryItems.length) {
                        player.currentItem = inventoryItems.length - 1;
                    }
                }
                break;
            case KeyEvent.VK_UP:
                currentOption--;
                if (currentOption < 0) {
                    currentOption = 0;
                }
                break;
            case KeyEvent.VK_DOWN:
                currentOption++;
                if (currentOption > 1) {
                    currentOption = 1;
                }
                break;
        }
    }

    @Override
    public void keyReleased(int key) {

    }
}
