package com.omic.kj.local;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import com.omic.kj.CommandListener;
import com.omic.kj.shared.ClientInterface;
import com.omic.kj.shared.PlayerCommandListener;
import com.omic.kj.shared.domain.*;

/**
 * Alternative: RMI Client connection to "ServerInterface" (delegate)
 */
public final class LocalGameConnector implements ClientInterface {

	private final Logger log = Logger.getLogger("Game");
	
	/** Connector is singleton */
	private static LocalGameConnector connector;

	private final LocalGSrv server;
	private final Map<User, PlayerCommandListener> playerlistener;

	public synchronized static LocalGameConnector getConnector() throws Exception {
		if (connector == null) {
			connector = new LocalGameConnector();
		}
		return connector;
	}

	private LocalGameConnector() throws Exception {
		// Create the simple local implemenation
		//
		this.server = new LocalGSrv();
		this.playerlistener = new HashMap<>();
		this.server.addCommandListener(new PlayerCommandDispatcher());

		log.info("LocalGameConnector initialized.");
	}

	private void start() throws InterruptedException {
		this.server.wait();
	}

	@Override
	public User login(String u, String p) throws Exception {
		final User user = this.server.login(u, p);
		return user;
	}

	@Override
	public void addPlayerCommandListener(User user, PlayerCommandListener listener) {
		if (user != null) {
			playerlistener.put(user, listener);
		}
	}

	/**
	 * TODO: convert to player command
	 */
	@Override
	public void startGame(User user, GameSettings settings) throws Exception {
		this.server.startGame(user, settings);
	}

	/**
	 * Forward command to client listener
	 * TODO: add queue and thread AND add game channel on server side
	 */
	private final class PlayerCommandDispatcher implements CommandListener {
		@Override
		public void toPlayer(PlayerCommand command) {
			synchronized (LocalGameConnector.this.playerlistener) {
				for (User u : playerlistener.keySet()) {
					//if (u.getId() == command.getPlayerId() || u.getGameId() == command.getGameId()) {
						LocalGameConnector.this.playerlistener.get(u).onMessage(command);
						break;
					//}
				}
			}
		}
	}

	/** 
	 * TODO: add queue and thread
	 */
	@Override
	public void playerResponse(PlayerResponse response) {
		this.server.forwardPlayerResponse(response);
	}

}
