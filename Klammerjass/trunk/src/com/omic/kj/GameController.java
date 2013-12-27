package com.omic.kj;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import com.omic.kj.local.ComputerPlayer;
import com.omic.kj.shared.PlayerCommandListener;
import com.omic.kj.shared.domain.*;

/**
 * One controller for all games
 */
public class GameController implements ServerInterface, PlayerResponseListener, CommandListener {
	
	private final Logger log = Logger.getLogger("Game");
	private final Set<Player> activePlayer;
	private final Set<Game> activeGames;
	private CommandListener commandListener;
	private int nextPlayerId = 1;
	private int playerCommandId = 1;
	
	public GameController() {
		this.activePlayer = new HashSet<>();	
		this.activeGames = new HashSet<>();	
	} 
	
	/**
	 * Notification interface to client
	 */
	public void addCommandListener(CommandListener commandListener){
		this.commandListener = commandListener;
	}

	/**
	 * Callback from client side, push into queue and forward to active game. 
	 */
	@Override
	public void onMessage(final PlayerResponse response) {
  	log.info("Player response: "+response);
	  Game g = findGame(response.getPlayerId());
	  if(g==null) {
	  	log.info("No game found for reponse +"+response);
	  }
	  g.onPlayerResponse(response);
	}

	public void toPlayer(final PlayerCommand command) {
	  command.setPlayerCommandId(playerCommandId);
	  playerCommandId++;
  	log.info("Player command: "+command);
		this.commandListener.toPlayer(command);	
	}
	
	
	@Override
	public User login(String us, String pw) throws Exception {
		if (StringUtils.isBlank(pw)) throw new Exception("Invalid user");
		if (StringUtils.isBlank(us)) throw new Exception("Invalid user");
		
		synchronized(this.activePlayer) {
			for(Player cp:activePlayer) {
			  if(cp.getUsername().equals(us)) throw new Exception("Already logged on.");  	
			}
		}
		final Player p = createNewPlayer(us);
  	return p;
	}

	private Player createNewPlayer(String username) {
		final Player p = new Player();
		p.setUsername(username);
		p.setPosition(1);
		p.setPunkte(0);
		p.setSpielbereit(Boolean.TRUE);
		p.setTeamId(1);
		synchronized (this.activePlayer) {
			p.setId(nextPlayerId);
			this.activePlayer.add(p);
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
			Game newGame = new Game();
			newGame.setCommandListener(this);
			newGame.joinGame(p,settings);
			p.setSpielbereit(true);
			for (int i=0; i < settings.getComputerCount();i++) {
	  	  Player pl = createNewPlayer("Computer "+(1+i));
	  	  ComputerPlayer cp = new ComputerPlayer(pl);
	  	  cp.getPlayer().setSpielbereit(true);
				newGame.joinGame(cp.getPlayer(),settings);
			}
			activeGames.add(newGame);
			newGame.startGame();
		}
	}
	
	private Player findPlayer(User user) throws Exception {
		synchronized(this.activePlayer) {
			for(Player cp:activePlayer) {
			  if(cp.getUsername().equals(user.getUsername())) return cp;  	
			}
		}
		throw new Exception("User not found.");
	}

	private Game findGame(int userId) {
		synchronized (this.activeGames) {
		  for (Game g:this.activeGames) {
		  	if(g.hasPlayer(userId))
		  			return g;
		  }	
		}
		return null;
	}


}
