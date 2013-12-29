package com.omic.kj.shared.domain;

import java.util.Arrays;


/** Anweisung an Spieler */
public class PlayerCommand {
	private int playerCommandId;
	private int playerId;
	private int teamId;
	private Command command;
  private Response[] allowedResponse;
  private String message;
  /** Karte 1 vom Stapel, Frage ob diese Farbe gespielt werden soll */
  private Farbe ersteKarte;
  private PlayerInfo info; 
  
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
		return info;
	}
	public void setInfo(PlayerInfo info) {
		this.info = info;
	}
	public int getTeamId() {
		return teamId;
	}
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
	@Override
	public String toString() {
		return "PlayerCommand [playerCommandId=" + playerCommandId + ", playerId=" + playerId + ", teamId=" + teamId + ", command=" + command + ", allowedResponse=" + Arrays.toString(allowedResponse)
				+ ", message=" + message + ", ersteKarte=" + ersteKarte + ", info=" + info + "]";
	}
	
}
