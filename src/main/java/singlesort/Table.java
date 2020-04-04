package singlesort;

import singlesort.component.Cardboard;
import singlesort.component.Component;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;

public class Table implements Serializable {
    private Component[][] table = new Component[Game.ROWS][Game.COLUMNS];
    private Selection selection;

    private Map<Cardboard, Point> trashMap;
    private List<Cardboard> trashHeap;

    public Table(Game game) {
        this.selection = new Selection(game);
        this.trashMap = new HashMap<Cardboard, Point>();
        this.trashHeap = new ArrayList<>();
    }

    public Component get(int r, int c) {
        return table[r][c];
    }

    public Cardboard[] getTrashHeap() {
        return trashHeap.toArray(new Cardboard[0]);
    }

    public Map<Cardboard, Point> getTrashMap() {
        return trashMap;
    }

    public void set(int r, int c, Component component) {
        table[r][c] = component;
    }

    public void removeAll(Collection<Component> comps) {
        for(Component comp : comps) {
            remove(comp);
        }
    }

    public int countFaceDownCardboard() {
        int count = 0;
        for(int r = 0; r < Game.ROWS; r++) {
            for(int c = 0; c < Game.COLUMNS; c++) {
                if(table[r][c] != null && table[r][c] instanceof Cardboard && !((Cardboard)table[r][c]).isFaceUp()) {
                    count++;
                }
            }
        }
        return count;
    }

    public void remove(Component comp) {
        for(int r = 0; r < Game.ROWS; r++) {
            for(int c = 0; c < Game.COLUMNS; c++) {
                if(table[r][c] == comp) {
                    table[r][c] = null;
                }
            }
        }
    }

    public Selection getSelected() {
        return selection;
    }

    private Point findFirstNull() {
        for(int r = 0; r < Game.ROWS; r++) {
            for (int c = 0; c < Game.COLUMNS; c++) {
                Point pt = new Point(c, r);
                if(this.get(r, c) == null && !trashMap.values().contains(pt)) {
                    return pt;
                }
            }
        }
        throw new RuntimeException("No empty spaces!");
    }

    public void putInPile(Component comp) {
        Point pt = findFirstNull();
        this.set(pt.y, pt.x, comp);
    }

    public void addTrash(Cardboard cardboard) {
        cardboard.setHighlight(false);
        trashHeap.add(cardboard);
        Point pt = findFirstNull();
        trashMap.put(cardboard, pt);
    }
}
