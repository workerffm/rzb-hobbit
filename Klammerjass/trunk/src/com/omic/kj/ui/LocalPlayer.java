package com.omic.kj.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.omic.kj.local.LocalGameConnector;
import com.omic.kj.shared.PlayerCommandListener;
import com.omic.kj.shared.domain.CardInfo;
import com.omic.kj.shared.domain.CardPlace;
import com.omic.kj.shared.domain.Farbe;
import com.omic.kj.shared.domain.GameInfo;
import com.omic.kj.shared.domain.GameSettings;
import com.omic.kj.shared.domain.Karte;
import com.omic.kj.shared.domain.MessageInfo;
import com.omic.kj.shared.domain.PlayerCommand;
import com.omic.kj.shared.domain.PlayerInfo;
import com.omic.kj.shared.domain.PlayerResponse;
import com.omic.kj.shared.domain.ResponseCode;
import com.omic.kj.shared.domain.User;
import com.omic.kj.shared.game.PlayRules;

class LocalPlayer implements PlayerCommandListener {

	private final Logger log = Logger.getLogger("UI");

	private LocalGameConnector connector;
	private JGamePanel gamedesk;
	private User user;
	private int maxPlayer;
	private final Map<Integer, List<CardInfo>> cardsPerPlace;
	private Comparator<CardInfo> cardSorter;
	private Farbe ersteFarbe;


	LocalPlayer() {
		cardsPerPlace = new HashMap<>();
	}

	public void setGameDesk(JGamePanel gamedesk) {
		this.gamedesk = gamedesk;
	}

	void run(final GameSettings gameSettings) throws Exception {
		maxPlayer = gameSettings.getComputerCount() + 1;
		connector = LocalGameConnector.getConnector();
		user = connector.login(System.getProperty("user.name"), "NO_PWD");
		connector.addPlayerCommandListener(user, this);
		connector.startGame(user, gameSettings);
	}

	@Override
	public void onMessage(final PlayerCommand command) {
		try {

			log.info("PlayerCommand received: " + command.getPlayerId()+" - "+command.getCommandCode().toString());
			
			// Filter für user, bekommt immer alle Meldungen für alle Spieler !!
			if (command.getPlayerId() == user.getId()) {

				//log.info("game command received: " + command.getCommandCode().toString());

				switch (command.getCommandCode()) {
				case gameReset: {
					cardSorter = null;
					ersteFarbe = null;
					break;
				}
				case gameInfo: {
					showGameInfo(command.getGameInfo());
					break;
				}
				case playerinfo: {
					final PlayerInfo info = command.getInfo();
					showPlayerInfo(info);
					break;
				}
				case frageOriginal: {
					final PlayerInfo info = command.getInfo();
					showPlayerInfo(info);
					this.ersteFarbe = command.getErsteFarbe();
					ResponseCode responseCode = gamedesk.askUser(command.getErsteFarbe() + " spielen?", command.getAllowedResponse());
					PlayerResponse response = new PlayerResponse();
					response.setPlayerId(info.getPlayerId());
					response.setResponseCode(responseCode==null ? ResponseCode.nein : responseCode);
					connector.playerResponse(response);
					break;
				}
				
				case frageKleines:{
					final PlayerInfo info = command.getInfo();
					showPlayerInfo(info);
					ResponseCode responseCode = gamedesk.askUser("Kleines spielen?", command.getAllowedResponse());
					PlayerResponse response = new PlayerResponse();
					response.setPlayerId(info.getPlayerId());
					response.setResponseCode(responseCode==null ? ResponseCode.nein : responseCode);
					connector.playerResponse(response);
					break;
				}

				case frageTrumpffarbe:{
					final PlayerInfo info = command.getInfo();
					showPlayerInfo(info);
					Farbe f=null;
					while(f==null) {
				    f = gamedesk.askUserFarbe("Heißt?",this.ersteFarbe);
					}
					PlayerResponse response = new PlayerResponse();
					response.setPlayerId(info.getPlayerId());
					response.setResponseCode(ResponseCode.waehleFarbe);
					response.setFarbe(f);
					connector.playerResponse(response);
					break;
				}
				
				case frageBesser:{
					final PlayerInfo info = command.getInfo();
					showPlayerInfo(info);
					ResponseCode responseCode = gamedesk.askUser("Besser?", command.getAllowedResponse());
					PlayerResponse response = new PlayerResponse();
					response.setPlayerId(info.getPlayerId());
					response.setResponseCode(responseCode==null ? ResponseCode.nein : responseCode);
					connector.playerResponse(response);
					break;
				}
					
				case spieleKarte: {
					final PlayerInfo info = command.getInfo();
					showPlayerInfo(info);
					Karte selectedCard=null;
					final List<Karte> roundCards = extractCards (info.getKarten(), CardPlace.Bid, 0); 
					final List<Karte> handCards = extractCards (info.getKarten(), CardPlace.Hand, info.getPosition()); 
					for(;;) {
					  Future<Karte> card = gamedesk.askForCard();
					  selectedCard = card.get();
					  if (PlayRules.isValidToPlay(selectedCard, handCards, roundCards, info.getTrumpf())){
					  	break;
					  }
					  else {
					  	gamedesk.showBubble(info.getPosition(), "falsch");	
					  }
					}
					PlayerResponse response = new PlayerResponse();
					response.setPlayerId(info.getPlayerId());
					response.setResponseCode(ResponseCode.play);
					response.setGespielteKarte(selectedCard);
					connector.playerResponse(response);
					break;
				}
			
//				case gameFinish: {
//					GameInfo info = command.getGameInfo();
//					showGameInfo(info);
//					break;
//				}
				case say: {
					MessageInfo mi = command.getMessageInfo();
					if (mi != null) {
						gamedesk.showBubble(mi.getPlayerPosition(), mi.getMessage());
					}
				}
					break;
				case tauscheSieben:
					break;
				case zeigeFuenfzig:
					break;
				case zeigeTerz:
					break;
				default:
					break;
				}
			} // My personal commands

			// Command for other user to render
			// Command for other user to render
			// Command for other user to render
			else {
				switch (command.getCommandCode()) {
				case say: {
					MessageInfo mi = command.getMessageInfo();
					if (mi != null) {
						gamedesk.showBubble(mi.getPlayerPosition(), mi.getMessage());
					}
				}
				default:
					// Ignore all other commands
				}
			}

		} catch (Exception x) {
			log.log(Level.SEVERE, "failed", x);
		}
	}

	private void showGameInfo(GameInfo gameInfo) {
		if (gameInfo != null) {
			gamedesk.showGameInfo(gameInfo);
		}
	}

	private void showPlayerInfo(final PlayerInfo info) {

		// Remove last card sortage
		for (List<CardInfo> list : cardsPerPlace.values()) {
			list.clear();
		}

		// Distribute the card to each place on the game desk
		int originalPosition = 0;
		for (CardInfo c : info.getKarten()) {
			int deskPlaceId = 0;
			if (c.getCardPlace() == CardPlace.Bid)
				deskPlaceId = 5;
			else if (c.getCardPlace() == CardPlace.Stock)
				deskPlaceId = 0; //dont show STOCK ! // 6;
			else if (c.getCardPlace() == CardPlace.Original) {
				deskPlaceId = 7;
				originalPosition = c.getPlayerPosition();
			} else if (c.getCardPlace() == CardPlace.Hand)
				deskPlaceId = c.getPlayerPosition();

			if (deskPlaceId > 0) {
				List<CardInfo> list = cardsPerPlace.get(Integer.valueOf(deskPlaceId));
				if (list == null) {
					list = new ArrayList<>();
					cardsPerPlace.put(Integer.valueOf(deskPlaceId), list);
				}
				list.add(c);
			}
		}

		if (cardSorter==null && info.getTrumpf()!=null){
			cardSorter = new CardInfoSorter (info.getTrumpf());
		}
		// Now put the cards on the table....
		gamedesk.setStatus(info);
		gamedesk.setOriginalPosition(originalPosition);
		for (Integer place : cardsPerPlace.keySet()) {
			List<CardInfo> cards = cardsPerPlace.get(place);
			if(place.intValue()==1 && cardSorter!=null) {
				Collections.sort(cards,cardSorter);
			}
			gamedesk.setCards(place, cards);
			pause();
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


	private void pause() {
		try {
			Thread.sleep(150);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
