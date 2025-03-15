import static org.junit.Assert.assertEquals;

import java.awt.Rectangle;

import org.junit.Test;

import entity.Entity;
import main.CollisionChecker;
import main.GamePanel;
import object.SuperObject;
public class CollisionTest {

    private GamePanel gp = new GamePanel();
    private Entity entity = new Entity();

    public CollisionTest(){
        entity.speed = 4;
        entity.solidArea = new Rectangle();
        entity.solidArea.width = gp.tileSize;
        entity.solidArea.height = gp.tileSize;
    }

    /**
     * Tests handling of out of bounds tile collisions
     */
    @Test
    public void testCheckTileOOB(){
        CollisionChecker cc = new CollisionChecker(gp);

        int[][] initialPositions = {{20, 0}, {0,13}, {14, gp.fullMapRow-1}, {gp.fullMapCol-1, 30}};
        String[] directions = {"up", "left", "down", "right"};

        // attempt to check OOB for all directions
        for(int i = 0; i<4; i++){
            entity.collisionOn = false;
            entity.worldX = initialPositions[i][0]*gp.tileSize;
            entity.worldY = initialPositions[i][1]*gp.tileSize;
            entity.direction = directions[i];
            cc.checkTile(entity);

            assertEquals(true, entity.collisionOn);
        }
    }

    /**
     * Test collision checking for tiles with no collisions
     */
    @Test
    public void testCheckTileNoCollision(){
        CollisionChecker cc = new CollisionChecker(gp);

        String[] directions = {"up", "left", "down", "right"};

        // Tile 2 has no collisions in any of its directions. Test movements in all directions from tile 2.
        for(int i = 0; i<4; i++){
            entity.collisionOn = false;
            entity.worldX = 2*gp.tileSize;
            entity.worldY = 2*gp.tileSize;
            entity.direction = directions[i];
            cc.checkTile(entity);

            assertEquals(false, entity.collisionOn);
        }
    }

    /**
     * Test collision checking for tiles with collisions
     */
    @Test
    public void testCheckTileYesCollision(){
        CollisionChecker cc = new CollisionChecker(gp);
        
        int[][] initialPositions = {{3, 1}, {1,13}, {2, 3}, {gp.fullMapCol-2, 2}};
        String[] directions = {"up", "left", "down", "right"};

        // Test movements in position-direction pairs where collisions are expected
        for(int i = 0; i<4; i++){
            entity.collisionOn = false;
            entity.worldX = initialPositions[i][0]*gp.tileSize;
            entity.worldY = initialPositions[i][1]*gp.tileSize;
            entity.direction = directions[i];
            cc.checkTile(entity);

            assertEquals(true, entity.collisionOn);
        }
    }

    /**
     * Test collisions with objects where the player shouldn't collide with objects
     */
    @Test
    public void testCheckObjectNoCollision(){
        GamePanel gamePanel = new GamePanel();
        gamePanel.obj = new SuperObject[10]; // empty set of objects

        // create an object at coordinates (4,2) at index 0
        gamePanel.obj[0] = new SuperObject();
        gamePanel.obj[0].setObjPosition(4*gamePanel.tileSize, 2*gamePanel.tileSize);
        CollisionChecker cc = new CollisionChecker(gamePanel);

        // set entity to move right from (2,2)
        entity.worldX = 2*gamePanel.tileSize;
        entity.worldY = 2*gamePanel.tileSize;
        entity.direction = "right";

        int objectIndex = cc.checkObject(entity, true);
        assertEquals(-1, objectIndex);

    }

    /**
     * Test collisions with objects where the player should collide with objects and be blocked
     */
    @Test
    public void testCheckObjectYesCollisionBlock(){
        GamePanel gamePanel = new GamePanel();
        gamePanel.obj = new SuperObject[10]; // empty set of objects

        // create an object at coordinates (3,2) at index 3
        gamePanel.obj[3] = new SuperObject();
        gamePanel.obj[3].setObjPosition(3*gamePanel.tileSize, 2*gamePanel.tileSize);
        gamePanel.obj[3].collision = true;
        CollisionChecker cc = new CollisionChecker(gamePanel);

        // set entity to move right from (2,2)
        entity.worldX = 2*gamePanel.tileSize;
        entity.worldY = 2*gamePanel.tileSize;
        entity.direction = "right";

        entity.collisionOn = false;
        int objectIndex = cc.checkObject(entity, true);
        assertEquals(3, objectIndex);
        assertEquals(true, entity.collisionOn);

    }

    /**
     * Test collisions with objects where the player should collide with objects and the objects are non-blocking
     */
    @Test
    public void testCheckObjectYesCollisionPickUp(){
        GamePanel gamePanel = new GamePanel();
        gamePanel.obj = new SuperObject[10]; // empty set of objects

        // create an object at coordinates (3,2) at index 3
        gamePanel.obj[3] = new SuperObject();
        gamePanel.obj[3].setObjPosition(3*gamePanel.tileSize, 2*gamePanel.tileSize);
        gamePanel.obj[3].collision = false;
        CollisionChecker cc = new CollisionChecker(gamePanel);

        // set entity to move right from (2,2)
        entity.worldX = 2*gamePanel.tileSize;
        entity.worldY = 2*gamePanel.tileSize;
        entity.direction = "right";

        entity.collisionOn = false;
        int objectIndex = cc.checkObject(entity, true);
        assertEquals(3, objectIndex);
        assertEquals(false, entity.collisionOn);

    }
}
