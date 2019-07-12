package versionconflict;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.ImageObserver;

public class PanelRenderer implements ImageObserver {
    private int windowWidth = 1400;
    private int windowHeight = 800;

    private Game game;
    private Image player2back;

    private static int cw = 165;
    private static int ch = 225;

    public PanelRenderer(Game game) {
        this.game = game;
        try {
            player2back = ImageIO.read(getClass().getResource("/player2-back.png"));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

//    @Override
    public void paint(Graphics g) {
//        super.paint(g);

        int x = 20;
        int y = 500;

        for(Card card : game.getPlayer1().getHand()) {
            g.drawImage(card.getImage(), x, y, cw, ch, this);
            x += cw + 20;
        }

        x = 20;
        y = 20;
        for(Card card : game.getPlayer2().getHand()) {
            g.drawImage(player2back, x, y, cw, ch, this);
            x += cw + 20;
        }
    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return false;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }
}
