package com.omic.kj.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import com.omic.kj.local.LocalGameConnector;
import com.omic.kj.shared.PlayerCommandListener;
import com.omic.kj.shared.domain.CardInfo;
import com.omic.kj.shared.domain.GameSettings;
import com.omic.kj.shared.domain.Karte;
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


	MyGameController() {}

	public void setGameDesk(JGameDesk gamedesk) {
		this.gamedesk = gamedesk;
	}

	void run(final GameSettings gameSettings) throws Exception {
		maxPlayer = gameSettings.getComputerCount()+1;
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
				PlayerResponse response = new PlayerResponse();
				response.setPlayerId(playerInfo.getPlayerId());
				response.setResponseCode(ResponseCode.play);
				for (CardInfo i : playerInfo.getKarten()) {
					if (i.getPosition() == playerInfo.getPosition() && i.getLocation() == 1) {
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
		final List<Karte> handcards = new ArrayList<>();
		for (int p=1; p <= maxPlayer; p++) {
			handcards.clear();
			for(CardInfo c:playerInfo.getKarten()) {
				if(c.getLocation()==1/* auf hand*/ && c.getPosition()==p /* Spielerplatz */){
				  handcards.add(c.getKarte());
				}
			}
  		gamedesk.setCards(p,handcards);
  		pause();
		}
		gamedesk.setStatus (playerInfo.getAufspieler(), playerInfo.getTrumpf(), playerInfo.getPunkte(), playerInfo.getRunde());
	}

	private void pause() {
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
}
