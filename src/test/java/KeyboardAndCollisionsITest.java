import java.awt.*;


import entity.Player;
import main.CollisionChecker;
import main.GamePanel;
import main.GamePanel.gameState;
import main.KeyHandler;
import entity.Entity;
import object.SuperObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import tile.Tile;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class KeyboardAndCollisionsITest {
    private GamePanel mockGamePanel = new GamePanel();
    KeyHandler keyH = new KeyHandler(mockGamePanel);
    private Player player = new Player(mockGamePanel, keyH);
    private Entity entity = new Entity();

    @Before
    public void setUp() {
        // Set up any necessary initialization before each test
        player.setDefaultValues(); // Reset player position

        entity.speed = 4;
        entity.solidArea = new Rectangle();
        entity.solidArea.width = mockGamePanel.tileSize;
        entity.solidArea.height = mockGamePanel.tileSize;
        mockGamePanel.obj = new SuperObject[10];
    }

    @Mock
    Graphics2D graphics;

    @Test
    public void testCollisionWithTileMovingUp() {
        CollisionChecker checker = new CollisionChecker(mockGamePanel);

        int initialY = player.worldY;
        keyH.upPressed = true;
        entity.spriteCounter = 20;
        entity.spriteNum = 2;
        player.update(); // Simulate player movement
        player.draw(graphics);

        int newY = player.worldY;
        assertEquals(initialY - player.speed, newY);

        // Assuming tileSize is 32 and entity's speed is less than tileSize
        entity.worldX = 32; // Position the entity at some X within the world
        entity.worldY = 64; // Position the entity at some Y within the world
        entity.solidArea = new Rectangle(0, 0, 32, 32); // Assuming solidArea size equals tileSize
        entity.direction = "up";
        entity.speed = 4; // Sample speed

        // Setup a scenario where there's a collision tile directly above the entity
        int collisionTileX = entity.worldX / mockGamePanel.tileSize;
        int collisionTileY = (entity.worldY - entity.speed) / mockGamePanel.tileSize; // Position directly above, considering speed
        mockGamePanel.tileM.mapTileNum[collisionTileX][collisionTileY] = 1; // Assuming tile at index 1 has collision
        mockGamePanel.tileM.tile[1] = new Tile();
        mockGamePanel.tileM.tile[1].collision = true; // Setting collision property of the tile

        for (int i = 0; i < 15; i++) {
            player.update();
        }
        player.draw(graphics);

        checker.checkTile(entity);
        assertTrue("Player should collide when moving up into a collision tile", entity.collisionOn);
    }

    @Test
    public void testCollisionWithTileMovingDown() {
        CollisionChecker checker = new CollisionChecker(mockGamePanel);

        int initialY = player.worldY;
        keyH.downPressed = true;
        entity.spriteCounter = 15;
        player.update(); // Simulate player movement
        player.draw(graphics);
        int newY = player.worldY;
        assertEquals(initialY + player.speed, newY);

        // Setup entity position and solid area
        entity.worldX = 32; // Assuming a tile size of 32 for simplicity
        entity.worldY = 32;
        entity.solidArea = new Rectangle(0, 0, 32, 32);
        entity.direction = "down";
        entity.speed = 4;

        // Setup a collision tile directly below the entity
        int collisionTileX = entity.worldX / mockGamePanel.tileSize;
        int collisionTileY = (entity.worldY + entity.speed + entity.solidArea.height) / mockGamePanel.tileSize;
        mockGamePanel.tileM.mapTileNum[collisionTileX][collisionTileY] = 1; // Assume tile 1 has collision
        mockGamePanel.tileM.tile[1] = new Tile();
        mockGamePanel.tileM.tile[1].collision = true;

        for (int i = 0; i < 15; i++) {
            player.update();
        }
        player.draw(graphics);

        checker.checkTile(entity);
        assertTrue("Player should collide when moving down into a collision tile", entity.collisionOn);
    }

    @Test
    public void testCollisionWithTileMovingLeft() {
        CollisionChecker checker = new CollisionChecker(mockGamePanel);

        int initialX = player.worldX;
        keyH.leftPressed = true;
        entity.spriteCounter = 15;
        player.update(); // Simulate player movement
        player.draw(graphics);
        int newX = player.worldX;
        assertEquals(initialX - player.speed, newX);

        // Setup entity position and solid area
        entity.worldX = 64; // Start the entity to the right to move left into a tile
        entity.worldY = 32;
        entity.solidArea = new Rectangle(0, 0, 32, 32);
        entity.direction = "left";
        entity.speed = 4;

        // Setup a collision tile directly to the left of the entity
        int collisionTileX = (entity.worldX - entity.speed) / mockGamePanel.tileSize;
        int collisionTileY = entity.worldY / mockGamePanel.tileSize;
        mockGamePanel.tileM.mapTileNum[collisionTileX][collisionTileY] = 1; // Assume tile 1 has collision
        mockGamePanel.tileM.tile[1] = new Tile();
        mockGamePanel.tileM.tile[1].collision = true;

        for (int i = 0; i < 15; i++) {
            player.update();
        }
        player.draw(graphics);

        checker.checkTile(entity);
        assertTrue("Player should collide when moving left into a collision tile", entity.collisionOn);
    }

    @Test
    public void testCollisionWithTileMovingRight() {
        CollisionChecker checker = new CollisionChecker(mockGamePanel);

        int initialX = player.worldX;
        keyH.rightPressed = true;
        entity.spriteCounter = 15;
        player.update(); // Simulate player movement
        player.draw(graphics);
        int newX = player.worldX;
        assertEquals(initialX + player.speed, newX);

        // Setup entity position and solid area
        entity.worldX = 32;
        entity.worldY = 32;
        entity.solidArea = new Rectangle(0, 0, 32, 32);
        entity.direction = "right";
        entity.speed = 4;

        // Setup a collision tile directly to the right of the entity
        int collisionTileX = (entity.worldX + entity.speed + entity.solidArea.width) / mockGamePanel.tileSize;
        int collisionTileY = entity.worldY / mockGamePanel.tileSize;
        mockGamePanel.tileM.mapTileNum[collisionTileX][collisionTileY] = 1; // Assume tile 1 has collision
        mockGamePanel.tileM.tile[1] = new Tile();
        mockGamePanel.tileM.tile[1].collision = true;

        for (int i = 0; i < 15; i++) {
            player.update();
        }
        player.draw(graphics);

        checker.checkTile(entity);
        assertTrue("Player should collide when moving right into a collision tile", entity.collisionOn);

        for (int i = 0; i < 15; i++) {
            player.update();
        }
            player.restoreScore();
    }

    @Test
    public void testPickUpKeycard() {
        int index = 0; // Index of the object in the gp.obj array
        SuperObject keycard = new SuperObject();
        keycard.name = "Keycard";
        keycard.scoreValue = 100;
        mockGamePanel.obj[index] = keycard;

        player.pickUpObject(index);

        assertEquals("Player should have 1 keycard", 1, player.keycardsObtained);
        assertEquals("Score should be increased by the keycard's value", 100, mockGamePanel.score);
        assertNull("The keycard should be removed from the game", mockGamePanel.obj[index]);
        assertTrue("Game should show a message about picking up a keycard", mockGamePanel.ui.message.contains("You got a Keycard"));
    }

    @Test
    public void testPickUpDoorWithEnoughKeycards() {
        player.keycardsObtained = 3; // Set up player with enough keycards

        int index = 1; // Index of the object in the gp.obj array
        SuperObject door = new SuperObject();
        door.name = "Door";
        mockGamePanel.obj[index] = door;

        player.pickUpObject(index);

        assertEquals("Keycards should be decreased by 3 after opening the door", 0, player.keycardsObtained);
        assertNull("The door should be removed from the game", mockGamePanel.obj[index]);
        assertTrue("Game should show a message about opening the door", mockGamePanel.ui.message.contains("You opened the door!"));
    }

    @Test
    public void testPickUpExit() {
        int index = 2; // Index of the object in the gp.obj array
        SuperObject exit = new SuperObject();
        exit.name = "Exit";
        mockGamePanel.obj[index] = exit;

        player.pickUpObject(index);

        assertTrue("Game should be marked as finished", mockGamePanel.ui.gameFinished);
        assertEquals("Game state should be set to gameWinState", gameState.win, mockGamePanel.currentGameState);
    }

    @Test
    public void testPickUpEMPWithNegativeScore() {
        mockGamePanel.score = -100; // Setting the score below 0
        int index = 3; // Index of the object in the gp.obj array
        SuperObject emp = new SuperObject();
        emp.name = "EMP";
        mockGamePanel.obj[index] = emp;

        player.pickUpObject(index);

        assertNull("The EMP should be removed from the game", mockGamePanel.obj[index]);
        assertEquals("Game state should be set to gameOverState due to negative score", gameState.gameOver, mockGamePanel.currentGameState);
    }

    @Test
    public void testPickUpEMPWithPositiveScore() {
        mockGamePanel.score = 100; // Setting the score above 0
        int index = 4; // Index of the EMP object in the gp.obj array
        SuperObject emp = new SuperObject();
        emp.name = "EMP";
        mockGamePanel.obj[index] = emp;

        player.pickUpObject(index);

        assertNull("The EMP should be removed from the game", mockGamePanel.obj[index]);
        assertTrue("Game should show a message about energy being drained", mockGamePanel.ui.message.contains("Your energy was drained"));
    }

    @Test
    public void testPickUpBattery() {
        int index = 5; // Index of the battery object in the gp.obj array
        SuperObject battery = new SuperObject();
        battery.name = "battery";
        mockGamePanel.obj[index] = battery;

        player.pickUpObject(index);

        assertNull("The battery should be removed from the game", mockGamePanel.obj[index]);
        assertTrue("Game should show a message about picking up a battery", mockGamePanel.ui.message.contains("You got a battery"));
    }

    @Test
    public void testPickUpDoorWithoutEnoughKeycards() {
        player.keycardsObtained = 1; // Set up player with not enough keycards

        int index = 6; // Index of the door object in the gp.obj array
        SuperObject door = new SuperObject();
        door.name = "Door";
        mockGamePanel.obj[index] = door;

        player.pickUpObject(index);

        assertNotNull("The door should not be removed from the game", mockGamePanel.obj[index]);
        assertTrue("Game should show a message about requiring more keycards", mockGamePanel.ui.message.contains("3 keycards are required"));
    }
    @Test
    public void testPickUpDefaultObject() {
        int index = 7; // Index for a generic object
        SuperObject genericObject = new SuperObject();
        genericObject.name = "Generic Object"; // Name not matching any specific case
        mockGamePanel.obj[index] = genericObject;

        player.pickUpObject(index);

        assertNull("The generic object should be removed from the game", mockGamePanel.obj[index]);
        assertTrue("Game should show a message about picking up the object", mockGamePanel.ui.message.contains("You got a Generic Object"));
    }

}