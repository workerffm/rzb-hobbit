package com.omic.kj.shared.domain;


public class Player extends User {

	/** User notified that he is ready to play */
	private boolean spielbereit;
	/** current points of player */
	private int punkte; 
	/** user belongs to this team/table */
	private int teamId;
	/** Position on table */
	private int position;

	
	public Player() {
		super();
		setSpielbereit(false);
		teamId=0;
		position=0;
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
		return "[id="+getId()+", uername="+getUsername()+", posi="+position+", team="+teamId+", readyToPlay="+getSpielbereit()+", points="+getPunkte()+"]";
	}

	public boolean isInMyTeam (Player p) {
		return p.getTeamId()==teamId && p.getId()!=getId();
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

	//	public void addPoints (int p) {
//		punkte += p;
//	}
}
