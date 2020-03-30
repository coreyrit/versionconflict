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

    private Metal(Game game, Type type) {
        super(game);
        this.type = type;
        this.color = this.type.color;
    }

    public Type getType() {
        return type;
    }

    public static Metal createGoldMetal(Game game) {
        return new Metal(game, Type.Gold);
    }

    public static Metal createSilverMetal(Game game) {
        return new Metal(game, Type.Silver);
    }

    public static Metal createBronzeMetal(Game game) {
        return new Metal(game, Type.Bronze);
    }

    public void draw(Graphics g, int x, int y) {
        g.setColor(type.color);
        g.fillRect(x+25, y+25, 50, 50);

        setStroke(g);

        g.drawRect(x+25, y+25, 50, 50);
    }

    public Material getMaterial() {
        return Material.Metal;
    }
}
