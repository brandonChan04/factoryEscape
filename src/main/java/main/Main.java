package main;
import javax.swing.JFrame;

/**
 * Initializes window and starts the game by creating a gamePanel
 */
class Main{
    public static void main(String[] args){

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Factory Escape");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack(); // resize the window to suit gamePanel

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // Starting game
        gamePanel.setupGame();
        gamePanel.startGameThread();

    }
}