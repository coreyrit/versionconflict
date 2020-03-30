package singlesort.component;

import singlesort.Game;

import java.awt.*;

public class Glass extends Component {

    private Glass(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public static Glass createGreenGlass() {
        return new Glass(Color.green.darker());
    }

    public static Glass createBlueGlass() {
        return new Glass(Color.blue);
    }

    public static Glass createYellowGlass() {
        return new Glass(Color.yellow);
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
