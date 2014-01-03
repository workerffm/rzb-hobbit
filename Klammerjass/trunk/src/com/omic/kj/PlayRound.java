package com.omic.kj;

import com.omic.kj.shared.domain.Player;

final class PlayRound {
  private Player winner;
  private int points;
	private int nr;
	private int currentPosition;
  
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
	public int getCurrentPosition() {
		return currentPosition;
	}
	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}


}
