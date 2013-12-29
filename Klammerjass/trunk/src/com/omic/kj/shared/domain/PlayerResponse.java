package com.omic.kj.shared.domain;

/**
 * Rückmeldung vom Spieler
 *
 * @version 26.12.2013 Markus create file
 *
 */
public class PlayerResponse {
	private int playerCommandId;
	private int playerId;
  private Response response;
  private Farbe farbe;
  private Karte karte;
  
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public Response getResponse() {
		return response;
	}
	public void setResponse(Response response) {
		this.response = response;
	}
	public Farbe getFarbe() {
		return farbe;
	}
	public void setFarbe(Farbe farbe) {
		this.farbe = farbe;
	}

	@Override
	public String toString() {
		return "PlayerResponse [playerCommandId=" + playerCommandId + ", playerId=" + playerId + ", response=" + response + ", farbe=" + farbe + "]";
	}
	public Karte getKarte() {
		return karte;
	}
	public void setKarte(Karte karte) {
		this.karte = karte;
	}
}
