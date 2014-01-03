package com.omic.kj.shared.domain;

import java.util.Arrays;


/** Anweisung an Spieler */
public class PlayerCommand {
	private int playerCommandId;
	private int playerId;
	private int gameId;
	private Command command;
  private Response[] allowedResponse;
  private String message;
  /** Karte 1 vom Stapel, Frage ob diese Farbe gespielt werden soll */
  private Farbe ersteKarte;
  private PlayerInfo playerInfo; 
//  private RoundInfo roundInfo; 
  private GameInfo gameInfo;
  
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
	public Command getCommand() {
		return command;
	}
	public void setCommand(Command command) {
		this.command = command;
	}
	public Response[] getAllowedResponse() {
		return allowedResponse;
	}
	public void setAllowedResponse(Response[] allowedResponse) {
		this.allowedResponse = allowedResponse;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
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
		return "PlayerCommand [playerCommandId=" + playerCommandId + ", playerId=" + playerId + ", gameId=" + gameId + ", command=" + command + ", allowedResponse=" + Arrays.toString(allowedResponse)
				+ ", message=" + message + ", ersteKarte=" + ersteKarte + ", playerInfo=" + playerInfo + ", gameInfo=" + gameInfo + "]";
	}
	
}
