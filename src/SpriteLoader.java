import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SpriteLoader {

    public static Image load(String path) {
        try {
            BufferedImage img = ImageIO.read(new File(path));
            return img;
        } catch (IOException e) {
            System.err.println("Cannot load image: " + path);
            return null;
        }
    }
}
