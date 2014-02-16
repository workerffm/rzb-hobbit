package com.omic.kj.shared.domain;

import java.util.List;

/**
 * Initial message after game has been started and after each "askforcard" command.
 */
public class GameInfo {
	private List<PlayerInfo> playerInfo;
	private int activePlayerPosition;

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

}
