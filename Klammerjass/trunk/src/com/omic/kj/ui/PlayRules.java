package com.omic.kj.ui;

import java.util.List;
import com.omic.kj.shared.domain.Farbe;
import com.omic.kj.shared.domain.Karte;

public class PlayRules {


	public static boolean isValidToPlay(Karte selectedCard, List<Karte> hardCards, List<Karte> roundCards, Farbe trumpf) {
		if(roundCards==null || roundCards.size()==0)
			return true;
		
		// ermittle h�chsten Trumpf im Stich?
		Karte maxTrumpfImStich = null;
		for(Karte g:roundCards) {
			if(g.getFarbe() == trumpf) {
				if(maxTrumpfImStich==null || g.getRank() > maxTrumpfImStich.getRank())
					maxTrumpfImStich = g;
			}
		}
		Karte karte1 = roundCards.get(0);
		if (maxTrumpfImStich==null) {
			if (selectedCard.getFarbe() == karte1.getFarbe()){
				return true;
			}
			// Hat Spieler noch die Farbe?
			if (hatNochFarbe(hardCards, karte1)) {
				if (selectedCard.getFarbe()==trumpf) {
					return true;
				}
			  return false;
			}
			// mu� trumpfen
			else if (hatNochFarbe(hardCards, trumpf)) {
				if (selectedCard.getFarbe()==trumpf) {
					return true;
				}
			  return false;
			}
			return true;
		}
		// Abwerfen ist erlaubt, wenn keine Tr�mpfe mehr vorhanden
		if(!hatNochFarbe(hardCards, karte1) && !hatNochFarbe(hardCards, trumpf)){
		  return true;
		}
		// Es mu� �bertrumpft werden
		if(maxTrumpfImStich!=null){
			if (selectedCard.getRank() > maxTrumpfImStich.getRank())
			  return true;
			else if (selectedCard.getFarbe()==trumpf) {
				// mu� untertrumpfen
				return true;
			}
			return false;
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
