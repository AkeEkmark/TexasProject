package control;




/**
 * A class to create the game and all the controllers needed for the game to function.
 * keeps references of the handlers for other classes to get.
 * @author Åke Ekmark, Andreas Wieselqvist och Simon Söderhäll.
 *
 */
public class GameCreator {
	private BoardHandler boardHandler;
	private DeckHandler deckHandler;
	private PlayerHandler playerHandler;
	private PlayerMoves playerMoves;
	private AiControl aiControl;
	private Rules rules;
	private Logger logger;
	private int nbrOfOpponents;
	private int difficulty;

	
	public GameCreator(int nbrOfOpponents, int difficulty) {
		this.nbrOfOpponents = nbrOfOpponents;
		this.difficulty = difficulty;
	}

	public void createHandlers() {
		logger = new Logger();
		rules = new Rules(logger);
		deckHandler = new DeckHandlerImpl();
		boardHandler = new BoardHandlerImpl();
		playerHandler = new PlayerHandlerImpl(nbrOfOpponents, difficulty, boardHandler);
		playerMoves = new PlayerMovesImpl(boardHandler, logger);
		aiControl = new AiControl(boardHandler, playerMoves, rules, logger);
		
	}

	public BoardHandler getBoardHandler() {
		return boardHandler;
	}

	public DeckHandler getDeckHandler() {
		return deckHandler;
	}

	public PlayerHandler getPlayerHandler() {
		return playerHandler;
	}

	public PlayerMoves getPlayerMoves() {
		return playerMoves;
	}

	public AiControl getAiControl() {
		return aiControl;
	}

	public int getNbrOfOpponents() {
		return nbrOfOpponents;
	}

	public int getDifficulty() {
		return difficulty;
	}
	public Rules getRules() {
		return rules;
	}
	public Logger getLogger() {
		return logger;
	}
}
