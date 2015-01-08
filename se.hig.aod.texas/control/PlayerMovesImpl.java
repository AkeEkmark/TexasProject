package control;

import model.Player;
/**
 * Implements the interface to make the moves for the players.
 * Checks if the moves are possible and if so makes them and updates the points accordingly.
 * @author Åke Ekmark, Andreas Wieselqvist och Simon Söderhäll.
 *
 */
public class PlayerMovesImpl implements PlayerMoves {
	private BoardHandler boardHandler;
	public PlayerMovesImpl(BoardHandler boardHandler) {
		this.boardHandler = boardHandler;
	}
	@Override
	public int fold(Player player) {
		System.out.println(player.getName() +" is folding");
		return -1;
		
	}
	@Override
	public int check(Player player) {
		System.out.println(player.getName() +" is checking");
		return 0;
	}
	@Override
	public int call(Player player) {
		System.out.println(player.getName() +" is calling");
		player.removeBlind();
		boardHandler.getBoard().addBlind(1);
		return 1;
	}
	@Override
	public int raise(Player player) {
		player.removeBlind();
		boardHandler.getBoard().addBlind(1);
		return 2;
	}

}
