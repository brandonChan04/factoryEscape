package main;

import entity.Entity;
//import javafx.scene.shape.Rectangle;
import object.SuperObject;

/**
 * Checks collision between entities, tiles, and objects
 */
public class CollisionChecker {

    GamePanel gp;
    /**
     * Checks collision between entities, tiles, and objects
     */
    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    private boolean checkCollision(GamePanel gp, int col1, int row1, int col2, int row2) {
        try {
            // Calculate the top row after moving up and get the tile numbers for collision checking
            int tileNum1 = gp.tileM.mapTileNum[col1][row1];
            int tileNum2 = gp.tileM.mapTileNum[col2][row2];

            // Check if any of the tiles have collision properties
            return gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision;
        } catch (ArrayIndexOutOfBoundsException e) {
            return true;
        }
    }

    private int calculateNextRow(int currentWorldY, int speed, int tileSize, String direction) {
        return direction.equals("up") ? (currentWorldY - speed) / tileSize : (currentWorldY + speed) / tileSize;
    }

    private int calculateNextCol(int currentWorldX, int speed, int tileSize, String direction) {
        return direction.equals("left") ? (currentWorldX - speed) / tileSize : (currentWorldX + speed) / tileSize;
    }

    /**
     * Check possible collision between entity and tiles
     * @param entity an entity that may collide with a tile
     */
    public void checkTile(Entity entity) {

        if(entity.worldX <= 0 && entity.direction == "left"){
            entity.collisionOn = true;
            return;
        }
        if(entity.worldY <= 0 && entity.direction == "up"){
            entity.collisionOn = true;
            return;
        }

        // collision coordinates
        // define entity collision areas
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        // define col & row numbers for accessing the tiles stored in tileM
        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        // Adjust the entity's position based on its direction and check for collisions
        switch (entity.direction) {
            case "up": // Moving up
                entityTopRow = calculateNextRow(entityTopWorldY, entity.speed, gp.tileSize, entity.direction);
                entity.collisionOn = checkCollision(gp, entityLeftCol, entityTopRow, entityRightCol, entityTopRow);
                break;
            case "down": // Moving down
                entityBottomRow = calculateNextRow(entityBottomWorldY, entity.speed, gp.tileSize, entity.direction);
                entity.collisionOn = checkCollision(gp, entityLeftCol, entityBottomRow, entityRightCol, entityBottomRow);
                break;
            case "left": // Moving left
                entityLeftCol = calculateNextCol(entityLeftWorldX, entity.speed, gp.tileSize, entity.direction);
                entity.collisionOn = checkCollision(gp, entityLeftCol, entityTopRow, entityLeftCol, entityBottomRow);
                break;
            case "right": // Moving right
                entityRightCol = calculateNextCol(entityRightWorldX, entity.speed, gp.tileSize, entity.direction);
                entity.collisionOn = checkCollision(gp, entityRightCol, entityTopRow, entityRightCol, entityBottomRow);
                break;
        }
    }

    /**
     * Checks if an entity is in contact with an object and returns index corresponding to object in gp.obj[] if player=true
     * @param entity an entity on the map
     * @param player true if entity is the player, otherwise false
     * @return integer corresponding to the index of the object in gp.obj[] that the entity is in contact with (returns -1 if none)
     */
    public int checkObject(Entity entity, boolean player) {

        int index = -1;

        for (int i = 0; i < gp.obj.length; i++) {

            if(gp.obj[i] != null) {
                // Get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // Check the entity's direction and adjust its solid area position accordingly
                switch(entity.direction) {
                    case "up":
                        // Move the solid area up based on the entity's speed
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        // Move the solid area down based on the entity's speed
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        // Move the solid area left based on the entity's speed
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        // Move the solid area right based on the entity's speed
                        entity.solidArea.x += entity.speed;
                        break;
                }

                // Check if the entity's solid area intersects with the object's solid area
                if(entity.solidArea.intersects(gp.obj[i].solidArea)) {
                    // If there's a collision, mark it on the entity
                    if(gp.obj[i].collision == true) {
                        entity.collisionOn = true;
                    }
                    // If the entity is the player, update the index to the current object
                    if(player == true) {
                        index = i;
                    }
                }
                // reset entity's solidArea back to default
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
            }   
        }
        // Return the index of the object in collision with the entity; returns -1 if no collision is detected
        return index;
    }
}
