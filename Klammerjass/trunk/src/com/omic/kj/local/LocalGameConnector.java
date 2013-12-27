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
	private static LocalGameConnector connector; 
	private final LocalGSrv delegate;
	private final Map<User, PlayerCommandListener> playerlistener;

	public synchronized static LocalGameConnector getConnector() throws Exception {
		if (connector==null)
			connector = new LocalGameConnector();
		return connector;
	}

	private LocalGameConnector() throws Exception {
		// Create the simple local implemenation
		//
		this.delegate = new LocalGSrv();
		this.playerlistener = new HashMap<>();
		this.delegate.addCommandListener(new PlayerCommandDispatcher());

		log.info("LocalGameConnector initialized.");
	}

	private void start() throws InterruptedException {
		this.delegate.wait();
	}

	@Override
	public User login(String u, String p, PlayerCommandListener listener) throws Exception {
		final User user = this.delegate.login(u, p);
		if(user!=null) {
			playerlistener.put(user,listener);
		}
		return user;
	}

	/**
	 * TODO: convert to player command
	 */
	@Override
	public void startGame(User user, GameSettings settings) throws Exception {
		this.delegate.startGame(user, settings);
	}

	/**
	 * Forward command to client listener
	 * TODO: add queue and thread
	 */
	private final class PlayerCommandDispatcher implements CommandListener {
		@Override
		public void toPlayer(PlayerCommand command) {
			synchronized (LocalGameConnector.this.playerlistener) {
				for(User u:playerlistener.keySet()) {
					if(u.getId() == command.getPlayerId()) {
						LocalGameConnector.this.playerlistener.get(u).onMessage(command);
						break;
					}
				}
			}
		}
	}

	/** 
	 * TODO: add queue and thread
	 */
	@Override
	public void playerResponse(PlayerResponse response) {
    this.delegate.forwardPlayerResponse(response);
	}

}
