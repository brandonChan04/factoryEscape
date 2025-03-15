import main.GamePanel;
import main.GamePanel.gameState;
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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UiTest {
    private final GamePanel mockGamePanel = new GamePanel();



    /**
    * Tests the showMessage method for correct message activation and storage.
    */
    @Test
    public void showMessageTest() {
        UI ui = new UI(mockGamePanel);
        String message = "Test Message";
        ui.showMessage(message);
        assertTrue(ui.messageOn);
        assertEquals(message, ui.message);
    }

    @Mock
    Graphics2D graphics;

    /**
     * Verifies the UI's draw method behavior in the title state, including font and color settings.
     */
    @Test
    public void TitleStateTest1(){
        UI ui = new UI(mockGamePanel);
        mockGamePanel.player.worldX = 20*mockGamePanel.tileSize;
        mockGamePanel.player.worldY = 20*mockGamePanel.tileSize;
        mockGamePanel.currentGameState = gameState.title;

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
    public void titleStateTest2(){
        UI ui = new UI(mockGamePanel);
        mockGamePanel.player.worldX = 20*mockGamePanel.tileSize;
        mockGamePanel.player.worldY = 20*mockGamePanel.tileSize;
        mockGamePanel.currentGameState = gameState.title;
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


    /**
     * Tests the UI drawing behavior during the pause state.
     */
    @Test
    public void pauseStateTest(){
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
    }

    /**
     * Tests the UI drawing behavior during the play state, specifically with an active message display.
     * This test sets up the game state to play and simulates a message being displayed on the screen. It then verifies that the
     * drawing operations (setFont and setColor) are called the correct number of times to reflect the UI elements that should be
     * drawn in this state, including the handling of the game message.
     *
     */
    @Test
    public void playStateTest(){
        UI ui = new UI(mockGamePanel);
        mockGamePanel.player.worldX = 20*mockGamePanel.tileSize;
        mockGamePanel.player.worldY = 20*mockGamePanel.tileSize;
        mockGamePanel.currentGameState = gameState.playing;
        ui.messageOn = true;
        ui.messageCounter = 130;

        when(graphics.getFont()).thenReturn(new Font("Arial", Font.PLAIN, 20));

        FontMetrics mockFontMetrics = mock(FontMetrics.class);
        lenient().when(graphics.getFontMetrics()).thenReturn(mockFontMetrics);

        ui.draw(graphics);

        verify(graphics, times(3)).setFont(any(Font.class));
        verify(graphics, times(4)).setColor(Color.white);
    }

    /**
     * Tests the UI drawing behavior when the game is in the win state.
     * This test configures the game state to simulate a win condition and checks if the UI correctly displays the winning screen.
     * It specifically verifies that the drawing operations behave as expected, with the correct number of font and color adjustments,
     * reflecting the visual elements to be drawn in the win state, such as messages and options presented to the player.
     */
    @Test
    public void winStateTest1(){
        UI ui = new UI(mockGamePanel);
        mockGamePanel.player.worldX = 20*mockGamePanel.tileSize;
        mockGamePanel.player.worldY = 20*mockGamePanel.tileSize;
        mockGamePanel.currentGameState = gameState.win;
        ui.commandNum = 0;

        FontMetrics mockFontMetrics = mock(FontMetrics.class);
        when(graphics.getFontMetrics()).thenReturn(mockFontMetrics);

        when(mockFontMetrics.getStringBounds(anyString(), any(Graphics.class)))
                .thenReturn(new Rectangle2D.Double(0, 0, 100, 10));

        ui.draw(graphics);

        verify(graphics, times(4)).setFont(any(Font.class));
        verify(graphics, times(3)).setColor(Color.white);
    }

    @Test
    public void winStateTest2(){
        UI ui = new UI(mockGamePanel);
        mockGamePanel.player.worldX = 20*mockGamePanel.tileSize;
        mockGamePanel.player.worldY = 20*mockGamePanel.tileSize;
        mockGamePanel.currentGameState = gameState.win;
        ui.commandNum = 1;

        FontMetrics mockFontMetrics = mock(FontMetrics.class);
        when(graphics.getFontMetrics()).thenReturn(mockFontMetrics);

        when(mockFontMetrics.getStringBounds(anyString(), any(Graphics.class)))
                .thenReturn(new Rectangle2D.Double(0, 0, 100, 10));

        ui.draw(graphics);

        verify(graphics, times(4)).setFont(any(Font.class));
        verify(graphics, times(3)).setColor(Color.white);
    }


    /**
     * Tests the UI's drawing behavior during the game over state.
     * The test sets up the game environment to simulate a game over condition by adjusting the game state accordingly.
     * It verifies that the drawing operations (setFont and setColor) are invoked the correct number of times,
     * indicating that the UI elements specific to the game over screen are being drawn correctly.
     */
    @Test
    public void gameOverStateTest1() {
        UI ui = new UI(mockGamePanel);
        mockGamePanel.player.worldX = 20*mockGamePanel.tileSize;
        mockGamePanel.player.worldY = 20*mockGamePanel.tileSize;
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
    public void gameOverStateTest2() {
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

    /**
     * Tests that the energy image is successfully loaded during the UI object's initialization.
     */
    @Test
    public void testEnergyImageLoaded() {
        UI ui = new UI(mockGamePanel);
        assertNotNull("Energy image should be loaded in constructor", ui.energyImage);
    }

    @Test
    public void testKeyImageLoaded() {
        UI ui = new UI(mockGamePanel);
        assertNotNull("Key image should be loaded in constructor", ui.keyImage);
    }

}
