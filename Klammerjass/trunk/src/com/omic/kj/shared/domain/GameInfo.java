package com.omic.kj.shared.domain;

import java.util.List;

/**
 * Initial message after game has been started and after each "askforcard" command.
 */
public class GameInfo {
	private int activePlayerPosition;
	private List<PlayerInfo> playerInfo;
	private List<GameHistoryInfo> gameHistory;
	private int maxPoints;
	private int maxRunden;

	public List<GameHistoryInfo> getGameHistory() {
		return gameHistory;
	}

	public List<PlayerInfo> getPlayerInfo() {
		return playerInfo;
	}

	public void setPlayerInfo(List<PlayerInfo> playerInfo) {
		this.playerInfo = playerInfo;
	}

	public int getActivePlayerPosition() {
		return activePlayerPosition;
	}

	public void setActivePlayerPosition(int activePlayerPosition) {
		this.activePlayerPosition = activePlayerPosition;
	}

	public void setGameHistory(List<GameHistoryInfo> gameHistory) {
		this.gameHistory = gameHistory;
	}

	public int getMaxPoints() {
		return maxPoints;
	}

	public void setMaxPoints(int maxPoints) {
		this.maxPoints = maxPoints;
	}

	public int getMaxRunden() {
		return maxRunden;
	}

	public void setMaxRunden(int maxRunden) {
		this.maxRunden = maxRunden;
	}

}
