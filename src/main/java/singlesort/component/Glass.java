package singlesort.component;

import singlesort.Game;

import java.awt.*;

public class Glass extends Component {

    private Glass(Game game, Color color) {
        super(game);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public static Glass createGreenGlass(Game game) {
        return new Glass(game, Color.green.darker());
    }

    public static Glass createBlueGlass(Game game) {
        return new Glass(game, Color.blue);
    }

    public static Glass createYellowGlass(Game game) {
        return new Glass(game, Color.yellow);
    }

    public void draw(Graphics g, int x, int y) {
        g.setColor(color);
        g.fillOval(x+10, y+10, 80, 80);

        setStroke(g);

        g.drawOval(x+10, y+10, 80, 80);
    }

    public Material getMaterial() {
        return Material.Glass;
    }
}
