package com.omic.kj.shared.domain;

/**
 * used for client side rendering
 */
public class CardInfo {
  private Karte karte;
	private boolean offen;
	private int position;  //spieler position
	private int location;  //0=bereits gespielt/nicht sichtbar, 1=auf Hand, 2=Original, 3=Stich, 4=Stapel
	
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
	public int getLocation() {
		return location;
	}
	public void setLocation(int location) {
		this.location = location;
	}
	@Override
	public String toString() {
		return "CardInfo [karte=" + karte + ", offen=" + offen + ", position=" + position + ", location=" + location + "]";
	}
	
}
