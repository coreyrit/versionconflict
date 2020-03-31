package singlesort;

import singlesort.component.*;
import singlesort.component.Component;
import versionconflict.Card;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class Game { //extends JFrame implements MouseListener, MouseMotionListener {
    public static Random random = new Random();
    public static String VERSION = "0.1.6";

    public static final int ROWS = 9;
    public static final int COLUMNS = 15;
    public static final int CELL_SIZE = 100;

    public static int windowWidth = COLUMNS*CELL_SIZE+200;
    public static int windowHeight = ROWS*CELL_SIZE + 100;
//    private Insets insets;

    private Stack<Component> box;

    private Table table;
    private int turn;
    private List<Hand> hands;

    private State state;
    private List<Cardboard> take;
    private Cardboard lastSelected;

    private List<Cardboard> trashHeap;

    enum State {
        Take,
        Collect,
        Rot,
        RecycleOrReduce,
        RepairOrRepurpose,
        ReuseOrReturn,
        CleanUp,
        EndTurn
    }

    public Game(int players) {
        setup(players);

        PanelRenderer panelRenderer1 = new PanelRenderer(this);
//        this.setLayout(new BorderLayout());
//        this.add(panelRenderer1, BorderLayout.CENTER);
//        this.pack();

//        setTitle("Single-Sort");
//        setSize(windowWidth, windowHeight);
//        setResizable(false);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setVisible(true);

//        insets = getInsets();
//        setSize(insets.left + windowWidth + insets.right, insets.top + windowHeight + insets.bottom);

//        this.addMouseListener(this);
//        this.addMouseMotionListener(this);
    }

    public List<Component> getBox() {
        return box;
    }

    public Table getTable() {
        return table;
    }

    public Hand getHand() {
        return hands.get(turn);
    }

    public List<Hand> getHands() {
        return hands;
    }

    public State getGameState() {
        return state;
    }

    public List<Cardboard> getTake() {
        return take;
    }

    public Cardboard getLastSelected() {
        return lastSelected;
    }

    public void setLastSelected(Cardboard lastSelected) {
        this.lastSelected = lastSelected;
    }

    public List<Cardboard> getTrashHeap() {
        return trashHeap;
    }

    public boolean gameOver() {
        return getGameState() == Game.State.Take && getTake().size() == 0 &&
                turn == 0 && getTable().countFaceDownCardboard() < hands.size();
//        return getGameState() == State.RecycleOrReduce;
    }

    public void setup(int players) {
        box = new Stack<Component>();

        table = new Table(this);
        hands = new ArrayList<>();

        trashHeap = new ArrayList<>();

        turn = 0;
        for(int i = 0; i < players; i++) {
            hands.add(new Hand(this));
        }

        state = State.Take;
        take = new ArrayList<Cardboard>();
        lastSelected = null;

        int[] cleanCardboards = new int[] { 7, 6, 3, 2, 1, 1, 1 };
        int[] dirtyCardboards = new int[] { 3, 2, 1, 1, 1, 1 };
        for(int v = 1; v <= cleanCardboards.length; v++) {
            for(int i = 0; i < cleanCardboards[v-1]; i++) {
                box.add(Cardboard.createGreenCleanCardboard(this, v));
                box.add(Cardboard.createBlueCleanCardboard(this, v));
                box.add(Cardboard.createYellowCleanCardboard(this, v));
            }
        }
        for(int v = 1; v <= dirtyCardboards.length; v++) {
            for(int i = 0; i < dirtyCardboards[v-1]; i++) {
                box.add(Cardboard.createGreenDirtyCardboard(this, v));
                box.add(Cardboard.createBlueDirtyCardboard(this, v));
                box.add(Cardboard.createYellowDirtyCardboard(this, v));
            }
        }

        for(int i = 0; i < 10; i++) {
            box.add(Plastic.createGreenPlastic(this));
            box.add(Plastic.createBluePlastic(this));
            box.add(Plastic.createYellowPlastic(this));
        }

        for(int i = 0; i < 4; i++) {
            box.add(Glass.createGreenGlass(this));
            box.add(Glass.createBlueGlass(this));
            box.add(Glass.createYellowGlass(this));
        }

        box.add(Metal.createGoldMetal(this));
        box.add(Metal.createSilverMetal(this));
        box.add(Metal.createBronzeMetal(this));

        Collections.shuffle(box);

        int r = 0;
        int c = 0;
        while(box.size() > 0) {
            table.set(r, c, box.pop());
            c++;
            if(c >= COLUMNS) {
                r++;
                c = 0;
            }
        }

//        Cardboard cb = Cardboard.createGreenCleanCardboard(1);
//        cb.setFaceUp(false);
//        table.set(0, 0, cb);

        updateHighlights();
    }

    public boolean updateHighlights() {
        for(int r = 0; r < ROWS; r++) {
            for(int c = 0; c < COLUMNS; c++) {
                Component comp = table.get(r, c);
                if(comp != null) {
                    comp.setHighlight(false);
                    switch(state) {
                        case Take:
                            if(comp instanceof Cardboard) {
                                Cardboard cardboard = (Cardboard) comp;
                                cardboard.setHighlight(!cardboard.isFaceUp());
                            }
                            break;
                        case Rot:
                            if(comp instanceof Cardboard) {
                                Cardboard cb = (Cardboard) comp;
                                cb.setHighlight(!cb.getFace().isClean() && cb.getFace().getValue() == lastSelected.getFace().getValue() && cb.isFaceUp());
                            }
                            break;
                        case RecycleOrReduce:
                            if(table.getSelected().contains(comp)) {
                                comp.setHighlight(true);
                            } else if(comp instanceof Cardboard && getHand().getSelected().size() < 2) {
                                Cardboard cb = (Cardboard) comp;
                                if(cb.isFaceUp() && cb.getColor().equals(getHand().getSelected().getColor())) {
                                    if(table.getSelected().size() == 0) {
                                        cb.setHighlight( getHand().getSelected().getCardboardTotal() > cb.getFace().getValue());
                                    } else if(table.getSelected().size() == 1) {
                                        cb.setHighlight( getHand().getSelected().getCardboardTotal() == cb.getFace().getValue() + table.getSelected().getCardboardTotal());
                                    }
                                }

                            } else if(table.getSelected().size() == 0 && comp instanceof Plastic) {
                                Plastic pl = (Plastic)comp;
                                pl.setHighlight(pl.getColor().equals(getHand().getSelected().getColor()) &&
                                        getHand().getSelected().getCardboardTotal() == pl.getFace().getValue() &&
                                        pl.getFace().getValue() < 6);
                            }
                            break;
                        case RepairOrRepurpose:
                            if(getHand().getSelected().size() == 2 && comp instanceof Glass) {
                                Glass glass = (Glass)comp;
                                glass.setHighlight(glass.getColor().equals(getHand().getSelected().getColor()));
                            }
                            break;
                        case ReuseOrReturn:
                            if(getHand().getSelected().size() == 1 && comp instanceof Plastic) {
                                Plastic plastic = (Plastic)comp;
                                plastic.setHighlight(plastic.getColor().equals(getHand().getSelected().getColor()) && plastic.getFace().getValue() == 6);
                            }
                            else {
                                if(hands.size() >= 5) {
                                    comp.setHighlight(getHand().getSelected().size() == 2 && comp instanceof Metal);
                                } else {
                                    comp.setHighlight(getHand().getSelected().size() == 3 && comp instanceof Metal);
                                }
                            }
                            break;
                    }
                }
            }
        }
        for(Component comp : getHand()) {
            comp.setHighlight(false);
            switch(state) {
                case Collect:
                    comp.setHighlight(comp instanceof Cardboard && take.contains((Cardboard)comp));
                    break;
                case Rot:
                case RecycleOrReduce:
                    comp.setHighlight(!(comp instanceof Metal));
                    break;
                case RepairOrRepurpose:
                    if(comp instanceof Plastic) {
                        Plastic plastic = (Plastic)comp;
                        if(getHand().getSelected().contains(plastic)) {
                            plastic.setHighlight(true);
                        } else {
                            switch (hands.size()) {
                                case 1:
                                case 2:
                                    comp.setHighlight(getHand().getSelected().size() != 2 || !getHand().getSelected().getColor().equals(plastic.getColor()));
                                    break;
                                case 3:
                                case 4:
                                    comp.setHighlight(getHand().getSelected().size() <= 1);
                                    break;
                                case 5:
                                case 6:
                                    comp.setHighlight(getHand().getSelected().size() < 2 || getHand().getSelected().getColor().equals(Color.black));
                                    break;

                            }
                        }
                    } else {
                        comp.setHighlight(comp instanceof Glass);
                    }
                    break;
                case ReuseOrReturn:
                    comp.setHighlight(comp instanceof Glass);
                    break;
                case CleanUp:
                    comp.setHighlight(true);
                    break;
            }
        }
        for(int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                if (table.get(r, c) != null && table.get(r, c).isHighlight()) {
                    return true;
                }
            }
        }
        for(Component comp : getHand()) {
            if(comp.isHighlight()) {
                return true;
            }
        }
        return false;
    }

    private boolean lookForDirtyCardboard(int value) {
        for(int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                Component comp = table.get(r, c);
                if(comp instanceof Cardboard) {
                    Cardboard cb = (Cardboard)comp;
                    if(cb.isFaceUp() && cb.getFace().getValue() == value && cb.getFace().isClean()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void putAllInPile(Collection<Component> comps) {
        for(Component comp : comps) {
            putInPile(comp);
        }
    }
    private void putInPile(Component comp) {
        for(int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                if(table.get(r, c) == null) {
                    table.set(r, c, comp);
                    return;
                }
            }
        }
    }

    private boolean handSelect(Component component) {
       if(state != State.Collect && getHand().contains(component)) {
           if(state == State.CleanUp) {
               getHand().remove(component);
               if(getHand().sizeMinusPlastic4s() <= 10) {
                   endTurn();
               }
               return true;
           }
           // plastic is special.. double click will roll
           /*else if (state == State.RepairOrRepurpose &&
                   component instanceof Plastic &&
                   getHand().getSelected().size() == 1 &&
                   getHand().getSelected().contains(component)) {
               ((Plastic)component).roll();
               getHand().getSelected().clear();
               table.getSelected().clear();
               state = State.ReuseOrReturn;
               return true;
           } */
            else {
               getHand().getSelected().updateSelection(component);
               if (getHand().getSelected().isEmpty()) {
                   table.getSelected().clear();
               }

               if (component instanceof Cardboard) {
                   state = State.RecycleOrReduce;
                   return true;
               } else if (component instanceof Plastic) {
                   state = State.RepairOrRepurpose;
                   return true;
               } else if (component instanceof Glass) {
                   state = State.ReuseOrReturn;
                   return true;
               }
           }
       }
       return false;
    }

    private void endTurn() {
        table.getSelected().clear();
        getHand().getSelected().clear();

        getHand().removeTrash();

        if(getHand().sizeMinusPlastic4s() > 10) {
            state = State.CleanUp;
        } else {
            state = State.Take;
            turn++;
            if(turn >= hands.size()) {
                turn = 0;
            }
        }
    }

    private void swapSelections() {
        putAllInPile(getHand().getSelected());
        if(getHand().getSelected().getMaterial() == Component.Material.Plastic) {
            for(Component comp : getHand().getSelected()) {
                ((Plastic)comp).roll();
            }
        }
        getHand().removeAll(getHand().getSelected());
        table.removeAll(table.getSelected());
        getHand().addAll(table.getSelected());
        getHand().getSelected().clear();
        table.getSelected().clear();
    }

    public void mouseClicked(MouseEvent e) {
        mouseClicked(e.getX(), e.getY()-20);
    }
    public void mouseClicked(int x, int y) {
        if(x < 0 || y < 0) {
            return;
        }

        int r = y / CELL_SIZE;
        int c = x / CELL_SIZE;

        int index = -1;
        if(c == COLUMNS) {
            index = y / CELL_SIZE;
        } else if(c == COLUMNS+1) {
            index = (y / CELL_SIZE) + 10;
        }

        Component component = null;
        if(r < ROWS && c < COLUMNS) {
            component = table.get(r, c);
        }
        if(component == null && x > CELL_SIZE * COLUMNS && index >= 0 && index < getHand().size()) {
            component = getHand().get(index);
        }
        if(!gameOver() && component != null && component.isHighlight()) {
            if(!handSelect(component)) {
                switch (state) {
                    case Take:
                        getHand().add(component);
                        Cardboard takeCB = (Cardboard) component;
                        takeCB.flip();
                        table.remove(component);
                        take.add(takeCB);
                        if(hands.size() > 1) {
                            take.clear();
                            Cardboard collectCB = (Cardboard) component;
                            lastSelected = collectCB;
                            if (collectCB.getFace().isClean() && lookForDirtyCardboard(collectCB.getFace().getValue())) {
                                state = State.Rot;
                            } else {
                                state = State.RecycleOrReduce;
                            }
                        } else if(hands.size() == 1 && take.size() == 2) {
                            state = State.Collect;
                        }
                        break;
                    case Collect:
                        Cardboard collectCB = (Cardboard) component;
                        lastSelected = collectCB;
                        for(Cardboard cardboard : take) {
                            if(cardboard != collectCB) {
                                putInPile(cardboard);
                                getHand().remove(cardboard);
                            }
                        }
                        take.clear();
                        if (collectCB.getFace().isClean() && lookForDirtyCardboard(collectCB.getFace().getValue())) {
                            state = State.Rot;
                        } else {
                            state = State.RecycleOrReduce;
                        }
                        break;
                    case Rot:
                        if (!getHand().contains(component)) {
                            getHand().remove(lastSelected);
                            putInPile(lastSelected);
                            lastSelected = (Cardboard) component;
                            table.remove(component);
                            getHand().add(component);
                            getHand().getSelected().clear();
                            state = State.EndTurn;
//                            endTurn();
                        }
                        break;
                    case RecycleOrReduce:
                        if (!getHand().contains(component) && component instanceof Cardboard) {
                            Cardboard cb = (Cardboard) component;
                            table.getSelected().updateSelection(cb);

                            if (table.getSelected().size() == 2) {
                                swapSelections();
                                state = State.RepairOrRepurpose;
                            }
                        }

                        if (!getHand().contains(component) && component instanceof Plastic) {
                            table.getSelected().updateSelection(component);
                            swapSelections();
                            state = State.RepairOrRepurpose;
                        }
                        break;
                    case RepairOrRepurpose:
                        if (!getHand().contains(component) && component instanceof Glass) {
                            table.getSelected().updateSelection(component);
                            swapSelections();
                            state = State.ReuseOrReturn;
                        }
                        break;
                    case ReuseOrReturn:
                        if (!getHand().contains(component) && (component instanceof Metal || component instanceof Plastic)) {
                            table.getSelected().updateSelection(component);
                            swapSelections();
//                            endTurn();
                            state = State.EndTurn;
                        }
                        break;

                }
            }
        }

        Rectangle endTurnButton = new Rectangle(Game.COLUMNS*Game.CELL_SIZE - (2*Game.CELL_SIZE) + 10, Game.ROWS*Game.CELL_SIZE + 10, 2*Game.CELL_SIZE - 20, Game.CELL_SIZE - 20);
        if(gameOver() && endTurnButton.contains(x, y)) {
            setup(hands.size());
        } else if(endTurnButton.contains(x, y) && state == State.RepairOrRepurpose) {
            if(getHand().getSelected().size() == 0) {
                endTurn();
            } else if(getHand().getSelected().size() == 2 && !getHand().getSelected().getColor().equals(Color.black)) {
                endTurn();
            } else {
                for(Component comp : getHand().getSelected()) {
                    Plastic plastic = (Plastic)comp;
                    plastic.roll();
                }
                getHand().getSelected().clear();
                table.getSelected().clear();
                state = State.ReuseOrReturn;
            }
        } else if(endTurnButton.contains(x, y) && state != State.Take && state != State.Collect) {
            endTurn();
        }

        if(!updateHighlights()) {
            if(state == State.EndTurn && hands.size() == 1) {
                endTurn();
                // do it again after ending the turn
                updateHighlights();
            }
        }
//        this.repaint();
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseDragged(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {

    }

    public int getTurn() {
        return turn;
    }

    public int getScore() {
        int score = 0;
        Map<Integer, Integer> mapCardboard = new HashMap<Integer, Integer>();
        Set<Color> cardboardColors = new HashSet<Color>();
        int cardboardTotal = 0;
        Map<Color, Integer> mapGlass = new HashMap<Color, Integer>();
        Map<Color, Integer> mapColorCardboard = new HashMap<Color, Integer>();
        int maxGlass = 0;
        int plastic6count = 0;

        for(Component component : getHand()) {
            if(component instanceof Cardboard) {
                Cardboard cardboard = (Cardboard)component;
                cardboardColors.add(cardboard.getColor());

                if(cardboard.getFace().isClean()) {
                    int currentCount = 0;
                    if (mapCardboard.containsKey(cardboard.getFace().getValue())) {
                        currentCount = mapCardboard.get(cardboard.getFace().getValue());
                    }
                    mapCardboard.put(cardboard.getFace().getValue(), currentCount + 1);
                    cardboardTotal += cardboard.getFace().getValue();

                    int currentTotal = 0;
                    if(mapColorCardboard.containsKey(cardboard.getColor())) {
                        currentTotal = mapColorCardboard.get(cardboard.getColor());
                    }
                    mapColorCardboard.put(cardboard.getColor(), currentTotal + cardboard.getFace().getValue());
                }
            } else if(component instanceof Glass) {
                Glass glass = (Glass)component;
                if (mapGlass.containsKey(glass.getColor())) {
                    mapGlass.put(glass.getColor(), mapGlass.get(glass.getColor())+1);
                } else {
                    mapGlass.put(glass.getColor(), 1);
                }
                maxGlass = Math.max(maxGlass, mapGlass.get(glass.getColor()));
            }
        }
        for(int value : mapCardboard.keySet()) {
            score += value * Math.floor(mapCardboard.get(value) / 2.0);
        }

        boolean appliedMax = false;
        for(int glassCount : mapGlass.values()) {
            if(glassCount == maxGlass && !appliedMax) {
                score += 5 * glassCount;
                appliedMax = true;
            } else {
                score += 2 * glassCount;
            }
        }

        List<Integer> sixCounts = new ArrayList<>();
        for(int i = 0; i < hands.size(); i++) {
            int count = 0;
            for(Component component : hands.get(i)) {
                if(component instanceof Plastic && ((Plastic)component).getFace().getValue() == 6) {
                    count++;
                }
            }
            if(count > 0 && !sixCounts.contains(count)) {
                sixCounts.add(count);
            }
        }
        Collections.sort(sixCounts);
        Collections.reverse(sixCounts);


        for(Component component : getHand()) {
            if (component instanceof Plastic) {
                Plastic plastic = (Plastic)component;
                switch(plastic.getFace().getValue()) {
                    case 1:
                    case 2:
                        score += plastic.getFace().getValue();
                        break;
                    case 3:
//                        if(cardboardColors.size() == 3) {
//                        if(cardboardTotal >= 3) {
                        if(mapColorCardboard.containsKey(plastic.getColor()) && mapColorCardboard.get(plastic.getColor()) >= 3) {
                            score += 3;
                        }
                        break;
                    case 5:
//                        if(cardboardTotal == 5) {
//                        if(cardboardTotal >= 5) {
                        if(mapColorCardboard.containsKey(plastic.getColor()) && mapColorCardboard.get(plastic.getColor()) >= 5) {
                            score += 5;
                        }
                        break;
                    case 6:
                        plastic6count++;
                        break;

                }
            } else if(component instanceof Metal) {
                Metal metal = (Metal)component;
                switch (metal.getType()) {
                    case Gold:
                        score += 14;
                        break;
                    case Silver:
                        score += 12;
                        break;
                    case Bronze:
                        score += 10;
                        break;
                }
            }
        }

        if(hands.size() == 1) {
            if (plastic6count == 1) {
                score += 4;
            } else if (plastic6count == 2) {
                score += 10;
            } else if (plastic6count >= 3) {
                score += 18;
            }
        } else {
            if(sixCounts.size() > 0 && plastic6count == sixCounts.get(0)) {
                score += (14-hands.size());
            } else if (sixCounts.size() > 1 && plastic6count == sixCounts.get(1)) {
                score += 6;
            }
        }

        // penalties
//        for(int r = 0; r < ROWS; r++) {
//            for (int c = 0; c < COLUMNS; c++) {
//                Component component = table.get(r, c);
//                if(component != null && component.getMaterial() == Component.Material.Cardboad) {
//                    Cardboard cb = (Cardboard)component;
//                    if(hands.size() == 1 && cb.isFaceUp() && !cb.getFace().isClean() && cb.getFace().getValue() == 3) {
//                        score -= 3;
//                    }
//                }
//            }
//        }
        for(Cardboard dirtyCardboard : trashHeap) {
            if(hands.size() == 1 && dirtyCardboard.getFace().getValue() == 3) {
                score += 3;
            }
        }

        return score;
    }
}
