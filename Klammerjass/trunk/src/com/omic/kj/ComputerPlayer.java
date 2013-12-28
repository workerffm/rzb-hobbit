package com.omic.kj;

import java.util.logging.Logger;
import com.omic.kj.shared.domain.Player;
import com.omic.kj.shared.domain.PlayerCommand;

/**
 * Klammerjass : Client execution machine, simulate Computer player
 *  
 * @author cb3arbe
 *
 */
public class ComputerPlayer implements CommandListener  {

	private final Player me;
	private final Logger log;
	private PlayerResponseListener listener;

	public ComputerPlayer(Player player) {
		this.me = player;
		this.log = Logger.getLogger("Player");
	}

	public Player getPlayer() {
		return me;
	}

	public void setPlayerResponseListener(PlayerResponseListener listener) {
		this.listener = listener;
	}

	@Override
	public void toPlayer(PlayerCommand command) {
		
	}

//	@Override
//	public Color askFarbe(Game g) throws GameException {
//		log.fine ("g="+g);
//		return null;
//	}
//
//	@Override
//	public boolean askKleines(Game g) throws GameException {
//		log.fine ("g="+g);
//		return false;
//	}
//
//	@Override
//	public boolean askOriginal(Game g) throws GameException {
//		log.fine ("g="+g);
//		return false;
//	}
//
//	@Override
//	public boolean askTausche7(Game g) throws GameException {
//		log.fine ("g="+g);
//		return true;
//	}
//
//	@Override
//	public void giveCard(Game g) throws GameException {
//		log.fine ("g="+g);
//	}
//
//	@Override
//	public void updateRound(Game g) throws GameException {
//		log.fine ("g="+g);
//	}
//
//	public void msgFromCoPlayer(Player p, String message) throws GameException {
//		log.fine ("p="+p+", msg="+message);
//	}
//
//	/**
//	  - m�gliche karten zum spielen?
//	    =1: dann spielen
//	    >1: spiele ich allein?
//	            j: h�chste spielen
//	            n: bin ich aufspieler/rauskommer?
//	               j: h�chste karte spielen
//	               n: ist das bereits user stich?
//	                   j: h�chste karte spielen
//	                   n: �berstechen wenn m�glich
//   	  =0: spiele ich allein?
//   	       j: bin ich aufspieler/rauskommer?
//   	           j: hohe karte spielen
//   	           n: niedrige karte spielen 
//   	       n: ist das bereits unser stich?
//   	           j: hohe karte abwerfen
//   	           n: niedrige karte abwerfen
//	 */ 
//	@Override
//	public Card askCard(Game game) throws GameException {
//		List<PlayCard> c = game.getCardsOnHand(me);
//		c = game.getCardsToPlay(me);
//		Card card=null;
//		if (c.size()==1) {
//			card = c.get(0).getCard();
//		}
//		else if (c.size()>1) {
//		  if(game.isSinglePlayer(me))
//		  	card = game.getHighestCard(me);
//		  else
//		  	if (game.isFirstPlayerInRound(me)) //komme ich raus?
//			  	card = game.getHighestCard(me);
//		  	else
//		  		if (game.isOurRound(me))
//				  	card = game.getHighestCardNonTrumpfBedienen(me);
//		  		else
//		  			card = game.getLowestStrikeCardBedienen(me);
//		}
//		else if (c.size()==0) {
//		  if(game.isSinglePlayer(me))
//		  	if (game.isFirstPlayerInRound(me)) //komme ich raus?
//		  		card = game.getHighestCard(me);
//	  	  else
//	    		card = game.getLowestCardBedienen(me);
//		  else
//	  		if (game.isOurRound(me))
//			  	card = game.getHighestCardNonTrumpfBedienen(me);
//	  		else
//			  	card = game.getLowestCardNonTrumpfBedienen(me);
//		}
//		return card;
//	}
//
}