package object;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * Exit object containing a name and image.
 */
public class OBJ_Exit extends SuperObject{
    /**
     * Exit object containing a name and image.
     */
    public OBJ_Exit() {
        name = "Exit";

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/exit.png"));

        }catch(IOException e) {
            e.printStackTrace();
        }

    }
}
