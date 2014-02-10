package com.omic.kj.test;

import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import com.omic.kj.local.LocalGameConnector;
import com.omic.kj.shared.PlayerCommandListener;
import com.omic.kj.shared.domain.*;

public class TestKJ {
	static Logger log;
	protected static PlayerInfo gameinfo;

	public static void main(String[] args) throws Exception {
		InputStream is = ClassLoader.getSystemResourceAsStream("logging.properties");
		LogManager.getLogManager().readConfiguration(is);
		is.close();

		log = Logger.getLogger("TestPlayer");

		final LocalGameConnector conn = LocalGameConnector.getConnector();
		final User me = conn.login("markus", "xxx");

		conn.addPlayerCommandListener(me, new PlayerCommandListener() {
			@Override
			public void onMessage(PlayerCommand command) {
				log.info(command + "");
				if (command.getPlayerId() == me.getId()) {
					switch (command.getCommandCode()) {
					case playerinfo: {
						gameinfo = command.getInfo();
						break;
					}
					case frageOriginal: {
						PlayerResponse response = new PlayerResponse();
						response.setPlayerId(gameinfo.getPlayerId());
						response.setResponseCode(ResponseCode.ja);
						conn.playerResponse(response);
						break;
					}
					case spieleKarte:{
						gameinfo = command.getInfo();
						PlayerResponse response = new PlayerResponse();
						response.setPlayerId(gameinfo.getPlayerId());
						response.setResponseCode(ResponseCode.play);
						for(CardInfo i:gameinfo.getKarten()) {
							if(i.getPosition()==gameinfo.getPosition() && i.getLocation()==1){
								response.setGespielteKarte(i.getKarte());
							}
						}
						conn.playerResponse(response);
						break;
					}}
				}
			}
		});
		conn.startGame(me, new GameSettings(true, 3, 300));

	}

}
