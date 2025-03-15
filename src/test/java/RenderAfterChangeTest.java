import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import main.GamePanel;
import main.GamePanel.gameState;
import main.UI;
import object.SuperObject;
import tile.TileManager;

/**
 * Test that changes in the game state are accurately reflected in the next draw() call in a GamePanel
 */
@RunWith(MockitoJUnitRunner.class)
public class RenderAfterChangeTest {

    private GamePanel gp;

    @Mock
    private SuperObject superObjectMock;

    @Mock
    private UI ui;

    @Mock
    private TileManager tilem;

    private Graphics graphics;

    @Before
    public void setup() {
        gp = new GamePanel();
        gp.currentGameState = gameState.playing;
        gp.obj = new SuperObject[5];
        gp.player.worldX = 20*gp.tileSize;
        gp.player.worldY = 20*gp.tileSize;
        gp.tileM = tilem;
        gp.ui = ui;

        BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        graphics = image.createGraphics();

        // Specify behavior for mocked objects
        doNothing().when(ui).draw(any(Graphics2D.class));
        doNothing().when(tilem).draw(any(Graphics2D.class));

        reset(superObjectMock);
    }

    /**
     * Test that any objects added are reflected in the subsequent gp.paintComponent call
     */
    @Test
    public void testAddObject(){
        gp.obj[0] = superObjectMock;
        gp.paintComponent(graphics);

        // verify gp.obj[0] was drawn
        verify(superObjectMock, times(1)).draw(any(Graphics2D.class), any(GamePanel.class));
    }

    /**
     * Test that any objects removed are reflected in the subsequent gp.paintComponent call
     */
    @Test
    public void testRemoveObject(){
        gp.obj[0] = superObjectMock;
        gp.paintComponent(graphics);

        // verify gp.obj[0] was drawn
        verify(superObjectMock, times(1)).draw(any(Graphics2D.class), any(GamePanel.class));

        gp.paintComponent(graphics);
        gp.obj[0] = null;
        
        reset(superObjectMock);
        // verify gp.obj[0] was not drawn
        verify(superObjectMock, times(0)).draw(any(Graphics2D.class), any(GamePanel.class));
    }
    
}
