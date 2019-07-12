package versionconflict;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public enum DiamondSymbol {
    Blank("no-symbol.png"),
    Punch("punch-symbol.png"),
    Kick("kick-symbol.png"),
    ChargedPunch("punch-fill-symbol.png"),
    ChargedKick("kick-fill-symbol.png"),
    Wild("wild-fill-symbol.png");

    public Image image;

    DiamondSymbol(String image) {
        if(image != null) {
            try {
                this.image = ImageIO.read(getClass().getResource("/" + image));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
