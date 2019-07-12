package versionconflict;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public enum CircleSymbol {
    Blank(null),
    PunchCharge("punch-symbol.png"),
    KickCharge("kick-symbol.png");

    public Image image;

    CircleSymbol(String image) {
        if(image != null) {
            try {
                this.image = ImageIO.read(getClass().getResource("/" + image));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
