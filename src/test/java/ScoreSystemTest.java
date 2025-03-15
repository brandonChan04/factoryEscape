import static org.junit.Assert.assertEquals;
import org.junit.Test;

//import javafx.scene.input.KeyEvent;
//import javafx.scene.input.KeyCode;
import main.GamePanel;
import main.KeyHandler;
import main.GamePanel.gameState;
import entity.Player;
import object.ObjectSpawner;

public class ScoreSystemTest {

    /**
     * Validates the correct implementation of the gameOverState when the score falls below 0
     * pickUpObject: Switches currentGameState 
     * @param 
     * @return
     */
    @Test
    public void scoreBelowZeroTest (){
        //INITIALIZE GamePanel
        GamePanel gpT = new GamePanel();
        gpT.setupGame(); 
        
        //SET Score to < 0 (Illustrating a Negative Score)
        gpT.score = -2;

        //TESTING if the game will send currentGameState to gameOverState
        gpT.player.pickUpObject(7);
        assertEquals(gameState.gameOver,gpT.currentGameState);
        gpT.setcurrentGameState(gameState.gameOver);
    }

    /**
     * Validates the correct implementation of the the keycard Aquisition
     * @param 
     * @return
     */
    @Test
    public void keycardAquisionTest (){
        //INITIALIZE GamePanel
        GamePanel gpT = new GamePanel();
        gpT.setupGame(); 

        //LOOP allows us to grab 3 keycards
        for (int x = 0; x < 3; x++){
            //TESTING if the game will send currentGameState to gameOverState
            gpT.player.pickUpObject(x);
        }
        assertEquals(gpT.player.keycardsObtained,3);
        gpT.setcurrentGameState(gameState.gameOver);

    }

    /**
     * Validates the correct implementation of the the score reset function
     * restoreScore: Sets both score and keycardsObtained to zero 
     * @param 
     * @return
     */
    @Test
    public void restoreScoreTest (){
        //INITIALIZE GamePanel
        GamePanel gpT = new GamePanel();
        gpT.setupGame(); 

        //SET test case score to 2
        gpT.score = 2;

        //SET test case keycards at 2
        gpT.player.keycardsObtained = 2;

        //RESET score
        gpT.player.restoreScore();
        assertEquals(gpT.player.keycardsObtained + gpT.score,0);
        gpT.setcurrentGameState(gameState.gameOver);

    }
}
