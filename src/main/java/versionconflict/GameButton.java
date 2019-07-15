package versionconflict;

import java.awt.*;

public class GameButton {
    private boolean enabled = false;
    private String text = "";
    private Color color = Color.lightGray;
    private Rectangle bounds = new Rectangle(0, 0, 0, 0);

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }
}
