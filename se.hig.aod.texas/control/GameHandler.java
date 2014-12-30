package control;



import model.Player;
/**
 * A class create and set up the game after the user has choosen the game options.
 * @author Åke Ekmark, Andreas Wieselqvist och Simon Söderhäll.
 *
 */
public class GameHandler{
	private GameCreator gameCreator;
	private int round = 0;

	public GameHandler(int nbrOfOpponents, int difficulty) {
		gameCreator = new GameCreator(nbrOfOpponents, difficulty);
		gameCreator.createHandlers();
		setUpGame();
		
	}
	/**
	 * deals the starting cards to the players.
	 */
	private void setUpGame() {
		gameCreator.getDeckHandler().shuffleDeck();
		for (int i = 0; i < 2; i++) {
			for (Player player : gameCreator.getPlayerHandler().getPlayers()) {
				gameCreator.getPlayerHandler().dealCardToPlayer(player, gameCreator.getDeckHandler().getCard(0));
			}
		}
	}
	private void newGame() {
		
	}
	
}
