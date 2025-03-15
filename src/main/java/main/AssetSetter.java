package main;

import object.OBJ_Exit;

import java.lang.reflect.Constructor;

import object.OBJ_Door;
import object.OBJ_EMP;
import object.OBJ_Keycard;
import object.SuperObject;

/**
 * Sets objects on the map
 */
public class AssetSetter {

    GamePanel gp;
    private int numObjects; // number of SuperObjects instantiated by AssetSetter
    private int[][] keyCardPositions = {{20,1}, {3,47}, {30,19}};
    private int[][] doorPositions = {{48,42}};
    private int[][] exitPositions = {{49,42}};
    private int[][] empPositions = {{23, 27}, {40,21}, {35, 37}, {30,2}, {30,1}};
    

    /**
     * Sets objects in a GamePanel.
     * @param gp
     */
    public AssetSetter(GamePanel gp) {
        this.gp = gp;
        this.numObjects = 0;
    }

    /**
     * Sets assets of type `object` in their specified positions in the GamePanel's object array
     * @param positions
     * @param object
     */
    private void setSuperObjects(int[][] positions, Class<? extends SuperObject> objClass){
        try {
            Constructor<? extends SuperObject> constructor = objClass.getConstructor();
            for (int i = 0; i < positions.length; i++) {
                SuperObject obj = constructor.newInstance();
                obj.setObjPosition(positions[i][0] * gp.tileSize, positions[i][1] * gp.tileSize);
                gp.obj[numObjects] = obj;
                this.numObjects++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets objects on the map by placing them in gp.obj
     */
    public void setObject() {

        this.numObjects = 0;

        //KEYS
        setSuperObjects(keyCardPositions, OBJ_Keycard.class);

        //DOORS
        setSuperObjects(doorPositions, OBJ_Door.class);

        //EXIT
        setSuperObjects(exitPositions, OBJ_Exit.class);

        // EMP
        setSuperObjects(empPositions, OBJ_EMP.class);

    }
}
