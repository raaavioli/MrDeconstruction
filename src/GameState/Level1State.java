package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Main.Game;
import Main.GamePanel;
import TileMap.*;
import Unit.DynamicObject;
import Unit.Item;
import Unit.Player;

public class Level1State extends GameState {
    TileMap tileMap;
    Player player;

    ArrayList<Item> items;

    public Level1State(GameStateManager gsm, Player player) {
        this.gsm = gsm;
        this.player = player;
        init();
    }

    @Override
    public void init() {
        //Ändra från 32 om jag upptäcker att jag behöver större än 30x30 tiles.
        tileMap = new TileMap(32);
        tileMap.loadTiles("/Tilesets/groundTiles.png");
        tileMap.loadMap("/Maps/testMap..csv");

        items = new ArrayList<>();
        items.add(new Item(tileMap, Item.SPADE));
        items.add(new Item(tileMap, Item.HAMMER));
        items.add(new Item(tileMap, Item.PICKAXE));
        items.add(new Item(tileMap, Item.CHAINSAW));

        player.setTileMap(tileMap);


        player.setPosition(120, 150);
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            item.setPosition(100 + 50 * i, 50);
        }
        tileMap.setPosition(-0, -0);


    }

    @Override
    public void update() {
        player.update();
        tileMap.setPosition(GamePanel.WIDTH / 2 - player.getX(), GamePanel.HEIGHT / 2 - player.getY());
        for (Item item : items) {
            if (!item.isInInventory()) {
                item.update();
            } else {
                item.setPosition(player.getX(), player.getY());
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
        tileMap.draw(g);

        for (Item item : items) {
            if (!item.isInInventory()) {
                item.draw(g);
            }
        }

        player.draw(g);

    }

    @Override
    public void keyPressed(int key) {
        switch (key) {
            case KeyEvent.VK_LEFT:
                player.setMovingLeft(true);
                player.setLatestDirection(DynamicObject.LEFT);
                player.setFacingLeft(true);
                break;
            case KeyEvent.VK_RIGHT:
                player.setMovingRight(true);
                player.setLatestDirection(DynamicObject.RIGHT);
                player.setFacingLeft(false);
                break;
            case KeyEvent.VK_UP:
                player.setMovingUp(true);
                player.setLatestDirection(DynamicObject.UP);
                break;
            case KeyEvent.VK_DOWN:
                player.setMovingDown(true);
                player.setLatestDirection(DynamicObject.DOWN);
                break;
            case KeyEvent.VK_SPACE:
                player.setJumping(true);
                break;
            case KeyEvent.VK_C:
                for (Item item : items) {
                    if (player.collidesWithObject(item) && !item.isInInventory()) {
                        player.pickup(item);
                    }
                }
                break;
            case KeyEvent.VK_X:
                if (player.getItems()[player.getCurrentItem()] != null) {
                    player.dropCurrentItem();
                }
                break;
            case KeyEvent.VK_V:
                if (!player.isFalling() && player.getItems()[player.getCurrentItem()] != null) {
                    if (player.isMovingLeft()) {
                        player.destroyBlock(player.getCurrentRow(), player.getLeftCol() - 1);
                    } else if (player.isMovingRight()) {
                        player.destroyBlock(player.getCurrentRow(), player.getCurrentCol() + 1);
                    } else if (player.isMovingUp()) {
                        player.destroyBlock(player.getCurrentRow() - 1, player.getCurrentCol());
                    } else if (player.isMovingDown()) {
                        player.destroyBlock(player.getCurrentRow() + 1, player.getCurrentCol());
                    }
                }
                break;
            case KeyEvent.VK_B:
                if (player.getLatestDirection() == DynamicObject.LEFT) {
                    player.buildBlock(player.getCurrentRow(), player.getLeftCol() - 1, player.getCurrentMaterial());
                } else if (player.getLatestDirection() == DynamicObject.RIGHT) {
                    player.buildBlock(player.getCurrentRow(), player.getCurrentCol() + 1, player.getCurrentMaterial());
                } else if (player.getLatestDirection() == DynamicObject.UP) {
                    player.buildBlock(player.getCurrentRow() - 1, player.getCurrentCol(), player.getCurrentMaterial());
                } else if (player.getLatestDirection() == DynamicObject.DOWN) {
                    player.buildBlock(player.getCurrentRow() + 1, player.getCurrentCol(), player.getCurrentMaterial());
                }
                break;
        }
    }


    @Override
    public void keyReleased(int key) {
        switch (key) {
            case KeyEvent.VK_LEFT:
                player.setMovingLeft(false);
                break;
            case KeyEvent.VK_RIGHT:
                player.setMovingRight(false);
                break;
            case KeyEvent.VK_UP:
                player.setMovingUp(false);
                break;
            case KeyEvent.VK_DOWN:
                player.setMovingDown(false);
                break;
        }
    }
}
