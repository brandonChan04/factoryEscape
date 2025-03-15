package tile;
import java.awt.image.BufferedImage;

/**
 * Contains data about the image associated with the tile and if the tile blocks player movement
 */
public class Tile {

    /**
     * Image associated with the tile
     */
    public BufferedImage image;
    /**
     * Set to true if player is blocked by the tile
     */
    public boolean collision = false;

}
