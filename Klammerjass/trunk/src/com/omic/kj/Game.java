package com.omic.kj;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import com.omic.kj.shared.domain.*;

final class Game {

	private final Logger log = Logger.getLogger("Game");

	private int gameId; // identifier for compare method
	private String gameName;
	private int maximumPoints; // Spielende bei diesen Punkten
	private int round; // Runde / Stich Nummer
	private String teamName;
	private Date start, end; // game duration
	private List<PlayCard> cards; // 32 cards with special tags on it
	private Farbe trumpf; // Trumpffarbe
	private Player geber; // Kartengeber
	private Player mainplayer; // Spieler/Gewinner beim "Reizen"
	private Player firstPlayer; // Spieler der beim ersten Stich rausgekommen ist
	private Player roundFirstPlayer; // Spieler der beim aktuellen Stich rausgekommen ist / rauskommen muß
	private Player currentPlayer; // Der Spieler ist grad an der Reihe
	private Karte roundCard; // Zuerst ausgespielte Karte in aktueller Runde, diese Farbe muß bedient werden.
	private Karte original;

	private CommandListener commandListener; // send player command to this listener!
	private ArrayBlockingQueue<PlayerResponse> responseQueue;

	private final List<Player> player; // 2 to 4 players per game
	private final Set<ComputerPlayer> computerPlayer; // save extra the auto player
	private final List<PlayRound> rounds;

	private GameState state;

	/*             Spiel        | Runde
	 * ----------------------------------------
	 * Reizen     mainplayer      n/a      
	 * Erster     firstPlayer     roundFirstPlayer   
	 * Aktuell    n/a             currentPlayer  
	 * Gewinner   sum(points)     roundwinner
	 */

	public Game() {
		this.player = new ArrayList<>();
		this.computerPlayer = new HashSet<>();
		this.rounds = new ArrayList<>();
		this.responseQueue = new ArrayBlockingQueue<>(8);
	}

	public void joinGame(final Player p, final GameSettings gs) throws Exception {
		final int playerCount = player.size();
		if (playerCount < 4) {
			p.setPosition(playerCount + 1);
			player.add(p);
			/** TODO: Position/Lücke beachten ! */
			if (gs != null) {
				setLimit(gs.getMaximumPoints());
			}
			setLimit(Math.min(300, maximumPoints));
		} else {
			throw new Exception("Too many players for this game.");
		}
	}

	public void joinGame(final ComputerPlayer computerPlayer) throws Exception {
		joinGame(computerPlayer.getPlayer(), null);
		this.computerPlayer.add(computerPlayer);
	}

	private void setLimit(int maximumPoints) {
		this.maximumPoints = maximumPoints;
	}

	public boolean hasPlayer(int playerId) {
		for (Player p : player) {
			if (p.getId() == playerId)
				return true;
		}
		return false;
	}

	public void startGame() throws Exception {
		int n = player.size();
		if (n < 2) {
			throw new Exception("Zu wenig Spieler für ein Spiel.");
		}
		if (n > 4) {
			throw new Exception("Zu viele Spieler für ein Spiel.");
		}

		loadCards();

		this.start = new Date();
		this.teamName = "Team X";
		this.gameName = "Game " + gameId;
		this.round = 1;
		this.state = GameState.G0;
		final int playerCount = player.size();

		if (playerCount < 2 || playerCount > 4) {
			throw new Exception("Unpassende Spieleranzahl.");
		}

		// LOOP FOR 4 PLAYER !!

		for (;;) {

			final PlayerResponse response = getNextPlayerResponse(2000);
			log.info("State=" + this.state + ", response=" + response);

			switch (this.state) {

			// -----------------------------------------------------------
			case G0: {
				selectGeber();
				// 3 Karten für jeden
				for (int i = 0; i < 3; i++) {
					for (Player p : player) {
						assignFreeCardToPlayer(p, false);
					}
				}
				sendPlayerInfo();
				this.state = GameState.G1;
				break;
			}

			// -----------------------------------------------------------
			case G1: {
				// 2 Karten für jeden
				this.original = null;
				for (int i = 0; i < 2; i++) {
					for (Player p : player) {
						assignFreeCardToPlayer(p, p.getId() == this.geber.getId());
					}
				}
				sendPlayerInfo();
				this.state = GameState.G2;
				break;
			}
			// -----------------------------------------------------------
			case G2: {
				Player p = getPlayerFromGeber(+1);
				sendPlayerCommand(p, Command.frageOriginal,new Response[]{Response.ja,Response.nein});
				this.state = GameState.G3;
				//
				break;
			}
			// -----------------------------------------------------------
			case G3: {
				if (response != null) {

				}
				break;
			}
			// -----------------------------------------------------------
			case G4: {
				break;
			}
			// -----------------------------------------------------------
			case G5: {
				break;
			}
			// -----------------------------------------------------------
			case G6: {
				break;
			}
			// -----------------------------------------------------------
			case G7: {
				break;
			}
			// -----------------------------------------------------------
			case G8: {
				break;
			}
			// -----------------------------------------------------------
			case G9: {
				break;
			}
			// -----------------------------------------------------------
			case G10: {
				break;
			}
			// -----------------------------------------------------------
			case G11: {
				break;
			}
			// -----------------------------------------------------------
			case G12: {
				break;
			}
			// -----------------------------------------------------------
			case G13: {
				break;
			}
			// -----------------------------------------------------------
			case G14: {
				break;
			}
			// -----------------------------------------------------------
			case S0: {
				break;
			}
			// -----------------------------------------------------------
			case S1: {
				break;
			}
			// -----------------------------------------------------------
			case X2: {
				break;
			}
			// -----------------------------------------------------------
			case X4: {
				break;
			}
			}
		}
		//sayHello();
		//selectGeber();
		//geben();
		//	GameLogger.printGame(game);
		//	startRounds();
	}


	/**
	 * n-ter Spieler im Uhrzeigersinn vom Geber ausgehend
	 * =REST(A2-1+$B$1;4)+1
	 */
	private Player getPlayerFromGeber(int step) throws Exception {
		int nextPosition = ((this.geber.getPosition() - 1 + step) % player.size()) + 1;
		for (Player p : player) {
			if (p.getPosition() == nextPosition)
				return p;
		}
		throw new Exception("next player not found");
	}

	private void sendPlayerInfo() {
		for (Player p : player) {
			sendPlayerInfo(p);
		}
	}

	/**
	* Geber wir anhand der höchsten gezogenen karte gewählt,
	* Kurzversion: per Zufall. 
	*/
	public void selectGeber() {
		this.geber = player.iterator().next();
	}

	//	private void geben() throws Exception {
	//		//Set<Player> player = game.getPlayers();
	//		if (player.size() == 4)
	//			geben4();
	//		else if (player.size() == 2 || player.size() == 3)
	//			geben2();
	//		else
	//			throw new Exception("Unpassende Spieleranzahl.");
	//	}
	//
	//	private void geben2() {
	//
	//	}

	//	/**
	//	* Kartenverteilregel für 4 Spieler
	//	* @throws GameException 
	//	*/
	//	private void geben4() {
	//		//		// 3 Karten für jeden
	//		//		for (int i = 0; i < 3; i++) {
	//		//			for (Player p : player) {
	//		//				assignFreeCardToPlayer(p);
	//		//				sendPlayerInfo(p);
	//		//			}
	//		//		}
	//		//		// 2 Karten für jeden
	//		//		for (int i = 0; i < 2; i++) {
	//		//			for (Player p : player) {
	//		//				assignFreeCardToPlayer(p);
	//		//				sendPlayerInfo(p);
	//		//			}
	//		//		}
	//		//			boolean ok = pli(geber).askOriginal(game);
	//		//			if (ok) {
	//		//				// 3 Karten für jeden
	//		//				for (int i = 0; i < 3; i++) {
	//		//					for (Player p : player) {
	//		//						assignFreeCardToPlayer(p);
	//		//						sendPlayerInfo(p);
	//		//					}
	//		//				}
	//		//			}
	//		//			else{
	//		//				throw new GameOverException("Kein Trumpf gewählt.");
	//		//			}
	//	}

	private void sendPlayerInfo(final Player p) {
		final PlayerCommand pc = new PlayerCommand();
		pc.setPlayerId(p.getId());
		pc.setGameId(gameId);
		pc.setCommand(Command.playerinfo);
		pc.setInfo(buildPlayerInfo(p));
		this.commandListener.toPlayer(pc);
	}

	private void sendPlayerCommand(Player p, Command command, Response[] allowedResponse) {
		final PlayerCommand pc = new PlayerCommand();
		pc.setPlayerId(p.getId());
		pc.setGameId(gameId);
		pc.setCommand(command);
		pc.setAllowedResponse(allowedResponse);
		pc.setErsteKarte(original.getFarbe());
		this.commandListener.toPlayer(pc);
	}

	private PlayerInfo buildPlayerInfo(final Player p) {
		final PlayerInfo i = new PlayerInfo();
		i.setAufspieler(roundFirstPlayer != null ? roundFirstPlayer.getUsername() : "");
		i.setGameName(gameName);
		i.setGeber(geber.getUsername());
		i.setKarten(getCardsOnHand(p));
		i.setAktiv(false);
		i.setPlayerId(p.getId());
		i.setPlayerName(p.getUsername());
		i.setPosition(p.getPosition());
		i.setPunkte(p.getPunkte());
		i.setRunde(round);
		i.setTrumpf(trumpf);
		return i;
	}

	private Set<Handkarte> getCardsOnHand(Player p) {
		final Set<Handkarte> playercards = new HashSet<>();
		for (PlayCard pc : cards) {
			if (pc.getOwner() != null && pc.getOwner().getId() == p.getId()) {
				Handkarte hc = new Handkarte();
				hc.setKarte(pc.getKarte());
				hc.setOffen(!p.isComputer());
				playercards.add(hc);
			}
		}
		return playercards;
	}

	private void sayHello() {
		for (Player p : player) {
			PlayerCommand cmd = new PlayerCommand();
			cmd.setCommand(Command.say);
			cmd.setMessage("hello world!");
			cmd.setPlayerId(p.getId());
			commandListener.toPlayer(cmd);
		}
	}

	private void assignFreeCardToPlayer(final Player p, boolean assignOriginal) {
		for (;;) {
			long skip = Math.round(Math.random() * 61), s = 0;
			for (final PlayCard c : cards) {
				if (s++ >= skip) {
					if (c.isFree()) {
						c.setOwner(p);
						log.info("card assigned: card=" + c.getKarte() + ", player=" + p.getId());
						if (assignOriginal && this.original == null) {
							this.original = c.getKarte();
							log.info("original assigned: card=" + c.getKarte());
						}
						return;
					}
				}
			}
		}
	}

	/**
	* Karten frisch initialisieren.
	*/
	private void loadCards() {
		ArrayList<PlayCard> cards = new ArrayList<PlayCard>();
		for (Karte c : Karte.values()) {
			PlayCard p = new PlayCard(c);
			cards.add(p);
		}
		setCards(Collections.unmodifiableList(cards));
	}

	private void setCards(List<PlayCard> playCards) {
		this.cards = playCards;
	}

	public void setId(int id) {
		this.gameId = id;
	}

	public void stopGame() {
		// TODO: 
	}

	public void setCommandListener(CommandListener commandListener) {
		this.commandListener = commandListener;
	}

	public void onPlayerResponse(PlayerResponse response) {
		this.responseQueue.add(response);
	}

	private PlayerResponse getNextPlayerResponse(int timeout_ms) throws InterruptedException {
		return this.responseQueue.poll(timeout_ms, TimeUnit.MILLISECONDS);
	}

}
