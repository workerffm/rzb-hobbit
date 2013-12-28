package com.omic.kj.shared.domain;

import java.util.Set;

/** 
 * Message from Server to Client with round specific player info,
 * should be used to render the player 
 */
public class PlayerInfo {
	private int playerId;
	private String gameName, playerName, teamName;
	private int position, runde, punkte;
	private Boolean playerActive;
	private Farbe trumpf;
	private String geber, aufspieler;
	private Set<Handkarte> karten;
	
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getRunde() {
		return runde;
	}
	public void setRunde(int runde) {
		this.runde = runde;
	}
	public int getPunkte() {
		return punkte;
	}
	public void setPunkte(int punkte) {
		this.punkte = punkte;
	}
	public Boolean getPlayerActive() {
		return playerActive;
	}
	public void setPlayerActive(Boolean playerActive) {
		this.playerActive = playerActive;
	}
	public Farbe getTrumpf() {
		return trumpf;
	}
	public void setTrumpf(Farbe trumpf) {
		this.trumpf = trumpf;
	}
	public String getGeber() {
		return geber;
	}
	public void setGeber(String geber) {
		this.geber = geber;
	}
	public String getAufspieler() {
		return aufspieler;
	}
	public void setAufspieler(String aufspieler) {
		this.aufspieler = aufspieler;
	}
	public Set<Handkarte> getKarten() {
		return karten;
	}
	public void setKarten(Set<Handkarte> karten) {
		this.karten = karten;
	}
}
