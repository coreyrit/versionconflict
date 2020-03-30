package singlesort;

import singlesort.component.Cardboard;
import singlesort.component.Component;
import singlesort.component.Plastic;

import java.util.ArrayList;

public class Hand extends ArrayList<Component> {
    private Selection selected = new Selection();

    public Selection getSelected() {
        return selected;
    }

    public void removeTrash() {
        for(int i = size()-1; i >= 0; i--) {
            if(get(i) instanceof Cardboard && !((Cardboard)get(i)).getFace().isClean()) {
                remove(i);
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
