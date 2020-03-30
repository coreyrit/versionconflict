package singlesort;

import castlepanic.base.cards.MaterialCard;
import singlesort.component.Cardboard;
import singlesort.component.Component;

import java.awt.*;
import java.util.HashSet;

public class Selection extends HashSet<Component> {
    public Color getColor() {
        return size() > 0 ? iterator().next().getColor() : Color.black;
    }

    public Component.Material getMaterial() {
        return size() > 0 ? iterator().next().getMaterial() : Component.Material.None;
    }

    public void updateSelection(Component component) {
        if(this.contains(component)) {
            this.remove(component);
//            if(size() == 0) {
//                Game.SingleSort.getHand().getSelected().clear();
//                Game.SingleSort.getTable().getSelected().clear();
//            }
        } else {
            if(!component.getMaterial().equals(getMaterial()) ||
                    (component.getMaterial() != Component.Material.Glass && !component.getColor().equals(getColor()))) {
                this.clear();
                if(this == Game.SingleSort.getHand().getSelected()) {
                    Game.SingleSort.getTable().getSelected().clear();
                }
            }
            this.add(component);
        }
    }

    public int getCardboardTotal() {
        int total = 0;
        for(Component component : this) {
            if(component instanceof Cardboard) {
                Cardboard cb = (Cardboard)component;
                total += cb.getFace().getValue();
            }
        }
        return total;
    }
}
