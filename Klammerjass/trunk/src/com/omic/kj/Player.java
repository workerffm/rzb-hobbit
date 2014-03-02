package com.omic.kj;

import com.omic.kj.shared.domain.User;

class Player extends User {

	/** User notified that he is ready to play */
	private boolean spielbereit;

	/** Position on table : 1-4 */
	private int position;

	/** Team 1: Spieler 1+3, Team 2: Spieler 2+4 */
	private int teamId;
	
	/** is computer player */
	private boolean computer;

	public Player() {
		super();
		setSpielbereit(false);
		position = 0;
		teamId=0;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
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

	@Override
	public String toString() {
		return "Player [position=" + position + ", name=" + getUsername() + "]";
	}

}
