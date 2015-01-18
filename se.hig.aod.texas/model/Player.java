package model;

import java.util.ArrayList;
/**
 * An abstract class describing a Player.
 * The class is used as a superclass to HumanPlayer and ComputerPlayer.
 * 
 * @author Åke Ekmark, Andreas Wieselqvist och Simon Söderhäll.
 * @param points : number of points the player has
 * @param position : position on the board
 * @param name : the name of the player
 * @param cardsOnHand : an arrayList of the cards the player has on his hand.
 *  
 */
public abstract class Player {
	protected int blinds = 50;
	protected int position;
	protected String name;
	protected ArrayList<Card> cardsOnHand;
	protected double myHandStrength;
	protected double opponentHandStrength;

	public void addBlinds(int blinds) {
		this.blinds += blinds;
	}
	public void removeBlind() {
		this.blinds--;
	}
	public ArrayList<Card> getCardsOnHand() {
		return cardsOnHand;
	}

	public void dealCardToHand(Card card) {
		cardsOnHand.add(card);
	}
	public void clearHand() {
		cardsOnHand.clear();
	}

	public int getBlinds() {
		return blinds;
	}
	public int getPosition() {
		return position;
	}
	public String getName() {
		return name;
	}
	public double getOpponentHandStrength() {
		return opponentHandStrength;
	}
	public void setOpponentHandStrength(double opponentHandStrength) {
		this.opponentHandStrength = opponentHandStrength;
	}
	public double getMyHandStrength() {
		return myHandStrength;
	}
	public void setMyHandStrength(double myHandStrength) {
		this.myHandStrength = myHandStrength;
	}

	
}
