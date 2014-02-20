package com.omic.kj.shared.domain;

public class MessageInfo {
  private String message;
  private int playerPosition;
  
	public MessageInfo(int playerPosition, String message) {
		this.playerPosition = playerPosition;
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getPlayerPosition() {
		return playerPosition;
	}
	public void setPlayerPosition(int playerPosition) {
		this.playerPosition = playerPosition;
	}
}
