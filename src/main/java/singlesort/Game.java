package singlesort;

import singlesort.ai.Tree;
import singlesort.component.*;
import singlesort.component.Component;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class Game implements Cloneable, Serializable {
    public static Random random = new Random();
    public static String VERSION = "0.4.0";

    public static final int ROWS = 9;
    public static final int COLUMNS = 15;
    public static final int CELL_SIZE = 100;

    public static int windowWidth = COLUMNS*CELL_SIZE+200;
    public static int windowHeight = ROWS*CELL_SIZE + 100;

    private Table table;
    private int turn;
    private List<Hand> hands;

    private State state;
    private List<Cardboard> take;
    private Component rotSelection;

    private int time = 0;

    public enum State {
        Take,
        Collect,
        Rot,
        RotSwap,
        RecycleOrReduce,
        RepairOrRepurpose,
        ReuseOrReturn,
        CleanUp,
        EndTurn
    }

    public Game(int players) {
        setup(Math.max(1, Math.min(players, 6)));
    }

    public Game clone() {
        try {
            return (Game) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public Game deepClone() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Game) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

    public boolean gameOver() {
//        return getHand().size() > 2;
        if(getGameState() == Game.State.Take) {
            if (hands.size() == 1) {
                return getTake().size() == 0 && turn == 0 && getTable().countFaceDownCardboard() < 2;
            } else {
//                return getTake().size() == 0 && turn == 0 && getTable().countFaceDownCardboard() < hands.size();
                return getTable().countFaceDownCardboard() == 0;
//                return getTake().size() == 0 && getTable().countFaceDownCardboard() < 2;
            }
        } else {
            return false;
        }
//        return getGameState() == State.RecycleOrReduce;
    }

    public void setup(int players) {
        time = 0;
        Stack<Component> box = new Stack<Component>();

        table = new Table(this);
        hands = new ArrayList<>();

        Stack<Goal> goals = new Stack<Goal>();
        if(players == 1) {
            goals.add(new Goal(
                    Cardboard.createYellowDirtyCardboard(this, 3),
                    Cardboard.createBlueDirtyCardboard(this, 3),
                    Cardboard.createGreenDirtyCardboard(this, 3)
            ));
        } else {
            goals.add(new Goal(Cardboard.createYellowDirtyCardboard(this, 1), Cardboard.createYellowDirtyCardboard(this, 2)));
            goals.add(new Goal(Cardboard.createYellowDirtyCardboard(this, 4), Cardboard.createYellowDirtyCardboard(this, 5)));
            goals.add(new Goal(Cardboard.createBlueDirtyCardboard(this, 1), Cardboard.createBlueDirtyCardboard(this, 2)));
            goals.add(new Goal(Cardboard.createBlueDirtyCardboard(this, 4), Cardboard.createBlueDirtyCardboard(this, 5)));
            goals.add(new Goal(Cardboard.createGreenDirtyCardboard(this, 1), Cardboard.createGreenDirtyCardboard(this, 2)));
            goals.add(new Goal(Cardboard.createGreenDirtyCardboard(this, 4), Cardboard.createGreenDirtyCardboard(this, 5)));
        }

        Collections.shuffle(goals);

        turn = 0;
        for(int i = 0; i < players; i++) {
            hands.add(new Hand(this, goals.pop()));
        }

        state = State.Take;
        take = new ArrayList<Cardboard>();
        rotSelection = null;

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

        updateHighlights();
    }

    private boolean hasHighlightsExcludingSelections= false;

    public boolean updateHighlights() {
        Set<MatchedComponent> matches = new HashSet<MatchedComponent>();

        for(int r = 0; r < ROWS; r++) {
            for(int c = 0; c < COLUMNS; c++) {
                Component comp = table.get(r, c);
                if(comp != null) {
                    MatchedComponent mc = new MatchedComponent(comp);

                    comp.setHighlight(false);
                    switch(state) {
                        case Take:
                            if(comp instanceof Cardboard) {
                                Cardboard cardboard = (Cardboard) comp;
                                cardboard.setHighlight(!cardboard.isFaceUp());
                            }
                            break;
                        case Rot:
                            if(!matches.contains(mc)) {
                                if (comp instanceof Cardboard) {
                                    Cardboard cb = (Cardboard) comp;
                                    cb.setHighlight(cb.isFaceUp() && getHand().hasCardboardNotColor(cb.getFace().getValue(), cb.getColor()));
                                } else if (comp instanceof Plastic) {
                                    Plastic plastic = (Plastic) comp;
                                    plastic.setHighlight(getHand().hasPlasticNotColor(plastic.getFace().getValue(), plastic.getColor()));
                                } else if (comp instanceof Glass) {
                                    comp.setHighlight(getHand().hasGlassNotColor(comp.getColor()));
                                }
                            }
                            if(comp.isHighlight() && ai && !getTable().getSelected().contains(comp)) {
                                matches.add(mc);
                            }
                            break;
                        case RotSwap:
                            comp.setHighlight(comp == rotSelection);
                            break;
                        case RecycleOrReduce:
                            if(table.getSelected().contains(comp)) {
                                comp.setHighlight(true);
                            } else if(comp instanceof Cardboard && getHand().getSelected().size() < 2 && !matches.contains(mc)) {
                                Cardboard cb = (Cardboard) comp;
                                if(cb.isFaceUp() && cb.getColor().equals(getHand().getSelected().getColor())) {
                                    if(table.getSelected().size() == 0) {
                                        cb.setHighlight( getHand().getSelected().getCardboardTotal() >= cb.getFace().getValue());
                                    } else if(table.getSelected().size() == 1) {
                                        cb.setHighlight( getHand().getSelected().getCardboardTotal() >= cb.getFace().getValue() + table.getSelected().getCardboardTotal());
                                    } else if(table.getSelected().size() == 2) {
                                        cb.setHighlight( getHand().getSelected().getCardboardTotal() == cb.getFace().getValue() + table.getSelected().getCardboardTotal());
                                    }
                                }

                                if(cb.isHighlight() && ai && !getTable().getSelected().contains(comp)) {
                                    matches.add(mc);
                                }
                            } else if(table.getSelected().size() == 0 && comp instanceof Plastic && !matches.contains(mc)) {
                                Plastic pl = (Plastic)comp;
                                pl.setHighlight(pl.getColor().equals(getHand().getSelected().getColor()) &&
                                        getHand().getSelected().getCardboardTotal() == pl.getFace().getValue() &&
                                        pl.getFace().getValue() < 6);

                                if(pl.isHighlight() && ai && !getTable().getSelected().contains(comp)) {
                                    matches.add(mc);
                                }
                            }
                            break;
                        case RepairOrRepurpose:
                            if(getHand().getSelected().size() == 2 && comp instanceof Glass && !matches.contains(mc)) {
                                Glass glass = (Glass)comp;
                                glass.setHighlight(glass.getColor().equals(getHand().getSelected().getColor()));
                                if(glass.isHighlight() && ai && !getTable().getSelected().contains(comp)) {
                                    matches.add(mc);
                                }
                            }
                            break;
                        case ReuseOrReturn:
                            if(getHand().getSelected().size() == 1 && comp instanceof Plastic) {
                                Plastic plastic = (Plastic)comp;
                                plastic.setHighlight(plastic.getColor().equals(getHand().getSelected().getColor()) && plastic.getFace().getValue() == 6);
                            }
                            else {
                                if(hands.size() >= 4) {
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

        Set<MatchedComponent> matchedComponents = new HashSet<>();
        for(Component comp : getHand()) {
            MatchedComponent mc = new MatchedComponent(comp);
            comp.setHighlight(false);

            if(!matchedComponents.contains(mc)) {
                switch(state) {
                    case Collect:
                        comp.setHighlight(comp instanceof Cardboard && take.contains((Cardboard) comp));
                        break;
                    case Rot:
                    case RecycleOrReduce:
                        if (getHand().getSelected().contains(comp)) {
                            comp.setHighlight(true);
                        } else if (getHand().getSelected().size() == 0) {
                            comp.setHighlight(!(comp instanceof Metal));
                        } else {
                            comp.setHighlight(comp instanceof Cardboard && comp.getColor().equals(getHand().getSelected().getColor()));
                        }
                        break;
                    case RotSwap:
                        if (rotSelection.getMaterial().equals(comp.getMaterial())) {
                            switch (rotSelection.getMaterial()) {
                                case Cardboad:
                                    Cardboard rotCb = (Cardboard) rotSelection;
                                    Cardboard handCb = (Cardboard) comp;
                                    comp.setHighlight(!rotCb.getColor().equals(handCb.getColor()) && rotCb.getFace().getValue() == handCb.getFace().getValue());
                                    break;
                                case Plastic:
                                    Plastic rotPlastic = (Plastic) rotSelection;
                                    Plastic handPlastic = (Plastic) comp;
                                    comp.setHighlight(!rotPlastic.getColor().equals(handPlastic.getColor()) && rotPlastic.getFace().getValue() == handPlastic.getFace().getValue());
                                    break;
                                case Glass:
                                    comp.setHighlight(!rotSelection.getColor().equals(comp.getColor()));
                                    break;
                            }
                        }
                        break;
                    case RepairOrRepurpose:
                        if (comp instanceof Plastic) {
                            Plastic plastic = (Plastic) comp;
                            if (getHand().getSelected().contains(plastic)) {
                                plastic.setHighlight(true);
                            } else {
                                switch (hands.size()) {
                                    case 1:
                                    case 2:
                                        comp.setHighlight(getHand().getSelected().size() == 0 || comp.getColor().equals(getHand().getSelected().getColor()));
                                        break;
                                    case 3:
                                    case 4:
                                        comp.setHighlight(getHand().getSelected().size() <= 1);
                                        break;
                                    case 5:
                                    case 6:
                                        comp.setHighlight(getHand().getSelected().size() <= 1 || !comp.getColor().equals(getHand().getSelected().getColor()));
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
                if(comp.isHighlight() && !getHand().getSelected().contains(comp) && ai) {
                    matchedComponents.add(mc);
                }
            }
        }
        return hasHighlights();
    }

    public boolean inRepair() {
        return this.getGameState() == Game.State.RepairOrRepurpose &&
                (this.getHand().getSelected().size() == 1 || (this.getHand().getSelected().size() > 1 && this.getHand().getSelected().getColor().equals(Color.black)));
    }

    public boolean hasHighlightsExcludingSelections() {
        for(int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                Component comp = table.get(r, c);
                if (comp != null && comp.isHighlight() && !table.getSelected().contains(comp)) {
                    return true;
                }
            }
        }
        for(Component comp : getHand()) {
            if(comp.isHighlight() && !getHand().getSelected().contains(comp)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasHighlights() {
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

    private void putAllInPile(Collection<Component> comps) {
        for(Component comp : comps) {
            table.putInPile(comp);
        }
    }

    private int HAND_SIZE = 10;

    private boolean handSelect(Component component) {
       if(state != State.Collect && state != State.RotSwap && getHand().contains(component)) {
           if(state == State.CleanUp) {
               getHand().remove(component);
               if(getHand().sizeMinusPlastic4s() <= HAND_SIZE) {
                   endTurn();
               }
               return true;
           } else {
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

        if(getHand().sizeMinusPlastic4s() > HAND_SIZE) {
            state = State.CleanUp;
        } else {
            hands.get(turn).turns++;

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

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return this.time;
    }

    private static boolean ai = false;

    public Component getComponentAt(int x, int y) {
        if(x < 0 || y < 0) {
            return null;
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
        return component;
    }

    public Game mouseClicked(int x, int y) {


        if(!ai && getGameState() != State.Take && turn != 0) {
            // lets try the AI
            ai = true;
            Tree tree = new Tree(turn, this);
            tree.performTurn();
            ai = false;
            return this;
        }


        Component component = getComponentAt(x, y);
        if(!gameOver() && component != null && component.isHighlight()) {
            if(!handSelect(component)) {
                switch (state) {
                    case Take:
                        getHand().add(component);
                        Cardboard takeCB = (Cardboard) component;
                        takeCB.flip();
                        table.remove(component);
                        take.add(takeCB);
                        if(hands.size() > 1 && takeCB.getFace().isClean()) {
//                        if(hands.size() > 1 && take.size() == 2) {
                            take.clear();
                            state = State.Rot;
                        } else if(hands.size() == 1 && take.size() == 2) {
                            state = State.Collect;
                        }
                        break;
                    case Collect:
                        Cardboard collectCB = (Cardboard) component;
                        for(Cardboard cardboard : take) {
                            if(cardboard != collectCB) {
                                table.putInPile(cardboard);
                                getHand().remove(cardboard);
                            }
                        }
                        take.clear();
                        state = State.Rot;
                        break;
                    case Rot:
                        if (!getHand().contains(component)) {
                            rotSelection = component;
                            table.getSelected().updateSelection(rotSelection);
                            state = State.RotSwap;
                        }
                        break;
                    case RotSwap:
                        if(getHand().contains(component)) {
                            getHand().remove(component);
                            table.putInPile(component);
                            getHand().add(rotSelection);
                            table.remove(rotSelection);
                            rotSelection = null;
                            table.getSelected().clear();
                            getHand().getSelected().clear();
                            state = State.EndTurn;
                        } else if(!getHand().contains(component)) {
                            table.getSelected().updateSelection(component);
                            rotSelection = null;
                            state = State.Rot;
                        }
                        break;
                    case RecycleOrReduce:
                        if (!getHand().contains(component) && component instanceof Cardboard) {
                            Cardboard cb = (Cardboard) component;
                            table.getSelected().updateSelection(cb);

                            if(table.getSelected().getCardboardTotal() == getHand().getSelected().getCardboardTotal()) {
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
                            state = State.EndTurn;
                        }
                        break;

                }
            }
        }

        Rectangle endTurnButton = getButtonBounds();
        if(gameOver() && endTurnButton.contains(x, y)) {
            setup(hands.size());
        } else if(endTurnButton.contains(x, y) && state == State.RepairOrRepurpose) {
            if(getHand().getSelected().size() == 0) {
                endTurn();
            } else if(getHand().getSelected().size() >= 2 && !getHand().getSelected().getColor().equals(Color.black)) {
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
        return this;
    }

    public Rectangle getButtonBounds() {
        return new Rectangle(Game.COLUMNS*Game.CELL_SIZE - (2*Game.CELL_SIZE) + 10, Game.ROWS*Game.CELL_SIZE + 10, 2*Game.CELL_SIZE - 20, Game.CELL_SIZE - 20);
    }

    public int getTurn() {
        return turn;
    }

    public List<Integer> getRankedSixes() {
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
        return sixCounts;
    }

    public int getScore() {
        return getScore(turn);
    }

    public int getScore(int player) {
        int score = 0;
        Map<Integer, Integer> mapCardboard = new HashMap<Integer, Integer>();
        Set<Color> cardboardColors = new HashSet<Color>();
        int cardboardTotal = 0;
        Map<Color, Integer> mapGlass = new HashMap<Color, Integer>();
        Map<Color, Integer> mapColorCardboard = new HashMap<Color, Integer>();
        int maxGlass = 0;
        int plastic6count = 0;

        boolean hasGold = false;
        boolean hasSilver = false;
        boolean hasBronze = false;

        for(Component component : hands.get(player)) {
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
            } else if(component instanceof Metal) {
                Metal metal = (Metal)component;
                switch (metal.getType()) {
                    case Gold:
                        score += 15; //14;
                        hasGold = true;
                        break;
                    case Silver:
                        score += 15;
                        hasSilver = true;
                        break;
                    case Bronze:
                        score += 15; //10;
                        hasBronze = true;
                        break;
                }
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

        for(Component component : hands.get(player)) {
            if (component instanceof Plastic) {
                Plastic plastic = (Plastic)component;
                switch(plastic.getFace().getValue()) {
                    case 1:
                        if(hasGold) {
                            score += 3;
                        }
                        if(hasSilver) {
                            score += 2;
                        }
                        if(hasBronze) {
                            score += 1;
                        }
                    case 2:
                        score += plastic.getFace().getValue();
                        break;
                    case 3:
                        if(mapColorCardboard.containsKey(plastic.getColor()) && mapColorCardboard.get(plastic.getColor()) >= 3) {
                            score += 3;
                        }
                        break;
                    case 5:
                        if(mapColorCardboard.containsKey(plastic.getColor()) && mapColorCardboard.get(plastic.getColor()) >= 5) {
                            score += 5;
                        }
                        break;
                    case 6:
                        plastic6count++;
                        break;

                }
            }
        }

        List<Integer> sixCounts = getRankedSixes();

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
                score += (8-hands.size());
            }
        }

        for(Cardboard dirtyCardboard : table.getTrashHeap()) {
            if(getHands().get(player).getGoal().contains(dirtyCardboard.getColor(), dirtyCardboard.getFace().getValue())) {
                score += dirtyCardboard.getFace().getValue();
            }
        }

        return score;
    }
}
