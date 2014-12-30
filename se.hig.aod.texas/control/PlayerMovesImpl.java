package control;

import java.util.ArrayList;

import model.Card;
import model.Card.Value;
import model.Player;
/**
 * Implements the interface to make the moves for the players.
 * Checks if the moves are possible and if so makes them and updates the points accordingly.
 * @author Åke Ekmark, Andreas Wieselqvist och Simon Söderhäll.
 *
 */
public class PlayerMovesImpl implements PlayerMoves {
	private BoardHandler boardHandler;
	private PointCounter pointCounter;
	public PlayerMovesImpl(BoardHandler boardHandler, PointCounter pointCounter) {
		this.boardHandler = boardHandler;
		this.pointCounter = pointCounter;
	}
	@Override
	public void fold(Player player) {
		System.out.println(player.getName() +" is folding");
		
	}
	@Override
	public void check(Player player) {
		System.out.println(player.getName() +" is checking");
		
	}
	@Override
	public void call(Player player) {
		System.out.println(player.getName() +" is calling");
		
	}
	@Override
	public void raise(Player player) {
		System.out.println(player.getName() +" is raising");
		
	}

}
