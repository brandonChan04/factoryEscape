package entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import main.GamePanel;

/**
 * Contains behavior of a guard the chases the player
 */
public class Guard extends Entity {

    private Random rand = new Random();

    private Player player;

    private GamePanel gp;

    // Variables used to invoke speed changes in the guard
    private int speedChangeCounter = 0;
    private final int speedChangeInterval = 3000; // 5 seconds

    // Variables used to avoid running into walls when moving towards player
    private boolean moveVertically;
    private boolean abstractMovement = false; // set to true when the guard stops moving directly towards the player in order to not get stuck on walls
    private final int abstractMovementLimit = 60; // 1 second
    private int abstractMovementCounter = 0;
    private final String[] directions = {"right", "left", "up", "down"};

    /**
     * @param gp GamePanel used to get reference to player
     * @param worldX starting x position
     * @param worldY starting y position
     */
    public Guard(GamePanel gp, int worldX, int worldY) {
        this.gp = gp;
        this.player = gp.player;
        this.worldX = worldX; // Initial X position
        this.worldY = worldY; // Initial Y position
        this.speed = 3; // Movement speed
        this.direction = "down"; // Initial direction

        // set solid area used to calculate collision
        solidArea = new Rectangle();
        solidArea.x = 8;  // Solid area offset from left and right edges of tile
        solidArea.y = 16;  // Solid area offset from top of tile.
        solidAreaDefaultX = solidArea.x;  // Store the default X offset for collision checking.
        solidAreaDefaultY = solidArea.y;  // Store the default Y offset for collision checking.
        solidArea.width = 32;  // Width of the collision area.
        solidArea.height = 32;  // Height of the collision area.
        loadGuardImage(); // Load guard image
    }

    /**
     * Method to set Abstract Movement of guard
     */
    public void setAbstractMovement(boolean newMovement) {
        this.abstractMovement = newMovement;
    }

    /**
     * Method to update guard state. Causes guard to move towards the player and may cause it to change speed
     */
    public void update() {
        
        // Move guard towards the player's position
        moveTowardsPlayer();

        speedChangeCounter++;
        // Check if it's time to change speed
        if (speedChangeCounter == speedChangeInterval) {
            randomSpeed();
            speedChangeCounter = 0;
        }
    }

    /**
     * Generates and sets a new random speed for the guard to avoid clumping
     */
    public void randomSpeed() {
        int min = 3;
        int max = 4;
        int newSpeed = rand.nextInt((max - min) + 1) + min;
        this.speed = newSpeed;
    }

    /**
     * Moves the guard in the current direction by the amount indicated by speed, if collisionOn is false.
     */
    private void move(){
        if(collisionOn == false){
            switch (direction) {
                case "right":
                    this.worldX += speed;
                    break;
                case "left":
                    this.worldX -= speed;
                    break;
                case "down":
                    this.worldY += speed;
                    break;
                case "up":
                    this.worldY -= speed;
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Sets a new movement direction towards the player based on the dirrection indicated by dx or dy
     * If moveVertically is false, the direction is on the x axis, otherwise on the y axis.
     * @param dx distance from player on x axis
     * @param dy distance from player on y axis
     */
    private void setDirection(int dx, int dy){
        if(this.moveVertically == false){
            if (dx > 0) {
                direction = "right";
            }
            else{
                direction = "left";
            }
        }
        else{
            if (dy > 0) {
                direction = "down";
            }
            else {
                direction = "up";
            }
        }
    }

    private void attemptMove(int dx, int dy) {
        setDirection(dx, dy);
        collisionOn = false;
        gp.cChecker.checkTile(this);
    }

    /**
     * Moves the guard towards the player, or moves towards the player in an abstract direction to avoid continuously bumping into walls.
     * Employs the following movement algorithm:
     * - move towards player, prioritizing the axis with longest distance from the player
     * - if this would cause a collision, attempt to move towards the player on the shorter axis, 
     *      then away from the player on the same shorter axis, then away from the player on the longer axis
     */
    private void moveTowardsPlayer() {
        if(abstractMovement){
            moveAbstractly();
        }
        else{
            // Calculate the direction towards the player
            int dx = player.worldX - worldX;
            int dy = player.worldY - worldY;

            // Move guard towards the player along the longer axis
            if (Math.abs(dx) > Math.abs(dy)) {
                if (dx > 0) {
                    // Move right
                    this.direction = "right";
                } else {
                    // Move left
                    this.direction = "left";
                }
                this.moveVertically = false;
            } else {
                if (dy > 0) {
                    // Move down
                    this.direction = "down";
                } else {
                    // Move up
                    this.direction = "up";
                }
                this.moveVertically = true;
            }

            // check if moving towards the player causes a collision
            collisionOn = false;
            gp.cChecker.checkTile(this);
            // Choose another direction if the moving towards the player causes a collision
            if(collisionOn) {
                // move towards player on the shorter axis
                this.moveVertically = !this.moveVertically;

                attemptMove(dx, dy);

                if(collisionOn){
                    // move away from player on the shorter axis
                    attemptMove(-dx, -dy);

                    if(collisionOn){
                        // move away from player on the longer axis
                        this.moveVertically = !this.moveVertically;
                        attemptMove(-dx, -dy);
                    }
                }
                this.abstractMovement = true;
            }

            move();          
        }
        
    }

    /**
     * Called by moveTowardsPlayer to avoid continuously bumping into walls. 
     * Continues moving the guard in the previous direction, but if another collision occurs, move in a random direction.
     */
    private void moveAbstractly(){
        abstractMovementCounter++;
        if(abstractMovementCounter == abstractMovementLimit){
            abstractMovementCounter = 0;
            abstractMovement = false;
        }
        collisionOn = false;
        gp.cChecker.checkTile(this);
        if(collisionOn == true){ // pick a new random direction if a collision occurs
            String currentDir = this.direction;
            this.direction = directions[rand.nextInt(4)];
            while (this.direction == currentDir) {
                this.direction = directions[rand.nextInt(4)];
            }
            move();
        }
        else{ // keep moving in the previous direction
            move();
        }
    }

    /**
     * load assets for guard
     */
    private void loadGuardImage() {
        try {
            down1 = ImageIO.read(getClass().getResourceAsStream("/guard/down.png"));
            up1 = ImageIO.read(getClass().getResourceAsStream("/guard/up.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/guard/left.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/guard/right.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * draws the guard at its current position and direction
     * @param g2 Graphics2D object that performs the rendering
     */
    public void draw(Graphics2D g2) {
        BufferedImage guardImage = null;
        switch(direction){
            case "up":
                guardImage = up1;
                break;
            case "down":
                guardImage = down1;
                break;
            case "left":
                guardImage = left1;
                break;
            case "right":
                guardImage = right1;
                break;
        }

        if (guardImage != null) {
            g2.drawImage(guardImage, worldX - player.worldX + player.screenX, 
                                  worldY - player.worldY + player.screenY, null);
        }
    }

    /**
     * returns the result of a collision check between guard and player
     * @param player the player character followed by the guard.
     * @return true if a guard is in contact with player, otherwise false.
     */
    public boolean isCollidingWith(Player player) {

        // calculate box ranges - make the guard's range slightly smaller so the collision looks more realistic
        int leftSideGuardHitbox = worldX + solidArea.x*2;
        int rightSideGuardHitbox = worldX + solidArea.x/2 + solidArea.width;
        int topSideGuardHitbox = worldY + solidArea.y*2;
        int bottomSideGuardHitbox= worldY + solidArea.y/2 + solidArea.height;

        int leftSidePlayerHitbox = player.worldX + player.solidArea.x;
        int rightSidePlayerHitbox = player.worldX + player.solidArea.x + player.solidArea.width;
        int topSidePlayerHitbox = player.worldY + player.solidArea.y;
        int bottomSidePlayerHitbox= player.worldY + player.solidArea.y + player.solidArea.height;

        // Check for overlap on both axis
        boolean xOverlap = leftSideGuardHitbox <= rightSidePlayerHitbox && rightSideGuardHitbox >= leftSidePlayerHitbox;
        boolean yOverlap = topSideGuardHitbox <= bottomSidePlayerHitbox && bottomSideGuardHitbox >= topSidePlayerHitbox;

        // If there's overlap on both axes, the boxes are colliding
        return xOverlap && yOverlap;
    }
}

