package com.omic.kj.shared.domain;

public class GameHistory {
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
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
	public GameHistory(int gameId, int points, String winner) {
		this.gameId = gameId;
		this.points = points;
		this.winner = winner;
	}
	private int gameId;
	private int points;
	private String winner;
}
