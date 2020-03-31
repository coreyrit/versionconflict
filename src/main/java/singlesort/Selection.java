package singlesort;

import singlesort.component.Cardboard;
import singlesort.component.Component;

import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;

public class Selection extends HashSet<Component> {
    public Color getColor() {
        Iterator<Component> iter = iterator();
        Color color = size() > 0 ? iter.next().getColor() : Color.black;
        while(iter.hasNext()) {
            Component comp = iter.next();
            if(!comp.getColor().equals(color)) {
                color = Color.black;
            }
        }
        return color;
    }

    private Game game;

    public Selection(Game game) {
        this.game = game;
    }

    public Component.Material getMaterial() {
        return size() > 0 ? iterator().next().getMaterial() : Component.Material.None;
    }

    private void reset() {
        this.clear();
        if(this == game.getHand().getSelected()) {
            game.getTable().getSelected().clear();
        }
    }

    public void updateSelection(Component component) {
        if(this.contains(component)) {
            this.remove(component);
//            if(size() == 0) {
//                Game.SingleSort.getHand().getSelected().clear();
//                Game.SingleSort.getTable().getSelected().clear();
//            }
        } else {
            if(!component.getMaterial().equals(getMaterial())) {
                reset();
            }
            else if(component.getMaterial() != Component.Material.Glass && !component.getColor().equals(getColor())) {
                if(component.getMaterial() != Component.Material.Plastic || game.getGameState() != Game.State.RepairOrRepurpose) {
                    reset();
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
