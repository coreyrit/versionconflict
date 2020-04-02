package singlesort;

import singlesort.component.Cardboard;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;

public class PanelRenderer extends JPanel {

    private Game game;
    private static Font font0 = new Font("Arial", Font.PLAIN, 14);
    private static Font font1 = new Font("Arial", Font.PLAIN, 24);
    private static Font font2 = new Font("Arial", Font.PLAIN, 18);
    private static Font font3 = new Font("Arial", Font.PLAIN, 36);

    public static Stroke normalStroke = new BasicStroke(1);
    public static Stroke fatStroke = new BasicStroke(4);

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
                boolean newGame = game.gameOver();
                java.util.List<Integer> sixCount = game.getRankedSixes();

                if (newGame) {
                    // write out all the players components and their score
                    int y = 100;
                    int x = 200;
                    for(int i = 0; i < game.getHands().size(); i++) {
                        g.setFont(font1);
                        g.setColor(Color.black);
                        g.drawString("Player " + (i+1) + "(" + game.getScore(i)  + "):", 10, y);

                        // draw collection
                        for(singlesort.component.Component comp: game.getHands().get(i)) {
                            comp.draw(g, x, y);
                            x += Game.CELL_SIZE;
                        }
                        // draw trash
                        for(Cardboard cardboard : game.getTable().getTrashHeap()) {
                            if(game.getHands().get(i).getGoal().contains(cardboard.getColor(), cardboard.getFace().getValue())) {
                                cardboard.draw(g, x, y);
                                x += Game.CELL_SIZE;
                            }
                        }
                        y += Game.CELL_SIZE + 20;
                        x = 200;
                    }

                    g.setFont(font2);
                    g.setColor(Color.green);
                    g.fillRect(Game.COLUMNS * Game.CELL_SIZE - (2 * Game.CELL_SIZE) + 10, Game.ROWS * Game.CELL_SIZE + 10, 2 * Game.CELL_SIZE - 20, Game.CELL_SIZE - 20);
                    g.setColor(Color.black);
                    ((Graphics2D) g).setStroke(fatStroke);
                    g.drawRect(Game.COLUMNS * Game.CELL_SIZE - (2 * Game.CELL_SIZE) + 10, Game.ROWS * Game.CELL_SIZE + 10, 2 * Game.CELL_SIZE - 20, Game.CELL_SIZE - 20);
                    g.setColor(Color.black);
                    g.drawString("New Game", Game.COLUMNS * Game.CELL_SIZE - (2 * Game.CELL_SIZE) + 10 + 50, Game.ROWS * Game.CELL_SIZE + 10 + 50);

                } else {
                    for (int r = 0; r < Game.ROWS; r++) {
                        for (int c = 0; c < Game.COLUMNS; c++) {
                            if (game.getTable().get(r, c) != null) {
                                game.getTable().get(r, c).draw(g, c * Game.CELL_SIZE, r * Game.CELL_SIZE);
                            }
                        }
                    }
                    g.setColor(Color.black);
                    ((Graphics2D) g).setStroke(normalStroke);
                    g.drawLine(Game.COLUMNS * Game.CELL_SIZE, 0, Game.COLUMNS * Game.CELL_SIZE, Game.windowHeight);

                    for (int i = 0; i < game.getHand().size(); i++) {
                        int x = Game.COLUMNS * Game.CELL_SIZE;
                        int y = i * Game.CELL_SIZE;
                        if (i >= 10) {
                            x += Game.CELL_SIZE;
                            y -= 10 * Game.CELL_SIZE;
                        }
                        game.getHand().get(i).draw(g, x, y);
                    }


                    g.setColor(Color.black);
                    g.setFont(font0);
                    final Properties properties = new Properties();
                    properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
                    g.drawString("Version: " + Game.VERSION, 50, Game.windowHeight - 90);
                    g.setFont(font1);
                    g.drawString("Player: " + (game.getTurn() + 1) + "/" + game.getHands().size(), 50, Game.windowHeight - 65);
                    g.drawString("Score: " + game.getScore(), 50, Game.windowHeight - 45);

                    g.drawString("Goal: ", 50, Game.windowHeight - 25);
                    g.setFont(font3);
                    int x = 75;
                    for(Cardboard target : game.getHand().getGoal().getTargets()) {
                        x += 40;
                        g.setColor(Color.black);
                        if(target.getColor().equals(Color.yellow)) {
                            g.fillOval(x - 2, Game.windowHeight - 44, 32, 32);
                        } else {
                            ((Graphics2D) g).setStroke(fatStroke);
                            g.drawOval(x - 1, Game.windowHeight - 43, 30, 30);
                        }
                        g.setColor(target.getColor());
                        g.drawString(target.getFace().getText(), x, Game.windowHeight - 15);

                    }

                    g.setColor(Color.black);
                    g.setFont(font2);
                    switch (game.getGameState()) {
                        case Take:
                            String msg = "Choose a face down cardboard in the pile to Collect";
                            if (game.getHands().size() == 1) {
                                msg += "(" + (game.getTake().size() + 1) + " of 2).";
                            }
                            g.drawString(msg, 300, Game.windowHeight - 65);
                            break;
                        case Rot:
//                            String colorWord;
//                            if (game.getLastSelected().getColor().equals(Color.blue)) {
//                                colorWord = "blue";
//                            } else if (game.getLastSelected().getColor().equals(Color.yellow)) {
//                                colorWord = "yellow";
//                            } else {
//                                colorWord = "green";
//                            }

//                            g.drawString("Choose a dirty cardboard to Rot your clean " + colorWord + " " + game.getLastSelected().getFace().getValue() + ".", 300, Game.windowHeight - 65);
                            g.drawString("Choose a component in the pile for Recycle.", 300, Game.windowHeight - 65);
                            g.drawString("Or select a component in your collection to perform actions.", 300, Game.windowHeight - 35);
                            break;
                        case RotSwap:
                            g.drawString("Choose a component in your collection to Recycle.", 300, Game.windowHeight - 65);
                            break;
                        case RecycleOrReduce:
                            switch(game.getHand().getSelected().size()) {
                                case 0:
                                    g.drawString("Select a cardboard, plastic, or glass in your collection to perform actions.", 300, Game.windowHeight - 65);
                                    break;
                                case 1:
                                    switch(game.getTable().getSelected().size()) {
                                        case 0:
                                            g.drawString("Choose a plastic in the pile to Reduce or more cardboard to Repair.", 300, Game.windowHeight - 65);
                                            g.drawString("Or select more cardboard in your collection for Reduce.", 300, Game.windowHeight - 35);
                                            break;
                                        default:
                                            g.drawString("Choose more cardboard to Repair.", 300, Game.windowHeight - 65);
                                            break;
                                    }
                                    break;
                                default:
                                    g.drawString("Choose a plastic in the pile to Reduce.", 300, Game.windowHeight - 65);
                                    g.drawString("Or select more cardboard in your collection for Reduce.", 300, Game.windowHeight - 35);
                                    break;
                            }
                            break;
                        case RepairOrRepurpose:
                            if (game.getHand().getSelected().size() == 0) {
                                g.drawString("Select a plastic or glass in your collection to perform actions.", 300, Game.windowHeight - 65);
                            } else if (game.getHand().getSelected().size() == 1) {
                                String repairMsg = "Click the button below to Rethink";
                                if (game.getHands().size() > 2) {
                                    repairMsg += " or select another plastic to also Rethink.";
                                }
                                g.drawString(repairMsg, 300, Game.windowHeight - 65);
                                g.drawString("Or select an additional plastic in your collection to Repurpose.", 300, Game.windowHeight - 35);
                            } else if (game.getHand().getSelected().size() == 2 && !game.getHand().getSelected().getColor().equals(Color.black)) {
                                g.drawString("Choose a glass in the pile to Repurpose.", 300, Game.windowHeight - 65);
                            } else if (game.getHand().getSelected().size() == 2 && game.getHand().getSelected().getColor().equals(Color.black)) {
                                String repairMsg = "Click the button below to Rethink";
                                if (game.getHands().size() > 4) {
                                    repairMsg += " or select another plastic to also Rethink.";
                                }
                                g.drawString(repairMsg, 300, Game.windowHeight - 65);
                            } else if (game.getHand().getSelected().size() == 3) {
                                String repairMsg = "Click the button below to Rethink";
                                g.drawString(repairMsg, 300, Game.windowHeight - 65);
                            }
                            break;
                        case ReuseOrReturn:
                            if (game.getHand().getSelected().size() == 0) {
                                g.drawString("Select glass in your collection to perform actions.", 300, Game.windowHeight - 65);
                            } else if (game.getHand().getSelected().size() == 1) {
                                g.drawString("Select a plastic 6 from the pile to Reuse or select more glass in your collection to Return.", 300, Game.windowHeight - 65);
                                if (game.getHands().size() >= 5) {
                                    g.drawString("Or select one more glass in your collection to Return.", 300, Game.windowHeight - 35);
                                }
                            } else if (game.getHand().getSelected().size() == 3 || (game.getHands().size() >= 5 && game.getHand().getSelected().size() == 2)) {
                                g.drawString("Select a metal in the pile to Return.", 300, Game.windowHeight - 65);
                            } else if (game.getHand().getSelected().size() == 2) {
                                g.drawString("Select one more glass in your collection to Return.", 300, Game.windowHeight - 65);
                            }
                            break;
                        case Collect:
                            g.drawString("Select a cardboard to keep.", 300, Game.windowHeight - 65);
                            break;
                        case CleanUp:
                            g.drawString("You must select a component in your collection for Clean Up.", 300, Game.windowHeight - 65);
                            break;

                    }

                    boolean repair = game.getGameState() == Game.State.RepairOrRepurpose &&
                            (game.getHand().getSelected().size() == 1 || (game.getHand().getSelected().size() > 1 && game.getHand().getSelected().getColor().equals(Color.black)));

                    if (newGame) {
                        g.setColor(Color.green);
                    } else if (repair) {
                        g.setColor(Color.gray);
                    } else {
                        g.setColor(Color.red);
                    }
                    g.fillRect(Game.COLUMNS * Game.CELL_SIZE - (2 * Game.CELL_SIZE) + 10, Game.ROWS * Game.CELL_SIZE + 10, 2 * Game.CELL_SIZE - 20, Game.CELL_SIZE - 20);
                    g.setColor(Color.black);
                    ((Graphics2D) g).setStroke(fatStroke);
                    g.drawRect(Game.COLUMNS * Game.CELL_SIZE - (2 * Game.CELL_SIZE) + 10, Game.ROWS * Game.CELL_SIZE + 10, 2 * Game.CELL_SIZE - 20, Game.CELL_SIZE - 20);

                    if (newGame) {
                        g.setColor(Color.black);
                        g.drawString("New Game", Game.COLUMNS * Game.CELL_SIZE - (2 * Game.CELL_SIZE) + 10 + 50, Game.ROWS * Game.CELL_SIZE + 10 + 50);
                    } else if (repair) {
                        g.setColor(Color.black);
                        g.drawString("Rethink", Game.COLUMNS * Game.CELL_SIZE - (2 * Game.CELL_SIZE) + 10 + 50, Game.ROWS * Game.CELL_SIZE + 10 + 50);
                    } else {
                        g.setColor(Color.white);
                        g.drawString("End Turn", Game.COLUMNS * Game.CELL_SIZE - (2 * Game.CELL_SIZE) + 10 + 50, Game.ROWS * Game.CELL_SIZE + 10 + 50);
                    }

                    g.setFont(font2);
                    g.setColor(Color.black);
                    if(sixCount.size() > 0) {
                        g.drawString("1st sixes: " + sixCount.get(0), Game.COLUMNS * Game.CELL_SIZE - (2 * Game.CELL_SIZE) -150, Game.ROWS * Game.CELL_SIZE + 10 + 50);
                    } else {
                        g.drawString("1st sixes: 0", Game.COLUMNS * Game.CELL_SIZE - (2 * Game.CELL_SIZE) -150, Game.ROWS * Game.CELL_SIZE + 10 + 50);
                    }
                    if(sixCount.size() > 1) {
                        g.drawString("2nd sixes: " + sixCount.get(1), Game.COLUMNS * Game.CELL_SIZE - (2 * Game.CELL_SIZE) -150, Game.ROWS * Game.CELL_SIZE + 10 + 75);
                    } else {
                        g.drawString("2nd sixes: 0", Game.COLUMNS * Game.CELL_SIZE - (2 * Game.CELL_SIZE) -150, Game.ROWS * Game.CELL_SIZE + 10 + 75);
                    }

                    // draw trash
                    for(Cardboard cardboard : game.getTable().getTrashHeap()) {
                        Point pt = game.getTable().getTrashMap().get(cardboard);
                        drawTrash(g, cardboard, pt.y, pt.x);
                    }
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void drawTrash(Graphics g, Cardboard cardboard, int r, int c) {
        cardboard.draw(g, c * Game.CELL_SIZE, r * Game.CELL_SIZE);
        g.setColor(Color.red);

        int x1 = c * Game.CELL_SIZE + 5;
        int y1 = r * Game.CELL_SIZE + 5;
        int x2 = c * Game.CELL_SIZE + Game.CELL_SIZE - 5;
        int y2 = r * Game.CELL_SIZE + Game.CELL_SIZE - 5;

        ((Graphics2D) g).setStroke(fatStroke);
        g.drawLine(x1, y1, x2, y2);
        g.drawLine(x2, y1, x1, y2);
    }
}
