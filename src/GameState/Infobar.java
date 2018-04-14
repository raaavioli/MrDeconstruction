package GameState;

import Main.GamePanel;
import Misc.BorderedBufferedImage;
import Unit.Item;
import Unit.Player;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Infobar extends GameState{

    private Player player;
    private Item[] items;
    private BorderedBufferedImage[] materialImages;

    private int currentMaterial;
    private int currentItem;
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

    public Infobar(GameStateManager gsm, Player player){
        width = GamePanel.WIDTH;
        height = 45;
        currentMaterial = WOOD;
        currentItem = 0;
        currentOption = MATERIALS;
        options = new String[]{"Materials", "Items"};

        materialImages = createMaterialImages();
        this.gsm = gsm;
        this.player = player;
        items = player.getItems();
    }

    private BorderedBufferedImage[] createMaterialImages(){
        BorderedBufferedImage[] images = new BorderedBufferedImage[4];
        for (int i = 0; i < 4; i++) {
            System.out.println(height/3);
            images[i] = new BorderedBufferedImage("/HUD/Materials/material"+i+".png", 115 + (i+1)*25, 3*height/24, 17, 17, 10);
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
        //Update materials
        for (int i = 0; i < 4; i++) {
            if(i == currentMaterial){
                materialImages[i].setBorderColor(Color.LIGHT_GRAY);
            }else {
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
            g.drawString("Items", 80 , (8*height/9));
            g.setColor(Color.LIGHT_GRAY);
            g.drawString("Materials", 80 , (2*height/5));

        } else if(currentOption == ITEMS){
            g.drawString("Materials", 80 , (2*height/5));
            g.setColor(Color.LIGHT_GRAY);
            g.drawString("Items", 80 , (8*height/9));
        }


        //Boxes
        for (int i = 1; i <= player.getMaxHealth(); i++) {
            //Health
            if(i <= player.getHealth()){
                //Draw red heart
                g.setColor(Color.RED);
                g.fillRect(-5 + 15*i, 7*height/12, 10,10);
            }else{
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(-5 + 15*i, 7*height/12, 10,10);
            }

            //Items
            if(items[i-1] != null) {
                g.setColor(Color.DARK_GRAY);
                g.drawRoundRect(115 + i * 25, height / 2, 17, 17, 10, 10);
            }else{
                g.setColor(Color.DARK_GRAY);
                g.drawRoundRect(115 + i * 25, 7*height / 12, 17, 17, 10, 10);
            }

            //Materials

            materialImages[i-1].draw(g);
            g.setColor(Color.DARK_GRAY);
        }
    }

    @Override
    public void keyPressed(int key) {
        switch (key){
            case KeyEvent.VK_LEFT:
                if(currentOption == MATERIALS) {
                    currentMaterial--;
                    if (currentMaterial < 0) {
                        currentMaterial = 0;
                    }
                }else if(currentOption == ITEMS){
                    currentItem--;
                    if (currentItem < 0) {
                        currentItem = 0;
                    }
                }
                break;
            case KeyEvent.VK_RIGHT:
                if(currentOption == MATERIALS) {
                    currentMaterial++;
                    if (currentMaterial >= materialImages.length) {
                        currentMaterial = materialImages.length-1;
                    }
                }else if(currentOption == ITEMS){
                    currentItem++;
                    if (currentItem > 2) {
                        currentItem = 2;
                    }
                }
                break;
            case KeyEvent.VK_UP:
                currentOption--;
                if(currentOption < 0){
                    currentOption = 0;
                }
                break;
            case KeyEvent.VK_DOWN:
                currentOption++;
                if(currentOption > 1){
                    currentOption = 1;
                }
                break;
        }
    }

    @Override
    public void keyReleased(int key) {

    }
}
