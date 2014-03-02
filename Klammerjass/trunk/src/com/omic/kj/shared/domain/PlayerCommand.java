package com.omic.kj.shared.domain;

import java.util.Arrays;


/** Anweisung an Spieler */
public class PlayerCommand {
	private int playerCommandId;
	private int playerId;
	private int gameId;
	private CommandCode commandCode;
  private ResponseCode[] allowedResponse;
  /** Karte 1 vom Stapel, Frage ob diese Farbe gespielt werden soll */
  private Farbe ersteKarte;
  private PlayerInfo playerInfo; 
  private GameInfo gameInfo;
  private MessageInfo messageInfo;
  
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public int getPlayerCommandId() {
		return playerCommandId;
	}
	public void setPlayerCommandId(int playerCommandId) {
		this.playerCommandId = playerCommandId;
	}
	
	public ResponseCode[] getAllowedResponse() {
		return allowedResponse;
	}
	public void setAllowedResponse(ResponseCode[] allowedResponse) {
		this.allowedResponse = allowedResponse;
	}
	public Farbe getErsteKarte() {
		return ersteKarte;
	}
	public void setErsteKarte(Farbe ersteKarte) {
		this.ersteKarte = ersteKarte;
	}
	public PlayerInfo getInfo() {
		return playerInfo;
	}
	public void setInfo(PlayerInfo info) {
		this.playerInfo = info;
	}
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public GameInfo getGameInfo() {
		return gameInfo;
	}
	public void setGameInfo(GameInfo gameInfo) {
		this.gameInfo = gameInfo;
	}
	@Override
	public String toString() {
		return "PlayerCommand [playerCommandId=" + playerCommandId + ", playerId=" + playerId + ", gameId=" + gameId + ", command=" + getCommandCode() + ", allowedResponse=" + Arrays.toString(allowedResponse)
				+ ", messagInfoe=" + messageInfo + ", ersteKarte=" + ersteKarte + ", playerInfo=" + playerInfo + ", gameInfo=" + gameInfo + "]";
	}
	public CommandCode getCommandCode() {
		return commandCode;
	}
	public void setCommandCode(CommandCode commandCode) {
		this.commandCode = commandCode;
	}
	public MessageInfo getMessageInfo() {
		return messageInfo;
	}
	public void setMessageInfo(MessageInfo messageInfo) {
		this.messageInfo = messageInfo;
	}
	
}
