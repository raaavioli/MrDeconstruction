package Unit;

import TileMap.TileMap;

public abstract class Mob extends DynamicObject {

    private int health;
    private int maxHealth;
    /*
    protected double movementSpeed;
    protected double fallingSpeed;
    protected double maxFallingSpeed;
    protected double startJumpSpeed;
    protected double maxJumpHeight;
     */

    public Mob(TileMap tileMap, int width, int height, int cWidth, int cHeight) {
        super(tileMap, width, height, cWidth, cHeight);
    }

    abstract void attack();

    abstract void setMaxHealth();

    abstract void setHealth(int health);


    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }
}
