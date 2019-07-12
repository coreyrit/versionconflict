package capitalvices;

import java.awt.*;

public class SinCard {
    private Rectangle bounds = new Rectangle(0, 0, 0, 0);

    private Sin sin;
    private boolean faceUp;

    public SinCard(Sin sin) {
        this.sin = sin;
        this.faceUp = true;
    }

    public Sin getSin() {
        return sin;
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    public void setFaceUp(boolean faceUp) {
        this.faceUp = faceUp;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }
}
