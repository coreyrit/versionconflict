package singlesort.ai;

import java.util.Comparator;

public class LeastComponents implements Comparator<Branch> {
    private Tree tree;
    public LeastComponents(Tree tree) {
        this.tree = tree;
    }

    public int compare(Branch a, Branch b) {
        return a.getGame().getHands().get(tree.getPlayer()).size() - b.getGame().getHands().get(tree.getPlayer()).size();
    }
}
