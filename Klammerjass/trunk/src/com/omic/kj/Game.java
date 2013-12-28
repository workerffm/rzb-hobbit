package com.omic.kj;

import java.util.*;
import com.omic.kj.shared.domain.*;

public class Game {
  private int id;                  // identifier for compare method
	private int maximumPoints;       // Spielende bei diesen Punkten
	private int round;               // Runde / Stich Nummer
	private Date start, end;         // game duration
//	private List<Card> cards;    // 32 cards with special tags on it
	private Farbe trumpf;            // Trumpffarbe
	private Player geber;            // Kartengeber
	private Player mainplayer;       // Spieler/Gewinner beim "Reizen"
	private Player firstPlayer;      // Spieler der beim ersten Stich rausgekommen ist
	private Player roundFirstPlayer; // Spieler der beim aktuellen Stich rausgekommen ist / rauskommen muß
	private Player currentPlayer;    // Der Spieler ist grad an der Reihe
	private Karte roundCard;         // Zuerst ausgespielte Karte in aktueller Runde, diese Farbe muß bedient werden.
	private Map<Integer,Player> roundwinner;  // Liste der Rundengewinner
	private CommandListener commandListener;  // send player command to this listener!
	private Set<Player> player;      // 2 to 4 players per game
	private Set<ComputerPlayer> computerPlayers;  // save extra the auto player

	/*             Spiel        | Runde
	 * ----------------------------------------
	 * Reizen     mainplayer      n/a      
	 * Erster     firstPlayer     roundFirstPlayer   
	 * Aktuell    n/a             currentPlayer  
	 * Gewinner   sum(points)     roundwinner
	 */
	
	
	public Game() {
	  this.player = new HashSet<>();	
	  this.computerPlayers = new HashSet<>();	
	}
	
	public void joinGame(final Player p, final GameSettings gs) throws Exception  {
		final int playerCount = player.size(); 
		if (playerCount < 4) { 
			p.setPosition(playerCount+1);
			player.add(p);
		  /** TODO: Position/Lücke beachten ! */
			if(gs!=null) {
			  setLimit (gs.getMaximumPoints());
			}
			setLimit (Math.min(300, maximumPoints));
		}
		else {
			throw new Exception("Too many players for this game.");
		} 
	}

	public void joinGame(final ComputerPlayer computerPlayer) throws Exception {
		joinGame(computerPlayer.getPlayer(),null);
		computerPlayers.add(computerPlayer);
	}

	private void setLimit(int maximumPoints) {
		this.maximumPoints = maximumPoints;
	}

	public boolean hasPlayer(int playerId) {
		for (Player p:player){
			if(p.getId()==playerId)
				return true;
		}
		return false;
	}
	
	public void startGame() throws Exception {
		int n = getPlayers().size();
		if (n<2) throw new Exception("Zu wenig Spieler für ein Spiel.");
		if (n>4) throw new Exception("Zu viele Spieler für ein Spiel.");
		
		for (Player p:player){
			PlayerCommand cmd = new PlayerCommand();
			cmd.setCommand(Command.say);
			cmd.setMessage("hello world!");
			cmd.setPlayerId(p.getId());
			commandListener.toPlayer(cmd);
		}
		
//	game.setStart(new Date());
//	loadCards();
//	selectGeber();
//	geben();
//	GameLogger.printGame(game);
//	startRounds();

	}
	
	public void stopGame() {
	}
		

	public Set<Player> getPlayers() {
		return player;
	}

	public void onPlayerResponse(PlayerResponse response) {
		// TODO add queue and thread
		
	}

	public void setCommandListener(CommandListener commandListener) {
		this.commandListener = commandListener;
	}



}
