package control;

import model.Card;
import model.Deck;
/**
 * Implementation of the interface to control the deck and keep a reference of the deck.
 * @author �ke Ekmark, Andreas Wieselqvist och Simon S�derh�ll.
 *
 */
public class DeckHandlerImpl implements DeckHandler {
	private Deck deck;
	public DeckHandlerImpl() {
		deck = new Deck();
	}
	@Override
	public Card getCard(int i) {
		Card card = deck.getCard(i);
		return card;
	}

	@Override
	public void shuffleDeck() {
		deck.shuffleDeck();

	}
	@Override
	public Deck getDeck() {
		return deck;
	}
	@Override
	public void newDeck() {
		deck = new Deck();
		
	}

}
