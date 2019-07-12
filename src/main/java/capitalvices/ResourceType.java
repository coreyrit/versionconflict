package capitalvices;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public enum ResourceType {
    Money("money.png"),
    Food("food.png");

    private Image image;

    ResourceType(String image) {
        try {
            this.image = ImageIO.read(getClass().getResource("/" + image));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Image getImage() {
        return image;
    }
}
