package singlesort;

import singlesort.component.*;
import singlesort.component.Component;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class Game { //extends JFrame implements MouseListener, MouseMotionListener {
    public static Random random = new Random();

    public static final int ROWS = 9;
    public static final int COLUMNS = 15;
    public static final int CELL_SIZE = 100;

    public static int windowWidth = COLUMNS*CELL_SIZE+200;
    public static int windowHeight = ROWS*CELL_SIZE + 100;
//    private Insets insets;

    private Stack<Component> box;

    private Table table;
    private Hand hand;

    private State state;
    private List<Cardboard> take;
    private Cardboard lastSelected;

    enum State {
        Take,
        Collect,
        Rot,
        RecycleOrReduce,
        RepairOrRepurpose,
        ReuseOrReturn,
        CleanUp
    }

    public Game() {
        setup();

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
        return hand;
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

    public boolean gameOver() {
        return getGameState() == Game.State.Take && getTake().size() == 0 && getTable().countFaceDownCardboard() < 2;
//        return getGameState() == State.RecycleOrReduce;
    }

    public void setup() {
        box = new Stack<Component>();

        table = new Table(this);
        hand = new Hand(this);

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
                            } else if(comp instanceof Cardboard && hand.getSelected().size() < 2) {
                                Cardboard cb = (Cardboard) comp;
                                if(cb.isFaceUp() && cb.getColor().equals(hand.getSelected().getColor())) {
                                    if(table.getSelected().size() == 0) {
                                        cb.setHighlight( hand.getSelected().getCardboardTotal() > cb.getFace().getValue());
                                    } else if(table.getSelected().size() == 1) {
                                        cb.setHighlight( hand.getSelected().getCardboardTotal() == cb.getFace().getValue() + table.getSelected().getCardboardTotal());
                                    }
                                }

                            } else if(table.getSelected().size() == 0 && comp instanceof Plastic) {
                                Plastic pl = (Plastic)comp;
                                pl.setHighlight(pl.getColor().equals(hand.getSelected().getColor()) &&
                                        hand.getSelected().getCardboardTotal() == pl.getFace().getValue() &&
                                        pl.getFace().getValue() < 6);
                            }
                            break;
                        case RepairOrRepurpose:
                            if(hand.getSelected().size() == 2 && comp instanceof Glass) {
                                Glass glass = (Glass)comp;
                                glass.setHighlight(glass.getColor().equals(hand.getSelected().getColor()));
                            }
                            break;
                        case ReuseOrReturn:
                            if(hand.getSelected().size() == 1 && comp instanceof Plastic) {
                                Plastic plastic = (Plastic)comp;
                                plastic.setHighlight(plastic.getColor().equals(hand.getSelected().getColor()) && plastic.getFace().getValue() == 6);
                            }
                            else {
                                comp.setHighlight(hand.getSelected().size() == 3 && comp instanceof Metal);
                            }
                            break;
                    }
                }
            }
        }
        for(Component comp : hand) {
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
                        comp.setHighlight(hand.getSelected().contains(plastic) ||
                                hand.getSelected().size() != 2 ||
                                !hand.getSelected().getColor().equals(plastic.getColor()));
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
        for(Component comp : hand) {
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
       if(state != State.Collect && hand.contains(component)) {
           if(state == State.CleanUp) {
               hand.remove(component);
               if(hand.sizeMinusPlastic4s() <= 10) {
                   endTurn();
               }
               return true;
           }
           // plastic is special.. double click will roll
           else if (state == State.RepairOrRepurpose &&
                   component instanceof Plastic &&
                   hand.getSelected().size() == 1 &&
                   hand.getSelected().contains(component)) {
               ((Plastic)component).roll();
               hand.getSelected().clear();
               table.getSelected().clear();
               state = State.ReuseOrReturn;
               return true;
           } else {
               hand.getSelected().updateSelection(component);
               if (hand.getSelected().isEmpty()) {
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
        hand.getSelected().clear();

        hand.removeTrash();

        if(hand.sizeMinusPlastic4s() > 10) {
            state = State.CleanUp;
        } else {
            state = State.Take;
        }
    }

    private void swapSelections() {
        putAllInPile(hand.getSelected());
        if(hand.getSelected().getMaterial() == Component.Material.Plastic) {
            for(Component comp : hand.getSelected()) {
                ((Plastic)comp).roll();
            }
        }
        hand.removeAll(hand.getSelected());
        table.removeAll(table.getSelected());
        hand.addAll(table.getSelected());
        hand.getSelected().clear();
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
        if(component == null && x > CELL_SIZE * COLUMNS && index >= 0 && index < hand.size()) {
            component = hand.get(index);
        }
        if(!gameOver() && component != null && component.isHighlight()) {
            if(!handSelect(component)) {
                switch (state) {
                    case Take:
                        hand.add(component);
                        Cardboard takeCB = (Cardboard) component;
                        takeCB.flip();
                        table.remove(component);
                        take.add(takeCB);
                        if(take.size() == 2) {
                            state = State.Collect;
                        }
                        break;
                    case Collect:
                        Cardboard collectCB = (Cardboard) component;
                        lastSelected = collectCB;
                        for(Cardboard cardboard : take) {
                            if(cardboard != collectCB) {
                                putInPile(cardboard);
                                hand.remove(cardboard);
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
                        if (!hand.contains(component)) {
                            hand.remove(lastSelected);
                            putInPile(lastSelected);
                            lastSelected = (Cardboard) component;
                            table.remove(component);
                            hand.add(component);
                            hand.getSelected().clear();
                            state = State.RecycleOrReduce;
                            endTurn();
                        }
                        break;
                    case RecycleOrReduce:
                        if (!hand.contains(component) && component instanceof Cardboard) {
                            Cardboard cb = (Cardboard) component;
                            table.getSelected().updateSelection(cb);

                            if (table.getSelected().size() == 2) {
                                swapSelections();
                                state = State.RepairOrRepurpose;
                            }
                        }

                        if (!hand.contains(component) && component instanceof Plastic) {
                            table.getSelected().updateSelection(component);
                            swapSelections();
                            state = State.RepairOrRepurpose;
                        }
                        break;
                    case RepairOrRepurpose:
                        if (!hand.contains(component) && component instanceof Glass) {
                            table.getSelected().updateSelection(component);
                            swapSelections();
                            state = State.ReuseOrReturn;
                        }
                        break;
                    case ReuseOrReturn:
                        if (!hand.contains(component) && (component instanceof Metal || component instanceof Plastic)) {
                            table.getSelected().updateSelection(component);
                            swapSelections();
                            endTurn();
                        }
                        break;

                }
            }
        }

        Rectangle endTurnButton = new Rectangle(Game.COLUMNS*Game.CELL_SIZE - (2*Game.CELL_SIZE) + 10, Game.ROWS*Game.CELL_SIZE + 10, 2*Game.CELL_SIZE - 20, Game.CELL_SIZE - 20);
        if(gameOver() && endTurnButton.contains(x, y)) {
            setup();
        } else if(endTurnButton.contains(x, y) && state != State.Take && state != State.Collect) {
            endTurn();
        }

        if(!updateHighlights()) {
            endTurn();
            // do it again after ending the turn
            updateHighlights();
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

    public int getScore() {
        int score = 0;
        Map<Integer, Integer> mapCardboard = new HashMap<Integer, Integer>();
        Set<Color> cardboardColors = new HashSet<Color>();
        int cardboardTotal = 0;
        Map<Color, Integer> mapGlass = new HashMap<Color, Integer>();
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
                        if(cardboardTotal >= 3) {
                            score += 3;
                        }
                        break;
                    case 5:
//                        if(cardboardTotal == 5) {
                        if(cardboardTotal >= 5) {
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

        if(plastic6count == 1) {
            score += 4;
        } else if(plastic6count == 2) {
            score += 10;
        } else if(plastic6count >= 3) {
            score += 18;
        }

        for(int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                Component component = table.get(r, c);
                if(component != null && component.getMaterial() == Component.Material.Cardboad) {
                    Cardboard cb = (Cardboard)component;
                    if(cb.isFaceUp() && !cb.getFace().isClean() && cb.getFace().getValue() == 3) {
                        score -= 3;
                    }
                }
            }
        }

        return score;
    }
}
