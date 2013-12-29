package com.omic.kj.shared.domain;


public class Player extends User {

	/** User notified that he is ready to play */
	private boolean spielbereit;
	/** current points of player */
	private int punkte; 
	/** Position on table */
	private int position;
	/** is computer player */
	private boolean computer;

	
	public Player() {
		super();
		setSpielbereit(false);
		position=0;
	}


	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getPunkte() {
		return punkte;
	}

	public void setPunkte(int punkte) {
		this.punkte = punkte;
	}

  // -------------------------------------------------------------------
  // -------------------------------------------------------------------
  // -------------------------------------------------------------------
	
	public String toString() {
		return "[id="+getId()+", uername="+getUsername()+", posi="+position+", team="+getTeamId()+", readyToPlay="+getSpielbereit()+", points="+getPunkte()+"]";
	}

	public boolean isInMyTeam (Player p) {
		return p.getTeamId()==getTeamId() && p.getId()!=getId();
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

	//	public void addPoints (int p) {
//		punkte += p;
//	}
}
