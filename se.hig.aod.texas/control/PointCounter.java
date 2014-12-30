package control;

import java.util.ArrayList;
import model.Card;
import model.Card.Suit;
import model.Card.Value;
import model.Player;
/**
 * A class to calculate points of moves and at the end of the deck.
 * @author Åke Ekmark, Andreas Wieselqvist och Simon Söderhäll.
 *
 */
public class PointCounter {
	private BoardHandler boardHandler;
	public PointCounter(BoardHandler boardHandler) {
		this.boardHandler = boardHandler;
	}
	public void calculatePoints(ArrayList<Card> cards, Player player) {
		int points = 0;
		for (Card card: cards) {
			if (card.getSuit() == Suit.SPADE && card.getValue() == Value.TWO) {
				points++;
			}
			if (card.getSuit() == Suit.DIAMOND && card.getValue() == Value.TEN) {
				points+=2;
			}
			if (card.getValue() == Value.ACE) {
				points++;
			}
		}
		if (boardHandler.getCardsOnBoard().size() == 0) {
			points++;
		}
		
		addPoints(points, player);
		
	}
	private void addPoints(int points, Player player) {
//		player.addPoints(points);
		//sbp.addPlayerScore(points, player.getPosition());
	}


	
	
}
