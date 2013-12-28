package com.omic.kj.shared.domain;

/**
 * used for client side rendering
 */
public class Handkarte {
  private Karte karte;
	private Boolean offen;
	
  public Karte getKarte() {
		return karte;
	}
	public void setKarte(Karte karte) {
		this.karte = karte;
	}
	public Boolean getOffen() {
		return offen;
	}
	public void setOffen(Boolean offen) {
		this.offen = offen;
	}
}
