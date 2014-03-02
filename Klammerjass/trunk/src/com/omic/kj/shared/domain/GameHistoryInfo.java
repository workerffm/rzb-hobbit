package com.omic.kj.shared.domain;

public class GameHistoryInfo {
  private int points;
  private String winner;
  private int spielNr;

  public GameHistoryInfo(int spielNr, int points, String winner) {
		this.spielNr = spielNr;
		this.points = points;
		this.winner = winner;
	}
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
	public int getSpielNr() {
		return spielNr;
	}
	public void setSpielNr(int spielNr) {
		this.spielNr = spielNr;
	}
	
}
