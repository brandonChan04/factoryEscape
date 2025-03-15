import entity.Player;
import main.AssetSetter;
import main.GamePanel;
import main.UI;
import object.OBJ_Keycard;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;

import javax.imageio.ImageIO;


import object.ObjectSpawner;
import object.SuperObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ImageRenderingTest {
    private GamePanel mockGamePanel = new GamePanel();

    @Test
    public void testObjectSpawner(){
        ObjectSpawner os = new ObjectSpawner(mockGamePanel);
        os.ticks = 400;
        os.spawnCycles = 5;
        os.update();

        assertEquals(0,os.ticks);
    }

    @Test
    public void testKeyImageLoaded() {
        UI ui = new UI(mockGamePanel);
        assertNotNull("Key image should be loaded in constructor", ui.keyImage);
    }

    @Test
    public void assetSetterTest(){
        AssetSetter as = new AssetSetter(mockGamePanel);
        as.setObject();

        // check the position of Keycards
        assertEquals("Unexpected worldX for keycard 0", 20 * mockGamePanel.tileSize, mockGamePanel.obj[0].worldX);
        assertEquals("Unexpected worldY for keycard 0", 1 * mockGamePanel.tileSize, mockGamePanel.obj[0].worldY);

        assertEquals("Unexpected worldX for keycard 1", 3 * mockGamePanel.tileSize, mockGamePanel.obj[1].worldX);
        assertEquals("Unexpected worldY for keycard 1", 47 * mockGamePanel.tileSize, mockGamePanel.obj[1].worldY);

        assertEquals("Unexpected worldX for keycard 2", 30 * mockGamePanel.tileSize, mockGamePanel.obj[2].worldX);
        assertEquals("Unexpected worldY for keycard 2", 19 * mockGamePanel.tileSize, mockGamePanel.obj[2].worldY);

        // check the position of doors
        assertEquals("Unexpected worldX for door", 48 * mockGamePanel.tileSize, mockGamePanel.obj[3].worldX);
        assertEquals("Unexpected worldY for door", 42 * mockGamePanel.tileSize, mockGamePanel.obj[3].worldY);

        // check the position of exit
        assertEquals("Unexpected worldX for exit", 49 * mockGamePanel.tileSize, mockGamePanel.obj[4].worldX);
        assertEquals("Unexpected worldY for exit", 42 * mockGamePanel.tileSize, mockGamePanel.obj[4].worldY);

        // check the position of EMPS
        assertEquals("Unexpected worldX for EMP at index 7", 23 * mockGamePanel.tileSize, mockGamePanel.obj[5].worldX);
        assertEquals("Unexpected worldY for EMP at index 7", 27 * mockGamePanel.tileSize, mockGamePanel.obj[5].worldY);

        assertEquals("Unexpected worldX for EMP at index 8", 40 * mockGamePanel.tileSize, mockGamePanel.obj[6].worldX);
        assertEquals("Unexpected worldY for EMP at index 8", 21 * mockGamePanel.tileSize, mockGamePanel.obj[6].worldY);

        assertEquals("Unexpected worldX for EMP at index 9", 35 * mockGamePanel.tileSize, mockGamePanel.obj[7].worldX);
        assertEquals("Unexpected worldY for EMP at index 9", 37 * mockGamePanel.tileSize, mockGamePanel.obj[7].worldY);

        assertEquals("Unexpected worldX for EMP at index 10", 30 * mockGamePanel.tileSize, mockGamePanel.obj[8].worldX);
        assertEquals("Unexpected worldY for EMP at index 10", 2 * mockGamePanel.tileSize, mockGamePanel.obj[8].worldY);

        assertEquals("Unexpected worldX for EMP at index 11", 30 * mockGamePanel.tileSize, mockGamePanel.obj[9].worldX);
        assertEquals("Unexpected worldY for EMP at index 11", 1 * mockGamePanel.tileSize, mockGamePanel.obj[9].worldY);
    }

    @Mock
    Graphics2D graphics;

    @Test
    public void superObjectTest(){
        SuperObject os = new SuperObject();
        os.worldX = 1000; // Far from the player's worldX
        os.worldY = 1000; // Far from the player's worldY
        os.draw(graphics,mockGamePanel);

        // Verify drawImage was called
        verify(graphics, never()).drawImage(any(BufferedImage.class), anyInt(), anyInt(), anyInt(), anyInt(), any());
    }
}
