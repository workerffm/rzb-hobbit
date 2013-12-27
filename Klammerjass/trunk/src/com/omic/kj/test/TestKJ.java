package com.omic.kj.test;

import java.io.InputStream;
import java.util.logging.LogManager;
import com.omic.kj.local.LocalGameConnector;
import com.omic.kj.shared.PlayerCommandListener;
import com.omic.kj.shared.domain.*;

public class TestKJ {

	public static void main(String[] args) throws Exception {
		InputStream is = ClassLoader.getSystemResourceAsStream("logging.properties");
		LogManager.getLogManager().readConfiguration(is);
		is.close();
		
		LocalGameConnector conn = LocalGameConnector.getConnector();
		User u= conn.login("markus","xxx", new PlayerCommandListener() {
			@Override
			public void onMessage(PlayerCommand c) {
			  System.out.println(""+c);	
			}
		});
    conn.startGame(u, new GameSettings(true,1,300));
    
    for(Response r:Response.values()){
      PlayerResponse response = new PlayerResponse();
      response.setPlayerId(u.getId());
      response.setResponse(r);
  		conn.playerResponse(response);
    }
	}

}
