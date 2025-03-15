package object;

import main.GamePanel;

import java.awt.*;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

/**
 * Handles objects that spawn and despawn periodically
 */
public class ObjectSpawner {

    public int ticks = 0;
    private List<objectTracker> spawnedObjects;
    public int spawnCycles = 0;
    private GamePanel gp;
    private Random random = new Random();

    private int minX = 1;
    private int maxX = 48;
    private int minY = 1;
    private int maxY = 48;

    private int idCounter = 0;

    /**
     * Storage class used to track spawned objects
     */
    private class objectTracker{
        int id;
        int index;
        private objectTracker(int id, int index){
            this.id = id;
            this.index = index;
        }
    }
    
    /**
     * Handles objects that spawn and despawn periodically
     * @param gp - the GamePanel holding an array of objects
     */
    public ObjectSpawner(GamePanel gp){
        this.spawnedObjects = new ArrayList<objectTracker>();
        this.gp = gp;
        this.maxX = gp.tileM.mapTileNum.length-2;
        this.maxY = gp.tileM.mapTileNum[0].length-2;
    }


    // Generate a random position and check if the tile is actually reachable by the player
    private Point findRandomNonCollidingPosition(GamePanel gp, int minX, int maxX, int minY, int maxY, int maxAttempts) {
        int attempts = 0;
        int x;
        int y;
        int tileNum;

        do {
            x = this.random.nextInt(maxX - minX + 1) + minX;
            y = this.random.nextInt(maxY - minY + 1) + minY;
            tileNum = gp.tileM.mapTileNum[x][y];
            attempts++;
        } while (gp.tileM.tile[tileNum].collision && attempts < maxAttempts);

        if (!gp.tileM.tile[tileNum].collision) {
            return new Point(x, y);
        }

        return null;
    }

    // Method to create a new object at a given position
    private void createObjectAtPosition(GamePanel gp, int x, int y, int objectIndex) {
        SuperObject obj = new OBJ_Battery();
        obj.setObjPosition(x * gp.tileSize, y * gp.tileSize);
        obj.id = idCounter++;
        gp.obj[objectIndex] = obj;
        spawnedObjects.add(new objectTracker(obj.id, objectIndex));
    }

    /**
     * Adds objects to gp.obj[] if there is space available and this function has been called 300 times (5s have passed)
     * Deletes an object from gp.obj[] after 25s have passed since it was spawned
     */
    public void update(){
        ticks++;
   
        if(ticks >= 300){
            for(int i = gp.obj.length-1; i>-1; i--){
                if(gp.obj[i] == null){
                    // Generate another position if tile is not reachable, give up after 10 failed attempts
                    Point position = findRandomNonCollidingPosition(gp, minX, maxX, minY, maxY, 10);
                    if (position != null) {
                        createObjectAtPosition(gp, position.x, position.y, i);
                    }
                    break;
                }
            }
            spawnCycles++;
            
            /*  delete item from gp.obj[]
                check id to ensure it's the correct item
                Start deleting every 5 seconds once 6 spawn cycles have passed (30s have passed since game start)
            */
            if (spawnCycles >= 6){
                try{
                    objectTracker objT = this.spawnedObjects.remove(0);
                    if(gp.obj[objT.index] != null &&  gp.obj[objT.index].id == objT.id){
                        gp.obj[objT.index] = null;
                    }
                }
                catch(IndexOutOfBoundsException e){}
                   
            }
            ticks = 0;
        }

    }
}
