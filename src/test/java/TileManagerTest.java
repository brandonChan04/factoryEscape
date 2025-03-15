import main.GamePanel;
import tile.Tile;
import tile.TileManager;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Objects;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TileManagerTest {

    private GamePanel gp = new GamePanel();
    // private TileManager tm = new TileManager(gp);

    /**
     * Compares 2 buffered images by pixels and returns true if all pixels are same
     * Source: https://stackoverflow.com/questions/15305037/java-compare-one-bufferedimage-to-another
     * @param img1
     * @param img2
     * @return
     */
    boolean bufferedImagesEqual(BufferedImage img1, BufferedImage img2) {
        if (img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight()) {
            for (int x = 0; x < img1.getWidth(); x++) {
                for (int y = 0; y < img1.getHeight(); y++) {
                    if (img1.getRGB(x, y) != img2.getRGB(x, y))
                        return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }
    
    @Test
    public void testgetTileImage(){
        TileManager tm = new TileManager(gp);
        tm.getTileImage();

        Tile tile1 = new Tile();
        Tile tile2 = new Tile();
        try{
            tile1.image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/Floor2.png")));
            tile2.image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/WallMetal.png")));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        assertEquals(true, bufferedImagesEqual(tile1.image, tm.tile[0].image));
        assertEquals(true, bufferedImagesEqual(tile2.image, tm.tile[1].image));
        assertEquals(false, tm.tile[0].collision);
        assertEquals(true, tm.tile[1].collision);
    }

    @Test
    public void testLoadMap(){
        TileManager tm = new TileManager(gp);
        int[][] expected = new int[gp.fullMapRow][gp.fullMapCol];
        for(int x = 0; x<gp.fullMapCol; x++){
            for(int y = 0; y<gp.fullMapRow; y++){
                expected[x][y] = 0;
                if(x == 0 && y == 39){
                    expected[x][y] = 1;
                }
                if(y == gp.fullMapRow-1){
                    expected[x][y] = 1;
                }
            }    
        }

        tm.loadMap("/maps/testmap.txt");

        assertArrayEquals(expected, tm.mapTileNum);
        
    }

    @Mock
    Graphics2D graphics;

    @Test
    public void testDrawMap(){
        TileManager tm = new TileManager(gp);
        gp.player.worldX = 20*gp.tileSize;
        gp.player.worldY = 20*gp.tileSize;
        tm.loadMap("/maps/testmap.txt");

        tm.draw(graphics);

        // verify draw calls
        verify(graphics, times((gp.maxScreenCol+1)*(gp.maxScreenRow+1))).drawImage(any(BufferedImage.class), 
            anyInt(), anyInt(), eq(gp.tileSize), eq(gp.tileSize), isNull());

       
    }
}
