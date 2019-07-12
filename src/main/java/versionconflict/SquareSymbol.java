package versionconflict;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public enum SquareSymbol {
    Blank("no-symbol.png"),
    Focus("chain-symbol.png"),
    Shield("shield-symbol.png");

    public Image image;

    SquareSymbol(String image) {
        if(image != null) {
            try {
                this.image = ImageIO.read(getClass().getResource("/" + image));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
