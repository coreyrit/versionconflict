package singlesort.component;

import java.awt.*;

public class Goal {

    private Cardboard[] targets;

    public Goal(Cardboard ... targets) {
        this.targets = targets;
    }

    public Cardboard[] getTargets() {
        return targets;
    }

    public boolean contains(Color color, int value) {
        for(Cardboard cardboard : targets) {
            if(cardboard.getColor().equals(color) && cardboard.getFace().getValue() == value) {
                return true;
            }
        }
        return false;
    }
}
