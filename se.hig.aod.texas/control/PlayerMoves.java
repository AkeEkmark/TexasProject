package control;

import model.Player;
/**
 * Interface for the players moves.
 *  @author Åke Ekmark, Andreas Wieselqvist och Simon Söderhäll.
 *
 */
public interface PlayerMoves {
	public int fold(Player player);
	public int check(Player player);
	public int call(Player player);
	public int raise(Player player);
}
