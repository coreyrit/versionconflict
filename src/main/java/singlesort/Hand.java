package singlesort;

import singlesort.component.Cardboard;
import singlesort.component.Component;
import singlesort.component.Glass;
import singlesort.component.Plastic;
import versionconflict.Card;

import java.awt.*;
import java.util.ArrayList;

public class Hand extends ArrayList<Component> {
    private Selection selected;
    private Game game;

    public Hand(Game game) {
        this.selected = new Selection(game);
        this.game = game;
    }

    public boolean hasCardboardNotColor(int value, Color color) {
        for(Component comp : this) {
            if(comp instanceof Cardboard) {
                Cardboard cb = (Cardboard)comp;
                if(cb.getFace().getValue() == value && !cb.getColor().equals(color)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasPlasticNotColor(int value, Color color) {
        for(Component comp : this) {
            if(comp instanceof Plastic) {
                Plastic plastic = (Plastic)comp;
                if(plastic.getFace().getValue() == value && !plastic.getColor().equals(color)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasGlassNotColor(Color color) {
        for(Component comp : this) {
            if(comp instanceof Glass && !comp.getColor().equals(color)) {
                return true;
            }
        }
        return false;
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
