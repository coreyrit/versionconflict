package capitalvices;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import java.util.List;

public class Game { //extends JFrame implements MouseListener, MouseMotionListener {
    private List<SinCard> sins = new ArrayList<SinCard>();
    private List<Coffer> coffers = new ArrayList<Coffer>();
    private int playerTurn = 0;

    private int fps = 30;

    private Insets insets;

    private Sin roundSin = null;
    private SinCard activeSin = null;
    private List<Resource> selected = new ArrayList<Resource>();

    private Resource trashedResource = null;

    private Coffer lustSource = null;
    private Coffer lustDestination = null;

    private Coffer prideSource = null;
    private Coffer prideDestination = null;
    private Resource prideResource = null;

    public Game() {
        setup();

//        PanelRenderer panelRenderer1 = new PanelRenderer(this);
//        this.setLayout(new BorderLayout());
//        this.add(panelRenderer1, BorderLayout.CENTER);
//        this.pack();

//        setTitle("Capital Vices");
//        setSize(windowWidth, windowHeight);
//        setResizable(false);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setVisible(true);

//        insets = getInsets();
//        setSize(insets.left + windowWidth + insets.right, insets.top + windowHeight + insets.bottom);

//        this.addMouseListener(this);
//        this.addMouseMotionListener(this);
    }

    public void setup() {
        for(Sin sin : Sin.values()) {
            sins.add(new SinCard(sin));
        }
        Collections.shuffle(sins);

        activeSin = sins.get(0);
        roundSin = activeSin.getSin();

        coffers.add(new Coffer(Color.red));
        coffers.add(new Coffer(Color.yellow));
        coffers.add(new Coffer(Color.blue));

        for(Coffer coffer : coffers) {
            coffer.moveResourceHere(new Resource(coffer.getColor(), coffer, ResourceType.Food));
            coffer.moveResourceHere(new Resource(coffer.getColor(), coffer, ResourceType.Money));
        }

        coffers.get(0).setFirstPlayer(true);
        coffers.get(1).moveResourceHere(new Resource(Color.gray, coffers.get(1), ResourceType.Food));
        coffers.get(2).moveResourceHere(new Resource(Color.gray, coffers.get(2), ResourceType.Money));
    }

    public List<SinCard> getSins() {
        return sins;
    }

    public List<Coffer> getCoffers() {
        return coffers;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void mouseClicked(MouseEvent e) {
        mouseClicked(e.getX(), e.getY());
    }
    public void mouseClicked(int x, int y) {
        if(activeSin.getSin() == Sin.Sloth) {
            for(SinCard sin : sins) {
                if(sin.isFaceUp() && sin.getBounds().contains(x, y) && sin.getSin() != Sin.Sloth) {
                    activeSin.setFaceUp(false);
                    activeSin = sin;
//                    repaint();
                }
            }
        } else {
            for (Coffer coffer : coffers) {
                for (Resource resource : coffer.getResources()) {
                    if (resource.getBounds().contains(x,y)) {
                        // selection rules
                        switch (activeSin.getSin()) {
                            case Sloth:
                                // don't select resources
                                break;
                            case Greed:
                            case Gluttony:
                                selected.clear();
                                selected.add(resource);
                                break;
                            case Pride:
                                if (resource != prideResource && (prideDestination == null || prideDestination == resource.getCoffer())) {
                                    selected.clear();
                                    selected.add(resource);
                                }
                                break;
                            case Envy:
                                if (selected.size() == 0 || (selected.size() < 2 && selected.get(0).getCoffer() != resource.getCoffer())) {
                                    selected.add(resource);
                                } else {
                                    selected.clear();
                                    selected.add(resource);
                                }
                                break;
                            case Lust:
                                if ((lustSource == null && resource.getCoffer().getResources().size() > 1) || resource.getCoffer() == lustSource ) {
                                    selected.clear();
                                    selected.add(resource);
                                }
                                break;
                            case Wrath:
                                // immediately destroy
                                coffer.trashResource(resource);
                                trashedResource = resource;
                                nextAction();
                                break;
                        }
//                        this.repaint();
                        return; // don't check coffers
                    }
                }
            }
            for (Coffer coffer : coffers) {
                if (coffer.getBounds().contains(x, y)) {
                    switch (activeSin.getSin()) {
                        case Greed:
                            if (selected.size() == 1 && coffer != selected.get(0).getCoffer()) {
                                Resource resource = selected.get(0);
                                selected.clear();
                                resource.setResourceType(ResourceType.Money);
                                coffer.moveResourceHere(resource);
                                nextAction();
                            }
                            break;
                        case Gluttony:
                            if (selected.size() == 1 && coffer != selected.get(0).getCoffer()) {
                                Resource resource = selected.get(0);
                                selected.clear();
                                resource.setResourceType(ResourceType.Food);
                                resource.getCoffer().moveResourceHere(resource);
                                coffer.moveResourceHere(resource);
                                nextAction();
                            }
                            break;
                        case Envy:
                            if (selected.size() == 2 && coffer != selected.get(0).getCoffer() && coffer != selected.get(1).getCoffer()) {
                                Resource resource1 = selected.get(0);
                                Resource resource2 = selected.get(1);
                                selected.clear();
                                coffer.moveResourceHere(resource1);
                                coffer.moveResourceHere(resource2);
                                nextAction();
                            }
                            break;
                        case Lust:
                            if (selected.size() == 1 && coffer != selected.get(0).getCoffer() && (lustDestination == null || coffer != lustDestination)) {
                                Resource resource = selected.get(0);
                                selected.clear();
                                if (lustSource == null) {
                                    lustSource = resource.getCoffer();
                                }
                                coffer.moveResourceHere(resource);
                                if (lustDestination == null) {
                                    lustDestination = coffer;
                                } else {
                                    nextAction();
                                }
                            }
                            break;
                        case Pride:
                            if(selected.size() == 1 && coffer != prideSource && coffer != prideDestination &&
                                    (coffer.getResources().size() > 0 || prideDestination != null)) {
                                Resource resource = selected.get(0);
                                selected.clear();
                                prideSource = resource.getCoffer();
                                prideDestination = coffer;
                                coffer.moveResourceHere(resource);
                                if(prideResource == null) {
                                    prideResource = resource;
                                } else {
                                    nextAction();
                                }
                            }
                            break;
                        case Wrath:
                        case Sloth:
                            // do nothing
                            break;
                    }

                }
            }
        }
//        this.repaint();
    }

    private void nextAction() {
        // clear selection
        selected.clear();

        // clear action data
        lustSource = null;
        lustDestination = null;

        prideSource = null;
        prideDestination = null;
        prideResource = null;

        // turn the current action over
        activeSin.setFaceUp(false);
        activeSin = null;

        // find the next action
        for(SinCard sin : sins) {
            if(sin.isFaceUp()) {
                activeSin = sin;
                break;
            }
        }

        // check for end of round
        if(activeSin == null) {
            nextRound();
        } else if(activeSin.getSin() == Sin.Sloth && sins.indexOf(activeSin) == 6) {
            activeSin = null;
            nextRound();
        } else {
            // next player's turn
            playerTurn++;
            if (playerTurn >= 3) {
                playerTurn = 0;
            }
        }
    }

    private void nextRound() {
        calculateScores();

        // check for patience
        for(Coffer coffer : coffers) {
            if(getCofferVirtue(coffer) == 0) {
                coffer.increaseScore(1);
            }
        }

        // move the first player token
        for(int i = 0; i < coffers.size(); i++) {
            if(coffers.get(i).isFirstPlayer()) {
                coffers.get(i).setFirstPlayer(false);
                playerTurn = i+1;
            }
        }
        if (playerTurn >= 3) {
            playerTurn = 0;
        }
        coffers.get(playerTurn).setFirstPlayer(true);

        // give the first player the trashed token
        coffers.get(playerTurn).moveResourceHere(trashedResource);
        trashedResource = null;

        // reset the actions
        Collections.shuffle(sins);
        for(SinCard sin : sins) {
            sin.setFaceUp(true);
        }
        activeSin = sins.get(0);
        roundSin = activeSin.getSin();
    }

    private int getCofferVirtue(Coffer coffer) {
        boolean hasCoffer1Color = false;
        boolean hasCoffer2Color = false;
        boolean hasCoffer3Color = false;
        boolean hasCommonColor = false;
        boolean hasMoney = false;
        boolean hasFood = false;

        for(Resource resource : coffer.getResources()) {
            hasCoffer1Color = hasCoffer1Color || resource.getColor().equals(coffers.get(0).getColor());
            hasCoffer2Color = hasCoffer2Color || resource.getColor().equals(coffers.get(1).getColor());
            hasCoffer3Color = hasCoffer3Color || resource.getColor().equals(coffers.get(2).getColor());
            hasCommonColor = hasCommonColor || resource.getColor().equals(Color.gray);
            hasMoney = hasMoney || resource.getResourceType() == ResourceType.Money;
            hasFood = hasFood || resource.getResourceType() == ResourceType.Food;
        }

        int count = (hasCoffer1Color ? 1 : 0) +
                (hasCoffer2Color ? 1 : 0) +
                (hasCoffer3Color ? 1 : 0) +
                (hasCommonColor ? 1 : 0) +
                (hasMoney ? 1 : 0) +
                (hasFood ? 1 : 0);
        return 6 - count;
    }

    private void calculateScores() {
        Map<Coffer, Integer> sinScore = new HashMap<Coffer, Integer>();
        Map<Coffer, Integer> virtueScore = new HashMap<Coffer, Integer>();

        int maxSin = 0;
        int maxVirtue = 0;

        for(int me = 0; me < coffers.size(); me++) {
            Coffer coffer = coffers.get(me);

            int count = 0;
            int left = me-1;
            if(left < 0) {
                left = coffers.size()-1;
            }
            int right = me+1;
            if(right >= coffers.size()) {
                right = 0;
            }

            // find sinner(s)
            for(Resource resource : coffer.getResources()) {
                switch (roundSin) {
                    case Sloth: // most of own color
                        count += resource.getColor().equals(coffers.get(me).getColor()) ? 1 :0;
                        break;
                    case Envy: // most of left color
                        count += resource.getColor().equals(coffers.get(left).getColor()) ? 1 :0;
                        break;
                    case Lust: // most of right color
                        count += resource.getColor().equals(coffers.get(right).getColor()) ? 1 :0;
                        break;
                    case Wrath: // most resources
                        count++;
                        break;
                    case Pride: // most commmon color
                        count += resource.getColor().equals(Color.gray) ? 1 :0;
                        break;
                    case Greed: // most money
                        count += resource.getResourceType() == ResourceType.Money ? 1 : 0;
                        break;
                    case Gluttony: // most food
                        count += resource.getResourceType() == ResourceType.Food ? 1 : 0;
                        break;
                }
            }
            maxSin = Math.max(maxSin, count);
            sinScore.put(coffer, count);
        }


        // calculate virtue
        for(Coffer coffer : coffers) {
            int count = getCofferVirtue(coffer);
            maxVirtue = Math.max(maxVirtue, count);
            virtueScore.put(coffer, count);
        }

        // now update scores
        for(Coffer coffer : coffers) {
            if(sinScore.get(coffer) < maxSin || sinScore.get(coffer) == 0) {
                if(virtueScore.get(coffer) == maxVirtue) {
                    coffer.increaseScore(2);
                } else {
                    coffer.increaseScore(1);
                }
            }
        }

        // check for a winner!
        for(Coffer coffer : coffers) {
            if(coffer.getScore() == 7 && (sinScore.get(coffer) < maxSin || sinScore.get(coffer) == 0)) {
                System.out.println("Winner!");
            }
        }
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

    public void run() {

    }

    public List<Resource> getSelected() {
        return selected;
    }

    public SinCard getActiveSin() {
        return activeSin;
    }

    public Sin getRoundSin() {
        return roundSin;
    }

    public Resource getTrashedResource() {
        return trashedResource;
    }
}
