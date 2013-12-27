package com.omic.kj;

import com.omic.kj.shared.domain.*;

/**
 * Client side interface, independent from RMI/Webservice, etc. implementation.
 */
public interface ServerInterface {
	
	User login (String u, String p) throws Exception;
	
	// play against PC
	void startGame (User user, GameSettings settings) throws Exception;

	// play online
	///void listGames();
	///void joinGame();
}
