import entity.Player;
import main.GamePanel;
import main.KeyHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

/**
 * Tests the movement behavior of the Player class.
 */
@RunWith(MockitoJUnitRunner.class)
public class PlayerMovementTest {

    private GamePanel gp = new GamePanel();
    KeyHandler keyH = new KeyHandler(gp);
    private Player player = new Player(gp, keyH); // Mock KeyHandler not needed for movement tests

    @Before
    public void setUp() {
        // Set up any necessary initialization before each test
        player.setDefaultValues(); // Reset player position
    }

    /**
     * Tests upward movement of the player.
     * <p>
     * The player's position on the Y-axis should decrease by its speed after moving upward.
     */
    @Test
    public void testPlayerUpMovement() {
        int initialY = player.worldY; // accessing original player y coordinate
        keyH.upPressed = true; // simulating moving upwards
        player.update(); // updating player location with player.update
        int newY = player.worldY; // calculating new y coordinate
        assertEquals(initialY - player.speed, newY); //calculating if player's new y coordinate changed according to speed
    }

    /**
     * Tests downward movement of the player.
     * <p>
     * The player's position on the Y-axis should increase by its speed after moving downward.
     */
    @Test
    public void testPlayerDownMovement() {
        int initialY = player.worldY; // accessing original player y coordinate
        keyH.downPressed = true; // simulating moving downwards
        player.update(); // updating player location with player.update
        int newY = player.worldY; // calculating new y coordinate
        assertEquals(initialY + player.speed, newY); //calculating if player's new y coordinate changed according to speed
    }

    /**
     * Tests leftward movement of the player.
     * <p>
     * The player's position on the X-axis should decrease by its speed after moving leftward.
     */
    @Test
    public void testPlayerLeftMovement() {
        int initialX = player.worldX; // accessing original player x coordinate
        keyH.leftPressed = true; // simulating leftward movement
        player.update(); // updating player location with player.update
        int newX = player.worldX; // calculating new x coordinate
        assertEquals(initialX - player.speed, newX); //calculating if player's new x coordinate changed according to speed
    }

    /**
     * Tests rightward movement of the player.
     * <p>
     * The player's position on the X-axis should increase by its speed after moving rightward.
     */
    @Test
    public void testPlayerRightMovement() {
        int initialX = player.worldX; // accessing original player x coordinate
        keyH.rightPressed = true; // simulating rightward movement
        player.update(); // updating player location with player.update
        int newX = player.worldX; // calculating new x coordinate
        assertEquals(initialX + player.speed, newX); //calculating if player's new x coordinate changed according to speed
    }
}
