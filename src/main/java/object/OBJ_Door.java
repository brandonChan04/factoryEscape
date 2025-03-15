package object;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * Door object containing a name and image
 */
public class OBJ_Door extends SuperObject{
    /**
     * Door object containing a name and image
     */
    public OBJ_Door() {
        name = "Door";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/door.png"));

        }catch(IOException e) {
            e.printStackTrace();
        }
        collision = true;
    }
}
