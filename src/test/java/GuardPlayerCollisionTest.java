import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import entity.Guard;
import entity.Player;
import main.GamePanel;
import main.KeyHandler;

public class GuardPlayerCollisionTest {

    private GamePanel gp;
    private KeyHandler kh;
    private Player player;
    private Guard guard;

    @Before
    public void setup(){
        gp = new GamePanel();
        kh = new KeyHandler(gp);
        player = new Player(gp, kh);
        guard = new Guard(gp, 0, 0);
    }

    /**
     * Test collision between Player and Guard when the edges of their collision box align
     */
    @Test
    public void testPlayerGuardCollisionEdge(){
        int x = 2*gp.tileSize;
        int y = 4*gp.tileSize;
        player.worldX = x + player.solidArea.x;
        player.worldY = y + player.solidArea.y;
        // place the guard to the bottom right of the player
        guard.worldX = x + 1*gp.tileSize - guard.solidArea.x*2 - player.solidArea.x;
        guard.worldY = y + 1*gp.tileSize - guard.solidArea.y*2;

        assertEquals(true, guard.isCollidingWith(player));
    }

    /**
     * Test collision between Player and Guard when they have the same position
     */
    @Test
    public void testPlayerGuardCollisionCenter(){
        int x = 2*gp.tileSize;
        int y = 4*gp.tileSize;
        player.worldX = x;
        player.worldY = y;
        guard.worldX = x;
        guard.worldY = y;

        assertEquals(true, guard.isCollidingWith(player));
    }

    /**
     * Test that no collision is detected when the Player and Guard are at different y values but same x values
     */
    @Test
    public void testPlayerGuardNoCollisionX(){
        int x = 2*gp.tileSize;
        int y = 4*gp.tileSize;
        player.worldX = x;
        player.worldY = y;
        guard.worldX = x;
        guard.worldY = y + 1*gp.tileSize;

        assertEquals(false, guard.isCollidingWith(player));
    }

    /**
     * Test that no collision is detected when the Player and Guard are at different x values but same y values
     */
    @Test
    public void testPlayerGuardNoCollisionY(){
        int x = 2*gp.tileSize;
        int y = 4*gp.tileSize;
        player.worldX = x;
        player.worldY = y;
        guard.worldX = x + 1*gp.tileSize;
        guard.worldY = y;

        assertEquals(false, guard.isCollidingWith(player));
        
    }
}
