package singlesort.component;

import singlesort.Game;

import java.awt.*;

public class Metal extends Component {
    public enum Type {
        Gold(Color.yellow.darker()),
        Silver(Color.gray),
        Bronze(Color.orange.darker().darker().darker());

        private Color color;

        Type(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }

    private Type type;

    private Metal(Type type) {
        this.type = type;
        this.color = this.type.color;
    }

    public Type getType() {
        return type;
    }

    public static Metal createGoldMetal() {
        return new Metal(Type.Gold);
    }

    public static Metal createSilverMetal() {
        return new Metal(Type.Silver);
    }

    public static Metal createBronzeMetal() {
        return new Metal(Type.Bronze);
    }

    public void draw(Graphics g, int x, int y) {
        g.setColor(type.color);
        g.fillRect(x+25, y+25, 50, 50);

        if(Game.SingleSort.getHand().getSelected().contains(this)) {
            g.setColor(Color.magenta);
            ((Graphics2D)g).setStroke(new BasicStroke(4));
        } else if(highlight) {
            g.setColor(Color.black);
            ((Graphics2D)g).setStroke(new BasicStroke(4));
        } else {
            g.setColor(Color.black);
            ((Graphics2D)g).setStroke(new BasicStroke(1));
        }

        g.drawRect(x+25, y+25, 50, 50);
    }

    public Material getMaterial() {
        return Material.Metal;
    }
}
