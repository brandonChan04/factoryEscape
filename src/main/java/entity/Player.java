package entity;
// import java.awt.Color;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import main.GamePanel.gameState;

/**
 * Manages the movement, visualization, and items held by the player character.
 * Also manages player collision and possesion of game objects.
 */
public class Player extends Entity{

    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    public int keycardsObtained = 0;


    /**
     * Constructs a Player object with initial settings and dependencies.
     * This constructor initializes the player's position on the screen, sets up the collision area,
     * and loads the player's image. It also sets the game panel and key handler that the player will interact with.
     *
     * @param gp The GamePanel instance which contains game state and settings.
     * @param keyH The KeyHandler responsible for processing player's keyboard input.
     */
    public Player(GamePanel gp, KeyHandler keyH){
        this.gp = gp;  // Assign the GamePanel instance for game state access.
        this.keyH = keyH;  // Assign the KeyHandler for input processing.

        // Calculate and set the player's initial screen position centered on the game window.
        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        // Initialize the player's collision boundary.
        solidArea = new Rectangle();
        solidArea.x = 8;  // Solid area offset from left and right edges of tile
        solidArea.y = 16;  // Solid area offset from top of tile.
        solidAreaDefaultX = solidArea.x;  // Store the default X offset for collision checking.
        solidAreaDefaultY = solidArea.y;  // Store the default Y offset for collision checking.
        solidArea.width = 32;  // Width of the collision area.
        solidArea.height = 32;  // Height of the collision area.

        setDefaultValues();  // Set player's default properties.
        getPlayerImage();  // Load the player's image assets.
    }

    /**
     * Set default values for player speed, starting position and direction
     */
    public void setDefaultValues() {
        worldX = gp.tileSize*24;
        worldY = gp.tileSize*24;
        speed = 4;
        direction = "right";
    }

    /**
     * Resets score and keycardsObtained
     */
    public void restoreScore(){
        gp.score = 0;
        keycardsObtained = 0;
    }


    /**
     * Get images associated with player direction and movement
     */
    public void getPlayerImage(){
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/up.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/up2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/down.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/down2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/left.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/left2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/right.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/right2.png"));

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Update the player state. Updates the player's:
     * - Direction
     * - Tile collision state
     * - Object collision state
     * - Player sprite
     */
    public void update() {
        if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true
                || keyH.rightPressed == true) {
        if(keyH.upPressed == true) {
            direction = "up";
        }
        else if(keyH.downPressed == true){
            direction = "down";
        }
        else if (keyH.leftPressed == true){
            direction = "left";
        }
        else if (keyH.rightPressed == true){
            direction = "right";
        }

        // CHECK TILE COLLISION
        collisionOn = false;
        gp.cChecker.checkTile(this);

        // CHECK OBJECT COLLISION
        int objIndex = gp.cChecker.checkObject(this, true);
        pickUpObject(objIndex);

        // IF COLLISION IS FALSE, PLAYER CAN MOVE
        if (collisionOn == false) {

            switch (direction) {
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
            }
        }
            updateSpriteCounter();
        }
    }

    /**
     * Called by update(). Updates the player sprite after moving for some amount of time
     */
    private void updateSpriteCounter(){
        spriteCounter++;
        if(spriteCounter > 14){ // change sprite every 15 frames (1/4 second)
            if(spriteNum == 1){
                spriteNum = 2;
            }
            else if(spriteNum == 2){
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

    /**
     * Handles the item that the player picks up appropriately
     * @param i index corresponding to object location in gp.obj[] returned by CollisionChecker.checkObject
     */
    public void pickUpObject(int i) {

        if(i != -1) {
            String objectName = gp.obj[i].name;
            gp.score += gp.obj[i].scoreValue;

            switch(objectName) {
                case "Keycard":
                    keycardsObtained++;
                    gp.obj[i] = null;
                    gp.ui.showMessage("You got a " + objectName);
                    break;
                case "Door":
                    if (keycardsObtained >= 3) {
                        gp.obj[i] = null;
                        keycardsObtained-=3;
                        gp.ui.showMessage("You opened the door!");
                    } else {
                        gp.ui.showMessage("3 keycards are required to unlock the exit");
                    }
                    break;
                case "Exit":
                    gp.ui.gameFinished = true;
                    gp.currentGameState = gameState.win;
                    break;
                case "EMP":
                    gp.obj[i] = null;
                    if(gp.score < 0){
                        gp.currentGameState = gameState.gameOver;
                    }
                    else{
                        gp.ui.showMessage("Your energy was drained by an " + objectName);
                    }
                    break;
                case "battery":
                    gp.obj[i] = null;
                    gp.ui.showMessage("You got a " + objectName);
                    break;
                default:
                    gp.obj[i] = null;
                    gp.ui.showMessage("You got a " + objectName);
                    break;
            }

        }

    }

    /**
     * Draws the player
     */
    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        switch(direction){
            case "up":
                if (spriteNum == 1){
                    image = up1;
                }
                if (spriteNum == 2){
                    image = up2;
                }
                break;
            case "down":
                if (spriteNum == 1){
                    image = down1;
                }
                if (spriteNum == 2){
                    image = down2;
                }
                break;
            case "left":
                if (spriteNum == 1){
                    image = left1;
                }
                if (spriteNum == 2){
                    image = left2;
                }
                break;
            case "right":
                if (spriteNum == 1){
                    image = right1;
                }
                if (spriteNum == 2){
                    image = right2;
                }
                break;      
        }

        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}
