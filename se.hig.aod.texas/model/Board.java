package model;

import java.util.ArrayList;
/**
 * A class describing a cardgame board.
 * The class keeps track of the cards on the board with the help of an ArrayList
 * @author �ke Ekmark, Andreas Wieselqvist och Simon S�derh�ll.
 *
 */
public class Board {
	private int blinds = 0;
	private ArrayList<Card> cardsOnBoard;
	/**
	 * Simple constructor
	 */
	public Board() {
		cardsOnBoard = new ArrayList<Card>();
	}
	/**
	 * 
	 * @return ArrayList containing the cards on the board
	 */
	public ArrayList<Card> getCardsOnBoard() {
		return cardsOnBoard;
	}
	/**
	 * 
	 * @param card : the card to add to the board
	 */
	public void addCardToBoard(Card card) {
		cardsOnBoard.add(card);
	}
	/**
	 * 
	 * Clears the board from cards and blinds.
	 */
	public void clearBoard() {
		blinds = 0;
		cardsOnBoard.clear();
	}
	/**
	 * 
	 * @param blind : the amount of blinds to place on the board.
	 */
	public void addBlind(int blind) {
		this.blinds += blind;
	}
	public int getBlinds() {
		return blinds;
	}
}
