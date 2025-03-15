package object;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

/**
 * Keycard object containing a name, image and score
 */
public class OBJ_Keycard extends SuperObject{
    /**
     * Keycard object containing a name, image and score
     */
    public OBJ_Keycard() {
        name = "Keycard";
        scoreValue = 200;

        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/keycard.png")));

        }catch(IOException e) {
            e.printStackTrace();
        }

    }
}
