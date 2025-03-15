import main.GamePanel;
import entity.Guard;
import org.junit.Before;
import org.junit.Test;
import main.CollisionChecker;

import java.awt.Rectangle;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;

/**
 * Integration test to verify the behavior of guard movement when a collision occurs.
 */
public class EnemyCollisionIntegrationTest {
    private GamePanel gp = new GamePanel();
    private Guard guard;

    @Before
    public void setUp() {
        // Set up any necessary initialization before each test
        guard = new Guard(gp, 0, 0); // Initialize guard at position (5, 0)
        guard.solidArea = new Rectangle();
        guard.solidArea.width = gp.tileSize;
        guard.solidArea.height = gp.tileSize;
    }

    /**
     * Tests the change of direction when a collision occurs.
     * The guard's collisionOn should change after a collision.
     */
    @Test
    public void testCollisionOnFromCollision() {
        CollisionChecker cc = new CollisionChecker(gp);
        int[][] initialPositions = {{3, 1}, {1,13}, {2, 3}, {gp.fullMapCol-2, 2}};
        String[] directions = {"up", "left", "down", "right"};

        //checking that guard's collisionOn switches when collision occurs
        for(int i = 0; i < 4; i++) {
            guard.collisionOn = false;
            guard.worldX = initialPositions[i][0]*gp.tileSize;
            guard.worldY = initialPositions[i][1]*gp.tileSize;
            guard.direction = directions[i];
            cc.checkTile(guard);
            
            //verify collisionOn is true
            assertEquals(true, guard.collisionOn);
        }
    }

    /**
     * Tests the change of direction when a collision occurs.
     * The guard's direction should change after a collision.
     */
    @Test
    public void testChangeDirectionFromCollision() {
        int[][] initialPositions = {{3, 1}, {1,13}, {2, 3}, {gp.fullMapCol-2, 2}};
        String[] directions = {"up", "left", "down", "right"};

        //checking that guard changes direction when collision occurs
        for(int i = 0; i < 4; i++) {
            guard.collisionOn = false;
            guard.worldX = initialPositions[i][0]*gp.tileSize;
            guard.worldY = initialPositions[i][1]*gp.tileSize;
            guard.direction = directions[i];
            guard.update();
            
            //check for new direction
            assertNotEquals(directions[i], guard.direction);
        }
    }
}
