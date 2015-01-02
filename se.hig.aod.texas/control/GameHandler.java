package control;



import model.Player;
/**
 * A class create and set up the game after the user has choosen the game options.
 * @author Åke Ekmark, Andreas Wieselqvist och Simon Söderhäll.
 *
 */
public class GameHandler{
	private GameCreator gameCreator;
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
		comp1 = gameCreator.getPlayerHandler().getPlayers().get(0);
		comp2 = gameCreator.getPlayerHandler().getPlayers().get(1);
		//while (comp1.getBlinds() > 0 && comp2.getBlinds() > 0){
			playGame();
		//}
	}
	private void playGame() {
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
			
			while (stage < 4 && round < 1 && gameGoing) {
				
				if (round == 1) {
					cTurn++;
					for (int i = 0; i < 3; i++) {
						gameCreator.getBoardHandler().addCardtoBoard(gameCreator.getDeckHandler().getCard(0));
					}
				}
				else if (round >= 2) {
					cTurn++;
					gameCreator.getBoardHandler().addCardtoBoard(gameCreator.getDeckHandler().getCard(0));
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
					move = gameCreator.getAiControl().makeMove(compTurn, round, stage);
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
					stage = 0;
					break;
				case RAISE:
					compTurn.setOpponentHandStrength(compTurn.getOpponentHandStrength() + 2.0);
					move = gameCreator.getAiControl().makeMove(compTurn, round, stage);
					cTurn++;
					stage++;
					break;
				}	
			}
			winner = checkWinner();
			gameGoing = false;
			
		}
		endGame(winner);		
	}
	private Player checkWinner() {
		// TODO Auto-generated method stub
		return null;
	}
	private void endGame(Player winner) {
		System.out.println("Player " + winner.getName() +" won");
		int winnings = gameCreator.getBoardHandler().getBoard().getBlinds();
		winner.addBlinds(winnings);
		System.out.println("the winnings were: " + winnings);
		
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
