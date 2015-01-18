package control;

import java.io.IOException;
import java.util.ArrayList;

import model.Card;
import model.Player;
/**
 * A class to create and set up the game after the user has choosen the game options.
 * @author Åke Ekmark, Andreas Wieselqvist
 *
 */
public class GameHandler{
	private GameCreator gameCreator;
	private Rules rules;
	private Logger logger;
	private int round;
	private int stage;
	private Player comp1;
	private Player comp2;
	private boolean gameGoing = true;
	private static final int FOLD = -1;
	private static final int CHECK = 0;
	private static final int CALL = 1;
	private static final int RAISE = 2;
	/**
	 * Constructor that plays the game until someone has no blinds left.
	 * Creates the gameCreator and starts all the handlers.
	 * @param nbrOfOpponents
	 * @param difficulty
	 */
	public GameHandler(int nbrOfOpponents, int difficulty) {
		gameCreator = new GameCreator(nbrOfOpponents, difficulty);
		gameCreator.createHandlers();
		rules = gameCreator.getRules();
		logger = gameCreator.getLogger();
		comp1 = gameCreator.getPlayerHandler().getPlayers().get(0);
		comp2 = gameCreator.getPlayerHandler().getPlayers().get(1);
		System.out.println("Game Begins");
		int games = 0;
		while (comp1.getBlinds() > 0 && comp2.getBlinds() > 0){
			playGame();
			games++;
		}
		logger.addString("Someone won ! after "+games +" nbr of games");
		logger.addString("Computer 1 has " +comp1.getBlinds() +"blinds");
		logger.addString("Computer 2 has " +comp2.getBlinds() +"blinds");
		try {
			logger.endString();
		} catch (IOException e) {
			System.out.println("File was not found, game was not saved.");
			e.printStackTrace();
		}
		System.out.println("Game Ended");
	}
	/**
	 * A method to play a game of holdem, runs through a whole game until someone folds
	 * or a winner is decided. 
	 */
	private void playGame() {
		logger.addString("Computer 1 has " +comp1.getBlinds() +"blinds");
		logger.addString("Computer 2 has " +comp2.getBlinds() +"blinds");
		setUpGame();
		int move= 0;
		Player winner = null;
		Player compTurn;
		int cTurn = 0;
		
		
		while (gameGoing) {
			//pre-flop
			for (Player player : gameCreator.getPlayerHandler().getPlayers()) {
				logger.addString(player.getName());
				player.setOpponentHandStrength(3.0);
				player.setMyHandStrength(rules.chenFormula(player.getCardsOnHand()));
			}
			comp1.removeBlind();
			comp2.removeBlind();
			gameCreator.getBoardHandler().getBoard().addBlind(2);
			
			while ((stage < 4 && round < 4) && gameGoing) {
				
				if (round == 1 && gameCreator.getBoardHandler().getCardsOnBoard().size() < 3 ) {
					logger.addString("");
					logger.addString("Here comes the flop");
					move = 0;
					cTurn++;
					for (int i = 0; i < 3; i++) {
						gameCreator.getBoardHandler().addCardtoBoard(gameCreator.getDeckHandler().getCard(0));
					}
					printPlayersHands();
					printBoard();
				}
				if (round == 2 && gameCreator.getBoardHandler().getCardsOnBoard().size() < 4) {
					logger.addString("");
					logger.addString("Here comes the turn");
					move = 0;
					gameCreator.getBoardHandler().addCardtoBoard(gameCreator.getDeckHandler().getCard(0));
					printPlayersHands();
					printBoard();
				}
				if (round == 3 && gameCreator.getBoardHandler().getCardsOnBoard().size() < 5) {
					logger.addString("");
					logger.addString("Here comes the river");
					move = 0;
					gameCreator.getBoardHandler().addCardtoBoard(gameCreator.getDeckHandler().getCard(0));
					printPlayersHands();
					printBoard();
				}
				logger.addString("");
				logger.addString("round: " +round);
				logger.addString("stage : " + stage);
				logger.addString("Blinds in pot = " +gameCreator.getBoardHandler().getBoard().getBlinds());
				compTurn = gameCreator.getPlayerHandler().getPlayers().get(cTurn%2);
				logger.addString("Player making a move is :" + compTurn.getName());
				switch (move) {
				case FOLD:
					winner = compTurn;
					cTurn++;
					gameGoing = false;
					break;
				case CHECK:
					move = gameCreator.getAiControl().makeMove(compTurn, round, stage, move);
					if (move == 2) {
						logger.addString(compTurn.getName() +" is betting");
					}
					cTurn++;
					stage++;
					// 2 checks in a row
					if (stage == 2 && move == 0) {
						round++;
						stage = 0;
					}
					break;
				case CALL:
					compTurn.setOpponentHandStrength(compTurn.getOpponentHandStrength() + 1.0);
					round++;
					if (stage == 3) {
						cTurn++;
					}
					stage = 0;
					break;
				case RAISE:
					compTurn.setOpponentHandStrength(compTurn.getOpponentHandStrength() + 2.0);
					move = gameCreator.getAiControl().makeMove(compTurn, round, stage, move);
					if (move == 2) {
						logger.addString(compTurn.getName() +" is reraising");
					}
					cTurn++;
					stage++;
					break;
				}	
			}
			if (winner == null) {
				winner = checkWinner();
				gameGoing = false;
			}	
		}
		endGame(winner);		
	}
	private void printPlayersHands() {
		logger.addString(comp1.getName());
		for (Card card : comp1.getCardsOnHand()) {
			logger.addString((card.toString()));
		}
		logger.addString("");
		logger.addString(comp2.getName());
		for (Card card : comp2.getCardsOnHand()) {
			logger.addString(card.toString());
		}
		logger.addString("");
	}
	private void printBoard() {
		for (Card card : gameCreator.getBoardHandler().getCardsOnBoard()) {
			logger.addString("Card on the board : " +card);
		}
	}
	/**
	 * checks the players cards for a winner after the game is over.
	 * @return
	 */
	private Player checkWinner() {
		double comp1Score = 0;
		double comp2Score = 0;
		int comp1StraightFlush = 0;
		int comp2StraightFlush = 0;
		double check;
		ArrayList<Card> board = gameCreator.getBoardHandler().getCardsOnBoard();
		//check for pairs
		check = rules.chancePair(comp1.getCardsOnHand(), board, round);
		if (check > 0) {
			comp1Score = 1;
		}
		check = rules.chancePair(comp2.getCardsOnHand(), board, round);
		if (check > 0) {
			comp2Score = 1;
		}
		
		//check for two pairs
		check = rules.chanceTwoPair(comp1.getCardsOnHand(), board, round);
		if (check > 0) {
			comp1Score = 2;
		}
		check = rules.chanceTwoPair(comp2.getCardsOnHand(), board, round);
		if (check > 0) {
			comp2Score = 2;
		}
		//check for three of a kind
		check = rules.chanceTreeOaK(comp1.getCardsOnHand(), board, round);
		if (check > 0) {
			comp1Score = 3;
		}
		check = rules.chanceTreeOaK(comp2.getCardsOnHand(), board, round);
		if (check > 0) {
			comp2Score = 3;
		}
		//check for straight
		check = rules.chanceStraight(comp1.getCardsOnHand(), board, round);
		if (check > 0) {
			comp1Score = 4;
			comp1StraightFlush++;
		}
		check = rules.chanceStraight(comp2.getCardsOnHand(), board, round);
		if (check > 0) {
			comp2Score = 4;
			comp2StraightFlush++;
		}
		//check for flush
		check = rules.chanceFlush(comp1.getCardsOnHand(), board, round);
		if (check > 1) {
			comp1Score = 5;
			comp1StraightFlush++;
		}
		check = rules.chanceFlush(comp2.getCardsOnHand(), board, round);
		if (check > 1) {
			comp2Score = 5;
			comp2StraightFlush++;
		}
		//check for full house
		
		//check for four of a kind
		check = rules.chanceFourOaK(comp1.getCardsOnHand(), board, round);
		if (check > 0) {
			comp1Score = 7;
		}
		check = rules.chanceFourOaK(comp2.getCardsOnHand(), board, round);
		if (check > 0) {
			comp2Score = 7;
		}
		//check for straight flush
		if (comp1StraightFlush == 2) {
			comp1Score = 8;
		}
		if (comp2StraightFlush == 2) {
			comp2Score = 8;
		}
		logger.addString("computer 1 has score of : "+ comp1Score);
		logger.addString("computer 2 has score of : "+ comp2Score);
		if (comp1Score > comp2Score) {
			return comp1;
		}
		if (comp2Score > comp1Score) {
			return comp2;
		}
		else if (comp1Score == comp2Score){ 
			return null;
		}
		return null;
		
	}
	private void endGame(Player winner) {
		int winnings = gameCreator.getBoardHandler().getBoard().getBlinds();
		if (winner == null) {
			logger.addString("No winner, they split the pot");
			logger.addString("The pot is "+ winnings);
			comp1.addBlinds(Math.round(winnings/2));
			comp2.addBlinds(winnings/2);
		}
		else {
			logger.addString("Player " + winner.getName() +" won");
			
			winner.addBlinds(winnings);
			logger.addString("the winnings were: " + winnings);
		}
		
		
	}
	/**
	 * deals the starting cards to the players and clears the board.
	 * Creates a new deck of cards and shuffles them. 
	 */
	private void setUpGame() {
		round = 0;
		stage = 0;
		gameGoing = true;
		gameCreator.getDeckHandler().newDeck();
		gameCreator.getDeckHandler().shuffleDeck();
		comp1.clearHand();
		comp2.clearHand();
		gameCreator.getBoardHandler().clearBoard();
		for (int i = 0; i < 2; i++) {
			for (Player player : gameCreator.getPlayerHandler().getPlayers()) {
				gameCreator.getPlayerHandler().dealCardToPlayer(player, gameCreator.getDeckHandler().getCard(0));
			}
		}
	}	
}
