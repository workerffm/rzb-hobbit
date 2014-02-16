package com.omic.kj.shared.domain;

/**
 * used for client side rendering
 */
public class CardInfo implements Comparable<CardInfo>{
	
  private Karte karte;
  private CardPlace cardPlace;
	private boolean offen;
	private int position;  //1-4 spieler position
	
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
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	
	public CardPlace getCardPlace() {
		return cardPlace;
	}
	public void setCardPlace(CardPlace cardPlace) {
		this.cardPlace = cardPlace;
	}
	

	@Override
	public String toString() {
		return "CardInfo [karte=" + karte + ", offen=" + offen + ", position=" + position + ", cardPlace=" + cardPlace + "]";
	}
	@Override
	public int compareTo (final CardInfo other) {
		if (karte.getRank()<other.karte.getRank()) return -1;
		if (karte.getRank()>other.karte.getRank()) return 1;
		return 0;
	}

	
}
