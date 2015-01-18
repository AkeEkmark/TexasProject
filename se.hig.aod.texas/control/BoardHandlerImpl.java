package control;


import java.util.ArrayList;

import model.Board;
import model.Card;
/**
 * Implementation of the interface to control the board and keep a reference of the board.
 * @author Åke Ekmark, Andreas Wieselqvist
 *
 */
public class BoardHandlerImpl implements BoardHandler {
	private Board board;

	
	public BoardHandlerImpl() {
		board = new Board();
	}
	
	@Override
	public ArrayList<Card> getCardsOnBoard() {
		ArrayList<Card> cards = board.getCardsOnBoard();
		return cards;
	}

	@Override
	public void addCardtoBoard(Card card) {
		board.addCardToBoard(card);
	}

	@Override
	public void clearBoard() {
		board.clearBoard();
	}
	@Override
	public Board getBoard() {
		return board;
	}

}
