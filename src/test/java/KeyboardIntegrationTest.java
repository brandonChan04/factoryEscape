import main.GamePanel;
import main.UI;
import main.GamePanel.gameState;
import main.KeyHandler;
import object.OBJ_Keycard;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;

import javax.imageio.ImageIO;


import object.ObjectSpawner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class KeyboardIntegrationTest {
    private GamePanel mockGamePanel = new GamePanel();

    @Mock
    Graphics2D graphics;

    @Test
    public void testTitleStateKeyPress1() {
        KeyHandler keyHandler = new KeyHandler(mockGamePanel);
        UI ui = new UI(mockGamePanel);

        mockGamePanel.currentGameState = gameState.title;
        mockGamePanel.ui.commandNum = 1;

        KeyEvent wKeyPressed = new KeyEvent(mockGamePanel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        keyHandler.keyPressed(wKeyPressed);

        assertEquals(0, mockGamePanel.ui.commandNum);

        keyHandler.keyPressed(wKeyPressed);
        assertEquals(1, mockGamePanel.ui.commandNum);

        keyHandler.keyReleased(wKeyPressed);


        wKeyPressed = new KeyEvent(mockGamePanel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_S, 'S');
        keyHandler.keyPressed(wKeyPressed);

        assertEquals(0, mockGamePanel.ui.commandNum);

        keyHandler.keyPressed(wKeyPressed);
        assertEquals(1, mockGamePanel.ui.commandNum);

        keyHandler.keyReleased(wKeyPressed);

        // Mock the behavior of Graphics2D.getFont() to return a specific Font object.
        when(graphics.getFont()).thenReturn(new Font("Arial", Font.PLAIN, 20));

        // Create a mock FontMetrics to be returned by Graphics2D.getFontMetrics().
        FontMetrics mockFontMetrics = mock(FontMetrics.class);
        when(graphics.getFontMetrics()).thenReturn(mockFontMetrics);

        // Mock the behavior of FontMetrics.getStringBounds() to return a predetermined rectangle.
        when(mockFontMetrics.getStringBounds(anyString(), any(Graphics.class)))
                .thenReturn(new Rectangle2D.Double(0, 0, 100, 10));

        ui.draw(graphics);

        // Verify that Graphics2D.setFont() was called three times during the drawing process.
        verify(graphics, times(3)).setFont(any(Font.class));

        // Verify that Graphics2D.setColor() was called twice with Color.white during the drawing process.
        verify(graphics, times(2)).setColor(Color.white);

    }

    @Test
    public void testTitleStateKeyPress2() {
        KeyHandler keyHandler = new KeyHandler(mockGamePanel);

        mockGamePanel.currentGameState = gameState.title;

        mockGamePanel.ui.commandNum = 0;

        KeyEvent KeyPressed = new KeyEvent(mockGamePanel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        keyHandler.keyPressed(KeyPressed);

        assertEquals(0, mockGamePanel.ui.commandNum);
    }

    @Test
    public void testPlayStateKeyPress() {
        KeyHandler keyHandler = new KeyHandler(mockGamePanel);
        mockGamePanel.currentGameState = gameState.playing;


        KeyEvent KeyPressed = new KeyEvent(mockGamePanel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        keyHandler.keyPressed(KeyPressed);
        assertTrue(keyHandler.upPressed);

        keyHandler.keyReleased(KeyPressed);
        assertFalse(keyHandler.upPressed);


        KeyPressed = new KeyEvent(mockGamePanel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_S, 'S');

        keyHandler.keyPressed(KeyPressed);
        assertTrue(keyHandler.downPressed);

        keyHandler.keyReleased(KeyPressed);
        assertFalse(keyHandler.downPressed);


        KeyPressed = new KeyEvent(mockGamePanel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 'A');

        keyHandler.keyPressed(KeyPressed);
        assertTrue(keyHandler.leftPressed);

        keyHandler.keyReleased(KeyPressed);
        assertFalse(keyHandler.leftPressed);


        KeyPressed = new KeyEvent(mockGamePanel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_D, 'D');

        keyHandler.keyPressed(KeyPressed);
        assertTrue(keyHandler.rightPressed);

        keyHandler.keyReleased(KeyPressed);
        assertFalse(keyHandler.rightPressed);


        KeyPressed = new KeyEvent(mockGamePanel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_P, 'P');

        keyHandler.keyPressed(KeyPressed);
        assertEquals(gameState.paused, mockGamePanel.currentGameState);

        UI ui = new UI(mockGamePanel);
        mockGamePanel.currentGameState = gameState.playing;
        ui.messageOn = true;
        ui.messageCounter = 130;

        // Mock the behavior of Graphics2D.getFont() to return a specific Font object.
        when(graphics.getFont()).thenReturn(new Font("Arial", Font.PLAIN, 20));

        // Create a mock FontMetrics to be returned by Graphics2D.getFontMetrics().
        FontMetrics mockFontMetrics = mock(FontMetrics.class);
        lenient().when(graphics.getFontMetrics()).thenReturn(mockFontMetrics);

        ui.draw(graphics);

        // Verify that Graphics2D.setFont() was called three times during the drawing process.
        verify(graphics, times(3)).setFont(any(Font.class));

        // Verify that Graphics2D.setColor() was called twice with Color.white during the drawing process.
        verify(graphics, times(4)).setColor(Color.white);
    }

    @Test
    public void testPauseStateKeyPress() {
        KeyHandler keyHandler = new KeyHandler(mockGamePanel);
        mockGamePanel.currentGameState = gameState.paused;


        KeyEvent KeyPressed = new KeyEvent(mockGamePanel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_P, 'P');

        keyHandler.keyPressed(KeyPressed);
        assertEquals(gameState.playing, mockGamePanel.currentGameState);


        UI ui = new UI(mockGamePanel);
        mockGamePanel.player.worldX = 20*mockGamePanel.tileSize;
        mockGamePanel.player.worldY = 20*mockGamePanel.tileSize;
        mockGamePanel.currentGameState = gameState.paused;

        when(graphics.getFont()).thenReturn(new Font("Arial", Font.PLAIN, 20));
        FontMetrics mockFontMetrics = mock(FontMetrics.class);
        when(graphics.getFontMetrics()).thenReturn(mockFontMetrics);

        when(mockFontMetrics.getStringBounds(anyString(), any(Graphics.class)))
                .thenReturn(new Rectangle2D.Double(0, 0, 100, 10));

        ui.draw(graphics);

        verify(graphics, times(2)).setFont(any(Font.class));
        verify(graphics, times(1)).setColor(Color.white);

        String message = "Test Message";
        ui.showMessage(message);
        assertTrue(ui.messageOn);
        assertEquals(message, ui.message);
    }

    @Test
    public void testGameOverStateKeyPress() {
        KeyHandler keyHandler = new KeyHandler(mockGamePanel);
        mockGamePanel.currentGameState = gameState.gameOver;

        mockGamePanel.ui.commandNum = 1;


        KeyEvent KeyPressed = new KeyEvent(mockGamePanel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        keyHandler.keyPressed(KeyPressed);


        assertEquals(0, mockGamePanel.ui.commandNum);


        keyHandler.keyPressed(KeyPressed);
        assertEquals(1, mockGamePanel.ui.commandNum);

        keyHandler.keyReleased(KeyPressed);


        KeyPressed = new KeyEvent(mockGamePanel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_S, 'S');
        keyHandler.keyPressed(KeyPressed);


        assertEquals(0, mockGamePanel.ui.commandNum);


        keyHandler.keyPressed(KeyPressed);
        assertEquals(1, mockGamePanel.ui.commandNum);

        keyHandler.keyReleased(KeyPressed);

        mockGamePanel.ui.commandNum = 0;
        KeyPressed = new KeyEvent(mockGamePanel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, 'E');
        keyHandler.keyPressed(KeyPressed);

        mockGamePanel.ui.commandNum = 1;
        KeyPressed = new KeyEvent(mockGamePanel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, 'E');
        keyHandler.keyPressed(KeyPressed);


        UI ui = new UI(mockGamePanel);
        mockGamePanel.currentGameState = gameState.gameOver;

        when(graphics.getFont()).thenReturn(new Font("Arial", Font.PLAIN, 20));
        FontMetrics mockFontMetrics = mock(FontMetrics.class);
        when(graphics.getFontMetrics()).thenReturn(mockFontMetrics);

        when(mockFontMetrics.getStringBounds(anyString(), any(Graphics.class)))
                .thenReturn(new Rectangle2D.Double(0, 0, 100, 10));

        ui.draw(graphics);

        verify(graphics, times(3)).setFont(any(Font.class));
        verify(graphics, times(2)).setColor(Color.white);
    }

    @Test
    public void testGameOverStateKeyPress2() {
        UI ui = new UI(mockGamePanel);
        mockGamePanel.player.worldX = 20*mockGamePanel.tileSize;
        mockGamePanel.player.worldY = 20*mockGamePanel.tileSize;
        mockGamePanel.currentGameState = gameState.gameOver;
        ui.commandNum = 1;

        when(graphics.getFont()).thenReturn(new Font("Arial", Font.PLAIN, 20));
        FontMetrics mockFontMetrics = mock(FontMetrics.class);
        when(graphics.getFontMetrics()).thenReturn(mockFontMetrics);

        when(mockFontMetrics.getStringBounds(anyString(), any(Graphics.class)))
                .thenReturn(new Rectangle2D.Double(0, 0, 100, 10));

        ui.draw(graphics);

        verify(graphics, times(3)).setFont(any(Font.class));
        verify(graphics, times(2)).setColor(Color.white);
    }

    @Test
    public void testWinStateKeyPress() {
        KeyHandler keyHandler = new KeyHandler(mockGamePanel);
        mockGamePanel.currentGameState = gameState.win;
        mockGamePanel.ui.commandNum = 1;


        KeyEvent KeyPressed = new KeyEvent(mockGamePanel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        keyHandler.keyPressed(KeyPressed);


        assertEquals(0, mockGamePanel.ui.commandNum);


        keyHandler.keyPressed(KeyPressed);
        assertEquals(1, mockGamePanel.ui.commandNum);

        keyHandler.keyReleased(KeyPressed);


        KeyPressed = new KeyEvent(mockGamePanel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_S, 'S');
        keyHandler.keyPressed(KeyPressed);


        assertEquals(0, mockGamePanel.ui.commandNum);


        keyHandler.keyPressed(KeyPressed);
        assertEquals(1, mockGamePanel.ui.commandNum);

        keyHandler.keyReleased(KeyPressed);

        mockGamePanel.ui.commandNum = 0;
        KeyPressed = new KeyEvent(mockGamePanel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, 'E');
        keyHandler.keyPressed(KeyPressed);

        mockGamePanel.currentGameState = gameState.win;
        mockGamePanel.ui.commandNum = 1;
        KeyPressed = new KeyEvent(mockGamePanel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, 'E');
        keyHandler.keyPressed(KeyPressed);


        UI ui = new UI(mockGamePanel);
        mockGamePanel.currentGameState = gameState.win;

        FontMetrics mockFontMetrics = mock(FontMetrics.class);
        when(graphics.getFontMetrics()).thenReturn(mockFontMetrics);

        when(mockFontMetrics.getStringBounds(anyString(), any(Graphics.class)))
                .thenReturn(new Rectangle2D.Double(0, 0, 100, 10));

        ui.draw(graphics);

        verify(graphics, times(4)).setFont(any(Font.class));
        verify(graphics, times(3)).setColor(Color.white);
    }

}
