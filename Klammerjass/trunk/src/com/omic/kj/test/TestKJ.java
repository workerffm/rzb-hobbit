package com.omic.kj.test;

import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import com.omic.kj.local.LocalGameConnector;
import com.omic.kj.shared.PlayerCommandListener;
import com.omic.kj.shared.domain.*;

public class TestKJ {
	 static Logger log; 
	
	public static void main(String[] args) throws Exception {
		InputStream is = ClassLoader.getSystemResourceAsStream("logging.properties");
		LogManager.getLogManager().readConfiguration(is);
		is.close();
		
		log = Logger.getLogger("TestPlayer");
		
		final LocalGameConnector conn = LocalGameConnector.getConnector();
		final User u= conn.login("markus","xxx");
		conn.addPlayerCommandListener(u, new PlayerCommandListener() {
			@Override
			public void onMessage(PlayerCommand command) {
			  log.info("I'm user "+u.getId()+" "+command+"");	
			}
		});
    conn.startGame(u, new GameSettings(true,3,300));
    
    for(Response r:Response.values()){
      PlayerResponse response = new PlayerResponse();
      response.setPlayerId(u.getId());
      response.setResponse(r);
  		conn.playerResponse(response);
    }
	}

}
