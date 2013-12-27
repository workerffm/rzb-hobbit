package com.omic.kj.local;

import com.omic.kj.*;
import com.omic.kj.shared.domain.*;

/**
 * Alternative: RMI Server
 */
public class LocalGSrv implements ServerInterface {

	private GameController gamecontroller;
	//private PlayerResponseListener responseListener;

	public LocalGSrv() {
		this.gamecontroller = new GameController();
	}

	public User login(String us, String pw) throws Exception {
		return gamecontroller.login(us, pw);
	}

	public void startGame(User user, GameSettings settings) throws Exception {
		gamecontroller.startGame(user, settings);
	}

	public void forwardPlayerResponse(PlayerResponse response) {
		gamecontroller.onMessage(response);
	}

	public void addCommandListener(CommandListener commandListener) {
		gamecontroller.addCommandListener(commandListener);
	}
}
