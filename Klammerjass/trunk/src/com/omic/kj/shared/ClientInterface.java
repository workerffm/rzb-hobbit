package com.omic.kj.shared;

import com.omic.kj.shared.domain.*;

/**
 * Client side interface, independent from RMI/Webservice, etc. implementation.
 */
public interface ClientInterface {
	
	User login (String u, String p, PlayerCommandListener listener) throws Exception;
	
	void playerResponse (PlayerResponse response);

	// play against PC
	void startGame (User user, GameSettings settings) throws Exception;

	
	// play online
	///void listGames();
	///void joinGame();
}
