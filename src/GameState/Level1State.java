package GameState;

import java.awt.*;

import Main.GamePanel;
import TileMap.*;

public class Level1State extends GameState {
    TileMap tileMap;

    public Level1State(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    @Override
    public void init() {
        //Ändra från 32 om jag upptäcker att jag behöver större än 30x30 tiles.
        tileMap = new TileMap(32);
        tileMap.loadTiles("/Tilesets/groundTest.png");
        tileMap.loadMap("/Maps/someMap..csv");
        tileMap.setPosition(0,0);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.GRAY);
        g.fillRect(0,0, GamePanel.WIDTH, GamePanel.HEIGHT);
        //System.out.println("wtf");
        tileMap.draw(g);
    }

    @Override
    public void keyPressed(int key) {

    }

    @Override
    public void keyReleased(int key) {

    }
}
