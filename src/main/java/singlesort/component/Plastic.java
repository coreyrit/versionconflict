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

    private Plastic(Game game, Color color, Color textColor) {
        super(game);
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

    public static Plastic createGreenPlastic(Game game) {
        return new Plastic(game, Color.green.darker(), Color.white) ;
    }

    public static Plastic createBluePlastic(Game game) {
        return new Plastic(game, Color.blue, Color.white) ;
    }

    public static Plastic createYellowPlastic(Game game) {
        return new Plastic(game, Color.yellow, Color.black) ;
    }

//    private static Font font = new Font("Arial", Font.PLAIN, 128);
    private static Font font = new Font("Arial", Font.PLAIN, 100);
    private static Stroke stroke = new BasicStroke(8);

    public void draw(Graphics g, int x, int y) {
        g.setColor(color);
        g.fillRect(x + 15, y + 15, 70, 70);
        g.setFont(font);
        g.setColor(textColor);
        g.drawString(face.text, x+5, y+85);
        g.setColor(color);
        ((Graphics2D)g).setStroke(stroke);
        g.drawRect(x + 17, y + 17, 64, 64);

        setStroke(g);

        g.drawRect(x + 15, y + 15, 70, 70);
    }

    public Material getMaterial() {
        return Material.Plastic;
    }
}
