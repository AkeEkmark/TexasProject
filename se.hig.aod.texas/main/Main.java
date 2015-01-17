package main;

import control.GameHandler;


/**
 * Main class of our program
 * @author Åke Ekmark, Andreas Wieselqvist och Simon Söderhäll.
 *
 */



public class Main {
	static GameHandler gameHandler;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		gameHandler = new GameHandler(2, 1);
	}
}
