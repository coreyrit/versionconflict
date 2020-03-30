package singlesort;

import singlesort.component.Component;

import java.util.Collection;

public class Table {
    private Component[][] table = new Component[Game.ROWS][Game.COLUMNS];
    private Selection selection = new Selection();

    public Component get(int r, int c) {
        return table[r][c];
    }

    public void set(int r, int c, Component component) {
        table[r][c] = component;
    }

    public void removeAll(Collection<Component> comps) {
        for(Component comp : comps) {
            remove(comp);
        }
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
}
