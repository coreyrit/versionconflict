package singlesort.ai;

import singlesort.component.Component;

import java.util.Comparator;

public class MostOfMaterial implements Comparator<Branch> {
    private Tree tree;
    private Component.Material material;

    public MostOfMaterial(Tree tree, Component.Material material) {
        this.tree = tree;
        this.material = material;
    }

    public int compare(Branch a, Branch b) {
        return b.getGame().getHands().get(tree.getPlayer()).countMaterial(material) - a.getGame().getHands().get(tree.getPlayer()).countMaterial(material);
    }
}
