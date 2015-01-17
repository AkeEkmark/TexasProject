package control;


import java.util.ArrayList;
import java.util.Collections;

import model.Card;

public class Rules {
	private Logger logger;
	public Rules(Logger logger) {
		this.logger = logger;
	}

	public double chenFormula(ArrayList<Card> cards) {
		Card card1 = cards.get(0);
		Card card2 = cards.get(1);
		double score = 0;
		logger.addString(card1 +" : " +card2);
		double baseScore = Math.max(score(card1), score(card2));
		if (card1.getValue() == card2.getValue()) {
			baseScore = (Math.max(5, baseScore*2));
		}
		if (card1.getSuit() == card2.getSuit()) {
			baseScore += 2;
		}
		score = baseScore - gap(card1, card2);
		logger.addString("Score of cards is :" +score );
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

	public double chancePair(ArrayList<Card> cardsOnHand, ArrayList<Card> cardsOnBoard, int round) {
		Card cardOnHand1 = cardsOnHand.get(0);
		Card cardOnHand2 = cardsOnHand.get(1);
		if (cardOnHand1.getValue() == cardOnHand2.getValue()) {
			return 1;
		}
		for (Card cardOnBoard : cardsOnBoard) {
			if (cardOnHand1.getValue() == cardOnBoard.getValue() || cardOnHand2.getValue() == cardOnBoard.getValue()) {
				return 1;
			}
		}
		return 0;
	}

	public double chanceTwoPair(ArrayList<Card> cardsOnHand, ArrayList<Card> cardsOnBoard, int round) {
		Card cardOnHand1 = cardsOnHand.get(0);
		Card cardOnHand2 = cardsOnHand.get(1);
		double chance = 0;
		if (cardOnHand1.getValue() == cardOnHand2.getValue()) {
			for (int i = 0; i < cardsOnBoard.size(); i++) {
				for (int j = i+1; j < cardsOnBoard.size(); j++) {
					if (cardsOnBoard.get(i).getValue() == cardsOnBoard.get(j).getValue()) {
						return 1;
					}
				}
			}
		}
		else {
			for (Card cardOnBoard : cardsOnBoard) {
				if (cardOnHand1.getValue() == cardOnBoard.getValue() || cardOnHand2.getValue() == cardOnBoard.getValue()) {
					chance += 0.5;
				}
			}
		}
		
		if (chance == 1) {
			return chance;
		}
		return 0;
		
	}

	public double chanceTreeOaK(ArrayList<Card> cardsOnHand, ArrayList<Card> cardsOnBoard, int round) {
		Card cardOnHand1 = cardsOnHand.get(0);
		Card cardOnHand2 = cardsOnHand.get(1);
		if (cardOnHand1.getValue() == cardOnHand2.getValue()) {
			for (Card cardOnBoard : cardsOnBoard) {
				if (cardOnBoard.getValue() == cardOnHand1.getValue()) {
					return 1;
				}
			}
		}
		else {
			for (int i = 0; i < cardsOnBoard.size(); i++) {
				for (int j = i+1; j < cardsOnBoard.size(); j++) {
					if ((cardsOnBoard.get(i).getValue() == cardsOnBoard.get(j).getValue()) && ((cardsOnBoard.get(i).getValue() == cardOnHand1.getValue()) 
							|| (cardsOnBoard.get(i).getValue() == cardOnHand2.getValue()))) {
						return 1;
					}
				}
			}
		}
		return 0;
	}

	public double chanceFourOaK(ArrayList<Card> cardsOnHand, ArrayList<Card> cardsOnBoard, int round) {
		Card cardOnHand1 = cardsOnHand.get(0);
		Card cardOnHand2 = cardsOnHand.get(1);
		int cardsWithValue = 0;
		if (cardOnHand1.getValue() == cardOnHand2.getValue()) {
			cardsWithValue = 2;
			for (Card cardOnBoard : cardsOnBoard) {
				if (cardOnBoard.getValue() == cardOnHand1.getValue()) {
					cardsWithValue++;
				}
			}
			if (cardsWithValue == 4) {
				return 1;
			}
			return 0;
		}
		else {
			if (chanceTreeOaK(cardsOnHand, cardsOnBoard, round) != 1) {
				return 0;
			}
			else {
				cardsWithValue = 1;
				for (Card cardOnBoard : cardsOnBoard) {
					if (cardOnBoard.getValue() == cardOnHand1.getValue()) {
						cardsWithValue++;
					}
				}
				if (cardsWithValue == 4) {
					return 1;
				}
				cardsWithValue = 1;
				for (Card cardOnBoard : cardsOnBoard) {
					if (cardOnBoard.getValue() == cardOnHand2.getValue()) {
						cardsWithValue++;
					}
				}
				if (cardsWithValue == 4) {
					return 1;
				}
			}
		}
		return 0;
	}

	public double chanceFlush(ArrayList<Card> cardsOnHand, ArrayList<Card> cardsOnBoard, int round) {
		Card cardOnHand1 = cardsOnHand.get(0);
		Card cardOnHand2 = cardsOnHand.get(1);
		int cardsWithSuit = 0;
		int cardsWithSuit2 = 0;
		if (cardOnHand1.getSuit() == cardOnHand2.getSuit()) {
			cardsWithSuit = 2;
			for (Card cardOnBoard : cardsOnBoard) {
				if (cardOnBoard.getSuit() == cardOnHand1.getSuit()) {
					cardsWithSuit++;
				}
			}
			if (cardsWithSuit == 5) {
				return 3;
			}
			if (cardsWithSuit == 3 && round == 1) {
				return 0.5;
			}
			if (cardsWithSuit == 4 && round != 3) {
				return 2;
			}
			else {
				return 0;
			}
			
		}
		else {
			cardsWithSuit = 1;
			cardsWithSuit2 = 1;
			for (Card cardOnBoard : cardsOnBoard) {
				if (cardOnBoard.getSuit() == cardOnHand1.getSuit()) {
					cardsWithSuit++;
				}
				if (cardOnBoard.getSuit() == cardOnHand2.getSuit()) {
					cardsWithSuit2++;
				}
			}
			if (cardsWithSuit == 5 || cardsWithSuit2 == 5) {
				return 2;
			}
			if ((cardsWithSuit == 3 || cardsWithSuit2 == 3) && round == 1) {
				return 0.5;
			}
			if ((cardsWithSuit == 4 || cardsWithSuit2 == 4) && round != 3) {
				return 2;
			}
			else {
				return 0;
			}
			
		}
	}

	public double chanceStraight(ArrayList<Card> cardsOnHand, ArrayList<Card> cardsOnBoard, int round) {
		Card cardOnHand1 = cardsOnHand.get(0);
		Card cardOnHand2 = cardsOnHand.get(1);
		int cardsInStraight = 0;
		boolean ace = false;
		int points = 0;
		ArrayList<Card> sortedList = new ArrayList<Card>();
		ArrayList<Card> straight = new ArrayList<Card>();
		sortedList.add(cardOnHand2);
		sortedList.add(cardOnHand1);
		sortedList.addAll(cardsOnBoard);
		Collections.sort(sortedList);
		
		for (Card card : sortedList) {
			if (card.getValue() == model.Card.Value.ACE) {
				ace = true;
			}
		}
		if (ace) {
			straight.add(sortedList.get(0));
			cardsInStraight++;
			for (Card king : sortedList) {
				if (king.getValue() == model.Card.Value.KING) {
					straight.add(king);
					cardsInStraight++;
					for (Card queen : sortedList) {
						if (queen.getValue() == model.Card.Value.QUEEN) {
							straight.add(queen);
							cardsInStraight++;
							for (Card jack : sortedList) {
								if (jack.getValue() == model.Card.Value.JACK) {
									straight.add(jack);
									cardsInStraight++;
									for (Card ten : sortedList) {
										if (ten.getValue() == model.Card.Value.TEN) {
											straight.add(ten);
											cardsInStraight++;
										}
									}
								}
							}		
						}
					}
				}
			}
			if 	(straight.contains(cardOnHand1) || straight.contains(cardOnHand2) ) {
				points = 1;
				if (straight.containsAll(cardsOnHand)) {
					points = 2;
				}
			}
			if (cardsInStraight > 4 && round == 3) {
				return 1*points;
			}
			if (cardsInStraight > 3 && round < 3) {
				return 1*points;
			}
		}
		points = 0;
		cardsInStraight = 0;
		straight.clear();
		for (int i = 1; i < sortedList.size(); i++) {
			if (sortedList.get(i).getValue().value() - sortedList.get(i-1).getValue().value() == 1 ) {
				if (cardsInStraight == 0) {
					straight.add(sortedList.get(i-1));
					cardsInStraight++;
				}
				if (sortedList.get(i).getValue().value() - straight.get(cardsInStraight-1).getValue().value() == 1) {
					straight.add(sortedList.get(i));
					cardsInStraight++;
				}
			}
		}
		
		if 	(straight.contains(cardOnHand1) || straight.contains(cardOnHand2) ) {
			points = 1;
			if (straight.containsAll(cardsOnHand)) {
				points = 2;
			}
		}
		if (cardsInStraight > 4 && round == 3) {
			return 1*points;
		}
		if (cardsInStraight > 3 && round < 3) {
			return 1*points;
		}
		
		return 0;
	}

//	public double chanceFullHouse(ArrayList<Card> cardsOnHand, ArrayList<Card> cardsOnBoard, int round) {
//		Card cardOnHand1 = cardsOnHand.get(0);
//		Card cardOnHand2 = cardsOnHand.get(1);
//		ArrayList<Card> checkedCards = new ArrayList<Card>();
//		int pair = 0;
//		int toak = 0;
//		
//		if (cardOnHand1.getValue() == cardOnHand2.getValue()) {
//			for (Card cardOnBoard : cardsOnBoard) {
//				if (cardOnBoard.getValue() == cardOnHand1.getValue()) {
//					toak = 1;
//					checkedCards.add(cardOnHand2);
//					checkedCards.add(cardOnHand1);
//					checkedCards.add(cardOnBoard);
//				}
//			}
//			if (toak != 1) {
//				checkedCards.add(cardOnHand2);
//				checkedCards.add(cardOnHand1);
//				pair = 1;
//			}
//			else {
//				
//			}
//			
//		}
//		else {
//			for (Card cardOnBoard : cardsOnBoard) {
//				if (cardOnHand1.getValue() == cardOnBoard.getValue() || cardOnHand2.getValue() == cardOnBoard.getValue()) {
//					return 1;
//				}
//			}
//		}
//		
//		return 0;
//	}

	
}
