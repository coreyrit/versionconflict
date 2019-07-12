package capitalvices;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public enum Sin {
    Greed("greed"),
    Gluttony("gluttony"),
    Lust("lust"),
    Envy("envy"),
    Wrath("wrath"),
    Sloth("sloth"),
    Pride("pride");

    private Image image;
    private Image icon;

    Sin(String image) {
        try {
            this.image = ImageIO.read(getClass().getResource("/" + image + ".png"));
            this.icon = ImageIO.read(getClass().getResource("/" + image + "-icon.png"));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Image getImage() {
        return image;
    }

    public Image getIcon() {
        return icon;
    }
}
