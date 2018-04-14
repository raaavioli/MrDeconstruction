package Unit;

import TileMap.TileMap;

/**
 * A static immovable object.
 *
 * Static objects are immovable in the sense that they cannot be moved after placed. They can
 * either be blocking or non-blocking and will stay where it is created unless it is destroyed.
 */
public abstract class StaticObject extends GameObject {
    public StaticObject(boolean collidable, int width, int height) {
        super(collidable, width, height);
    }
}
