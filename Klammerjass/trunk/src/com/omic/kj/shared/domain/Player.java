package com.omic.kj.shared.domain;

public class Player extends User {

	/** User notified that he is ready to play */
	private boolean spielbereit;

//	/** Points of current round */
//	private int roundPoints;

	/** Position on table : 1-4 */
	private int position;

	/** is computer player */
	private boolean computer;

	public Player() {
		super();
		setSpielbereit(false);
		position = 0;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean isInMyTeam(Player p) {
		return p.getTeamId() == getTeamId() && p.getId() != getId();
	}

	public boolean isSpielbereit() {
		return spielbereit;
	}

	public boolean getSpielbereit() {
		return spielbereit;
	}

	public void setSpielbereit(boolean spielbereit) {
		this.spielbereit = spielbereit;
	}

	public boolean isComputer() {
		return computer;
	}

	public void setComputer(boolean computer) {
		this.computer = computer;
	}

//	public int getRoundPoints() {
//		return roundPoints;
//	}
//
//	public void setRoundPoints(int roundPoints) {
//		this.roundPoints = roundPoints;
//	}

}
