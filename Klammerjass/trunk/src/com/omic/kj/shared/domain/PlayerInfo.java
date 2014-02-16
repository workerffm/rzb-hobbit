package com.omic.kj.shared.domain;

import java.util.Set;

/** 
 * Message will be sent after each change of a player (response, card played, etc)
 */
public class PlayerInfo {
	private int playerId;
	private String name;
	private int position, runde, punkte, spieler;
	private boolean active;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
