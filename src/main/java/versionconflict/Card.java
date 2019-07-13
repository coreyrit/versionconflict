package versionconflict;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Card {
    private int number;
    private Rectangle bounds = new Rectangle(0, 0, 0, 0);

    private CircleSymbol circleSymbol = CircleSymbol.Blank;
    private Special special = null;

    private SquareSymbol leftTopSquare = SquareSymbol.Blank;
    private SquareSymbol leftMiddleSquare = SquareSymbol.Blank;
    private SquareSymbol leftBottomSquare = SquareSymbol.Blank;

    private DiamondSymbol leftTopDiamond = DiamondSymbol.Blank;
    private DiamondSymbol leftMiddleDiamond = DiamondSymbol.Blank;
    private DiamondSymbol leftBottomDiamond = DiamondSymbol.Blank;

    private SquareSymbol rightTopSquare = SquareSymbol.Blank;
    private SquareSymbol rightMiddleSquare = SquareSymbol.Blank;
    private SquareSymbol rightBottomSquare = SquareSymbol.Blank;

    private Image image;

    private Card() {
    }

    public static CardBuilder builder() {
        return new CardBuilder();
    }

    public static class CardBuilder {
        private Card card = new Card();

        public CardBuilder withNumber(int number) {
            card.number = number;
            return this;
        }
        public CardBuilder withSpecial(Special special) {
            card.special = special;
            return this;
        }
        public CardBuilder withLeftTopSquare(SquareSymbol symbol) {
            card.leftTopSquare = symbol;
            return this;
        }
        public CardBuilder withLeftMiddleSquare(SquareSymbol symbol) {
            card.leftMiddleSquare = symbol;
            return this;
        }
        public CardBuilder withLeftBottomSquare(SquareSymbol symbol) {
            card.leftBottomSquare = symbol;
            return this;
        }
        public CardBuilder withRightTopSquare(SquareSymbol symbol) {
            card.rightTopSquare = symbol;
            return this;
        }
        public CardBuilder withRightMiddleSquare(SquareSymbol symbol) {
            card.rightMiddleSquare = symbol;
            return this;
        }
        public CardBuilder withRightBottomSquare(SquareSymbol symbol) {
            card.rightBottomSquare = symbol;
            return this;
        }
        public CardBuilder withLeftTopDiamond(DiamondSymbol symbol) {
            card.leftTopDiamond = symbol;
            return this;
        }
        public CardBuilder withLeftMiddleDiamond(DiamondSymbol symbol) {
            card.leftMiddleDiamond = symbol;
            return this;
        }
        public CardBuilder withLeftBottomDiamond(DiamondSymbol symbol) {
            card.leftBottomDiamond = symbol;
            return this;
        }
        public CardBuilder withCircleSymbol(CircleSymbol symbol) {
            card.circleSymbol = symbol;
            return this;
        }
        public Card build() {
            try {
                card.image = new BufferedImage(825, 1125, BufferedImage.TYPE_INT_ARGB);
                Graphics g = card.image.getGraphics();
                Graphics2D g2 = (Graphics2D) g;
                Image tgcBackground = ImageIO.read(getClass().getResource("/blank-card.png"));
                ImageIO.read(getClass().getResource("/pow.png"));
                g.drawImage(tgcBackground, 0, 0, null);
                if(card.getCircleSymbol() != CircleSymbol.Blank) {
                    g.drawImage(ImageIO.read(getClass().getResource("/pow.png")), 0, 0, null);
                    g.drawImage(card.getCircleSymbol().image, 515, 144, null);
                }

                g.drawImage(card.getLeftTopDiamond().image, 247, 410, null);
                g.drawImage(card.getLeftMiddleDiamond().image, 247, 634, null);
                g.drawImage(card.getLeftBottomDiamond().image, 247, 866, null);

                g.drawImage(card.getLeftTopSquare().image, 62, 416, null);
                g.drawImage(card.getLeftMiddleSquare().image, 62, 640, null);
                g.drawImage(card.getLeftBottomSquare().image, 62, 872, null);

                g.drawImage(card.getRightTopSquare().image, 574, 416, null);
                g.drawImage(card.getRightMiddleSquare().image, 574, 640, null);
                g.drawImage(card.getRightBottomSquare().image, 574, 872, null);

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            return card;
        }
    }

    public int getNumber() {
        return number;
    }

    public CircleSymbol getCircleSymbol() {
        return circleSymbol;
    }

    public Special getSpecial() {
        return special;
    }

    public SquareSymbol getLeftTopSquare() {
        return leftTopSquare;
    }

    public SquareSymbol getLeftMiddleSquare() {
        return leftMiddleSquare;
    }

    public SquareSymbol getLeftBottomSquare() {
        return leftBottomSquare;
    }

    public DiamondSymbol getLeftTopDiamond() {
        return leftTopDiamond;
    }

    public DiamondSymbol getLeftMiddleDiamond() {
        return leftMiddleDiamond;
    }

    public DiamondSymbol getLeftBottomDiamond() {
        return leftBottomDiamond;
    }

    public SquareSymbol getRightTopSquare() {
        return rightTopSquare;
    }

    public SquareSymbol getRightMiddleSquare() {
        return rightMiddleSquare;
    }

    public SquareSymbol getRightBottomSquare() {
        return rightBottomSquare;
    }

    public Image getImage() {
        return image;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }
}
