package GameState;

import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState extends GameState {

    private Background bg;

    private int currentChoice = 0;
    private String[] options = {
            "Start",
            "Help",
            "Quit"
    };

    private Color titleColor;
    private Font titleFont;

    private Font font;

    public MenuState(GameStateManager gsm){
        this.gsm = gsm;

        try{
            bg = new Background("/Backgrounds/menubg.png", 1);
            bg.setVector(-0.1, 0);

            titleColor = new Color(128, 0, 0);
            titleFont = new Font("Century Gothic", Font.PLAIN, 28);

            font = new Font("Arial", Font.PLAIN, 12);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {
        bg.update();
    }

    @Override
    public void draw(Graphics2D g) {
        // Draw background
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("Mr. Deconstruction", 80, 70);

        g.setFont(font);
        for (int i = 0; i < options.length; i++) {
            if(i == currentChoice){
                g.setColor(Color.BLACK);
            }else{
                g.setColor(Color.RED);
            }
            g.drawString(options[i], 145, 140+ i * 15);
        }
    }

    private void select(){
        switch (currentChoice){
            case 0:
                //Start
                break;
            case 1:
                //Help
                break;
            case 2:
                System.exit(0);
                break;
        }
    }

    @Override
    public void keyPressed(int key) {
        if(key == KeyEvent.VK_ENTER){
            select();
        }
    }

    @Override
    public void keyReleased(int key) {

    }
}