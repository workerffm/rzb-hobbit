package com.omic.kj.shared.domain;

/**
 * used for client side rendering
 */
public class CardInfo {

	private final Karte karte;
	private boolean offen;
	private CardPlace cardPlace;
	private int playerPosition; // Spieler Position 1-4

	public CardInfo(Karte karte) {
		this.karte = karte;
	}

	public Karte getKarte() {
		return karte;
	}

	public boolean getOffen() {
		return offen;
	}

	public CardInfo setOffen(boolean offen) {
		this.offen = offen;
		return this;
	}

	public CardPlace getCardPlace() {
		return cardPlace;
	}

	public CardInfo setCardPlace(CardPlace cardPlace) {
		this.cardPlace = cardPlace;
		return this;
	}

	public int getPlayerPosition() {
		return playerPosition;
	}

	public CardInfo setPlayerPosition(int playerPosition) {
		this.playerPosition = playerPosition;
		return this;
	}

}
