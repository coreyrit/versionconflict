package singlesort;

import javax.swing.*;
import java.awt.*;

public class PanelRenderer extends JPanel {

    private Game game;
    private static Font font1 = new Font("Arial", Font.PLAIN, 48);
    private static Font font2 = new Font("Arial", Font.PLAIN, 18);

    public PanelRenderer(Game game) {
        this.game = game;
    }

    public int getWindowWidth() {
        return Game.windowWidth;
    }

    public int getWindowHeight() {
        return Game.windowHeight;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0, 0, getWindowWidth(), getWindowHeight());
        try {
            if (game != null) {
                for(int r = 0; r < Game.ROWS; r++) {
                    for(int c = 0; c < Game.COLUMNS; c++) {
                        if(game.getTable().get(r, c) != null) {
                            game.getTable().get(r, c).draw(g, c*Game.CELL_SIZE, r*Game.CELL_SIZE);
                        }
                    }
                }
                g.setColor(Color.black);
                ((Graphics2D)g).setStroke(new BasicStroke(2));
                g.drawLine(Game.COLUMNS*Game.CELL_SIZE, 0, Game.COLUMNS*Game.CELL_SIZE, Game.windowHeight);

                for(int i = 0; i < game.getHand().size(); i++) {
                    int x = Game.COLUMNS * Game.CELL_SIZE;
                    int y = i * Game.CELL_SIZE;
                    if(i >= 10) {
                        x += Game.CELL_SIZE;
                        y -= 10 * Game.CELL_SIZE;
                    }
                    game.getHand().get(i).draw(g, x, y);
                }

                g.setFont(font1);
                g.setColor(Color.black);
                g.drawString("Score: " + game.getScore(), 50, Game.windowHeight-25);
                g.setFont(font2);
                switch(game.getGameState()) {
                    case Take:
                        g.drawString("Choose a component in the pile to Take.", 300, Game.windowHeight-25);
                        break;
                    case Rot:
                        g.drawString("Choose a dirty cardboard to Rot or select a component in your collection to perform actions." + game.getScore(), 300, Game.windowHeight-25);
                        break;
                    case RecycleOrReduce:
                        if(game.getHand().getSelected().size() == 0) {
                            g.drawString("Select a cardboard, plastic, or glass in your collection to perform actions.", 300, Game.windowHeight-25);
                        } else if(game.getHand().getSelected().size() == 1 && game.getTable().getSelected().size() == 0) {
                            g.drawString("Choose a plastic in the pile to Reduce or cardboard to Recycle.  Or select more cardboard in your collection for Reduce.", 300, Game.windowHeight-25);
                        }else if(game.getHand().getSelected().size() == 1 && game.getTable().getSelected().size() == 1) {
                            g.drawString("Choose another cardboard in the pile to Recycle.", 300, Game.windowHeight-25);
                        }
                        break;
                    case RepairOrRepurpose:
                        if(game.getHand().getSelected().size() == 0) {
                            g.drawString("Select a plastic or glass in your collection to perform actions.", 300, Game.windowHeight-25);
                        } else if(game.getHand().getSelected().size() == 1) {
                            g.drawString("Select the plastic again to Repair or select an additional plastic in your collection to Repurpose.", 300, Game.windowHeight-25);
                        } else if(game.getHand().getSelected().size() == 2) {
                            g.drawString("Choose a glass in the pile to Repurpose.", 300, Game.windowHeight-25);
                        }
                        break;
                    case ReuseOrReturn:
                        if(game.getHand().getSelected().size() == 0) {
                            g.drawString("Select glass in your collection to perform actions.", 300, Game.windowHeight-25);
                        } else if(game.getHand().getSelected().size() == 1) {
                            g.drawString("Select a plastic 6 from the pile to Reuse or select more glass in your collection to Return.", 300, Game.windowHeight-25);
                        } else if(game.getHand().getSelected().size() == 2) {
                            g.drawString("Select one more glass in your collection to Return.", 300, Game.windowHeight-25);
                        } else if(game.getHand().getSelected().size() == 3) {
                            g.drawString("Select a metal in the pile to Return.", 300, Game.windowHeight-25);
                        }
                        break;
                    case Collect:
                        g.drawString("Select a cardboard to keep from the Take.", 300, Game.windowHeight-25);
                        break;
                    case CleanUp:
                        g.drawString("You must select a component in your collection for Clean Up.", 300, Game.windowHeight-25);
                        break;

                }

                g.setColor(Color.red);
                g.fillRect(Game.COLUMNS*Game.CELL_SIZE - (2*Game.CELL_SIZE) + 10, Game.ROWS*Game.CELL_SIZE + 10, 2*Game.CELL_SIZE - 20, Game.CELL_SIZE - 20);
                g.setColor(Color.black);
                ((Graphics2D)g).setStroke(new BasicStroke(4));
                g.drawRect(Game.COLUMNS*Game.CELL_SIZE - (2*Game.CELL_SIZE) + 10, Game.ROWS*Game.CELL_SIZE + 10, 2*Game.CELL_SIZE - 20, Game.CELL_SIZE - 20);
                g.setColor(Color.white);
                g.drawString("End Turn", Game.COLUMNS*Game.CELL_SIZE - (2*Game.CELL_SIZE) + 10+50, Game.ROWS*Game.CELL_SIZE + 10+50);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
