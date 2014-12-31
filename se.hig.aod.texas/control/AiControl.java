package control;

import gui.Board.BoardFrame;
import gui.Board.Players;

import java.util.ArrayList;
import java.util.Random;

import model.Card;
import model.Card.Suit;
import model.Card.Value;
import model.ComputerPlayer;
import model.Player;

/**
 * A class to control the computer-players of the game with references to parts
 * of the program the method needs.
 * 
 * @author Åke Ekmark, Andreas Wieselqvist och Simon Söderhäll.
 * 
 */
public class AiControl {
	private Random random;
	private int difficulty;
	private BoardHandler boardHandler;
	private PlayerMoves playerMoves;
	private static final int preFlop = 0, first = 0;
	private static final int byTheBook = 1, flop = 1, second = 1;
	private static final int bluffer = 2, turn = 2, third = 2;
	private static final int river = 3;

	public AiControl(BoardHandler boardHandler, PlayerMoves playerMoves,
			Rules rules) {
		this.boardHandler = boardHandler;
		this.playerMoves = playerMoves;
		random = new Random();
	}

	/**
	 * Analyses the players hand and the board to come up with possible moves.
	 * If there are no moves it plays a card from the hand to the board. The
	 * difficulty of the computer-player determines how "smart" the move will
	 * be.
	 * 
	 * @param player
	 *            : the computer-player to make the move for.
	 * @return a string describing the move the player did.
	 */
	public String makeMove(Player player, int round, int stage) {
		int position = player.getPosition();
		int nbrBlinds = player.getBlinds();
		double opponentHandStrength = player.getOpponentHandStrength();
		double myHandStrength = player.getMyHandStrength();
		double chanceToWin;
		difficulty = ((ComputerPlayer) player).getDifficulty();

		switch (difficulty) {
		case byTheBook:
			switch (round) {
			case preFlop:
				if (opponentHandStrength >= 5) {
					if (myHandStrength >= 10) {
						if (stage == third) {
							playerMoves.call(player);
						} else {
							playerMoves.raise(player);
						}

					} else if (myHandStrength >= 0) {
						playerMoves.call(player);
					} else {
						playerMoves.fold(player);
					}
				} else {
					if (myHandStrength >= 5) {
						playerMoves.raise(player);
					} else {
						playerMoves.check(player);
					}
				}
				break;
			case flop:
				
				chanceToWin = analyzeCards();
				if (chanceToWin > 70) {
					playerMoves.raise(player);
				} else if (chanceToWin > 50) {
					playerMoves.check(player);
				} else {
					playerMoves.fold(player);
				}
			}

			break;
		case 2:

			break;
		case 3:

			break;
		default:
			break;
		}
		return "no moves avaliable";
	}

	private double analyzeCards() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * An inner class to save the possible moves.
	 * 
	 * @author Åke Ekmark, Andreas Wieselqvist och Simon Söderhäll.
	 * 
	 */
	public class AvaliableMoves {
		private int cardOnHand;
		private ArrayList<Integer> cardsOnBoard;
		private int points;

		public AvaliableMoves(int cardOnHand, ArrayList<Integer> cardsOnBoard,
				int points) {
			this.cardOnHand = cardOnHand;
			this.cardsOnBoard = cardsOnBoard;
			this.points = points;
		}

		public int getCardOnHand() {
			return cardOnHand;
		}

		public ArrayList<Integer> getCardsOnBoard() {
			return cardsOnBoard;
		}

		public int getPoints() {
			return points;
		}
	}
}
