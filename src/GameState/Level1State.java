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
        items.add(new Item(Item.HAMMER, 120, 30));
        items.add(new Item(Item.PICKAXE, 170, 30));
        items.add(new Item(Item.SPADE, 210, 30));

        player.setTileMap(tileMap);


        player.setPosition(200, 150);
        tileMap.setPosition(-0,  -0);


    }

    @Override
    public void update() {
        player.update();
        for (Item item: items) {
            item.update();
        }
        tileMap.setPosition(GamePanel.WIDTH/2 - player.getX(),  GamePanel.HEIGHT/2 - player.getY());
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.GRAY);
        g.fillRect(0,0, GamePanel.WIDTH, GamePanel.HEIGHT);
        tileMap.draw(g);

        for (Item item: items) {
            item.draw(g);
        }

        player.draw(g);




    }

    @Override
    public void keyPressed(int key) {
        switch (key){
            case KeyEvent.VK_LEFT:
                player.setMovingLeft(true);
                break;
            case KeyEvent.VK_RIGHT:
                player.setMovingRight(true);
                break;
            case KeyEvent.VK_UP:
                player.setJumping(true);
                break;
        }
    }

    @Override
    public void keyReleased(int key) {
        switch (key){
            case KeyEvent.VK_LEFT:
                player.setMovingLeft(false);
                break;
            case KeyEvent.VK_RIGHT:
                player.setMovingRight(false);
                break;
        }
    }
}
