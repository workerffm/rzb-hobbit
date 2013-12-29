package com.omic.kj;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import com.omic.kj.shared.domain.*;

/**
 * One controller for all games
 */
public class GameController implements ServerInterface, PlayerResponseListener, CommandListener {
	
	private final Logger log = Logger.getLogger("Game");
	
	private final Set<Player> activePlayers;
	private final Set<Game> activeGames;
	private final Set<CommandListener> commandListeners;
	private final Object controllerLock = new Object();
	
	private int nextPlayerId = 1;
	private int nextGameId = 1;
	private int playerCommandId = 1;
	
	public GameController() {
		this.activePlayers = new HashSet<>();	
		this.activeGames = new HashSet<>();
		this.commandListeners = new HashSet<>();
	} 
	
	/**
	 * Notification interface to client
	 */
	public void addCommandListener(CommandListener commandListener){
		this.commandListeners.add(commandListener);
	}

	@Override
	public User login(String us, String pw) throws Exception {
		if (StringUtils.isBlank(pw)) throw new Exception("Invalid user");
		if (StringUtils.isBlank(us)) throw new Exception("Invalid user");
		
		synchronized(this.controllerLock) {
			for(Player cp:activePlayers) {
			  if(cp.getUsername().equals(us)) throw new Exception("Already logged on.");  	
			}
		}
		final Player p = createNewPlayer(us);
  	return p;
	}

	@Override
	public void logout(String us) throws Exception {
		if (StringUtils.isBlank(us)) throw new Exception("Invalid user");
		synchronized(this.controllerLock) {
			for(Player cp:activePlayers) {
			  if(cp.getUsername().equals(us)) {
			  	Game g = findGame(cp.getId());
			  	stopGame(g);
			  	activePlayers.remove(cp);
			  	activeGames.remove(g);
			  }  	
			}
		}
	}

	private Player createNewPlayer(String username) {
		final Player p = new Player();
		p.setUsername(username);
		p.setPosition(1);
		p.setPunkte(0);
		p.setSpielbereit(true);
		p.setTeamId(1);
		synchronized (this.controllerLock) {
			p.setId(nextPlayerId);
			this.activePlayers.add(p);
			nextPlayerId++;
		}
		return p;
	}


	@Override
	public void startGame(User user, GameSettings settings) throws Exception {
		
		if(user==null) {
			throw new IllegalArgumentException("user");
		}

		final Game actualGame = findGame(user.getId());
		if (actualGame!=null) {
	  	throw new Exception ("User "+user.getUsername()+" already joined to game "+actualGame);
		}
		final Player p = findPlayer(user);
		
		// Spiel gegen PC
		if(settings.isOption_PlayWithPC()){
			p.setSpielbereit(true);
  	  p.setComputer(false);
			final Game newGame = new Game();
			newGame.setCommandListener(this);
			newGame.joinGame(p,settings);
			
			for (int i=0; i < settings.getComputerCount();i++) {
	  	  final Player coPlayer = createNewPlayer("Computer "+(1+i)+"-"+p.getId());
	  	  coPlayer.setSpielbereit(true);
	  	  coPlayer.setComputer(true);
	  	  coPlayer.setPosition(2+i);
				
	  	  final ComputerPlayer cp = new ComputerPlayer(coPlayer);
	  	  cp.setPlayerResponseListener(this); // send response
	  	  addCommandListener(cp); // get commands
				newGame.joinGame(cp);
			}
			startGame(newGame);
		}
		else {
			// TODO: join existing game, wait for other player
		}
	}

	private void startGame(final Game game) throws Exception {
		synchronized(this.controllerLock) {
			game.setId(nextGameId);
			nextGameId++;
		  activeGames.add(game);
		}
		game.startGame();
		log.info("Game started: "+game);
	}

	private void stopGame(final Game game) {
		if(game==null) return;
		game.stopGame();
		// TODO Send message to all player
		synchronized(this.controllerLock) {
		  activeGames.remove(game);
		}
		log.info("Game stopped: "+game);
	}

	// --------------------------------------------------------------------
	// -- player-server communication -------------------------------------
	// --------------------------------------------------------------------
	
	/**
	 * Callback from client side, push into queue and forward to active game. 
	 */
	@Override
	public void onMessage(final PlayerResponse response) {
  	log.info("Player response: "+response);
	  Game g = findGame(response.getPlayerId());
	  if(g==null) {
	  	log.info("Response ignored, no game found for reponse: "+response);
	  }
	  else {
	    g.onPlayerResponse(response);
	  }
	}

	/**
	 * Forward message to all player listener
	 */
	public void toPlayer(final PlayerCommand command) {
	  command.setPlayerCommandId(playerCommandId);
	  playerCommandId++;
  	for(CommandListener listener:commandListeners) {
  		log.fine("CMD "+command.getPlayerCommandId()+" --> "+listener);
		  listener.toPlayer(command);
  	}
	}
	
	// --------------------------------------------------------------------
	// -- helper ----------------------------------------------------------
	// --------------------------------------------------------------------
	
	private Player findPlayer(User user) throws Exception {
		synchronized(this.controllerLock) {
			for(Player cp:activePlayers) {
			  if(cp.getUsername().equals(user.getUsername())) return cp;  	
			}
		}
		throw new Exception("User not found.");
	}

	private Game findGame(int userId) {
		synchronized (this.controllerLock) {
		  for (Game g:this.activeGames) {
		  	if(g.hasPlayer(userId))
		  			return g;
		  }	
		}
		return null;
	}

}
