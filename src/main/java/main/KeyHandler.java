package main;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import main.GamePanel.gameState;

/**
 * Handles keyboard inputs for movement (WASD)
 */
public class KeyHandler implements KeyListener {
    GamePanel gp;

    public boolean upPressed, downPressed, leftPressed, rightPressed;

    /**
     * Handles keyboard inputs for movement (WASD)
     * @param gp GamePanel controlling the game state
     */
    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        if(gp.currentGameState == gameState.title){
            if(code == KeyEvent.VK_W){
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0){
                    gp.ui.commandNum = 1;
                }
            }
            if(code == KeyEvent.VK_S){
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 1) {
                    gp.ui.commandNum = 0;
                }
            }
            if(code == KeyEvent.VK_ENTER){
                if(gp.ui.commandNum == 0){
                    gp.currentGameState = gameState.playing;
                }
                if(gp.ui.commandNum == 1){
                    System.exit(0);
                }
            }
        }
        if(gp.currentGameState == gameState.playing){
            if(code == KeyEvent.VK_W){
                upPressed = true;
            }
            if(code == KeyEvent.VK_S){
                downPressed = true;
            }
            if(code == KeyEvent.VK_A){
                leftPressed = true;
            }
            if(code == KeyEvent.VK_D){
                rightPressed = true;
            }
            if(code == KeyEvent.VK_P){
                gp.currentGameState = gameState.paused;
            }
        }
        else if(gp.currentGameState == gameState.paused) {
            if (code == KeyEvent.VK_P) {
                gp.currentGameState = gameState.playing;
            }
        }
        else if(gp.currentGameState == gameState.gameOver) {
            gameOverState(code);
        }
        else if(gp.currentGameState == gameState.win){
            gameWinState(code);
        }
    }

    /**
     * Handles keyboard inputs when the game is in a win state
     * @param code keyboard input code
     */
    private void gameWinState(int code) {
        if(code == KeyEvent.VK_W){
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0){
                gp.ui.commandNum = 1;
            }
        }
        if(code == KeyEvent.VK_S){
            gp.ui.commandNum++;
            if(gp.ui.commandNum > 1) {
                gp.ui.commandNum = 0;
            }
        }
        if(code == KeyEvent.VK_ENTER){
            if(gp.ui.commandNum == 0){
                gp.currentGameState = gameState.playing;
                gp.restart();
            }
            if(gp.ui.commandNum == 1){
                gp.currentGameState = gameState.title;
                gp.restart();
            }

        }
    }

    /**
     * Handles keyboard inputs when the game is in a game over state.
     * @param code keyboard input code
     */
    public void gameOverState(int code) {
        if(code == KeyEvent.VK_W){
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0){
                gp.ui.commandNum = 1;
            }
        }
        if(code == KeyEvent.VK_S){
            gp.ui.commandNum++;
            if(gp.ui.commandNum > 1) {
                gp.ui.commandNum = 0;
            }
        }
        if(code == KeyEvent.VK_ENTER){
            if(gp.ui.commandNum == 0){
                gp.currentGameState = gameState.playing;
                gp.restart();
            }
            if(gp.ui.commandNum == 1){
                gp.currentGameState = gameState.title;
                gp.restart();
            }

        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W){
            upPressed = false;
        }
        if(code == KeyEvent.VK_S){
            downPressed = false;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = false;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = false;
        }
    }

}

