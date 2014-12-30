package main;

import model.Card;
import model.Deck;
import control.Rules;

public class ChenTest {
	public static void main(String[] args) {
		double [] spread = new double [41];
		for (int i = 0; i < 1000000; i++) {
			Deck deck = new Deck();
			deck.shuffleDeck();
			Card card1 = deck.getCard(0);
			Card card2 = deck.getCard(0);
			Rules rules = new Rules();
			double score = rules.chenFormula(card1, card2);
			if (score <= 0) {
				spread[0]++;
			}
			else {
				spread[(int) (score*2)]++;
			}
		}
		System.out.println("Antal händer med score 0 eller under: " +spread[0]/1000000);
		for (int i = 1; i < spread.length; i++) {	
			System.out.println("Antal händer med score "+ i/2.0 +" är "+ spread[i]/1000000);
		}
	}
}
