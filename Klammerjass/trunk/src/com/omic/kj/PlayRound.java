package com.omic.kj;

import com.omic.kj.shared.domain.Player;

final class PlayRound {
  private Player winner;
  private int points;
  
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
}
