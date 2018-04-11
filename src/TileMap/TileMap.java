package TileMap;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileMap {
    // Position
    private double x;
    private double y;

    // Bounds
    private int xMin = 0;
    private int yMin = 0;
    private int xMax = GamePanel.WIDTH;
    private int yMax = GamePanel.HEIGHT;

    // Map
    private int[][] map;
    private int tileSize;
    private int numRows = 10;
    private int numCols = 20;
    private int width;
    private int height;

    // tileSet
    private BufferedImage tileSet;
    private int numTilesWide;
    private int numTilesHigh;
    private Tile[][] tiles;

    // Drawing

    private int rowOffset;
    private int colOffset;
    private int numRowsToDraw;
    private int numColsToDraw;

    public TileMap(int tileSize) {
        this.tileSize = tileSize;
        numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
        numColsToDraw = GamePanel.WIDTH / tileSize + 2;
    }

    public void loadTiles(String s) {
        try {

            tileSet = ImageIO.read(
                    getClass().getResourceAsStream(s)
            );
            numTilesWide = tileSet.getWidth() / tileSize;
            numTilesHigh = tileSet.getHeight() / tileSize;
            tiles = new Tile[numTilesHigh][numTilesWide];

            BufferedImage subimage;
            for (int col = 0; col < numTilesWide; col++) {
                for (int row = 0; row < numTilesHigh; row++) {
                    subimage = tileSet.getSubimage(col * tileSize, row * tileSize, tileSize, tileSize);
                    //andra parametern kan ändras framöver om olika tiles har olika egenskaper. Just nu är 0 = normal och 1 = blocked
                    tiles[row][col] = new Tile(subimage, row);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String s) {

        try {

            InputStream in = getClass().getResourceAsStream(s);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            map = new int[10][20];
            width = numCols * tileSize;
            height = numRows * tileSize;

            String space = ",";
            for (int row = 0; row < numRows; row++) {
                String[] tileValues = br.readLine().split(space);
                for (int col = 0; col < numCols; col++) {
                    int tileValue = Integer.parseInt(tileValues[col]);
                    if(tileValue == -1){ tileValue = 0;}
                    map[row][col] = tileValue;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getTileSize() {
        return tileSize;
    }

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


    public int getType(int row, int col) {
        int mapValue = map[row][col];
        int tileCol = mapValue % numTilesWide;
        int tileRow = mapValue / numTilesWide;

        return tiles[tileRow][tileCol].getType();
    }

    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;

        fixBounds();

        colOffset = (int) -this.x / tileSize;
        rowOffset = (int) -this.y / tileSize;
        System.out.println(String.valueOf(rowOffset)+" "+String.valueOf(colOffset));
    }

    private void fixBounds(){
        if(x < xMin){
            x = xMin;
        }else if(x > xMax){
            x = xMax;
        }

        if(y < yMin){
            y = yMin;
        }else if(y > yMax){
            y = yMax;
        }
    }

    //MÅSTE FIXA DETTA, Får nullpointer exception
    public void draw(Graphics2D g){

        for(int row = rowOffset; row < rowOffset + numRowsToDraw; row++){
            if(row >= numRows) {
                break;
            }
            for (int col = colOffset; col < colOffset + numColsToDraw; col++) {
                if(col >= numCols) {
                    break;
                }

                int mapValue = map[row][col];
                int tileCol = mapValue % numTilesWide;
                int tileRow = mapValue / numTilesWide;

                BufferedImage image = tiles[tileRow][tileCol].getImage();

                g.drawImage(image, (int) x + col * tileSize, (int) y + row * tileSize, null);

            }
        }
    }
}