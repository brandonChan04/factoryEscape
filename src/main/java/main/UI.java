package main;

import object.OBJ_Keycard;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;

import javax.imageio.ImageIO;
import main.GamePanel.gameState;

/**
 * Draws UI information such as score or other messages to the player
 */
public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_50b;
    public BufferedImage keyImage;
    public boolean messageOn = false;
    public String message = "";
    public int messageCounter = 0;
    public boolean gameFinished = false;

    double playTime;
    DecimalFormat dFormat = new DecimalFormat("#0.00");

    public BufferedImage energyImage; //https://www.flaticon.com/free-icons/energy
    public int commandNum = 0;
    public int titleScreenState = 0;

    /**
     * Constructor initializes UI components and loads resources.
     * @param gp the main GamePanel
     */
    public UI(GamePanel gp) {
        this.gp = gp;

        // Initialize font styles for use in UI elements.
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_50b = new Font("Arial", Font.BOLD, 50);

        // Load the key image for display in the UI.
        OBJ_Keycard key = new OBJ_Keycard();
        keyImage = key.image;

        try {
            energyImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/UI/energy.png")));

        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes the UI state to display a message
     * @param text a message that will be displayed
     */
    public void showMessage(String text) {
        message = text;
        messageOn = true;

    }

    /**
     * Renders game graphics, including end game messages and ongoing gameplay information.
     * @param g2 Graphics2D object for drawing on the screen.
     */
    public void draw(Graphics2D g2) {

        this.g2 = g2;
        g2.setFont(arial_40);
        g2.setColor(Color.white);

        if(gp.currentGameState == gameState.title) {
            drawTitleScreen();
        }
        // Pause state
        if(gp.currentGameState == gameState.paused){
            drawPauseScreen();
        }
        if(gp.currentGameState == gameState.gameOver){
            drawGameOverScreen();
        }
        if(gp.currentGameState == gameState.playing) {
            drawPlayState();
        }

        if (gp.currentGameState == gameState.win) {
            drawWinScreen();
        }

    }

    /**
     * Draws a game over screen
     */
    private void drawGameOverScreen() {
        g2.setColor(new Color(0,0,0,150));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110f));

        text = "Game Over";

        // Shadow
        g2.setColor(Color.black);
        x = getXForCenteredText(text);
        y = gp.tileSize *4;
        g2.drawString(text, x, y);

        // Main
        g2.setColor(Color.white);
        g2.drawString(text, x-4, y-4);

        // Retry
        g2.setFont(g2.getFont().deriveFont(50f));
        text = "Retry";
        x = getXForCenteredText(text);
        y += gp.tileSize*4;
        g2.drawString(text, x, y);
        if(commandNum == 0){
            g2.drawString(">", x-40, y);
        }

        text = "Quit";
        x = getXForCenteredText(text);
        y += 55;
        g2.drawString(text, x, y);
        if(commandNum == 1){
            g2.drawString(">", x-40, y);
        }
    }

    /**
     * Draws a title screen
     */
    private void drawTitleScreen() {
        g2.setColor(new Color(70,120,80));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

        // Title name
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,96F));
        String text = "Factory Escape";
        int x = getXForCenteredText(text);
        int y = gp.tileSize*3;

        // Shadow
        g2.setColor(Color.black);
        g2.drawString(text, x+5, y+5);

        // Main color
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        // Character Image
        x = gp.screenWidth/2 - (gp.tileSize*2)/2;
        y += gp.tileSize*2;
        g2.drawImage(gp.player.down1, x, y, gp.tileSize*2, gp.tileSize*2, null);

        // Menu
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,48F));

        text = "NEW GAME";
        x = getXForCenteredText(text);
        y += gp.tileSize * 3.5;
        g2.drawString(text, x, y);
        if(commandNum == 0){
            g2.drawString(">", x-gp.tileSize, y);
        }

        text = "EXIT";
        x = getXForCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if(commandNum == 1){
            g2.drawString(">", x-gp.tileSize, y);
        }
    }

    /**
     * draws a pause screen
     */
    public void drawPauseScreen() {
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "PAUSED";
        int x = getXForCenteredText(text);
        int y = gp.screenHeight / 2;

        g2.drawString(text, x, y);
    }

    /**
     * draw the UI when the game is being played
     */
    private void drawPlayState(){
        // Game is ongoing: set font and color for game stats
        g2.setFont(arial_40);
        g2.setColor(Color.black);

        // Draw the key image and the number of keycards obtained by the player
        g2.drawImage(keyImage, gp.tileSize / 2, gp.tileSize / 2, gp.tileSize, gp.tileSize, null);
        g2.drawString("x " + gp.player.keycardsObtained, 74, 65);
        g2.setColor(Color.white);
        g2.drawString("x " + gp.player.keycardsObtained, 74+2, 65+2);

        // Draw an energy icon
        g2.drawImage(energyImage, gp.tileSize * 10, gp.tileSize / 4, gp.tileSize, gp.tileSize, null);
        // Display score
        g2.setColor(Color.black);
        g2.drawString("Score: " + gp.score, gp.tileSize*11, gp.tileSize);
        g2.setColor(Color.white);
        g2.drawString("Score: " + gp.score, gp.tileSize*11+2, gp.tileSize+2);


        // Display a message if `messageOn` is true
        if (messageOn == true) {
            g2.setColor(Color.white);

            // Set the message font size
            g2.setFont(g2.getFont().deriveFont(30f));

            // Draw the message on the screen
            g2.drawString(message, gp.tileSize / 2, gp.tileSize * 5);

            // Update message counter, and turn off the message after a set duration
            messageCounter++;

            if (messageCounter > 120) {
                messageCounter = 0;
                messageOn = false;
            }
        }
    }

    /**
     * Draws a win screen
     */
    private void drawWinScreen(){
        g2.setColor(new Color(0,0,0,150));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

        // Set the font and color for the end game text
        g2.setFont(arial_40);
        g2.setColor(Color.white);

        // Declare variables to help position the text on screen
        String text;
        int textLength;
        int x;
        int y;

        // Display player score
        text = "Your Score is: " + gp.score;

        // Calculate the text length to center it
        textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        x = gp.screenWidth / 2 - textLength / 2;
        y = gp.screenHeight / 2 - (gp.tileSize * 2);
        g2.drawString(text, x, y);

        // Set the font and color for the congratulatory message
        g2.setFont(arial_50b);
        g2.setColor(Color.yellow);
        text = "You escaped the factory!";

        // Calculate the text length to center the congratulatory message
        textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        x = gp.screenWidth / 2 - textLength / 2;
        y = gp.screenHeight / 2 - (gp.tileSize * 4);
        g2.drawString(text, x, y);

        g2.setFont(arial_40);
        g2.setColor(Color.white);
        text = "Restart";
        x = getXForCenteredText(text);
        y += gp.tileSize*4;
        g2.drawString(text, x, y);
        if(commandNum == 0){
            g2.drawString(">", x-40, y);
        }

        text = "Quit";
        x = getXForCenteredText(text);
        y += 55;
        g2.drawString(text, x, y);
        if(commandNum == 1){
            g2.drawString(">", x-40, y);
        }

        //gp.gameThread = null; // Stop the game thread, ending the game loop
    }

    /**
     * Gets the x-value that is used to center a text message 
     * @param text a string containing a message
     * @return x-value that results in a cenetered message when used to draw the text.
     */
    private int getXForCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }

}
