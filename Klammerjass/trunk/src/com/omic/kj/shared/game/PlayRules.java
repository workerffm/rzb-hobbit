package com.omic.kj.shared.game;

import java.util.List;
import com.omic.kj.shared.domain.Farbe;
import com.omic.kj.shared.domain.Karte;

public class PlayRules {


  /**
   * Return true, if the card is allowed to play out.
	 */
	public static boolean isValidToPlay(Karte selectedCard, List<Karte> hardCards, List<Karte> roundCards, Farbe trumpf) {
		
		// Selbst rauskommen?
		if(roundCards==null || roundCards.size()==0)
			return true;
		
		final Karte karte1 = roundCards.get(0);
		if(karte1.getFarbe()!=trumpf){
			if (selectedCard.getFarbe() == karte1.getFarbe()){
				return true;
			}
			// Hat Spieler noch die Farbe?
			if (hatNochFarbe(hardCards, karte1)) {
				if (selectedCard.getFarbe()==karte1.getFarbe()) {
					return true;
				}
			  return false;
			}
			// muﬂ trumpfen
			else if (hatNochFarbe(hardCards, trumpf)) {
				if (selectedCard.getFarbe()==trumpf) {
					return true;
				}
			  return false;
			}
			return true;
		}
		// Abwerfen ist erlaubt, wenn keine Tr¸mpfe mehr vorhanden
		if(!hatNochFarbe(hardCards, karte1) && !hatNochFarbe(hardCards, trumpf)){
		  return true;
		}

		// ermittle hˆchsten Trumpf im Stich?
		Karte maxTrumpfImStich = null;
		for(Karte g:roundCards) {
			if(g.getFarbe() == trumpf) {
				if(maxTrumpfImStich==null || g.getRank(trumpf) > maxTrumpfImStich.getRank(trumpf))
					maxTrumpfImStich = g;
			}
		}

		// Es muﬂ ¸bertrumpft werden
		if(maxTrumpfImStich!=null){
			if (selectedCard.getRank(trumpf) > maxTrumpfImStich.getRank(trumpf))
			  return true;
			else if (selectedCard.getFarbe()==trumpf) {
				// muﬂ untertrumpfen
				return true;
			}
			else if (hatNochFarbe(hardCards, trumpf)) {
				return false;
			}
			return true;
		}
		return true;
	}

	private static boolean hatNochFarbe(List<Karte> hardCards, Farbe f) {
		for (Karte h:hardCards) {
			if(h.getFarbe()==f){
				return true;
			}
		}
		return false;
	}

	private static boolean hatNochFarbe(List<Karte> hardCards, Karte karte1) {
		for (Karte h:hardCards) {
			if(h.getFarbe()==karte1.getFarbe()){
				return true;
			}
		}
		return false;
	}

}
