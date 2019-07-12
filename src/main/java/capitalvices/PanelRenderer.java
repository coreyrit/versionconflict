package capitalvices;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;

public class PanelRenderer implements ImageObserver {

    private Game game;
    private int windowWidth = 1600;
    private int windowHeight = 800;

    public PanelRenderer(Game game) {
        this.game = game;
    }

    public void paint(Graphics g) {
        try {
            if (game != null) {
                int x = 20;
                int y = 150;
                int width = 175;
                int height = 239;

                g.setColor(Color.lightGray);
                g.fillRect(0, 0, getWindowWidth(), getWindowHeight());

                g.drawImage(game.getRoundSin().getIcon(), 10, 10, this);
                Graphics2D g2d = (Graphics2D)g;

                for (SinCard sin : game.getSins()) {
                    g.setColor(Color.darkGray);
                    sin.setBounds(new Rectangle(x, y, width, height));
                    if(sin.isFaceUp()) {
                        g.drawImage(sin.getSin().getImage(), x, y, width, height, this);
                    } else {
                        g.fillRect(x, y, width, height);
                    }

                    if(game.getActiveSin() == sin) {
                        g2d.setStroke(new BasicStroke(3));
                        g.setColor(Color.green);
                        g.drawRect(x, y, width, height);
                    }
                    x += width + 10;
                }
                g2d.setStroke(new BasicStroke(1));

                x = 20;
                y += height + 20;

                int cofferHeight = 100;


                int i = 0;
                for (Coffer coffer : game.getCoffers()) {

                    if (i == game.getPlayerTurn()) {
                        g.setColor(Color.WHITE);
                        g.fillRect(x, y, (int)(width * 2.5), cofferHeight);
                    }

                    g.setColor(coffer.getColor());
                    g2d.setStroke(new BasicStroke(3));

                    coffer.setBounds(new Rectangle(x, y, (int)(width * 2.5), cofferHeight));
                    g.drawRect(x, y, (int)(width * 2.5), cofferHeight);

                    int xx = x + 10;
                    int yy = y + 10;
                    for(Resource resource : coffer.getResources()) {
                        drawResource(g2d, resource, xx, yy);
                        xx += 60;
                    }

                    Font firstPlayer = new Font("Arial", Font.PLAIN, 32);
                    g.setFont(firstPlayer);
                    g.setColor(Color.black);
                    if(coffer.isFirstPlayer()) {
                        g.drawString("First Player", x, y + cofferHeight + 30);

                        if(game.getTrashedResource() != null) {
                            drawResource(g2d, game.getTrashedResource(), x + 50, y + cofferHeight + 50);
                        }
                    }
                    g.drawString(Integer.toString(coffer.getScore()), x + (int)(width*2.5) - 30, y + cofferHeight - 15);

                    x += (int)(width * 2.5) + 10;
                    i++;
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void drawResource(Graphics2D g2d, Resource resource, int x, int y) {
        g2d.setColor(resource.getColor());
        g2d.fillOval(x, y, 50, 50);
        g2d.setStroke(new BasicStroke(1));
        if(game.getSelected().contains(resource)) {
            g2d.setStroke(new BasicStroke(3));
            g2d.setColor(Color.green);
        } else {
            g2d.setStroke(new BasicStroke(1));
            g2d.setColor(Color.black);
        }
        resource.setBounds(new Rectangle(x, y, 50, 50));
        g2d.drawOval(x, y, 50, 50);

        g2d.drawImage(resource.getResourceType().getImage(), x + 5, y + 5, 40, 40, this);
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

