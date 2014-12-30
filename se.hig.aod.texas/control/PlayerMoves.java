package control;

import java.util.ArrayList;

import model.Card;
import model.Player;
/**
 * Interface for the players moves.
 *  @author Åke Ekmark, Andreas Wieselqvist och Simon Söderhäll.
 *
 */
public interface PlayerMoves {
	public void fold(Player player);
	public void check(Player player);
	public void call(Player player);
	public void raise(Player player);
}
