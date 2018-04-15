package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Main.Game;
import Main.GamePanel;
import TileMap.*;
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
        tileMap.loadTiles("/Tilesets/groundTest.png");
        tileMap.loadMap("/Maps/someMap..csv");

        items = new ArrayList<>();
        items.add(new Item(tileMap, Item.HAMMER));
        items.add(new Item(tileMap, Item.PICKAXE));
        items.add(new Item(tileMap, Item.SPADE));

        player.setTileMap(tileMap);


        player.setPosition(200, 150);
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            item.setPosition(100 + 50 * i, 150);
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
                break;
            case KeyEvent.VK_RIGHT:
                player.setMovingRight(true);
                break;
            case KeyEvent.VK_UP:
                player.setJumping(true);
                break;
            case KeyEvent.VK_SPACE:
                for (Item item : items) {
                    if (player.collidesWithObject(item) && !item.isInInventory()) {
                        player.pickup(item);
                    }
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
        }
    }
}
