package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import javax.imageio.ImageIO;

import main.GamePanel;

/**
 * Loads, stores and draws all tiles
 */
public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    /**
     * Loads, stores and draws all tiles
     * @param gp - the GamePanel holding data about the dimensions of the world
     */
    public TileManager(GamePanel gp) {
        //Sets GamePanel instance to our current pointer variable
        this.gp = gp;

        //Creates a new instance of tile and sets it to tile
        tile = new Tile[10];
        mapTileNum = new int [gp.fullMapCol] [gp.fullMapRow];

        //Grabs the tile image and loads the map from the filePath
        getTileImage();
        loadMap("/maps/worldmap.txt");
    }

    /**
     * Gets and stores the Image corresponding to each tile, sets collision to true if the tile blocks the player
     */
    public void getTileImage() {

        //Loads tiles images and assigns them to tile. In the event the grabbing was to error, we would catch the error. 
        try{
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/Floor2.png")));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/WallMetal.png")));
            tile[1].collision = true;

        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the tile type for each coordinate from a file and store it in mapTileNum
     * @param filePath path to the map datafile
     */
    public void loadMap(String filePath) {

        //Attempts to load map, if the code faults, we output a "mapFile not found" message
        try {
            //Loads input stream map from the filePath
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;
            
            //Reads tile numbers from file and stores them in the array
            while(col < gp.fullMapCol && row < gp.fullMapRow) {

                String line = br.readLine();

                while (col < gp.fullMapCol) {
                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;

                    col ++;
                }
                if(col == gp.fullMapCol) {
                    col = 0;
                    row ++;
                }
            }
            br.close();
        //error checking 
        }catch(Exception e) {
            System.out.println("mapfile not found");
        }
    }

    /**
     * Draws all tiles visible on the screen using data from mapTileNum (to get tile type) and tile (to get image corresponding to tile type)
     */
    public void draw(Graphics2D g2) {

        int worldCol = 0;
        int worldRow = 0;
        

        while(worldCol < gp.fullMapCol && worldRow < gp.fullMapRow) {

            int tileNum = mapTileNum[worldCol][worldRow];
            
            // worldX and worldY indicate tile position in the world map
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;

            // screenX and screenY indicate tile position relative to the screen
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            // Only draw if the tile is within the bounds of the screen
            // or if the tile is one tile away from the boundary of the screen
            if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                    worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                    worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.worldY + gp.player.screenY ) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }

            worldCol++;

            if(worldCol == gp.fullMapCol) {
                worldCol = 0;
                worldRow ++;
            }
        }
    }
}
