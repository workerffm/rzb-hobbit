package com.omic.kj.shared.domain;

/**
 * used for client side rendering
 */
public class CardInfo implements Comparable<CardInfo>{
	
  private Karte karte;
  private boolean offen;
  private CardPlace cardPlace;
	private int playerPosition;  // Spieler Position 1-4
	private int roundPosition;  // Reihenfolge im Stich 1-4, oder 0 (nicht im Stich)
	
  public Karte getKarte() {
		return karte;
	}
	public void setKarte(Karte karte) {
		this.karte = karte;
	}
	public boolean getOffen() {
		return offen;
	}
	public void setOffen(boolean offen) {
		this.offen = offen;
	}
	
	
	public CardPlace getCardPlace() {
		return cardPlace;
	}
	public void setCardPlace(CardPlace cardPlace) {
		this.cardPlace = cardPlace;
	}
	

	
	@Override
	public String toString() {
		return "CardInfo [karte=" + karte + ", offen=" + offen + ", position=" + getPlayerPosition() + ", cardPlace=" + cardPlace + "]";
	}
	@Override
	public int compareTo (final CardInfo other) {
		if (karte.getRank()<other.karte.getRank()) return -1;
		if (karte.getRank()>other.karte.getRank()) return 1;
		return 0;
	}
	public int getPlayerPosition() {
		return playerPosition;
	}
	public void setPlayerPosition(int playerPosition) {
		this.playerPosition = playerPosition;
	}
	public int getRoundPosition() {
		return roundPosition;
	}
	public void setRoundPosition(int roundPosition) {
		this.roundPosition = roundPosition;
	}
	
}
