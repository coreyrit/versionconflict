package singlesort.component;

import java.awt.*;

public abstract class Component {
    public enum Material {
        None,
        Cardboad,
        Plastic,
        Glass,
        Metal
    }

    protected Color color;
    protected boolean highlight = false;

    public abstract void draw(Graphics g, int x, int y);
    public abstract Material getMaterial();

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public Color getColor() {
        return color;
    }
}
