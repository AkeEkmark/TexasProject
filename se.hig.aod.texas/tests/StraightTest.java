package tests;



import java.util.ArrayList;

import model.Card;
import model.Deck;

import org.junit.Before;
import org.junit.Test;

import control.Logger;
import control.Rules;

public class StraightTest {
	Deck deck;
	Rules rules;
	ArrayList<Card> cardsOnHand;
	ArrayList<Card> cardsOnBoard;
	Logger logger;
	double answer;
	@Before
	public void setUp() throws Exception {
		deck = new Deck();
		logger = new Logger();
		rules = new Rules(logger);
		cardsOnHand = new ArrayList<Card>();
		cardsOnBoard = new ArrayList<Card>();
		cardsOnHand.add(deck.getCard(2));
		cardsOnHand.add(deck.getCard(37));
		cardsOnBoard.add(deck.getCard(3));
		cardsOnBoard.add(deck.getCard(4));
		cardsOnBoard.add(deck.getCard(6));
		cardsOnBoard.add(deck.getCard(5));
		cardsOnBoard.add(deck.getCard(8));
		
		for (Card card : cardsOnHand) {
			System.out.println(card);
		}
		for (Card card : cardsOnBoard) {
			System.out.println(card);
		}
	}

	@Test
	public void test() {
		answer = rules.chanceStraight(cardsOnHand, cardsOnBoard, 2);
		System.out.println(answer);
	}

}
