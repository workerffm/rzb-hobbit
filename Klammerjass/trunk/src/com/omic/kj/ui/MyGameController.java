package com.omic.kj.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import com.omic.kj.local.LocalGameConnector;
import com.omic.kj.shared.PlayerCommandListener;
import com.omic.kj.shared.domain.CardInfo;
import com.omic.kj.shared.domain.CardPlace;
import com.omic.kj.shared.domain.GameSettings;
import com.omic.kj.shared.domain.PlayerCommand;
import com.omic.kj.shared.domain.PlayerInfo;
import com.omic.kj.shared.domain.PlayerResponse;
import com.omic.kj.shared.domain.ResponseCode;
import com.omic.kj.shared.domain.User;

class MyGameController implements PlayerCommandListener {

	private final Logger log = Logger.getLogger("UI");

	private JGameDesk gamedesk;
	private LocalGameConnector connector;
	private User user;

	private PlayerInfo playerInfo;
	private int maxPlayer;

	private final Map<Integer, List<CardInfo>> cardsPerPlace;

	MyGameController() {
		cardsPerPlace = new HashMap<>();
	}

	public void setGameDesk(JGameDesk gamedesk) {
		this.gamedesk = gamedesk;
	}

	void run(final GameSettings gameSettings) throws Exception {
		maxPlayer = gameSettings.getComputerCount() + 1;
		connector = LocalGameConnector.getConnector();
		user = connector.login("markus", "xxx");
		connector.addPlayerCommandListener(user, this);
		connector.startGame(user, gameSettings);
	}

	@Override
	public void onMessage(PlayerCommand command) {

		log.info(command.toString());

		// Filter für user, bekommt immer alle Meldungen für alle Spieler !!

		if (command.getPlayerId() == user.getId()) {
			switch (command.getCommandCode()) {
			case playerinfo: {
				playerInfo = command.getInfo();
				showInfo();
				break;
			}
			case frageOriginal: {
				PlayerResponse response = new PlayerResponse();
				response.setPlayerId(playerInfo.getPlayerId());
				response.setResponseCode(ResponseCode.ja);
				connector.playerResponse(response);
				break;
			}
			case spieleKarte: {
				playerInfo = command.getInfo();
				showInfo();
				PlayerResponse response = new PlayerResponse();
				response.setPlayerId(playerInfo.getPlayerId());
				response.setResponseCode(ResponseCode.play);
				for (CardInfo i : playerInfo.getKarten()) {
					if (i.getPosition() == playerInfo.getPosition() && i.getCardPlace() == CardPlace.Hand) {
						response.setGespielteKarte(i.getKarte());
					}
				}
				//connector.playerResponse(response);
				break;
			}
			}
		}
	}

	private void showInfo() {
		
		// Remove last card sortage
		for (List<CardInfo> list : cardsPerPlace.values()) {
			list.clear();
		}
		
		// Distribute the card to each place on the game desk
		int originalPosition=0;
		for (CardInfo c : playerInfo.getKarten()) {
			int deskPlaceId = 0;
			if (c.getCardPlace() == CardPlace.Bid)
				deskPlaceId = 5;
			else if (c.getCardPlace() == CardPlace.Stock)
				deskPlaceId = 0; //dont show STOCK ! // 6;
			else if (c.getCardPlace() == CardPlace.Original) {
				deskPlaceId = 7;
				originalPosition = c.getPosition();
			}
			else if (c.getCardPlace() == CardPlace.Hand)
				deskPlaceId = c.getPosition();

			if (deskPlaceId > 0) {
				List<CardInfo> list = cardsPerPlace.get(Integer.valueOf(deskPlaceId));
				if (list == null) {
					list = new ArrayList<>();
					cardsPerPlace.put(Integer.valueOf(deskPlaceId), list);
				}
				list.add(c);
			}
		}
		
    // Now put the cards on the table....
		gamedesk.setStatus(playerInfo.getAufspieler(), playerInfo.getTrumpf(), playerInfo.getPunkte(), playerInfo.getRunde());
		gamedesk.setOriginalPosition (originalPosition);
		for (Integer place : cardsPerPlace.keySet()) {
			gamedesk.setCards (place, cardsPerPlace.get(place));
			pause();
		}
	}

	private void pause() {
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
