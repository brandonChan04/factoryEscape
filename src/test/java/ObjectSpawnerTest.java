import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.junit.Test;

import main.GamePanel;
import object.ObjectSpawner;
import object.SuperObject;

public class ObjectSpawnerTest {

    private GamePanel gp = new GamePanel();

    /**
     * Test behavior when update is called less than the amount required to spawn a battery
     */
    @Test
    public void sub300TicksTest(){
        gp.obj = new SuperObject[20];
        SuperObject[] expected = new SuperObject[gp.obj.length];

        ObjectSpawner os = new ObjectSpawner(gp);
        for(int i = 0; i<299; i++){
            os.update();
        }
        assertArrayEquals(expected, gp.obj);

    }

    /**
     * Test behavior when update is called exactly the amount required to spawn a battery
     */
    @Test
    public void exactly300TicksTest(){
        gp.obj = new SuperObject[20];

        ObjectSpawner os = new ObjectSpawner(gp);
        for(int i = 0; i<300; i++){
            os.update();
        }
        assertNotNull("object was not spawned", gp.obj[19]);

    }

    /**
     * Test behavior when update is called exactly the amount required to spawn a battery 5 times and despawn the first
     */
    @Test
    public void exactly1800TicksTest(){
        gp.obj = new SuperObject[20];

        ObjectSpawner os = new ObjectSpawner(gp);
        for(int i = 0; i<1800; i++){
            os.update();
        }

        assertEquals(null, gp.obj[19]); // check first battery was despawned

        // check remaining 4 batteries are not despawned
        for(int i = 18; i > 18-4;i--){
            assertNotNull(null, gp.obj[i]);
        }
        

    }

    /**
     * Test behavior when battery is spawned and despawned when gp.obj is full
     */
    @Test
    public void spawnerFullArrayTest(){
        gp.obj = new SuperObject[20];
        for(int i = 0; i<gp.obj.length; i++){
            gp.obj[i] = new SuperObject();
        }
        SuperObject[] expected = Arrays.copyOf(gp.obj, gp.obj.length);

        ObjectSpawner os = new ObjectSpawner(gp);
        for(int i = 0; i<300; i++){
            os.update();
        }

        assertArrayEquals(expected, gp.obj);

        for(int i = 0; i<1500; i++){
            os.update();
        }
        assertArrayEquals(expected, gp.obj);

    }
    
}
