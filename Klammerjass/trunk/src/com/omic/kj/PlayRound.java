package com.omic.kj;

import com.omic.kj.shared.domain.Player;

final class PlayRound {
  private Player winner;
  private int points;
	private int nr;
	private int currentPosition;
	private int count;
  
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
	@Override
	public String toString() {
		return "PlayRound [winner=" + winner + ", points=" + points + ", nr=" + nr + ", currentPosition=" + currentPosition + "]";
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}


}
