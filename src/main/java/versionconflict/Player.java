package versionconflict;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Card> deck = new ArrayList<Card>();
    private List<Card> discard = new ArrayList<Card>();
    private List<Card> hand = new ArrayList<Card>();
    private List<Card> selected = new ArrayList<Card>();

    public List<Card> getDeck() {
        return deck;
    }

    public List<Card> getHand() {
        return hand;
    }

    public List<Card> getDiscard() {
        return discard;
    }

    public List<Card> getSelected() {
        return selected;
    }

    public void drawCard() {
        hand.add(deck.remove(0));
    }

    public void discardCard(Card card) {
        hand.remove(card);
        discard.add(card);
    }
}
