package entity;

import java.awt.Graphics2D;

import main.GamePanel;
import main.GamePanel.gameState;

/**
 * Stores guards and controls their movement and state
 */
public class GuardManager {
    private Guard guards[];

    /**
     * Initializes guards
     * @param gp GamePanel that the guards belong to. Also used to retrieve the player that the guards will follow.
     */
    public GuardManager(GamePanel gp){
        guards = new Guard[4];
        guards[0] = new Guard(gp, 5*gp.tileSize, 3*gp.tileSize);
        guards[1] = new Guard(gp, (gp.fullMapCol-6)*gp.tileSize, (gp.fullMapRow-8)*gp.tileSize);
        guards[2] = new Guard(gp, (gp.fullMapCol/2)*gp.tileSize, (gp.fullMapRow - 3)*gp.tileSize);
        guards[3] = new Guard(gp, 30*gp.tileSize, 19*gp.tileSize);
    }

    /**
     * Invoked by gamepanel. Calls update on every guard.
     */
    public void update(Player player, GamePanel gPanel){
        for(Guard guard:guards){
            guard.update();
            if(guard.isCollidingWith(player)) {
                gPanel.setcurrentGameState(gameState.gameOver);
            }
        }
    }

    /**
     * Invoked by gamepanel. Calls draw on every guard
     */
    public void draw(Graphics2D g2){
        for(Guard guard:guards){
            guard.draw(g2);
        }
    }
}
