package com.omic.kj;

import com.omic.kj.shared.domain.Player;

final class PlayRound {

	/** Number of round in the game 1..N */
	private int nr;

	/** Number of current bid, 0 .. playercount-1 */
	private int bidNr;

	/** Gewinner der Runde */
	private Player winner;

	/** Spieler der beim aktuellen Stich rausgekommt */
	private Player aufspieler;

	/** Punkte für die Runde */
	private int points;

	public PlayRound() {
	}
	
	public Player getWinner() {
		return winner;
	}

	public void setWinner(Player winner) {
		this.winner = winner;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getNr() {
		return nr;
	}

	public void setNr(int roundNr) {
		this.nr = roundNr;
	}

	public Player getAufspieler() {
		return aufspieler;
	}

	public void setAufspieler(Player aufspieler) {
		this.aufspieler = aufspieler;
	}

	public int getBidNr() {
		return bidNr;
	}

	public void setBidNr(int bidNr) {
		this.bidNr = bidNr;
	}

}
