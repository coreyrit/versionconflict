package singlesort.ai;

import singlesort.Game;
import singlesort.component.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Branch {

    private java.awt.Point point;
    private Branch parent;
    private Game game;
    private int depth;

    public Branch(java.awt.Point point, Branch parent, Game game, int depth) {
        this.point = point;
        this.parent = parent;
        this.game = game;
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }

    public Game getGame() {
        return this.game;
    }

    public Branch getParent() {
        return this.parent;
    }

    public java.awt.Point getPoint() {
        return point;
    }

    public List<Branch> think() {
        List<Branch> children = new ArrayList<>();
        Game clone = game.deepClone();
        Rectangle endTurnButton = game.getButtonBounds();

        if(game.inRepair()) {
            // repair is a valid click
            int x = endTurnButton.x + 10;
            int y = endTurnButton.y + 10;
            children.add(new Branch(new java.awt.Point(x, y), this, game.deepClone().mouseClicked(x, y), depth+1));
        }

        for(int r = 0; r < Game.ROWS + 1; r++) {
            for(int c = 0; c < Game.COLUMNS + 2; c++) {
                int y = r * Game.CELL_SIZE + 50;
                int x = c * Game.CELL_SIZE + 50;
                boolean take = clone.getGameState() == Game.State.Take;
                Component component = clone.getComponentAt(x, y);
                if(component != null && component.isHighlight() &&
                        // never deselect
                        !clone.getHand().getSelected().contains(component) &&
                        !clone.getTable().getSelected().contains(component)) {
                    children.add(new Branch(new java.awt.Point(x, y), this, clone.mouseClicked(x, y), depth+1));
                    clone = game.deepClone();

                    if (take) {
                        // don't waste time looking for more
                        r = 1000;
                        c = 1000;
                    }
                }
            }
        }

        if(game.getGameState() != Game.State.Take && game.getGameState() != Game.State.Collect && game.getGameState() != Game.State.CleanUp) {
            // can end turn when you want
            int x = endTurnButton.x + 10;
            int y = endTurnButton.y + 10;
            children.add(new Branch(new java.awt.Point(x, y), this, game.deepClone().mouseClicked(x, y), depth+1));
        }

//        for(Branch branch : children) {
//            branch.think(depth+1, startTime);
//        }
        return children;
    }
}
