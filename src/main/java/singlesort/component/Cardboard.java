package singlesort.component;

import singlesort.Game;

import java.awt.*;

public class Cardboard extends Component {

    public enum Face {
        CleanOne(true, 1, 	"\u2673"),
        CleanTwo(true, 2, "\u2674"),
        CleanThree(true, 3, "\u2675"),
        CleanFour(true, 4, "\u2676"),
        CleanFive(true, 5, "\u2677"),
        CleanSix(true, 6, "\u2678"),
        CleanSeven(true, 7, "\u2679"),
        DirtyOne(false, 1, "\u278a"),
        DirtyTwo(false, 2, "\u278b"),
        DirtyThree(false, 3, "\u278c"),
        DirtyFour(false, 4, "\u278d"),
        DirtyFive(false, 5, "\u278e"),
        DirtySix(false, 6, "\u278f");

        private boolean clean;
        private int value;
        private String text;

        Face(boolean clean, int value, String text) {
            this.clean = clean;
            this.value = value;
            this.text = text;
        }

        public boolean isClean() {
            return clean;
        }

        public int getValue() {
            return value;
        }

        public String getText() {
            return text;
        }

        public static Face fromTypeAndValue(boolean clean, int value) {
            if(clean) {
                switch(value) {
                    case 1:
                        return Face.CleanOne;
                    case 2:
                        return Face.CleanTwo;
                    case 3:
                        return Face.CleanThree;
                    case 4:
                        return Face.CleanFour;
                    case 5:
                        return Face.CleanFive;
                    case 6:
                        return Face.CleanSix;
                    case 7:
                        return Face.CleanSeven;
                }
            } else {
                switch(value) {
                    case 1:
                        return Face.DirtyOne;
                    case 2:
                        return Face.DirtyTwo;
                    case 3:
                        return Face.DirtyThree;
                    case 4:
                        return Face.DirtyFour;
                    case 5:
                        return Face.DirtyFive;
                    case 6:
                        return Face.DirtySix;
                }
            }
            throw new RuntimeException("Invalid value for " + (clean ? "clean" : "dirty") + " cardboard: " + value);
        }
    }

    private boolean faceUp;
    private Face face;
    private Color textColor;

    private Cardboard(Face face, Color color, Color textColor) {
        this.face = face;
        this.color = color;
        this.textColor = textColor;
        this.faceUp = Game.random.nextBoolean(); //randomize the face to start
    }

    public void flip() {
        this.faceUp = true;
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    public Face getFace() {
        return face;
    }

    public static Cardboard createGreenCleanCardboard(int value) {
        return new Cardboard(Face.fromTypeAndValue(true, value), Color.green.darker(), Color.white);
    }

    public static Cardboard createBlueCleanCardboard(int value) {
        return new Cardboard(Face.fromTypeAndValue(true, value), Color.blue, Color.white);
    }

    public static Cardboard createYellowCleanCardboard(int value) {
        return new Cardboard(Face.fromTypeAndValue(true, value), Color.yellow, Color.black);
    }

    public static Cardboard createGreenDirtyCardboard(int value) {
        return new Cardboard(Face.fromTypeAndValue(false, value), Color.green.darker(), Color.white);
    }

    public static Cardboard createBlueDirtyCardboard(int value) {
        return new Cardboard(Face.fromTypeAndValue(false, value), Color.blue, Color.white);
    }

    public static Cardboard createYellowDirtyCardboard(int value) {
        return new Cardboard(Face.fromTypeAndValue(false, value), Color.yellow, Color.black);
    }

    private static Font font = new Font("Arial", Font.PLAIN, 48);
    public void draw(Graphics g, int x, int y) {
        if(faceUp) {
            g.setColor(color);
        } else {
            g.setColor(Color.orange.darker());
        }
        g.fillPolygon(new int[] {x+10, x+50, x+90}, new int[] {y+90, y+10, y+90}, 3);

        if(Game.SingleSort.getHand().getSelected().contains(this) || Game.SingleSort.getTable().getSelected().contains(this)) {
            g.setColor(Color.magenta);
            ((Graphics2D)g).setStroke(new BasicStroke(4));
        } else if(highlight) {
            g.setColor(Color.black);
            ((Graphics2D)g).setStroke(new BasicStroke(4));
        } else {
            g.setColor(Color.black);
            ((Graphics2D)g).setStroke(new BasicStroke(1));
        }
        g.drawPolygon(new int[] {x+10, x+50, x+90}, new int[] {y+90, y+10, y+90}, 3);

        if(faceUp) {
            g.setFont(font);
            g.setColor(textColor);
            g.drawString(face.text, x+(face.clean ? 25 : 30), y+80);
        }
    }

    public Material getMaterial() {
        return Material.Cardboad;
    }
}
