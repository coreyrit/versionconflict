package singlesort.component;

import singlesort.Game;

import java.awt.*;

public class Plastic extends Component {

    public enum Face {
        One(1, "\u2680"),
        Two(2, "\u2681"),
        Three(3, 	"\u2682"),
        Four(4, "\u2683"),
        Five(5, "\u2684"),
        Six(6, "\u2685");

        private int value;
        private String text;

        Face(int value, String text) {
            this.value = value;
            this.text = text;
        }

        public int getValue() {
            return value;
        }

        public String getText() {
            return text;
        }
    }

    private Face face;
    private Color textColor;

    private Plastic(Color color, Color textColor) {
        this.color = color;
        this.textColor = textColor;
        roll(); //randomize the face to start
    }

    public void roll() {
        this.face = Face.values()[Game.random.nextInt(Face.values().length)];
    }

    public Face getFace() {
        return face;
    }

    public static Plastic createGreenPlastic() {
        return new Plastic(Color.green.darker(), Color.white) ;
    }

    public static Plastic createBluePlastic() {
        return new Plastic(Color.blue, Color.white) ;
    }

    public static Plastic createYellowPlastic() {
        return new Plastic(Color.yellow, Color.black) ;
    }

    private static Font font = new Font("Arial", Font.PLAIN, 100);

    public void draw(Graphics g, int x, int y) {
        g.setColor(color);
        g.fillRect(x + 15, y + 15, 70, 70);
        g.setFont(font);
        g.setColor(textColor);
        g.drawString(face.text, x+5, y+85);
        g.setColor(color);
        ((Graphics2D)g).setStroke(new BasicStroke(8));
        g.drawRect(x + 17, y + 17, 64, 64);

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

        g.drawRect(x + 15, y + 15, 70, 70);
    }

    public Material getMaterial() {
        return Material.Plastic;
    }
}
