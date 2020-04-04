package singlesort.ai;

import java.util.Comparator;

public class MostGlass implements Comparator<Branch> {
    private Tree tree;
    public MostGlass(Tree tree) {
        this.tree = tree;
    }

    public int compare(Branch a, Branch b) {
        return a.getGame().getHands().get(tree.getPlayer()).size() - b.getGame().getHands().get(tree.getPlayer()).size();
    }
}
