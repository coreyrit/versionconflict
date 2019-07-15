package versionconflict;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game { //} extends JFrame implements MouseListener, MouseMotionListener {

    private Phase phase = Phase.Preparation;

    private Player player1 = new Player();
    private Player player2 = new Player();

    private GameButton button1 = new GameButton();
    private GameButton button2 = new GameButton();
    private GameButton button3 = new GameButton();

    private Card currentCharge = null;
    private Card previousAttack = null;

    public Game() {
        setup();

//        PanelRenderer panelRenderer1 = new PanelRenderer(this);
//        this.setLayout(new BorderLayout());
//        this.add(panelRenderer1, BorderLayout.CENTER);
//        this.pack();

//        setTitle("Version Conflict");
//        setSize(windowWidth, windowHeight);
//        setResizable(false);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setVisible(true);

//        insets = getInsets();
//        setSize(insets.left + windowWidth + insets.right, insets.top + windowHeight + insets.bottom);

//        this.addMouseListener(this);
//        this.addMouseMotionListener(this);
    }

    private void setup() {
        for(List<Card> deck : new List[] { player1.getDeck(), player2.getDeck() } ) {
            deck.add(Card.builder().withNumber(1)
                    .withLeftMiddleSquare(SquareSymbol.Focus)
                    .withLeftTopDiamond(DiamondSymbol.Punch)
                    .build());
            deck.add(Card.builder().withNumber(2)
                    .withLeftBottomSquare(SquareSymbol.Focus)
                    .withLeftMiddleDiamond(DiamondSymbol.Punch)
                    .build());
            deck.add(Card.builder().withNumber(3)
                    .withLeftTopSquare(SquareSymbol.Focus)
                    .withLeftBottomDiamond(DiamondSymbol.Punch)
                    .build());
            deck.add(Card.builder().withNumber(4)
                    .withLeftBottomSquare(SquareSymbol.Focus)
                    .withLeftTopDiamond(DiamondSymbol.Kick)
                    .build());
            deck.add(Card.builder().withNumber(5)
                    .withLeftTopSquare(SquareSymbol.Focus)
                    .withLeftMiddleDiamond(DiamondSymbol.Kick)
                    .build());
            deck.add(Card.builder().withNumber(6)
                    .withLeftMiddleSquare(SquareSymbol.Focus)
                    .withLeftBottomDiamond(DiamondSymbol.Kick)
                    .build());
            deck.add(Card.builder().withNumber(7)
                    .withLeftTopDiamond(DiamondSymbol.Punch)
                    .withRightTopSquare(SquareSymbol.Focus)
                    .build());
            deck.add(Card.builder().withNumber(8)
                    .withLeftMiddleDiamond(DiamondSymbol.Punch)
                    .withRightMiddleSquare(SquareSymbol.Focus)
                    .build());
            deck.add(Card.builder().withNumber(9)
                    .withLeftBottomDiamond(DiamondSymbol.Punch)
                    .withRightBottomSquare(SquareSymbol.Focus)
                    .build());
            deck.add(Card.builder().withNumber(10)
                    .withLeftTopDiamond(DiamondSymbol.Kick)
                    .withRightTopSquare(SquareSymbol.Focus)
                    .build());
            deck.add(Card.builder().withNumber(11)
                    .withLeftMiddleDiamond(DiamondSymbol.Kick)
                    .withRightMiddleSquare(SquareSymbol.Focus)
                    .build());
            deck.add(Card.builder().withNumber(12)
                    .withLeftBottomDiamond(DiamondSymbol.Kick)
                    .withRightBottomSquare(SquareSymbol.Focus)
                    .build());
            deck.add(Card.builder().withNumber(13)
                    .withLeftTopSquare(SquareSymbol.Focus)
                    .withLeftTopDiamond(DiamondSymbol.Punch)
                    .build());
            deck.add(Card.builder().withNumber(14)
                    .withLeftMiddleSquare(SquareSymbol.Focus)
                    .withLeftMiddleDiamond(DiamondSymbol.Punch)
                    .build());
            deck.add(Card.builder().withNumber(15)
                    .withLeftBottomSquare(SquareSymbol.Focus)
                    .withLeftBottomDiamond(DiamondSymbol.Punch)
                    .build());
            deck.add(Card.builder().withNumber(16)
                    .withLeftTopSquare(SquareSymbol.Focus)
                    .withLeftTopDiamond(DiamondSymbol.Kick)
                    .build());
            deck.add(Card.builder().withNumber(17)
                    .withLeftMiddleSquare(SquareSymbol.Focus)
                    .withLeftMiddleDiamond(DiamondSymbol.Kick)
                    .build());
            deck.add(Card.builder().withNumber(18)
                    .withLeftBottomSquare(SquareSymbol.Focus)
                    .withLeftBottomDiamond(DiamondSymbol.Kick)
                    .build());
            deck.add(Card.builder().withNumber(19)
                    .withLeftTopSquare(SquareSymbol.Shield)
                    .build());
            deck.add(Card.builder().withNumber(20)
                    .withLeftMiddleSquare(SquareSymbol.Shield)
                    .build());
            deck.add(Card.builder().withNumber(21)
                    .withLeftBottomSquare(SquareSymbol.Shield)
                    .build());
            deck.add(Card.builder().withNumber(22)
                    .withCircleSymbol(CircleSymbol.PunchCharge)
                    .build());
            deck.add(Card.builder().withNumber(23)
                    .withCircleSymbol(CircleSymbol.PunchCharge)
                    .build());
            deck.add(Card.builder().withNumber(24)
                    .withCircleSymbol(CircleSymbol.KickCharge)
                    .build());
            deck.add(Card.builder().withNumber(25)
                    .withCircleSymbol(CircleSymbol.KickCharge)
                    .build());

            Collections.shuffle(deck);
        }

        for(int i = 0; i < 7; i++) {
            player1.drawCard();
            player2.drawCard();
        }
    }


    public Phase getPhase() {
        return phase;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public GameButton getButton1() {
        return button1;
    }

    public GameButton getButton2() {
        return button2;
    }

    public GameButton getButton3() {
        return button3;
    }

    public Card getCurrentCharge() {
        return currentCharge;
    }

    public void mouseClicked(int x, int y) {
        if(phase == Phase.Preparation) {
            for (Card card : player1.getHand()) {
                if (card.getBounds().contains(x, y)) {
                    if (player1.getSelected().contains(card)) {
                        player1.getSelected().remove(card);
                    } else if (player1.getSelected().size() < 5) {
                        player1.getSelected().add(card);
                    }
                }
            }

            if (player1.getSelected().size() == 5 && button1.getBounds().contains(x, y)) {
                // remove unselected cards
                for (int i = player1.getHand().size() - 1; i >= 0; i--) {
                    Card card = player1.getHand().get(i);
                    if (!player1.getSelected().contains(card)) {
                        player1.discardCard(card);
                    }
                }
                player1.getSelected().clear();

                // remove random 2 from opponent
                player2.discardCard(player2.getHand().get(0));
                player2.discardCard(player2.getHand().get(0));

                if(hasCharge(player1)) {
                    phase = Phase.ConflictAttack;
                }
            }

            button1.setEnabled(true);
            button1.setText(player1.getSelected().size() == 5 ? "READY!" : "Choose 5");
            button1.setColor(player1.getSelected().size() == 5 ? Color.green : Color.lightGray);
        }

        if(phase == Phase.ConflictCharge) {
            button1.setEnabled(hasCharge(player1));
            button1.setText("Choose Charge");

            for (Card card : player1.getHand()) {
                if(card.getCircleSymbol() != CircleSymbol.Blank && card.getBounds().contains(x, y)) {
                    currentCharge = card;
                }
            }

            button2.setEnabled(currentCharge != null);
            button2.setText("Fight!");
            button2.setColor(Color.green);

            button3.setEnabled(true);
            button3.setText("End Turn");
            button3.setColor(Color.red);

            if(button2.isEnabled() && button2.getBounds().contains(x, y)) {
                phase = Phase.ConflictAttack;
            }
        }

        if(phase == Phase.ConflictAttack) {
            button1.setText("Choose Attack");
            button1.setEnabled(true);
            button1.setColor(Color.lightGray);

            player1.getSelected().clear();
            for(Card card : player1.getHand()) {
                if(isValidAttack(player1, card)) {
                    player1.getSelected().add(card);
                }
            }
        }
    }

    private boolean hasCharge(Player player) {
        for(Card card : player.getHand()) {
            if(card.getCircleSymbol() != CircleSymbol.Blank) {
                return true;
            }
        }
        return false;
    }

    private List<Card> findCharges(Player player) {
        List<Card> charges = new ArrayList<>();
        if(currentCharge != null) {
            charges.add(currentCharge);
        }
        for(Card card : player.getHand()) {
            if(card.getCircleSymbol() != CircleSymbol.Blank) {
                charges.add(card);
            }
        }
        return charges;
    }

    private boolean isValidAttack(Player player, Card card) {
        List<Card> charges = findCharges(player);
        for(Card chargeCard : charges) {

            // is it a possible starter?
            if (isStarter(card.getLeftTopSquare(), card.getLeftTopDiamond(), chargeCard.getCircleSymbol())) {
                return true;
            } else if (isStarter(card.getLeftMiddleSquare(), card.getLeftMiddleDiamond(), chargeCard.getCircleSymbol())) {
                return true;
            } else if (isStarter(card.getLeftBottomSquare(), card.getLeftBottomDiamond(), chargeCard.getCircleSymbol())) {
                return true;
            }

            // does it chain in from previous attack?
            if (previousAttack != null) {
                if (isChained(previousAttack.getRightTopSquare(), card.getLeftTopSquare(), card.getLeftTopDiamond(), chargeCard.getCircleSymbol())) {
                    return true;
                } else if (isChained(previousAttack.getRightMiddleSquare(), card.getLeftMiddleSquare(), card.getLeftMiddleDiamond(), chargeCard.getCircleSymbol())) {
                    return true;
                } else if (isChained(previousAttack.getRightBottomSquare(), card.getLeftBottomSquare(), card.getLeftBottomDiamond(), chargeCard.getCircleSymbol())) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isStarter(SquareSymbol squareSymbol, DiamondSymbol diamondSymbol, CircleSymbol circleSymbol) {
        if(squareSymbol == SquareSymbol.Blank) {
            // is it a kick starter?
            if(circleSymbol == CircleSymbol.KickCharge && diamondSymbol == DiamondSymbol.Kick) {
                return true;
            }
            // is it a punch starter?
            else if(circleSymbol == CircleSymbol.PunchCharge && diamondSymbol == DiamondSymbol.Punch) {
                return true;
            }
        }
        return false;
    }

    private boolean isChained(SquareSymbol squareOut, SquareSymbol squareIn, DiamondSymbol diamondSymbol, CircleSymbol circleSymbol) {
        if(squareOut == SquareSymbol.Focus && squareIn == SquareSymbol.Focus) {
            // is it a kick chain?
            if(circleSymbol == CircleSymbol.KickCharge && diamondSymbol == DiamondSymbol.Kick) {
                return true;
            }
            // is it a punch chain?
            else if(circleSymbol == CircleSymbol.PunchCharge && diamondSymbol == DiamondSymbol.Punch) {
                return true;
            }
        }
        return false;
    }
}

