import entity.Guard;
import main.GamePanel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

/**
 * Test class to verify the movement behavior of guards towards the player.
 */
@RunWith(MockitoJUnitRunner.class)
public class EnemyMovementTest {

    private GamePanel gp;
    private Guard guard;

    /**
     * Sets up the test environment before each test case.
     */
    @Before
    public void setUp() {
        // Set up any necessary initialization before each test
        gp = new GamePanel();
        gp.tileM.loadMap("/maps/testmap.txt");
        guard = new Guard(gp, 2*gp.tileSize, 5*gp.tileSize); // Initialize guard at position (2, 5)
        guard.setAbstractMovement(false);
    }

    /**
     * Tests if the guard moves towards the player when the player is above
     */
    @Test
    public void testGuardMovementTowardsPlayerUp() {
        guard.direction = "down";
        // Set player's position above the guard
        gp.player.worldX = 0;
        gp.player.worldY = 0;
        int speed = guard.speed;

        // Update guard's movement
        guard.update();

        // Check if guard's position changes towards player
        assertEquals("up", guard.direction);

        assertEquals(2*gp.tileSize, guard.worldX);
        assertEquals(5*gp.tileSize-speed, guard.worldY);
    }

    /**
     * Tests if the guard moves towards the player when the player is below
     */
    @Test
    public void testGuardMovementTowardsPlayerDown() {
        guard.direction = "left";
        // Set player's position below the guard
        gp.player.worldX = 3*gp.tileSize;
        gp.player.worldY = 8*gp.tileSize;
        int speed = guard.speed;

        // Update guard's movement
        guard.update();

        // Check if guard's position changes towards player
        assertEquals("down", guard.direction);

        assertEquals(2*gp.tileSize, guard.worldX);
        assertEquals(5*gp.tileSize+speed, guard.worldY);
    }

    /**
     * Tests if the guard moves towards the player when the player is right
     */
    @Test
    public void testGuardMovementTowardsPlayerRight() {
        guard.direction = "left";
        // Set player's position right of the guard
        gp.player.worldX = 5*gp.tileSize;
        gp.player.worldY = 5*gp.tileSize;
        int speed = guard.speed;

        // Update guard's movement
        guard.update();

        // Check if guard's position changes towards player
        assertEquals("right", guard.direction);

        assertEquals(2*gp.tileSize+speed, guard.worldX);
        assertEquals(5*gp.tileSize, guard.worldY);
    }

    /**
     * Tests if the guard moves towards the player when the player is left
     */
    @Test
    public void testGuardMovementTowardsPlayerLeft() {
        guard.direction = "down";
        // Set player's position left of the guard
        gp.player.worldX = 1*gp.tileSize;
        gp.player.worldY = 5*gp.tileSize;
        int speed = guard.speed;

        // Update guard's movement
        guard.update();

        // Check if guard's position changes towards player
        assertEquals("left", guard.direction);

        assertEquals(2*gp.tileSize-speed, guard.worldX);
        assertEquals(5*gp.tileSize, guard.worldY);
    }
}
