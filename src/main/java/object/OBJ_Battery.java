package object;

import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

/**
 * Battery object containing a score, image, and name
 */
public class OBJ_Battery extends SuperObject{
    /**
     * Battery object containing a score, image, and name
     */
    public OBJ_Battery() {
        name = "battery";
        scoreValue = 100;

        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/Battery.png")));

        }catch(IOException e) {
            e.printStackTrace();  
        }

    }
}
