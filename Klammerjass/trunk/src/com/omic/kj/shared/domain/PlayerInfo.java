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
	private Boolean active;
	private Farbe trumpf;
	private String geber, aufspieler;
	private Set<Handkarte> karten;
}
