import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.swing.JFrame;


import org.junit.Test;

import java.awt.event.KeyEvent;
import main.GamePanel;
import main.GamePanel.gameState;
import main.KeyHandler;



public class KeyHandlerTest {

    /**
     * Validates the correct implementation of the keyHandler by confirming the state of the keyPress has changed
     * KeyPressEvent simulates a keypress, passes it to hekeyPressed, then assert checks if our instance of keyH has changed states
     * @param 
     * @return
     */
    @Test
    public void KeyPressed(){
    
    GamePanel gameTest = new GamePanel();
    KeyHandler keyH = new KeyHandler(gameTest);
    gameTest.currentGameState = gameState.playing;
    int keyStroke = 0;

    //Iterating through the W,A,S,D assertions
    for (int x = 0; x<4; x++){
        switch (x){
            case(0):
            keyStroke = 87; // W Key
            break;
            case(1):
            keyStroke = 65; //A Key
            break;
            case(2):
            keyStroke = 83; //S Key
            break;
            case(3):
            keyStroke = 68; // D Key
            break;
        }
        // SIMULATE a keypress event via KeyEvent, then sending it to keyPressed
        KeyEvent KeyPressEvent = new KeyEvent(new JFrame(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, keyStroke, KeyEvent.CHAR_UNDEFINED);
        keyH.keyPressed(KeyPressEvent);

        //Checking each respective case
        switch (x){
            case(0):
            assertTrue(keyH.upPressed);
            break;
            case(1):
            assertTrue(keyH.leftPressed);
            break;
            case(2):
            assertTrue(keyH.downPressed);
            break;
            case(3):
            assertTrue(keyH.rightPressed);
            break;
            default: //In the event the case was to fail, the assertion will throw an error. 
            assertTrue(false);
        }
    }
    }

    /**
     * Validates the correct implementation of the keyHandler by confirming the state of the keyPress has changed
     * dKeyPressEvent simulates a keypress, passes it to heyH.keyPressed, then assert checks if our instance of keyH has changed states
     * @param 
     * @return
     */
    @Test
    public void enterKeyPressed(){
    GamePanel gameTest = new GamePanel();
    KeyHandler keyH = new KeyHandler(gameTest);
    gameTest.currentGameState = gameState.title;

    // SIMULATE a keypress event via KeyEvent, then sending it to keyPressed
    KeyEvent enterKeyPressEvent = new KeyEvent(new JFrame(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
    keyH.keyPressed(enterKeyPressEvent);

    // CHECK if the 'enterPressed' flag is set to true
    assertEquals(gameState.playing, gameTest.currentGameState);
    }
}
