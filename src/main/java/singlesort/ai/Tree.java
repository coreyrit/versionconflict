package singlesort.ai;

import singlesort.Game;
import singlesort.component.Component;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Tree {
    private static final int MAX_DEPTH = 100;
    private static final int MAX_TIME = 2000;

    private int player;
    private Game game;
    private Branch trunk;

    public Tree(int player, Game game) {
        this.player = player;
        this.game = game;
        this.trunk = new Branch(null, trunk, game.deepClone(), 0);
    }

    public int getPlayer() {
        return player;
    }

    private void trace(Branch branch, Stack<Branch> stack) {
        stack.add(branch);
        if(branch.getParent() != null && branch.getParent().getParent() != null) {
            trace(branch.getParent(), stack);
        }
    }

    private boolean isLeaf(Branch branch) {
        return !branch.getGame().hasHighlightsExcludingSelections() ||
                //branch.getGame().getGameState() == Game.State.Take ||
                branch.getGame().getTurn() != player;
    }

    private List<Branch> compute() {
        List<Branch> leaves = new ArrayList<>();
        List<Branch> bfs = new ArrayList<>();

        bfs.add(trunk);

        long startTime = new Date().getTime();
        int maxDepth = 0;
        while(bfs.size() > 0 && new Date().getTime()-startTime < MAX_TIME && bfs.get(0).getDepth() < MAX_DEPTH) {
            Branch branch = bfs.remove(0);
            maxDepth = Math.max(maxDepth, branch.getDepth());
            if(isLeaf(branch)) {
                leaves.add(branch);
            } else {
                List<Branch> next = branch.think();

                // random pruning (deeper you go, do more pruning)
                while(next.size() > Math.max(0, MAX_DEPTH - branch.getDepth())) {
                    leaves.add(next.remove(Game.random.nextInt(next.size())));
                }
                bfs.addAll(next);
            }
        }
        System.out.println(maxDepth);

        // what's left in the queue are the "leaves"
        leaves.addAll(bfs);
        return leaves;
    }

    public void performTurn() {
        List<Branch> leaves = compute();

        int maxScore = -1;
        Map<Integer, List<Branch>> mapBranches = new HashMap<>();


        // find the best path
        for(Branch branch : leaves) {
            int score = branch.getGame().getScore(player);

            // bump score by metal
//            score += branch.getGame().getHands().get(player).countMaterial(Component.Material.Glass) * 20;
//            score += branch.getGame().getHands().get(player).countMaterial(Component.Material.Metal) * 100;

            if(score > maxScore) {
                maxScore = score;
            }
            if(!mapBranches.containsKey(score)) {
                mapBranches.put(score, new ArrayList<>());
            }
            mapBranches.get(score).add(branch);
        }

        // walk back up
        if(maxScore >= 0) {
            List<Branch> bestBranches = mapBranches.get(maxScore);
            bestBranches.sort(new LeastComponents(this));

            Stack<Branch> stack = new Stack<>();
            if(bestBranches.size() > 0) {
                trace(bestBranches.get(0), stack);
            }

            boolean progress = false;
            while(stack.size() > 0 && (stack.peek().getGame().getGameState() != Game.State.Take || !progress)) {
                Point point = stack.pop().getPoint();
                if (point != null) {
                    game.mouseClicked(point.x, point.y);
                    progress = true;
                }
            }
            if(progress) {
                return;
            }
        }

        // end my turn
        Rectangle endTurnButton = game.getButtonBounds();
        game.mouseClicked(endTurnButton.x + 10, endTurnButton.y + 10);
    }
}
