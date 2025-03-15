package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Generic class that abstracts the characteristics of a moving entity on the map
 */
public class Entity {

    public int worldX, worldY;
    public int speed;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2; // stores sprite data
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Rectangle solidArea; // Collision area for the entity
    public int solidAreaDefaultX, solidAreaDefaultY; // stores default xy value of solidArea (solidArea.X/Y is changed in CollisionChecker)
    public boolean collisionOn = false;
    
}
