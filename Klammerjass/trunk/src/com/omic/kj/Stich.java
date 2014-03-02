package com.omic.kj;


final class Stich {

	/** Number of round in the game 1..N */
	private int stichNr;

	/** Number of current bid, 0 .. playercount-1 */
	private int count;

	/** Gewinner der Runde */
	private Player winner;

	/** Spieler der beim aktuellen Stich rausgekommt */
	private Player starter;

	/** Punkte für die Runde */
	private int points;

	public Stich() {
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

	public int getStichNr() {
		return stichNr;
	}

	public void setNr(int roundNr) {
		this.stichNr = roundNr;
	}

	public Player getStarter() {
		return starter;
	}

	public void setStarter(Player starter) {
		this.starter = starter;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "Stich [stichNr=" + stichNr + ", count=" + count + ", winner=" + winner + ", starter=" + starter + ", points=" + points + "]";
	}

}
