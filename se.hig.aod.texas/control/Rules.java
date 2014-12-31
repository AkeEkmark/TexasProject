package control;

import java.util.ArrayList;

import model.Card;

public class Rules {
	public double chenFormula(ArrayList<Card> cards) {
		Card card1 = cards.get(0);
		Card card2 = cards.get(1);
		double score = 0;
		System.out.println(card1 +" " +card2);
		double baseScore = Math.max(score(card1), score(card2));
		if (card1.getValue() == card2.getValue()) {
			baseScore = (Math.max(5, baseScore*2));
		}
		if (card1.getSuit() == card2.getSuit()) {
			baseScore += 2;
		}
		score = baseScore - gap(card1, card2);
		System.out.println("Score of cards is :" +score );
		return score;
	}

	private int gap(Card card1, Card card2) {
		int gap = 0;
		int score = 0;
		if (card1.getValue() == model.Card.Value.ACE || card2.getValue() == model.Card.Value.ACE) {
			if (card1.getValue() == model.Card.Value.ACE) {
				gap = Math.abs(Math.min(card2.getValue().value() - 1, 14 - card2.getValue().value()));
			}
			else {
				gap = Math.abs(Math.min(card1.getValue().value() - 1, 14 - card1.getValue().value()));
			}
			
		}
		else {
			gap = Math.abs(card1.getValue().value() - card2.getValue().value());
		}
		switch(gap) {
		case 0:
			score = 0;
			break;
		case 1:
			score = -1;
			break;
		case 2:
			score = 1;
			break;
		case 3: 
			score = 2;
			break;
		case 4: 
			score = 4;
			break;
		default:
			score = 5;
			break;
		}
		
		return score;
	}

	private double score(Card card) {
		double score = 0;
		switch(card.getValue()) {
		case ACE:
			score = 10;
			break;
		case KING:
			score = 8;
			break;
		case QUEEN:
			score = 7;
			break;
		case JACK:
			score = 6;
			break;
		default:
			score = card.getValue().value()/2.0;
			break;	
		}
		return score;
	}
	
}
