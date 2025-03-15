import entity.Player;
import main.GamePanel;
import main.GamePanel.gameState;
import main.KeyHandler;
import object.OBJ_Battery;
import object.OBJ_Exit;
import object.SuperObject;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Integration tests for interactions between the Player and game objects.
 */
public class PlayerObjectIntegrationTest {
    private GamePanel gp = new GamePanel();
    KeyHandler keyH = new KeyHandler(gp);
    private Player player;

    @Before
    /**
     * Sets up the environment before each test case.
     */
    public void setUp() {
        // Initialize player and battery objects
        player = new Player(gp, keyH);
        gp.setupGame();
    }

    @Test
    public void testPlayerBatteryCollision() {
        // Set initial player score
        int initialScore = gp.score;

        //creating new super object to store battery
        gp.obj = new SuperObject[2];
        gp.obj[0] = new OBJ_Battery();

        //new SuperObject automatically used within .pickUpObject
        player.pickUpObject(0);

        // Verify that the player's score has increased
        assertEquals(initialScore + 100, gp.score);
    }

    @Test
    /**
     * Tests the collision between the player and a keycard object.
     */
    public void testPlayerKeycardCollision() {
        // Set initial player score and keycard count
        int initialScore = gp.score;
        int initialKeycardCount = player.keycardsObtained; 

        // Simulate player colliding with Keycard
        player.pickUpObject(2);

        // Verify that the player's score and keycard count has increased
        assertEquals(initialScore + 200, gp.score);
        assertEquals(initialKeycardCount + 1, player.keycardsObtained);
    }

    @Test
    /**
     * Tests the collision between the player and an EMP object.
     */
    public void testPlayerEMPCollision() {
        // Set initial player score
        int initialScore = gp.score;

        // Simulate player colliding with emp
        player.pickUpObject(7);

        // Checks for correct game state and correct deductions of score
        assertEquals(gameState.gameOver, gp.currentGameState);
        assertEquals(initialScore - 300, gp.score);
    }

    @Test
    /**
     * Tests the collision between the player and a door object without having any keycards.
     */
    public void testPlayerDoorNoCardCollision() {
        // Set initial player score
        int initialScore = gp.score;
        // setting games state to play state
        gp.currentGameState = gameState.playing;

        player.keycardsObtained = 0;

        // Simulate player colliding with door without keycards
        player.pickUpObject(3);

        //check for correct score and that keycards are deducted
        assertEquals(initialScore, gp.score);
        assertEquals(0, player.keycardsObtained);
    }

    @Test
    /**
     * Tests the collision between the player and a door object with having enough keycards.
     */
    public void testPlayerDoorWithCardCollision() {
        // Set initial player score
        int initialScore = gp.score;
        // setting games state to play state
        gp.currentGameState = gameState.playing;

        player.keycardsObtained = 4;

        // Simulate player colliding with door with keycards
        player.pickUpObject(3);

        //check for correct score and that keycards are deducted
        assertEquals(initialScore, gp.score);
        assertEquals(1, player.keycardsObtained);
    }

    @Test
    /**
     * Tests the collision between the player and the exit object.
     */
    public void testPlayerExitCollision() {
        // Set initial player score
        int initialScore = gp.score;
        gp.score = gp.score++;
        gp.obj = new SuperObject[1];
        gp.obj[0] = new OBJ_Exit();

        // setting games state to play state
        gp.currentGameState = gameState.playing;

        // Simulate player colliding with exit
        player.pickUpObject(0);

        //check for correct score and that keycards are deducted
        assertEquals(initialScore, gp.score);
        assertEquals(gameState.win, gp.currentGameState);
    }
}
