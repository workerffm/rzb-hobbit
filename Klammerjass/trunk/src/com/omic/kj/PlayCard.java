package com.omic.kj;

import com.omic.kj.shared.domain.*;


final class PlayCard
{
	/* initialization data: */
	private final Karte karte;
	
  /* game data: */
	private int round;         // trick (engl.) Stich
	private int order;         // Reihenfolge innerhalb vom Stich
	private Player owner;
  
  
  public PlayCard(Karte c) {
  	karte = c;
		round = 0;
  }

	public void setOwner(Player p) {
		owner=p;
	}

	public Player getOwner() {
		return owner;
	}

	/**
	 * @return Karte is assigned to an owner/player and still on the hand, not played.
	 */
	public boolean isUnplayed() {
		return owner!=null && /*!original && */round==0;
	}

	/**
	 * @return Karte is unassigned and has no owner.
	 */
	public boolean isFree() {
		return owner==null;
	}


	public String toString() {
		return "["
		  +round+"|"
		  +order+"|"
		  +padr((owner!=null?owner.getUsername():null),15)+"|"
		  +(isFree()?"free":"used")+"|"
		  //+padr((original?"orig.":""),5)+"|"
		  //+(original?"trumpf":"")+"|"
		  +karte.toString()
		 +"]";
	}

	private static String padr (String value, int length)	{
		if(value==null) value="";
		while (value.length()<length) {
			value += " ";
		}
		return value.substring (0,length);
	}


	public Farbe getFarbe() {
		return karte.getFarbe();
	}

	public boolean equals (Farbe c, Kartenwert n) {
		if(c==null||n==null) return false;
		return  (c==karte.getFarbe() && n==karte.getWert());
	}

	public boolean equals (Karte c) {
		if(c==null) return false;
		return  (karte.getFarbe()==c.getFarbe() && karte.getWert()==c.getWert());
	}

	public void setRound(int round) {
		this.round = round;
	}

	/**
	 * Runde, in der die Karte gespielt wurde.
	 * @return
	 */
	public int getRound() {
		return round;
	}

	public Karte getKarte() {
		return karte;
	}

  /**
   * Reihenfolge innerhalb vom Stich
   * @return
   */
  public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * @param Farbe TrumpfFarbe
	 * @return True, wenn Trumpf Bube?
	 */
	public boolean isJass(Farbe f) {
		return karte.getFarbe()==f && karte.getWert()==Kartenwert.Bube;
	}

	/**
	 * @param Farbe TrumpfFarbe
	 * @return True, wenn Trumpf 9?
	 */
	public boolean isMie(Farbe f) {
		return karte.getFarbe()==f && karte.getWert()==Kartenwert.Neun;
	}

	/**
	 * Rankfolge der Karte, wird nach Trumpffestlegung neu berechnet!!
	 */
	public int getRank (Farbe trumpf) {
    return karte.getRank() + (trumpf==null ? 0 : (karte.getFarbe() == trumpf ? 100 : 0 ));
	}	

}
