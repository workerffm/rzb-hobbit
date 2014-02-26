package com.omic.kj.shared.domain;

public class GameHistory {
	public GameHistory(int gameNr, int points, String winner) {
		this.setGameNr(gameNr);
		this.points = points;
		this.winner = winner;
	}
	private int gameNr;
	private int points;
	private String winner;

	
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public String getWinner() {
		return winner;
	}
	public void setWinner(String winner) {
		this.winner = winner;
	}
	public int getGameNr() {
		return gameNr;
	}
	public void setGameNr(int gameNr) {
		this.gameNr = gameNr;
	}
}
