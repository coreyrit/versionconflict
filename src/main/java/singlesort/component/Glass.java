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

        if(Game.SingleSort.getHand().getSelected().contains(this)) {
            g.setColor(Color.red);
            ((Graphics2D)g).setStroke(new BasicStroke(4));
        } else if(highlight) {
            g.setColor(Color.black);
            ((Graphics2D)g).setStroke(new BasicStroke(4));
        } else {
            g.setColor(Color.black);
            ((Graphics2D)g).setStroke(new BasicStroke(1));
        }

        g.drawOval(x+10, y+10, 80, 80);
    }

    public Material getMaterial() {
        return Material.Glass;
    }
}
