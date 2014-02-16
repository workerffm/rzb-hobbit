package com.omic.kj.shared.domain;

import java.util.Set;

/** 
 * Message from Server to Client with round specific player info,
 * should be used to render the player 
 */
public class PlayerInfo {
	private int playerId;
	private String gameName, playerName;
	private int position, runde, punkte, spieler;
	//private boolean aktiv;
	//private boolean trumpfzeigen; // soll spieler die originalkarte offen zeigen?
	private String geber, aufspieler;
	private Farbe trumpf;
	private Set<CardInfo> karten;
	
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

	public int getSpieler() {
		return spieler;
	}
	public void setSpieler(int spieler) {
		this.spieler = spieler;
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
	public Set<CardInfo> getKarten() {
		return karten;
	}
	public void setKarten(Set<CardInfo> karten) {
		this.karten = karten;
	}
	@Override
	public String toString() {
		return "PlayerInfo [playerId=" + playerId + ", gameName=" + gameName + ", playerName=" + playerName + ", position=" + position + ", runde=" + runde + ", punkte=" + punkte + ", spieler=" + spieler
				+ ", geber=" + geber + ", aufspieler=" + aufspieler + ", trumpf=" + trumpf + ", karten=" + karten + "]";
	}

	
}
