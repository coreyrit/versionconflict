package singlesort;

import singlesort.component.Cardboard;
import singlesort.component.Component;
import singlesort.component.Plastic;

import java.util.ArrayList;

public class Hand extends ArrayList<Component> {
    private Selection selected;
    private Game game;

    public Hand(Game game) {
        this.selected = new Selection(game);
        this.game = game;
    }

    public Selection getSelected() {
        return selected;
    }

    public void removeTrash() {
        for(int i = size()-1; i >= 0; i--) {
            if(get(i) instanceof Cardboard && !((Cardboard)get(i)).getFace().isClean()) {
                Cardboard dirtyCardboard = (Cardboard)get(i);
                remove(dirtyCardboard);
                game.getTrashHeap().add(dirtyCardboard);
            }
        }
    }

    public int sizeMinusPlastic4s() {
        int count = 0;
        for(Component component : this) {
            if(!(component instanceof Plastic) || ((Plastic)component).getFace().getValue() != 4) {
                count++;
            }
        }
        return count;
    }
}
