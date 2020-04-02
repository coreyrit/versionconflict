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
}
