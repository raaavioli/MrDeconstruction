package GameState;

import Unit.Player;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GameStateManager {
    private Player player;
    private ArrayList<GameState> gameStates;
    private int currentState;
    private int previousState;

    public static final int MENUSTATE = 0;
    public static final int INFOBAR = 1;
    public static final int LEVEL1STATE = 2;

    public GameStateManager() {
        gameStates = new ArrayList<GameState>();
        player = new Player(32, 32, 16, 28);

        currentState = LEVEL1STATE;
        gameStates.add(new MenuState(this));
        gameStates.add(new Infobar(this, player));
        gameStates.add(new Level1State(this, player));
    }

    public void setState(int state) {
        currentState = state;
        gameStates.get(currentState).init();
    }

    public void update() {
        gameStates.get(currentState).update();
        gameStates.get(INFOBAR).update();
    }

    public void draw(Graphics2D g) {
        gameStates.get(currentState).draw(g);
        gameStates.get(INFOBAR).draw(g);
    }

    public void keyPressed(int key) {
        if (key == KeyEvent.VK_SHIFT && currentState != INFOBAR && player.isStandingStill()) {
            previousState = currentState;
            currentState = INFOBAR;
        }

        gameStates.get(currentState).keyPressed(key);
    }

    public void keyReleased(int key) {
        if (key == KeyEvent.VK_SHIFT) {
            currentState = previousState;
        }
        gameStates.get(currentState).keyReleased(key);
    }
}
