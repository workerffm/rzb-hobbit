package com.omic.kj;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import com.omic.kj.shared.domain.CardInfo;
import com.omic.kj.shared.domain.CardPlace;
import com.omic.kj.shared.domain.Karte;
import com.omic.kj.shared.domain.Player;
import com.omic.kj.shared.domain.PlayerCommand;
import com.omic.kj.shared.domain.PlayerInfo;
import com.omic.kj.shared.domain.PlayerResponse;
import com.omic.kj.shared.domain.ResponseCode;
import com.omic.kj.ui.PlayRules;

/**
 * Klammerjass : Client execution machine, simulate Computer player
 *  
 * @author cb3arbe
 *
 */
public class ComputerPlayer implements CommandListener {

	private final Logger log = Logger.getLogger("ComputerPlayer");
	private final Player me;
	private PlayerResponseListener listener;
	//private PlayerInfo gameinfo;

	public ComputerPlayer(Player player) {
		this.me = player;
	}

	public Player getPlayer() {
		return me;
	}

	public void setPlayerResponseListener(PlayerResponseListener listener) {
		this.listener = listener;
	}

	@Override
	public void toPlayer(PlayerCommand command) {
		if (command.getPlayerId() == me.getId()) {
			PlayerInfo gameinfo;
			//log.info(command + "");
			switch (command.getCommandCode()) {
			case playerinfo: {
				gameinfo = command.getInfo();
				break;
			}
			case frageOriginal: {
				gameinfo = command.getInfo();
				PlayerResponse response = new PlayerResponse();
				response.setPlayerId(gameinfo.getPlayerId());
				response.setResponseCode(ResponseCode.nein);
				listener.onMessage(response);
				break;
			}
			case spieleKarte: {
				final PlayerInfo info = command.getInfo();
				Karte selectedCard=null;
				final List<Karte> roundCards = extractCards (info.getKarten(), CardPlace.Bid, 0); 
				final List<Karte> handCards = extractCards (info.getKarten(), CardPlace.Hand, info.getPosition()); 
				while(selectedCard==null) {
				  for(Karte k:handCards) {
				    if (PlayRules.isValidToPlay(k, handCards, roundCards, info.getTrumpf())){
				    	selectedCard = k;
				    	break;
				    }
				  }
				}
				final PlayerResponse response = new PlayerResponse();
				response.setPlayerId(info.getPlayerId());
				response.setResponseCode(ResponseCode.play);
				response.setGespielteKarte(selectedCard);
				listener.onMessage(response);
				break;
			}
			
//			case spieleKarte:{
//				gameinfo = command.getInfo();
//				PlayerResponse response = new PlayerResponse();
//				response.setPlayerId(gameinfo.getPlayerId());
//				response.setResponseCode(ResponseCode.play);
//				for (CardInfo i : gameinfo.getKarten()) {
//					if (i.getPlayerPosition() == gameinfo.getPosition() && i.getCardPlace() ==CardPlace.Hand) {
//						response.setGespielteKarte(i.getKarte());
//					}
//				}
//				listener.onMessage(response);
//				break;
//			}
			}
		}
	}
	
	private List<Karte> extractCards(List<CardInfo> karten, CardPlace cardPlace, int position) {
		ArrayList<Karte> list = new ArrayList<>();
		for (CardInfo c : karten) {
			if(c.getCardPlace() == cardPlace && (position==0 ||c.getPlayerPosition() == position))
				list.add(c.getKarte());
		}
		return list;
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
	//	  - mögliche karten zum spielen?
	//	    =1: dann spielen
	//	    >1: spiele ich allein?
	//	            j: höchste spielen
	//	            n: bin ich aufspieler/rauskommer?
	//	               j: höchste karte spielen
	//	               n: ist das bereits user stich?
	//	                   j: höchste karte spielen
	//	                   n: überstechen wenn möglich
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
