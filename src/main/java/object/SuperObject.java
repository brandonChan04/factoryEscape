package object;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Holds abstract object data and draw method
 */
public class SuperObject {
    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    private int collisionWidth = 48, collisionHeight = 48;
    private int collisionTopOffset = 0, collisionLeftRightOffset = 0;
    public Rectangle solidArea;
    public int scoreValue = 0;
    public int id = -1;

    /**
     * Draws the object on the map
     * @param g2 used to draw objects
     * @param gp GamePanel holding the game state
     */
    public void draw(Graphics2D g2, GamePanel gp) {

        // Calculate the object's position on the screen based on the player's position.
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // Check if the object is within the player's viewport before drawing.
        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY ) {

            // Draw the object at the calculated screen position.
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }

    }

    public void setObjPosition(int worldX, int worldY){
        this.worldX = worldX;
        this.worldY = worldY;
        this.solidArea = new Rectangle(this.worldX + collisionLeftRightOffset, this.worldY + collisionTopOffset, collisionWidth, collisionHeight);
    }
}
