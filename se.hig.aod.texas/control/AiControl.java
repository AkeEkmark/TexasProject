package control;



import java.util.ArrayList;
import java.util.Random;

import model.Board;
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
	private Board board;
	private PlayerMoves playerMoves;
	private Rules rules;
	private static final int preFlop = 0, first = 0;
	private static final int byTheBook = 1, flop = 1, second = 1;
	private static final int bluffer = 2, turn = 2, third = 2;
	private static final int river = 3;

	public AiControl(BoardHandler boardHandler, PlayerMoves playerMoves,
			Rules rules) {
		this.board = boardHandler.getBoard();
		this.playerMoves = playerMoves;
		this.rules = rules;
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
	public int makeMove(Player player, int round, int stage, int cMove) {
		int move = 0;
		int position = player.getPosition();
		int nbrBlinds = board.getBlinds();
		double opponentHandStrength = player.getOpponentHandStrength();
		double myHandStrength = player.getMyHandStrength();
		double postFlopScore;
		difficulty = ((ComputerPlayer) player).getDifficulty();

		switch (difficulty) {
		case byTheBook:
			switch (round) {
			case preFlop:
				if (opponentHandStrength >= 5) {
					if (myHandStrength >= 7) {
						if (stage == third) {
							move = playerMoves.call(player);
						} else {
							move = playerMoves.raise(player);
						}

					} else if (myHandStrength >= 3) {
						move = playerMoves.call(player);
					} else {
						move = playerMoves.fold(player);
					}
				} else {
					if (myHandStrength >= 3.5) {
						move = playerMoves.raise(player);
					} else {
						move = playerMoves.check(player);
					}
				}
				
				break;
			case flop:
				postFlopScore = analyzeCards(round, player);
				System.out.println("Chance to win is : " + postFlopScore);
				if (postFlopScore > 1) {
					if (stage == third) {
						move = playerMoves.call(player);
					}
					else {
						move = playerMoves.raise(player);
					}
				} else if (postFlopScore > 0) {
					if (cMove == 2) {
						move = playerMoves.call(player);
					}
					else {
						move = playerMoves.check(player);
					}
					
				} else {
					if (stage == 0 || (stage == 1 && cMove == 0)) {
						move = playerMoves.check(player);
					}
					else {
						move = playerMoves.fold(player);
					}
					
				}
				break;
			case turn:
				postFlopScore = analyzeCards(round, player);
				System.out.println("Chance to win is : " + postFlopScore);
				if (postFlopScore > 1) {
					if (stage == third) {
						move = playerMoves.call(player);
					}
					else {
						move = playerMoves.raise(player);
					}
				} else if (postFlopScore > 0) {
					if (cMove == 2) {
						move = playerMoves.call(player);
					}
					else {
						move = playerMoves.check(player);
					}
					
				} else {
					if (stage == 0 || (stage == 1 && cMove == 0)) {
						move = playerMoves.check(player);
					}
					else {
						move = playerMoves.fold(player);
					}
					
				}
				break;
			case river:
				postFlopScore = analyzeCards(round, player);
				System.out.println("Chance to win is : " + postFlopScore);
				if (postFlopScore > 1) {
					if (stage == third) {
						move = playerMoves.call(player);
					}
					else {
						move = playerMoves.raise(player);
					}
				} else if (postFlopScore > 0) {
					if (cMove == 2) {
						move = playerMoves.call(player);
					}
					else {
						move = playerMoves.check(player);
					}
					
				} else {
					if (stage == 0 || (stage == 1 && cMove == 0)) {
						move = playerMoves.check(player);
					}
					else {
						move = playerMoves.fold(player);
					}
					
				}
				break;
			default:
				break;
			}

			break;
		case bluffer:

			break;
		case 3:

			break;
		default:
			break;
		}
		return move;
	}

	private double analyzeCards(int round, Player player) {
		ArrayList<Card> cardsOnHand = player.getCardsOnHand();
		ArrayList<Card> cardsOnBoard = board.getCardsOnBoard();
		int postFlopscore = 0;
		double chancePair = 0;
		double chanceTwoPair = 0;
		double chanceThreeOaK = 0;
		double chanceStraight = 0;
		double chanceFlush = 0;
		//double chanceFullHouse = 0;
		double chanceFourOaK = 0;
		double chanceStraightFlush = 0;
		chancePair = rules.chancePair(cardsOnHand, cardsOnBoard, round);
		chanceTwoPair = rules.chanceTwoPair(cardsOnHand, cardsOnBoard, round);
		chanceThreeOaK = rules.chanceTreeOaK(cardsOnHand, cardsOnBoard, round);
		chanceFourOaK = rules.chanceFourOaK(cardsOnHand, cardsOnBoard, round);
		chanceFlush = rules.chanceFlush(cardsOnHand, cardsOnBoard, round);
		chanceStraight = rules.chanceStraight(cardsOnHand, cardsOnBoard, round);
		//chanceFullHouse = rules.chanceFullHouse(cardsOnHand, cardsOnBoard, round); 
		if (chanceFlush == 1 && chanceStraight > 1) {
			chanceStraightFlush = 5;
		}
		if (chancePair > 0) {
			postFlopscore += chancePair;
			postFlopscore += chanceTwoPair;
			postFlopscore += chanceThreeOaK;
			postFlopscore += chanceFourOaK;
		}
		postFlopscore += chanceStraight;
		postFlopscore += chanceFlush;
		postFlopscore += chanceStraightFlush;
		
		if (player.getMyHandStrength() > 5 && postFlopscore == 0) {
			postFlopscore++;
		}
		return postFlopscore;
	}

	/**
	 * An inner class to save the possible moves.
	 * 
	 * @author Åke Ekmark, Andreas Wieselqvist och Simon Söderhäll.
	 * 
	 */
	public class AvaliableHands {
		private ArrayList<Card> cardsOnBoard;
		private int points;
		private double chanceToHit;

		public AvaliableHands(double chanceToHit, ArrayList<Card> cardsOnBoard, int points) {
			this.chanceToHit = chanceToHit;
			this.cardsOnBoard = cardsOnBoard;
			this.points = points;
		}
		public ArrayList<Card> getCardsOnBoard() {
			return cardsOnBoard;
		}
		public int getPoints() {
			return points;
		}
		public double getChance() {
			return chanceToHit;
		}
	}
}
