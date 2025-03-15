package object;

import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

/**
 * EMP object containing a name, image, and score
 */
public class OBJ_EMP extends SuperObject{
    /**
     * EMP object containing a name, image, and score
     */
    public OBJ_EMP() {
        name = "EMP";
        scoreValue = -300;

        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/EMP.png")));

        }catch(IOException e) {
            e.printStackTrace();
        }

    }
}
