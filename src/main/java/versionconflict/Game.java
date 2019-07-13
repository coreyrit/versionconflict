package versionconflict;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Collections;
import java.util.List;

public class Game { //} extends JFrame implements MouseListener, MouseMotionListener {

    private Insets insets;

    private Player player1 = new Player();
    private Player player2 = new Player();

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

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void mouseClicked(int x, int y) {
        for(Card card : player1.getHand()) {
            if(card.getBounds().contains(x, y)) {
                if(player1.getSelected().contains(card)) {
                    player1.getSelected().remove(card);
                } else if(player1.getSelected().size() < 5) {
                    player1.getSelected().add(card);
                }
            }
        }
    }
}

