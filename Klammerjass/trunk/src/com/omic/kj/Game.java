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
	private Date start, end; // game duration
	private List<PlayCard> playcards; // 32 cards with special tags on it
	private Farbe trumpfFarbe; // Trumpffarbe
	private Player geber; // Kartengeber
	private Player trumpfPlayer; // Spieler der original oder kleines spielt.
	private Karte original; // Die umgedrehte Karte beim Geben, für alle sichtbar.
	private boolean originalSelected; // trumpfPlayer wählte original
	private GameState state; // aktuelle Phase
	private Player winner; // Spielgewinner

	private int nextRoundNr; // ID,Rundennummer
	private Player aufspieler; // Spieler der beim aktuellen Stich rausgekommt

	private CommandListener commandListener; // send player command to this listener!
	private ArrayBlockingQueue<GameEvent> eventQueue;

	private final List<Player> player; // 2 to 4 players per game
	private final Set<ComputerPlayer> computerPlayer; // save extra the auto player
	private final List<PlayRound> playrounds;

	public Game() {
		this.player = new ArrayList<>();
		this.computerPlayer = new HashSet<>();
		this.playrounds = new ArrayList<>();
		this.eventQueue = new ArrayBlockingQueue<>(50);
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

		start = new Date();
		gameName = "Game " + gameId;
		gotoGameState(GameState.G0);

		final int playerCount = player.size();

		if (playerCount < 2 || playerCount > 4) {
			throw new Exception("Unpassende Spieleranzahl.");
		}

		// LOOP FOR 4 PLAYER !!

		for (;;) {

			final GameEvent event = getNextEvent(20000);
			Thread.sleep(500);

			if (event == null) {
				log.info("State=" + state + ", waiting for events");
				continue;
			}
			log.info("State=" + state + ", event=" + event);

			if (event.getEvent() == EventType.gostate) {
				state = event.getState();
			}

			switch (state) {

			// -----------------------------------------------------------
			case G0: {
				nextRoundNr = 1;
				selectGeber();
				// 3 Karten für jeden
				for (int i = 0; i < 3; i++) {
					for (Player p : player) {
						assignFreeCardToPlayer(p, false);
					}
				}
				sendPlayerInfo();
				gotoGameState(GameState.G1);
				break;
			}

			// -----------------------------------------------------------
			case G1: {
				// 2 Karten für jeden
				original = null;
				for (int i = 0; i < 2; i++) {
					for (Player p : player) {
						assignFreeCardToPlayer(p, p.getId() == this.geber.getId());
					}
				}
				sendPlayerInfo();
				gotoGameState(GameState.G1a);
				break;
			}
			// -----------------------------------------------------------
			case G1a: {
				// 3 Karten für jeden
				for (int i = 0; i < 3; i++) {
					for (Player p : player) {
						assignFreeCardToPlayer(p, false);
					}
				}
				sendPlayerInfo();
				gotoGameState(GameState.G2);
				break;
			}
			// -----------------------------------------------------------
			case G2: {
				Player p = getPlayerLeftFromGeber(+1);
				sendPlayerCommand(p, CommandCode.frageOriginal, new ResponseCode[] { ResponseCode.ja, ResponseCode.nein });
				state = GameState.G3;
				break;
			}
			// -----------------------------------------------------------
			case G3: {
				if (event.getEvent() == EventType.player) {
					final PlayerResponse response = event.getPlayerResponse();
					if (response.getResponseCode() == ResponseCode.nein) {
						sendPlayerMsg(getPlayerById(response.getPlayerId()), "Weg!");
						Player p = getPlayerLeftFromGeber(+2);
						sendPlayerCommand(p, CommandCode.frageOriginal, new ResponseCode[] { ResponseCode.ja, ResponseCode.nein });
						state = GameState.G4;
					} else if (response.getResponseCode() == ResponseCode.ja) {
						trumpfPlayer = getPlayerById(response.getPlayerId());
						trumpfFarbe = original.getFarbe();
						originalSelected = true;
						gotoGameState(GameState.S0);
					} else {
						throw new Exception("Unexpected response: state=" + state + ", response=" + response.getResponseCode());
					}
				}
				break;
			}
			// -----------------------------------------------------------
			case G4: {
				if (event.getEvent() == EventType.player) {
					final PlayerResponse response = event.getPlayerResponse();
					if (response.getResponseCode() == ResponseCode.nein) {
						sendPlayerMsg(getPlayerById(response.getPlayerId()), "Weg!");
						Player p = getPlayerLeftFromGeber(+3);
						sendPlayerCommand(p, CommandCode.frageOriginal, new ResponseCode[] { ResponseCode.ja, ResponseCode.nein });
						state = GameState.G5;
					} else if (response.getResponseCode() == ResponseCode.ja) {
						trumpfPlayer = getPlayerById(response.getPlayerId());
						trumpfFarbe = original.getFarbe();
						originalSelected = true;
						gotoGameState(GameState.S0);
					} else {
						throw new Exception("Unexpected response: state=" + state + ", response=" + response.getResponseCode());
					}
				}
				break;
			}
			// -----------------------------------------------------------
			case G5: {
				if (event.getEvent() == EventType.player) {
					final PlayerResponse response = event.getPlayerResponse();
					if (response.getResponseCode() == ResponseCode.nein) {
						sendPlayerMsg(getPlayerById(response.getPlayerId()), "Weg!");
						Player p = getPlayerLeftFromGeber(+0);
						sendPlayerCommand(p, CommandCode.frageOriginal, new ResponseCode[] { ResponseCode.ja, ResponseCode.nein });
						state = GameState.G6;
					} else if (response.getResponseCode() == ResponseCode.ja) {
						trumpfPlayer = getPlayerById(response.getPlayerId());
						trumpfFarbe = original.getFarbe();
						originalSelected = true;
						gotoGameState(GameState.S0);
					} else {
						throw new Exception("Unexpected response: state=" + state + ", response=" + response.getResponseCode());
					}
				}
				break;
			}
			// -----------------------------------------------------------
			case G6: {
				if (event.getEvent() == EventType.player) {
					final PlayerResponse response = event.getPlayerResponse();
					if (response.getResponseCode() == ResponseCode.nein) {
						sendPlayerMsg(getPlayerById(response.getPlayerId()), "Weg!");
						Player p = getPlayerLeftFromGeber(+1);
						sendPlayerCommand(p, CommandCode.frageKleines, new ResponseCode[] { ResponseCode.ja, ResponseCode.nein });
						state = GameState.G7;
					} else if (response.getResponseCode() == ResponseCode.ja) {
						trumpfPlayer = getPlayerById(response.getPlayerId());
						trumpfFarbe = original.getFarbe();
						originalSelected = true;
						gotoGameState(GameState.S0);
					} else {
						throw new Exception("Unexpected response: state=" + state + ", response=" + response.getResponseCode());
					}
				}
				break;
			}
			// -----------------------------------------------------------
			case G7: {
				if (event.getEvent() == EventType.player) {
					final PlayerResponse response = event.getPlayerResponse();
					if (response.getResponseCode() == ResponseCode.nein) {
						sendPlayerMsg(getPlayerById(response.getPlayerId()), "Noch weg!");
						Player p = getPlayerLeftFromGeber(+2);
						sendPlayerCommand(p, CommandCode.frageKleines, new ResponseCode[] { ResponseCode.ja, ResponseCode.nein });
						state = GameState.G8;
					} else if (response.getResponseCode() == ResponseCode.ja) {
						Player p = getPlayerLeftFromGeber(+1);
						sendPlayerCommand(p, CommandCode.frageTrumpffarbe, new ResponseCode[] { ResponseCode.waehleFarbe });
						state = GameState.G11;
					} else {
						throw new Exception("Unexpected response: state=" + state + ", response=" + response.getResponseCode());
					}
				}
				break;
			}
			// -----------------------------------------------------------
			case G8: {
				if (event.getEvent() == EventType.player) {
					final PlayerResponse response = event.getPlayerResponse();
					if (response.getResponseCode() == ResponseCode.nein) {
						sendPlayerMsg(getPlayerById(response.getPlayerId()), "Noch weg!");
						Player p = getPlayerLeftFromGeber(+3);
						sendPlayerCommand(p, CommandCode.frageKleines, new ResponseCode[] { ResponseCode.ja, ResponseCode.nein });
						state = GameState.G9;
					} else if (response.getResponseCode() == ResponseCode.ja) {
						Player p = getPlayerLeftFromGeber(+2);
						sendPlayerCommand(p, CommandCode.frageTrumpffarbe, new ResponseCode[] { ResponseCode.waehleFarbe });
						state = GameState.G12;
					} else {
						throw new Exception("Unexpected response: state=" + state + ", response=" + response.getResponseCode());
					}
				}
				break;
			}
			// -----------------------------------------------------------
			case G9: {
				if (event.getEvent() == EventType.player) {
					final PlayerResponse response = event.getPlayerResponse();
					if (response.getResponseCode() == ResponseCode.nein) {
						sendPlayerMsg(getPlayerById(response.getPlayerId()), "Noch weg!");
						Player p = getPlayerLeftFromGeber(+0);
						sendPlayerCommand(p, CommandCode.frageKleines, new ResponseCode[] { ResponseCode.ja, ResponseCode.nein });
						state = GameState.G10;
					} else if (response.getResponseCode() == ResponseCode.ja) {
						Player p = getPlayerLeftFromGeber(+3);
						sendPlayerCommand(p, CommandCode.frageTrumpffarbe, new ResponseCode[] { ResponseCode.waehleFarbe });
						state = GameState.G13;
					} else {
						throw new Exception("Unexpected response: state=" + state + ", response=" + response.getResponseCode());
					}
				}
				break;
			}
			// -----------------------------------------------------------
			case G10: {
				if (event.getEvent() == EventType.player) {
					final PlayerResponse response = event.getPlayerResponse();
					if (response.getResponseCode() == ResponseCode.nein) {
						sendPlayerMsg(getPlayerById(response.getPlayerId()), "Noch weg!");
						gotoGameState(GameState.GOV);
					} else if (response.getResponseCode() == ResponseCode.ja) {
						Player p = getPlayerLeftFromGeber(+0);
						sendPlayerCommand(p, CommandCode.frageTrumpffarbe, new ResponseCode[] { ResponseCode.waehleFarbe });
						state = GameState.G14;
					} else {
						throw new Exception("Unexpected response: state=" + state + ", response=" + response.getResponseCode());
					}
				}
				break;
			}
			// -----------------------------------------------------------
			case G11:
			case G12:
			case G13:
			case G14: {
				if (event.getEvent() == EventType.player) {
					final PlayerResponse response = event.getPlayerResponse();
					if (response.getResponseCode() == ResponseCode.waehleFarbe) {
						trumpfPlayer = getPlayerById(response.getPlayerId());
						trumpfFarbe = response.getFarbe();
						originalSelected = false;
						gotoGameState(GameState.S0);
					} else {
						throw new Exception("Unexpected response: state=" + state + ", response=" + response.getResponseCode());
					}
				}
				break;
			}

			// -----------------------------------------------------------
			case S0: {
				if (playerCount == 2 || playerCount == 3) {
					/** Spieler, der die Trumpf 7 hält, kann mit 'Original' tauschen */
					final PlayCard trumpf7 = getCard(trumpfFarbe, Kartenwert.Sieben);
					final Player trumpf7_owner = trumpf7.getOwner();
					if (trumpf7_owner != null && !original.equals(trumpf7)) {
						sendPlayerCommand(trumpf7_owner, CommandCode.tauscheSieben, new ResponseCode[] { ResponseCode.ja, ResponseCode.nein });
						state = GameState.X1;
					}
				} else {
					gotoGameState(GameState.S1);
				}
				break;
			}
			// -----------------------------------------------------------
			case S1: {
				if (!areCardsAvailable()) {
					gotoGameState(GameState.S4);
				} else {
					// In der 1. Runde spielt der Nebenmann auf:
					aufspieler = getPlayerLeftFromGeber(+1);
					// Neue Runde starten
					final PlayRound r = new PlayRound();
					r.setNr(nextRoundNr);
					r.setCurrentPosition(1);
					nextRoundNr++;
					playrounds.add(r);

					gotoGameState(GameState.S2);
				}
				break;
			}
			// -----------------------------------------------------------
			case S2: {
				final PlayRound r = getCurrentRound();
				Player p = getPlayerLeftFromAufspieler(r.getCurrentPosition() - 1);
				gotoGameState(GameState.S3);
				sendPlayerCommand(p, CommandCode.spieleKarte, new ResponseCode[] { ResponseCode.play });
				break;
			}
			// -----------------------------------------------------------
			case S3: {
				if (event.getEvent() == EventType.player) {
					final PlayerResponse response = event.getPlayerResponse();
					if (response.getResponseCode() == ResponseCode.play) {

						final PlayCard c = getCard(response.getGespielteKarte());
						final Player p = getPlayerById(response.getPlayerId());
						final PlayRound r = getCurrentRound();

						if (c == null)
							throw new Exception("card not found: " + response.getGespielteKarte());
						if (p == null)
							throw new Exception("player not found: " + response.getPlayerId());
						
						c.setRoundPosition(r.getCurrentPosition());
						c.setRoundNr(r.getNr());

						// TODO: Terz check
						// TODO: Bella check ...

						if (r.getCurrentPosition() >= playerCount) {
							// Runde ist beendet
							calculatePointsInRound(r);
							if (r.getNr() == 1) {
								aufspieler = r.getWinner();
							}
							gotoGameState(GameState.S1);
						} else {
							// Weiter auf nächste Karte warten
							r.setCurrentPosition(r.getCurrentPosition() + 1);
							gotoGameState(GameState.S2);
						}
					}
				}
				break;
			}
			// -----------------------------------------------------------
			case S4: {
				// Spielende, Gesamtpunkte, Gewinner berechnen.
				Player gameWinner = null;
				for (Player p : player) {
					if (gameWinner == null || p.getPoints() > gameWinner.getPoints())
						gameWinner = p;
				}
				this.winner = gameWinner;
				if (this.winner.getPoints() >= maximumPoints) {
					sendGameInfo();
					gotoGameState(GameState.GOV);
				} else {
					sendGameInfo();
					gotoGameState(GameState.S5);
				}
				break;
			}
			// -----------------------------------------------------------
			case S5: {
				log.info("GAME finished.");
				break;
			}
			// -----------------------------------------------------------
			case GOV: {
				end = new Date();
				log.info("GAME OVER.");
				break;
			}
			// -----------------------------------------------------------
			case X1: {
				if (event.getEvent() == EventType.player) {
					final PlayerResponse response = event.getPlayerResponse();
					if (response.getResponseCode() == ResponseCode.ja) {
						sendPlayerMsg(getPlayerById(response.getPlayerId()), "Tausche die 7!");
						PlayCard trumpf7 = getCard(trumpfFarbe, Kartenwert.Sieben);
						PlayCard changeCard = getCard(trumpfFarbe, original.getWert());
						changeCard.setOwner(trumpf7.getOwner());
						trumpf7.setOwner(null);
						original = trumpf7.getKarte();
					}
				}
				gotoGameState(GameState.S1);
				break;
			}
			// -----------------------------------------------------------
			case X2: {
				break;
			}
			}
		}
	}

	private void sendGameInfo() {
		// TODO Auto-generated method stub

	}

	/**
		* - Stichgewinner ermitteln
	  * - Mitspieler im gleichen Team ermitteln
	  * - Punkte zählen und allen im Team addieren
	  */
	private void calculatePointsInRound(final PlayRound round) {
		Player winner = null;
		PlayCard winnerCard = null;
		int points = 0;
		for (PlayCard c : playcards) {
			if (c.getRoundNr() == round.getNr()) {
				if (winner == null) {
					winner = c.getOwner();
					winnerCard = c;
				} else {
					int r1 = winnerCard.getRank(trumpfFarbe);
					int r2 = c.getRank(trumpfFarbe);
					if (r2 > r1) {
						winner = c.getOwner();
						winnerCard = c;
					}
				}
				points += c.getPoints();
				// Zusatzgewinne
				// 1) Jass
				if (c.isJass(trumpfFarbe)) {
					points += 20;
				}
				// 2) Mie
				if (c.isMie(trumpfFarbe)) {
					points += 14;
				}
			}
		}
		round.setWinner(winner);
		round.setPoints(points);
		winner.setPoints(winner.getPoints() + points);
		log.fine("Round " + round.getNr() + ": " + points + " points for player " + winner.getUsername());
	}

	/**
	 * Spielende feststellen:  wenn alle gegebenen Karten im Stich verwendet worden sind.
	 * @return True, wenn alle gegebenen Karten im Stich verwendet worden sind.
	 */
	private boolean areCardsAvailable() {
		for (PlayCard c : playcards) {
			if (c.getOwner() != null && c.getRoundNr() == 0) {
				return true;
			}
		}
		log.info("Cards available: NO");
		return false;
	}

	private PlayCard getCard(Farbe f, Kartenwert w) {
		for (PlayCard c : playcards) {
			if (c.equals(f, w))
				return c;
		}
		return null;
	}

	private PlayCard getCard(Karte k) {
		for (PlayCard c : playcards) {
			if (c.equals(k))
				return c;
		}
		return null;
	}

	private void gotoGameState(GameState state) {
		GameEvent e = new GameEvent(EventType.gostate, state);
		eventQueue.add(e);
	}

	private Player getPlayerById(int playerId) {
		for (Player p : player) {
			if (p.getId() == playerId)
				return p;
		}
		return null;
	}

	/**
	* Geber wir anhand der höchsten gezogenen karte gewählt,
	* Kurzversion: per Zufall. 
	*/
	public void selectGeber() {
		this.geber = player.iterator().next();
	}

	/**
	 * n-ter Spieler im Uhrzeigersinn vom Geber ausgehend
	 * =REST(A2-1+$B$1;4)+1
	 */
	private Player getPlayerLeftFromGeber(int step) throws Exception {
		final int nextPosition = ((this.geber.getPosition() - 1 + step) % player.size()) + 1;
		for (Player p : player) {
			if (p.getPosition() == nextPosition)
				return p;
		}
		throw new Exception("next player not found(1)");
	}

	private Player getPlayerLeftFromAufspieler(int step) throws Exception {
		final int nextPosition = ((this.aufspieler.getPosition() - 1 + step) % player.size()) + 1;
		for (Player p : player) {
			if (p.getPosition() == nextPosition)
				return p;
		}
		throw new Exception("next player not found(2)");
	}

	private void sendPlayerInfo() {
		for (Player p : player) {
			sendPlayerInfo(p);
		}
	}

	private void sendPlayerMsg(final Player p, final String message) {
		final PlayerCommand pc = new PlayerCommand();
		pc.setPlayerId(p.getId());
		pc.setGameId(gameId);
		pc.setCommandCode(CommandCode.say);
		pc.setMessage(message);
		this.commandListener.toPlayer(pc);
	}

	private void sendPlayerInfo(final Player p) {
		final PlayerCommand pc = new PlayerCommand();
		pc.setPlayerId(p.getId());
		pc.setGameId(gameId);
		pc.setCommandCode(CommandCode.playerinfo);
		pc.setInfo(buildPlayerInfo(p));
		this.commandListener.toPlayer(pc);
	}

	private void sendPlayerCommand(Player p, CommandCode command, ResponseCode[] allowedResponse) {
		final PlayerCommand pc = new PlayerCommand();
		pc.setPlayerId(p.getId());
		pc.setGameId(gameId);
		pc.setCommandCode(command);
		pc.setInfo(buildPlayerInfo(p));
		pc.setAllowedResponse(allowedResponse);
		pc.setErsteKarte(original.getFarbe());
		this.commandListener.toPlayer(pc);
	}

	private PlayerInfo buildPlayerInfo(final Player p) {
		final PlayerInfo i = new PlayerInfo();
		i.setAufspieler(aufspieler != null ? aufspieler.getUsername() : "");
		i.setGameName(gameName);
		i.setGeber(geber != null ? geber.getUsername() : "");
		i.setKarten(buildCardInfo());
		i.setAktiv(p.equals(aufspieler));
		i.setPlayerId(p.getId());
		i.setPlayerName(p.getUsername());
		i.setPosition(p.getPosition());
		i.setPunkte(p.getPoints());
		final PlayRound r = getCurrentRound();
		i.setRunde(r == null ? 0 : r.getNr());
		i.setTrumpf(trumpfFarbe);
		return i;
	}

	private Set<CardInfo> buildCardInfo() {
		final Set<CardInfo> playercards = new HashSet<>();
		for (PlayCard pc : playcards) {
			CardInfo ci = new CardInfo();
			ci.setKarte(pc.getKarte());
			ci.setOffen(pc.getOwner() != null && !pc.getOwner().isComputer());
			ci.setPosition(pc.getOwner() != null ? pc.getOwner().getPosition() : 0);

			CardPlace location = CardPlace.Invisible; // Karte bereits gespielt

			 //0=bereits gespielt/nicht sichtbar, 1=auf Hand, 2=Original, 3=Stich, 4=Stapel
			
			if (pc.equals(original)) {
				//if (state == GameState.G2 || state == GameState.G3 || state == GameState.G4 || state == GameState.G5) {
					location = CardPlace.Original; // Aufgedeckt vor dem Spieler anzeigen
				//}
			}
			if (location == CardPlace.Invisible && isCardInCurrentRound(pc)) {
				location = CardPlace.Bid; // im aktuellen Stich
			}
			if (location == CardPlace.Invisible && pc.getOwner() == null) {
				location = CardPlace.Stock; // im Stapel
			}
			if (location == CardPlace.Invisible && pc.getOwner() != null && pc.getRoundNr() == 0) {
				location = CardPlace.Hand; // Hand, nicht gespielt
			}
			ci.setCardPlace(location);
			playercards.add(ci);
		}
		return playercards;
	}

	private boolean isCardInCurrentRound(PlayCard pc) {
		final PlayRound r = getCurrentRound();
		return (r != null && r.getNr() == pc.getRoundNr());
	}

	private Set<CardInfo> getCardsOnHand(Player p) {
		final Set<CardInfo> playercards = new HashSet<>();
		for (PlayCard pc : playcards) {
			if (pc.getOwner() != null && pc.getOwner().getId() == p.getId()) {
				CardInfo hc = new CardInfo();
				hc.setKarte(pc.getKarte());
				hc.setOffen(!p.isComputer());
				playercards.add(hc);
			}
		}
		return playercards;
	}

	private PlayRound getCurrentRound() {
		return playrounds.size() > 0 ? playrounds.get(playrounds.size() - 1) : null;
	}

	private void sayHello() {
		for (Player p : player) {
			PlayerCommand cmd = new PlayerCommand();
			cmd.setCommandCode(CommandCode.say);
			cmd.setMessage("hello world!");
			cmd.setPlayerId(p.getId());
			commandListener.toPlayer(cmd);
		}
	}

	private void assignFreeCardToPlayer(final Player p, boolean assignOriginal) {
		for (;;) {
			long skip = Math.round(Math.random() * 61), s = 0;
			for (final PlayCard c : playcards) {
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
		this.playcards = playCards;
	}

	public void setId(int id) {
		this.gameId = id;
	}

	public void stopGame() {
		GameEvent e = new GameEvent();
		e.setEvent(EventType.stop);
		this.eventQueue.add(e);
	}

	public void setCommandListener(CommandListener commandListener) {
		this.commandListener = commandListener;
	}

	public void onPlayerResponse(PlayerResponse response) {
		GameEvent e = new GameEvent();
		e.setEvent(EventType.player);
		e.setPlayerResponse(response);
		this.eventQueue.add(e);
	}

	private GameEvent getNextEvent(int timeout_ms) throws InterruptedException {
		return this.eventQueue.poll(timeout_ms, TimeUnit.MILLISECONDS);
	}

}
