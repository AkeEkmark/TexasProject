package control;



import model.Card;
import model.Player;
/**
 * A class create and set up the game after the user has choosen the game options.
 * @author Åke Ekmark, Andreas Wieselqvist och Simon Söderhäll.
 *
 */
public class GameHandler{
	private GameCreator gameCreator;
	private Rules rules;
	private int round;
	private int stage;
	private Player comp1;
	private Player comp2;
	private boolean gameGoing = true;
	private static final int FOLD = -1;
	private static final int CHECK = 0;
	private static final int CALL = 1;
	private static final int RAISE = 2;
	public GameHandler(int nbrOfOpponents, int difficulty) {
		gameCreator = new GameCreator(nbrOfOpponents, difficulty);
		gameCreator.createHandlers();
		rules = gameCreator.getRules();
		comp1 = gameCreator.getPlayerHandler().getPlayers().get(0);
		comp2 = gameCreator.getPlayerHandler().getPlayers().get(1);
		while (comp1.getBlinds() > 0 && comp2.getBlinds() > 0){
			playGame();
		}
		System.out.println("Someone won !");
		System.out.println("Computer 1 has " +comp1.getBlinds() +"blinds");
		System.out.println("Computer 2 has " +comp2.getBlinds() +"blinds");
	}
	private void playGame() {
		System.out.println("Computer 1 has " +comp1.getBlinds() +"blinds");
		System.out.println("Computer 2 has " +comp2.getBlinds() +"blinds");
		setUpGame();
		int move= 0;
		Player winner = null;
		Player compTurn;
		int cTurn = 0;
		
		
		while (gameGoing) {
			//pre-flop
			for (Player player : gameCreator.getPlayerHandler().getPlayers()) {
				System.out.println(player.getName());
				player.setOpponentHandStrength(3.0);
				player.setMyHandStrength(gameCreator.getRules().chenFormula(player.getCardsOnHand()));
			}
			comp1.removeBlind();
			comp2.removeBlind();
			gameCreator.getBoardHandler().getBoard().addBlind(2);
			
			while ((stage < 4 && round < 4) && gameGoing) {
				
				if (round == 1 && gameCreator.getBoardHandler().getCardsOnBoard().size() < 3 ) {
					System.out.println();
					System.out.println("Here comes the flop");
					move = 0;
					cTurn++;
					for (int i = 0; i < 3; i++) {
						gameCreator.getBoardHandler().addCardtoBoard(gameCreator.getDeckHandler().getCard(0));
					}
					printPlayersHands();
					printBoard();
				}
				if (round == 2 && gameCreator.getBoardHandler().getCardsOnBoard().size() < 4) {
					System.out.println();
					System.out.println("Here comes the turn");
					cTurn++;
					move = 0;
					gameCreator.getBoardHandler().addCardtoBoard(gameCreator.getDeckHandler().getCard(0));
					printPlayersHands();
					printBoard();
				}
				if (round == 3 && gameCreator.getBoardHandler().getCardsOnBoard().size() < 5) {
					System.out.println();
					System.out.println("Here comes the river");
					cTurn++;
					move = 0;
					gameCreator.getBoardHandler().addCardtoBoard(gameCreator.getDeckHandler().getCard(0));
					printPlayersHands();
					printBoard();
				}
				System.out.println();
				System.out.println("round: " +round);
				System.out.println("stage : " + stage);
				System.out.println("Blinds in pot = " +gameCreator.getBoardHandler().getBoard().getBlinds());
				compTurn = gameCreator.getPlayerHandler().getPlayers().get(cTurn%2);
				System.out.println("Player making a move is :" + compTurn.getName());
				switch (move) {
				case FOLD:
					winner = compTurn;
					cTurn++;
					gameGoing = false;
					break;
				case CHECK:
					move = gameCreator.getAiControl().makeMove(compTurn, round, stage, move);
					if (move == 2) {
						System.out.println(compTurn.getName() +" is betting");
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
						System.out.println(compTurn.getName() +" is reraising");
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
		System.out.println(comp1.getName());
		for (Card card : comp1.getCardsOnHand()) {
			System.out.println(card);
		}
		System.out.println();
		System.out.println(comp2.getName());
		for (Card card : comp2.getCardsOnHand()) {
			System.out.println(card);
		}
		System.out.println();
	}
	private void printBoard() {
		for (Card card : gameCreator.getBoardHandler().getCardsOnBoard()) {
			System.out.println("Card on the board : " +card);
		}
	}
	private Player checkWinner() {
		double comp1Score = 0;
		double comp2Score = 0;
		int comp1StraightFlush = 0;
		int comp2StraightFlush = 0;
		double check;
		//check for pairs
		check = rules.chancePair(comp1.getCardsOnHand(), gameCreator.getBoardHandler().getCardsOnBoard(), round);
		if (check == 1) {
			comp1Score = 1;
		}
		check = rules.chancePair(comp2.getCardsOnHand(), gameCreator.getBoardHandler().getCardsOnBoard(), round);
		if (check == 1) {
			comp2Score = 1;
		}
		
		//check for two pairs
		check = rules.chanceTwoPair(comp1.getCardsOnHand(), gameCreator.getBoardHandler().getCardsOnBoard(), round);
		if (check == 1) {
			comp1Score = 2;
		}
		check = rules.chanceTwoPair(comp2.getCardsOnHand(), gameCreator.getBoardHandler().getCardsOnBoard(), round);
		if (check == 1) {
			comp2Score = 2;
		}
		//check for three of a kind
		check = rules.chanceTreeOaK(comp1.getCardsOnHand(), gameCreator.getBoardHandler().getCardsOnBoard(), round);
		if (check == 1) {
			comp1Score = 3;
		}
		check = rules.chanceTreeOaK(comp2.getCardsOnHand(), gameCreator.getBoardHandler().getCardsOnBoard(), round);
		if (check == 1) {
			comp2Score = 3;
		}
		//check for straight
		check = rules.chanceStraight(comp1.getCardsOnHand(), gameCreator.getBoardHandler().getCardsOnBoard(), round);
		if (check == 1) {
			comp1Score = 4;
			comp1StraightFlush++;
		}
		check = rules.chanceStraight(comp2.getCardsOnHand(), gameCreator.getBoardHandler().getCardsOnBoard(), round);
		if (check == 1) {
			comp2Score = 4;
			comp2StraightFlush++;
		}
		//check for flush
		check = rules.chanceFlush(comp1.getCardsOnHand(), gameCreator.getBoardHandler().getCardsOnBoard(), round);
		if (check == 1) {
			comp1Score = 5;
			comp1StraightFlush++;
		}
		check = rules.chanceFlush(comp2.getCardsOnHand(), gameCreator.getBoardHandler().getCardsOnBoard(), round);
		if (check == 1) {
			comp2Score = 5;
			comp2StraightFlush++;
		}
		//check for full house
		
		//check for four of a kind
		check = rules.chanceFourOaK(comp1.getCardsOnHand(), gameCreator.getBoardHandler().getCardsOnBoard(), round);
		if (check == 1) {
			comp1Score = 7;
		}
		check = rules.chanceFourOaK(comp2.getCardsOnHand(), gameCreator.getBoardHandler().getCardsOnBoard(), round);
		if (check == 1) {
			comp2Score = 7;
		}
		//check for straight flush
		if (comp1StraightFlush == 2) {
			comp1Score = 8;
		}
		if (comp2StraightFlush == 2) {
			comp2Score = 8;
		}
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
			System.out.println("No winner, they split the pot");
			System.out.println("The pot is "+ winnings);
			comp1.addBlinds(winnings/2);
			comp2.addBlinds(winnings/2);
		}
		else {
			System.out.println("Player " + winner.getName() +" won");
			
			winner.addBlinds(winnings);
			System.out.println("the winnings were: " + winnings);
		}
		
		
	}
	/**
	 * deals the starting cards to the players.
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
