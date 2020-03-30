package singlesort.component;

import singlesort.Game;
import singlesort.PanelRenderer;

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

    protected void setStroke(Graphics g) {
        if(Game.SingleSort.getHand().getSelected().contains(this) || Game.SingleSort.getTable().getSelected().contains(this)) {
            g.setColor(Color.magenta);
            ((Graphics2D)g).setStroke(PanelRenderer.fatStroke);
        } else if(highlight) {
            g.setColor(Color.black);
            ((Graphics2D)g).setStroke(PanelRenderer.fatStroke);
        } else {
            g.setColor(Color.black);
            ((Graphics2D)g).setStroke(PanelRenderer.normalStroke);
        }
    }
}
