import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import main.GamePanel;
import object.OBJ_Battery;
import object.ObjectSpawner;
import object.SuperObject;

/**
 * Ensure ObjectSpawner interacts properly with other components
 */
public class ObjectSpawnerIntegrationTest {
    
    /**
     * Test that objects are only being spawned on tiles that don't block the player
     */
    @Test
    public void testBlockingTileSpawn(){
        GamePanel gp = new GamePanel();
        gp.tileM.mapTileNum = new int[4][4];
        ObjectSpawner os = new ObjectSpawner(gp);

        // set all inner tiles except 1,1 to be blocking tiles
        gp.tileM.mapTileNum[1][2] = 1;
        gp.tileM.mapTileNum[2][1] = 1;
        gp.tileM.mapTileNum[2][2] = 1;

        // spawn an object 5 times and check that it's always spawned on 1,1
        for(int i = 0; i<5; i++){
            for(int j = 0; j<300; j++){
                os.update();
            }
            // when spawning, update checks for an available spawn location randomly. There's a chance it fails to find one.
            if(gp.obj[gp.obj.length-1] != null){
                assertEquals(1*gp.tileSize, gp.obj[gp.obj.length-1].worldX);
                assertEquals(1*gp.tileSize, gp.obj[gp.obj.length-1].worldY);
                gp.obj[gp.obj.length-1] = null;
            }
        }
    }

    /**
     * Test that object spawns and despawns won't overwrite other objects in gp.obj
     * (despawn testing included in the case that the object is collected by the player and its position is occupied by a new object)
     */
    @Test
    public void testObjectSpawnNoOverwrite(){
        GamePanel gp = new GamePanel();
        gp.tileM.mapTileNum = new int[4][4];
        ObjectSpawner os = new ObjectSpawner(gp);

        // test spawn
        gp.obj[gp.obj.length-1] = new SuperObject();
        gp.obj[gp.obj.length-2] = new SuperObject();
        gp.obj[gp.obj.length-4] = new SuperObject();
        for(int i = 0; i<300; i++){
            os.update();
        }
        // check other objects weren't overwritten
        assertEquals(SuperObject.class, gp.obj[gp.obj.length-1].getClass());
        assertEquals(SuperObject.class, gp.obj[gp.obj.length-2].getClass());
        assertEquals(SuperObject.class, gp.obj[gp.obj.length-4].getClass());

        // check battery was spawned
        assertEquals(OBJ_Battery.class, gp.obj[gp.obj.length-3].getClass());

        // test despawn
        for(int i = 0; i<1200; i++){
            os.update();
        }
        int length = gp.obj.length;
        gp.obj = new SuperObject[length];
        gp.obj[gp.obj.length-3] = new SuperObject();

        // ObjectSpawner will attempt to delete an object in an index containing another, ensure it doesn't change the object
        for(int i = 0; i<300; i++){
            os.update();
        }
        assertEquals(SuperObject.class, gp.obj[gp.obj.length-3].getClass()); // check object was unchanged
        assertEquals(OBJ_Battery.class, gp.obj[gp.obj.length-1].getClass()); // a new battery object was also added

    }
}
