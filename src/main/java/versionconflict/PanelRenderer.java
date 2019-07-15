package versionconflict;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.ImageObserver;

public class PanelRenderer implements ImageObserver {
    private int windowWidth = 1280;
    private int windowHeight = 720;

    private Game game;
    private Image player2back;

    private static int cw = 152;
    private static int ch = 207;

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
        int y = windowHeight - ch - 20;

        Graphics2D g2d = (Graphics2D)g;
        g2d.setStroke(new BasicStroke(3));
        g.setColor(Color.blue);

        for(Card card : game.getPlayer1().getHand()) {
            card.setBounds(new Rectangle(x, y, cw, ch));
            g.drawImage(card.getImage(), x, y, cw, ch, this);

            if(game.getPlayer1().getSelected().contains(card)) {
                g.drawRect(x, y, cw, ch);
            }

            x += cw + 20;
        }

        x = 20;
        y = -ch/2; // only show half of the opponenent's cards
        for(Card card : game.getPlayer2().getHand()) {
            card.setBounds(new Rectangle(x, y, cw, ch));
            g.drawImage(player2back, x, y, cw, ch, this);
            x += cw + 20;
        }

        if(game.getCurrentCharge() != null) {
            Rectangle cardBounds = game.getCurrentCharge().getBounds();
            g.setColor(Color.green);
            g.drawRect(cardBounds.x, cardBounds.y, cardBounds.width, cardBounds.height);
        }

        drawButton(g, game.getButton1(), getButton1Bounds());
        drawButton(g, game.getButton2(), getButton2Bounds());
        drawButton(g, game.getButton3(), getButton3Bounds());
    }

    private void drawButton(Graphics g, GameButton button, Rectangle bounds) {
        if(button.isEnabled()) {
            button.setBounds(bounds);
            g.setColor(button.getColor());
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            g.setColor(Color.black);
            drawCenteredString(g, button.getText(), bounds, new Font("Arial", Font.PLAIN, 24));
        }
    }

    private Rectangle getButton1Bounds() {
        return new Rectangle(getWindowWidth()/2-520, getWindowHeight()/2-50, 200, 100);
    }
    private Rectangle getButton2Bounds() {
        return new Rectangle(getWindowWidth()/2-300, getWindowHeight()/2-50, 200, 100);
    }
    private Rectangle getButton3Bounds() {
        return new Rectangle(getWindowWidth()/2-80, getWindowHeight()/2-50, 200, 100);
    }

    public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        // Set the font
        g.setFont(font);
        // Draw the String
        g.drawString(text, x, y);
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
