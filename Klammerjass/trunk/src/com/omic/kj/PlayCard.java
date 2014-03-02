package com.omic.kj;

import com.omic.kj.shared.domain.*;

final class PlayCard {
	
	private final Karte karte;

	/** Runden Nummer 1 .. (32 div PlayerCount) */
	private int stichNr; 
	
	/** Reihenfolge innerhalb vom Stich, 0..playercount-1*/
	private int bidNr; 
	
	/** Karte gehoert ... */
	private Player owner;

	public PlayCard(Karte karte) {
		this.karte = karte;
	}

	public void setOwner(Player player) {
		this.owner = player;
	}

	public Player getOwner() {
		return owner;
	}

	/**
	 * @return Karte is assigned to an owner/player and still on the hand, not played.
	 */
	public boolean isUnplayed() {
		return owner != null && /*!original && */stichNr == 0;
	}

	/**
	 * @return Karte is unassigned and has no owner.
	 */
	public boolean isFree() {
		return owner == null;
	}

	private static String padr(String value, int length) {
		if (value == null)
			value = "";
		while (value.length() < length) {
			value += " ";
		}
		return value.substring(0, length);
	}

	public Farbe getFarbe() {
		return karte.getFarbe();
	}

	public Kartenwert getWert() {
		return karte.getWert();
	}

	public boolean equals(Farbe c, Kartenwert n) {
		if (c == null || n == null)
			return false;
		return (c == karte.getFarbe() && n == karte.getWert());
	}

	public boolean equals(Karte c) {
		if (c == null)
			return false;
		return (karte.getFarbe() == c.getFarbe() && karte.getWert() == c.getWert());
	}

	public Karte getKarte() {
		return karte;
	}

	/**
	 * @param Farbe TrumpfFarbe
	 * @return True, wenn Trumpf Bube?
	 */
	public boolean isJass(Farbe f) {
		return karte.getFarbe() == f && karte.getWert() == Kartenwert.Bube;
	}

	/**
	 * @param Farbe TrumpfFarbe
	 * @return True, wenn Trumpf 9?
	 */
	public boolean isMie(Farbe f) {
		return karte.getFarbe() == f && karte.getWert() == Kartenwert.Neun;
	}

	/**
	 * Rankfolge der Karte, wird nach Trumpffestlegung neu berechnet!!
	 */
	public int getRank(Farbe trumpf) {
		return karte.getRank(trumpf);
	}

	public int getPoints() {
		return karte.getPunkte();
	}

	public int getStichNr() {
		return stichNr;
	}

	public void setStichNr(int stichNr) {
		this.stichNr = stichNr;
	}

	public int getBidNr() {
		return bidNr;
	}

	public void setBidNr(int bidNr) {
		this.bidNr = bidNr;
	}

	public String toString() {
		return "[" + stichNr + "|" + getBidNr() + "|" + padr((owner != null ? owner.getUsername() : null), 15) + "|" + (isFree() ? "free" : "used") + "|"
		//+padr((original?"orig.":""),5)+"|"
		//+(original?"trumpf":"")+"|"
				+ karte.toString() + "]";
	}
}
