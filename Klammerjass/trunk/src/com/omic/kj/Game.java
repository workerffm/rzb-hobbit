package com.omic.kj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import com.omic.kj.shared.domain.CardInfo;
import com.omic.kj.shared.domain.CardPlace;
import com.omic.kj.shared.domain.CommandCode;
import com.omic.kj.shared.domain.Farbe;
import com.omic.kj.shared.domain.GameHistory;
import com.omic.kj.shared.domain.GameInfo;
import com.omic.kj.shared.domain.GameSettings;
import com.omic.kj.shared.domain.Karte;
import com.omic.kj.shared.domain.Kartenwert;
import com.omic.kj.shared.domain.MessageInfo;
import com.omic.kj.shared.domain.Player;
import com.omic.kj.shared.domain.PlayerCommand;
import com.omic.kj.shared.domain.PlayerInfo;
import com.omic.kj.shared.domain.PlayerResponse;
import com.omic.kj.shared.domain.ResponseCode;

final class Game {

	private final Logger log = Logger.getLogger("Game");

	private int gameId; // identifier for compare method
	private String gameName;
	private int maxSessionPoints; // Spielende bei diesen Punkten

	private CommandListener commandListener; // send player command to this listener!
	private final ArrayBlockingQueue<GameEvent> eventQueue;
	private GameState gameState; // aktuelle Phase

	private final List<Player> player; // 2 to 4 players per game
	private final Set<ComputerPlayer> computerPlayer; // save extra the auto player
	private final List<GameHistory> gameHistory;

	/** Game transaction data : */
	private List<PlayCard> playcards; // 32 cards with special tags on it
	private Date start, end; // game duration
	private Farbe trumpfFarbe; // Trumpffarbe
	private Player geber; // Kartengeber
	private Player trumpfPlayer; // Spieler der original oder kleines spielt.
	private Karte original; // Die umgedrehte Karte beim Geben, für alle sichtbar.
	private boolean originalSelected; // trumpfPlayer wählte original
	private Player gameWinner; // Spielgewinner
	private int nextRoundNr; // Rundennummer
	private final List<PlayRound> playrounds;
	private int gameNr; // Spielzähler für die History
	private Player activePlayer;

	/** Game transaction data */

	private void resetGame() {
		//gameWinner = null;
		trumpfFarbe = null;
		geber = null;
		activePlayer = null;
		trumpfPlayer = null;
		original = null;
		originalSelected = false;
		nextRoundNr = 0;
		playrounds.clear();

		for (PlayCard c : playcards) {
			c.setOwner(null);
			c.setRoundNr(0);
			c.setBidNr(0);
		}
	}

	public Game() {
		this.player = new ArrayList<>();
		this.computerPlayer = new HashSet<>();
		this.gameHistory = new ArrayList<>();
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
			setLimit(Math.min(300, maxSessionPoints));
		} else {
			throw new Exception("Too many players for this game.");
		}
	}

	public void joinGame(final ComputerPlayer computerPlayer) throws Exception {
		joinGame(computerPlayer.getPlayer(), null);
		this.computerPlayer.add(computerPlayer);
	}

	private void setLimit(int maximumPoints) {
		this.maxSessionPoints = maximumPoints;
	}

	public boolean hasPlayer(int playerId) {
		for (Player p : player) {
			if (p.getId() == playerId)
				return true;
		}
		return false;
	}

	public void startGame() throws Exception {

		// PLAYER CHECK
		//
		final int playerCount = player.size();
		{
			if (playerCount < 2) {
				throw new Exception("Zu wenig Spieler für ein Spiel.");
			}
			if (playerCount > 4) {
				throw new Exception("Zu viele Spieler für ein Spiel.");
			}
			if (playerCount < 2 || playerCount > 4) {
				throw new Exception("Unpassende Spieleranzahl.");
			}
		}

		// FIRST TIME INITIALIZATION
		//
		loadCards();
		start = new Date();
		gameName = "Game " + gameId;
		gameNr = 0;
		gotoGameState(GameState.G0);

		// LOOP FOR 4 PLAYER !!

		for (;;) {

			final GameEvent event = getNextEvent(20000);
			Thread.sleep(50);

			if (event == null) {
				log.info("State=" + gameState + ", waiting for events");
				continue;
			}
			log.info("State=" + gameState + ", event=" + event);

			if (event.getEvent() == EventType.gostate) {
				gameState = event.getState();
			}

			switch (gameState) {

			// -----------------------------------------------------------
			case G0: {
				resetGame();
				gameNr++;
				nextRoundNr = 1;
				selectGeber();
				sendGameInfo();
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
				gameState = GameState.G3;
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
						gameState = GameState.G4;
					} else if (response.getResponseCode() == ResponseCode.ja) {
						trumpfPlayer = getPlayerById(response.getPlayerId());
						trumpfFarbe = original.getFarbe();
						originalSelected = true;
						gotoGameState(GameState.S0);
					} else {
						throw new Exception("Unexpected response: state=" + gameState + ", response=" + response.getResponseCode());
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
						gameState = GameState.G5;
					} else if (response.getResponseCode() == ResponseCode.ja) {
						trumpfPlayer = getPlayerById(response.getPlayerId());
						trumpfFarbe = original.getFarbe();
						originalSelected = true;
						gotoGameState(GameState.S0);
					} else {
						throw new Exception("Unexpected response: state=" + gameState + ", response=" + response.getResponseCode());
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
						gameState = GameState.G6;
					} else if (response.getResponseCode() == ResponseCode.ja) {
						trumpfPlayer = getPlayerById(response.getPlayerId());
						trumpfFarbe = original.getFarbe();
						originalSelected = true;
						gotoGameState(GameState.S0);
					} else {
						throw new Exception("Unexpected response: state=" + gameState + ", response=" + response.getResponseCode());
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
						gameState = GameState.G7;
					} else if (response.getResponseCode() == ResponseCode.ja) {
						trumpfPlayer = getPlayerById(response.getPlayerId());
						trumpfFarbe = original.getFarbe();
						originalSelected = true;
						gotoGameState(GameState.S0);
					} else {
						throw new Exception("Unexpected response: state=" + gameState + ", response=" + response.getResponseCode());
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
						gameState = GameState.G8;
					} else if (response.getResponseCode() == ResponseCode.ja) {
						Player p = getPlayerLeftFromGeber(+1);
						sendPlayerCommand(p, CommandCode.frageTrumpffarbe, new ResponseCode[] { ResponseCode.waehleFarbe });
						gameState = GameState.G11;
					} else {
						throw new Exception("Unexpected response: state=" + gameState + ", response=" + response.getResponseCode());
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
						gameState = GameState.G9;
					} else if (response.getResponseCode() == ResponseCode.ja) {
						Player p = getPlayerLeftFromGeber(+2);
						sendPlayerCommand(p, CommandCode.frageTrumpffarbe, new ResponseCode[] { ResponseCode.waehleFarbe });
						gameState = GameState.G12;
					} else {
						throw new Exception("Unexpected response: state=" + gameState + ", response=" + response.getResponseCode());
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
						gameState = GameState.G10;
					} else if (response.getResponseCode() == ResponseCode.ja) {
						Player p = getPlayerLeftFromGeber(+3);
						sendPlayerCommand(p, CommandCode.frageTrumpffarbe, new ResponseCode[] { ResponseCode.waehleFarbe });
						gameState = GameState.G13;
					} else {
						throw new Exception("Unexpected response: state=" + gameState + ", response=" + response.getResponseCode());
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
						gameState = GameState.G14;
					} else {
						throw new Exception("Unexpected response: state=" + gameState + ", response=" + response.getResponseCode());
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
						throw new Exception("Unexpected response: state=" + gameState + ", response=" + response.getResponseCode());
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
						gameState = GameState.X1;
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
					// Neue Runde starten
					final PlayRound newRound = new PlayRound();
					newRound.setNr(nextRoundNr);
					newRound.setBidNr(0);
					// In der 1. Runde spielt der Nebenmann auf:
					if (newRound.getNr() == 1) {
						newRound.setAufspieler(getPlayerLeftFromGeber(+1));
					} else {
						newRound.setAufspieler(getCurrentRound().getWinner());
					}
					nextRoundNr++;
					playrounds.add(newRound);
					// Prepare next step and send out game info 
					gotoGameState(GameState.S2);
				}
				break;
			}
			// -----------------------------------------------------------
			case S2: {
				final PlayRound r = getCurrentRound();
				// Nächster Spieler im Uhrzeigersinn:
				final Player p = getPlayerLeftFrom(r.getAufspieler(), r.getBidNr());
				setActivePlayer(p);
				sendGameInfo(false);
				sendPlayerInfo();
				sendPlayerCommand(p, CommandCode.spieleKarte, new ResponseCode[] { ResponseCode.play });
				gotoGameState(GameState.S3);
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

						c.setBidNr(r.getBidNr());
						c.setRoundNr(r.getNr());

						// TODO: Terz check
						// TODO: Bella check ...
						sendPlayerInfo();

						if (r.getBidNr() >= playerCount - 1) {
							// Runde ist beendet
							calculatePointsInRound(r);
							//aufspieler = r.getWinner();
							log.info("ROUND finished: " + r.toString());
							gotoGameState(GameState.S1);
						} else {
							// Weiter auf nächste Karte warten
							r.setBidNr(r.getBidNr() + 1);
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
				int gamePoints = 0;
				for (Player p : player) {
					final int n = getTotalGamePoints(p);
					if (gameWinner == null || n > gamePoints) {
						gameWinner = p;
						gamePoints = n;
					}
				}
				this.gameWinner = gameWinner;

				// Record the game
				final GameHistory h = new GameHistory(this.gameNr, gamePoints, this.gameWinner.getUsername());
				gameHistory.add(h);

				if (getMaxGamePoints() >= maxSessionPoints) {
					sendGameInfo();
					gotoGameState(GameState.GOV);
				} else {
					gotoGameState(GameState.S5);
				}
				break;
			}
			// -----------------------------------------------------------
			case S5: {
				log.info("Game finished.");
				for (Player p : player) {
					if ((System.currentTimeMillis() % 5) == p.getPosition()) {
						sendPlayerMsg(p, "Weiter!");
					}
				}
				sendPlayerInfo();
				sendGameInfo(true);
				// Restart Game:
				gotoGameState(GameState.G0);
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

	private void setActivePlayer(Player p) {
		this.activePlayer = p;
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
		//winner.setRoundPoints(points);
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
	private Player getPlayerLeftFrom(Player pp, int step) throws Exception {
		final int nextPosition = ((pp.getPosition() - 1 + step) % player.size()) + 1;
		for (Player p : player) {
			if (p.getPosition() == nextPosition)
				return p;
		}
		throw new Exception("next player not found(1)");
	}

	private Player getPlayerLeftFromGeber(int step) throws Exception {
		return getPlayerLeftFrom(this.geber, step);
	}

	/**
	 * Distribute all player info to all other player
	 */
	private void sendGameInfo(boolean withGameHistory) {
		final List<PlayerInfo> playerInfo = new ArrayList<PlayerInfo>(player.size());
		// Collect info about all players
		for (Player p : player) {
			playerInfo.add(buildPlayerInfo(p));
		}
		int activePlayerPosition = this.activePlayer != null ? this.activePlayer.getPosition() : 0;
		final GameInfo gameInfo = new GameInfo();
		gameInfo.setPlayerInfo(playerInfo);
		gameInfo.setMaxPoints(this.maxSessionPoints);
		// Send the last history if the game is finished only:
		gameInfo.setGameHistory(withGameHistory && gameHistory.size() > 0 ? gameHistory.get(gameHistory.size() - 1) : null);
		final PlayRound r = getCurrentRound();
		gameInfo.setActivePlayerPosition(activePlayerPosition);
		// Send to all players
		for (Player p : player) {
			final PlayerCommand pc = new PlayerCommand();
			pc.setGameId(gameId);
			pc.setCommandCode(CommandCode.gameinfo);
			pc.setGameInfo(gameInfo);
			pc.setPlayerId(p.getId());
			this.commandListener.toPlayer(pc);
		}
	}

	private void sendGameInfo() {
		sendGameInfo(false);
	}

	/**
	 * Distribute current player info to all other player
	 */
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
		final MessageInfo mi = new MessageInfo(p.getPosition(), message);
		pc.setMessageInfo(mi);
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
		final PlayRound r = getCurrentRound();
		final PlayerInfo pi = new PlayerInfo();
		pi.setAufspieler(r != null ? r.getAufspieler().getUsername() : "");
		pi.setGeber(geber != null ? geber.getUsername() : "");
		pi.setActive(p.equals(this.activePlayer));
		pi.setKarten(buildCardInfo());
		pi.setPlayerId(p.getId());
		pi.setPosition(p.getPosition());
		pi.setTotalpunkte(countTotalPoints(p));
		pi.setPunkte(getRoundPoints(p));
		pi.setName(p.getUsername());
		pi.setRunde(r == null ? 0 : r.getNr());
		pi.setTrumpf(trumpfFarbe);
		return pi;
	}

	private int getRoundPoints(Player p) {
		// TODO Auto-generated method stub
		return 0;
	}

	private int getTotalGamePoints(Player p) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private int countTotalPoints(Player p) {
		int points = 0;
		for (PlayRound r : this.playrounds) {
			if (r.getWinner() == p)
				points += getRoundPoints(p);
		}
		return points;
	}

	private int getMaxGamePoints() {
		int points = 0;
		for (Player p : player) {
			int n = countTotalPoints(p);
			if (n > points) {
				points = n;
			}
		}
		return points;
	}

	
	private List<CardInfo> buildCardInfo() {

		final List<CardInfo> playercards = new ArrayList<>();
		final Map<Integer, CardInfo> roundCards = new HashMap<>();

		for (final PlayCard card : playcards) {
			final CardInfo ci = new CardInfo();
			ci.setKarte(card.getKarte());
			ci.setOffen(card.getOwner() != null && !card.getOwner().isComputer());
			ci.setPlayerPosition(card.getOwner() != null ? card.getOwner().getPosition() : 0);
			//ci.setRoundPosition(0);

			CardPlace location = CardPlace.Invisible; // Karte bereits gespielt

			//0=bereits gespielt/nicht sichtbar, 1=auf Hand, 2=Original, 3=Stich, 4=Stapel

			if (card.equals(original)) {
				if (!originalSelected) {
					location = CardPlace.Original; // Aufgedeckt vor dem Spieler anzeigen
				}
			}
			if (location == CardPlace.Invisible && isCardInCurrentRound(card)) {
				location = CardPlace.Bid; // im aktuellen Stich
				//ci.setRoundPosition(card.getRoundPosition());
				roundCards.put(card.getBidNr(), ci);
			}
			if (location == CardPlace.Invisible && card.getOwner() == null) {
				location = CardPlace.Stock; // im Stapel
			}
			if (location == CardPlace.Invisible && card.getOwner() != null && card.getRoundNr() == 0) {
				location = CardPlace.Hand; // Hand, nicht gespielt
			}
			ci.setCardPlace(location);
			playercards.add(ci);
		}
		// Integrate cards from bid in correct order
		playercards.removeAll(roundCards.values());
		playercards.addAll(roundCards.values());
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
			sendPlayerMsg(p, "Hello Player!");
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

	public void setGameId(int gameId) {
		this.gameId = gameId;
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
