//package com.omic.kj;
//
//import java.awt.Color;
//import java.util.*;
//import java.util.logging.Logger;
//import com.omic.kj.shared.GameSettings;
//import com.omic.kj.shared.Player;
//
///**
// * Ein GameProcessor ist jeweils zuständig für genau ein Spiel.
// * Die Daten (Spieler, Karten, etc.) liegen im Member "game" bereit, die Spielregeln 
// * werden in dieser Klasse umgesetzt. 
// * 
// * @author cb3arbe
// * @version 28.7.2011 arbeitm 1st draft <br>
// *
// */
//public class GameProcessor {
//	
//	private transient final Logger log;
//	
//	/** Keeps information about one Klammerjass (player, points, round, trumpf, ....) */
//	private transient final Game game;
//	
//	/** Map of callback interfaces to a user/player */
//	private transient final Map<Player, PlayerInterface> playerInterfaces;
//
//	
//	public GameProcessor() throws GameException {
//		try { 
//			log = Logger.getLogger("Game");
//			Thread.currentThread().setName("Game-Processor");
//			//log = Logger.getLogger("Game");
//			//log.info("Starting new game...");
//			game = new Game();
//			playerInterfaces = new HashMap<Player,PlayerInterface>();
//		}
//		catch (Exception x) {
//			throw new GameException ("Setup of new Game Processor failed.",x);
//		}
//	}
//
//	
//	public void startGame() throws GameException {
//		try {
//			int n = game.getPlayers().size();
//			if (n<2) throw new GameException("Zu wenig Spieler für ein Spiel.");
//			if (n>4) throw new GameException("Zu viele Spieler für ein Spiel.");
//			game.setStart(new Date());
//			loadCards();
//			selectGeber();
//			geben();
//			GameLogger.printGame(game);
//			startRounds();
//		} 
//		catch (GameException e) {
//			throw e;
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Karten frisch initialisieren.
//	 */
//	private void loadCards() {
//		ArrayList<PlayCard> cards = new ArrayList<PlayCard>();
//		for (Card c : Card.getAllCards()) {
//			PlayCard p = new PlayCard(c);
//			cards.add(p);
//		}
//		game.setCards(Collections.unmodifiableList(cards));
//	}
//
//	/**
//	 * Geber wir anhand der höchsten gezogenen karte gewählt,
//	 * Kurzversion: per Zufall. 
//	 */
//	public void selectGeber() {
//		game.setGeber(game.getPlayers().iterator().next());
//	}
//
//	private void geben() throws GameException {
//		Set<Player> player = game.getPlayers();
//		if (player.size() == 4)
//			geben4();
//		else if (player.size() == 2 || player.size() == 3)
//			geben2();
//		else
//			throw new GameException("Unpassende Spieleranzahl.");
//	}
//
//	/**
//	 * Kartenverteilregel für 4 Spieler
//	 * @throws GameException 
//	 */ 
//	private void geben4() throws GameException {
//		Set<Player> player = game.getPlayers();
//		// 3 Karten für jeden
//		for (int i = 0; i < 3; i++) {
//			for (Player p : player) {
//				assignFreeCardToPlayer(p);
//				pli(p).giveCard(game);
//			}
//		}
//		// 2 Karten für jeden
//		for (int i = 0; i < 2; i++) {
//			for (Player p : player) {
//				assignFreeCardToPlayer(p);
//				pli(p).giveCard(game);
//			}
//		}
//		boolean ok = pli(game.getGeber()).askOriginal(game);
//		if (ok) {
//			// 3 Karten für jeden
//			for (int i = 0; i < 3; i++) {
//				for (Player p : player) {
//					assignFreeCardToPlayer(p);
//					pli(p).giveCard(game);
//				}
//			}
//		}
//		else{
//			throw new GameOverException("Kein Trumpf gewählt.");
//		}
//	}
//
//	/**
//	 * Return the PlayerInterface (callback methods) for a player.
//	 */
//	private PlayerInterface pli (Player p) {
//	  return playerInterfaces.get(p);	
//	}
//
//	/**
//	 * Kartenverteilregel für 2-3 Spieler
//	 * @throws GameException 
//	 * @throws Exception 
//	 */
//	private void geben2() throws GameException {
//		Set<Player> player = game.getPlayers();
//		// 3 Karten für jeden
//		for (int i = 0; i < 3; i++) {
//			for (Player p : player) {
//				assignFreeCardToPlayer(p);
//				pli(p).giveCard(game);
//			}
//		}
//		// 1 Karte in die Mitte
//		assignOriginal();
//		// 3 Karten für jeden
//		for (int i = 0; i < 3; i++) {
//			for (Player p : player) {
//				assignFreeCardToPlayer(p);
//				pli(p).giveCard(game);
//			}
//		}
//		// 
//		// Frage nach Spielfarbe, "Farbe XYZ spielen?"
//		//
//		for (Player p:game.getPlayers()) {
//			if (pli(p).askOriginal(game)) {
//				game.confirmOriginal();
//				game.setMainPlayer(p);
//			}
//		}
//		if (game.getTrumpf()!=null) {
//			// 3 Karten für jeden
//			for (int i = 0; i < 3; i++) {
//				for (Player p : player) {
//					assignFreeCardToPlayer(p);
//					pli(p).giveCard(game);
//				}
//			}
//		} 
//		else {
//			// 
//			// Frage nach Spielfarbe, "Farbe XYZ spielen?"
//			//
//			for (Player p:game.getPlayers()) {
//				if (pli(p).askKleines(game)) {
//				  Color f = pli(p).askFarbe(game);
//				  if (f!=null) {
//					  game.setTrumpf(f);
//					  game.setMainPlayer(p);
//				  }
//				}
//			}
//			if (game.getTrumpf()==null) {
//				stopGame();
//				throw new GameOverException("Keiner hat einen Trumpf gewählt.");
//			}
//			// 3 Karten für jeden
//			for (int i = 0; i < 3; i++) {
//				for (Player p : player) {
//					assignFreeCardToPlayer(p);
//					pli(p).giveCard(game);
//				}
//			}
//		}
//		/** Spieler, der die Trumpf 7 hält, kann mit 'Original' tauschen */
//		PlayCard trumpf7 = game.getCard (game.getTrumpf(), Card.Name.Sieben);
//		if(trumpf7.getOwner()!=null && !game.getOriginal().equals(trumpf7)) {
//			Player trumpf7_owner = trumpf7.getOwner();
//			if (pli(trumpf7_owner).askTausche7(game)) {
//				game.getOriginal().setOwner(trumpf7_owner);
//				trumpf7.setOwner(null);
//			}
//		}
//		// send refresh command to all player!
//		refreshClients();
//	}
//
//
//	private void assignOriginal() {
//		for(;;) {
//			long skip = Math.round(Math.random()*61), s=0;
//			for (PlayCard c : getGame().getCards()) {
//				if (s++ >= skip) {
//					if (c.isFree()) {
//						c.setOriginal(true);
//						return;
//					}
//				}
//			}
//		}
//	}
//
//	private void assignFreeCardToPlayer(Player p) {
//		for(;;) {
//			long skip = Math.round(Math.random()*61), s=0;
//			for (PlayCard c : getGame().getCards()) {
//				if (s++ >= skip) {
//					if (c.isFree()) {
//						c.setOwner(p);
//						//log.finer ("card assigned: card="+c+", player="+p);
//						return;
//					}
//				}
//			}
//		}
//	}
//
//
//	public Game getGame() {
//    return game;		
//	}
//
//	public boolean isRunning() {
//		return game.getStart()!=null;
//	}
//
//	
//	@Override
//	public void joinGame(Player p, GameSettings gs, PlayerInterface pi) throws GameException {
//		if (game.getPlayers().size()<4) { 
//			game.getPlayers().add(p);
//			p.setPosition(game.getPlayers().size());
//		  /** TODO: Position/Lücke beachten ! */
//			game.setLimit (gs.getMaximumPoints());
//		  playerInterfaces.put(p,pi);
//		}
//		else {
//			throw new GameException("Too many players for this game.");
//		} 
//	}
//
//	@Override
//	public void leaveGame(Player p) throws GameException {
//		if (game.getPlayers().contains(p)) {
//		  if (game.isRunning())
//    		stopGame();
//		  game.getPlayers().remove(p);
//		  /** TODO: Position/Lücke beachten ! */
//		}
//	}
//
//	private void stopGame() {
//		game.setEnd(new Date());
//		//playerInterfaces.clear();
//	}
//
//	/**
//	 * Wenn alle user auf "Start" geclickt haben, dann geht es los.
//	 */
//	@Override
//	public void startGame (Player p) throws GameException {
//		if (game.isTerminated()) throw new GameException("Game is already over.");
//		p.setReadyToPlay();   // mark the player wants to start
//		// enough players? try to start the game....
//		if(game.getPlayers().size()>1) { 
//			boolean all_user_want_start=true;
//			for (Player r : game.getPlayers()) {
//				if (!r.isReadyToPlay()) {
//					all_user_want_start=false;
//				}
//			}
//			if (all_user_want_start) {
//			  startGame();
//			}
//		}
//	}
//
//	
//	@Override
//	public void play(Player p, Card c) throws GameException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void say(Player p, String message) throws GameException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	// ------------------------------------------------------------------------------------------------------------------------------
//	// ------------------------------------------------------------------------------------------------------------------------------
//	// ------------------------------------------------------------------------------------------------------------------------------
//	
//	/**
//	 * Nach dem Geben das eigentliche Spiel beginnen.... 
//	 * @throws GameException 
//	 */
//	private void startRounds() throws GameException {
//		teamsVerteilen();
//	    
//	  	// 1) Aufspieler = nächsten Spieler hinter Geber finden....
//	  	//
//	  	Player aufspieler = getNextPlayer(game.getGeber());
//	  	game.setFirstPlayer(aufspieler);
//	  	game.setCurrentPlayer(aufspieler);
//	  	game.setRound(0);
//
//	  	// 2) Rauskommen
//	  	//
//			Player spieler = aufspieler;
//	  	int order =1;
//	  	
//		  while (areCardsAvailable()) {
//				refreshClients();
//		  	
//		  	// ein Stich oder eine Runde machen...
//		  	//
//				game.setRound(game.getRound()+1);
//		  	game.setRoundFirstPlayer(spieler);
//		  	order =1;  // Spielreihenfolge innerhalb vom Stich
//		  	
//		  	for (int r=0; r < game.getPlayers().size(); r++) {
//		  		refreshClients();
//		  		// Spieler ist dran, auf eine Karte warten.... 
//		  	  //
//		  		Card _c = pli(spieler).askCard(game);
//		  		sleep(50);
//		  	  // keine Karte gewählt: entweder ist keine mehr verfügbar (dann Spielende), der User hat nichts eingegeben (erneut versuchen)
//		  		if (_c==null) {
//		  	  	if (!areCardsAvailable())
//		  	  		break;
//		  	  	else
//		  	  	  continue;
//		  	  }
//		  	  
//		  	  // Karte im Stich verbuchen...
//		  	  //
//		  	  PlayCard c = game.getCard(_c);
//		  	  if(c.getRound()!=0) {
//		  	  	throw new GameException ("Karte wurde bereits gespielt. "+c);
//		  	  }
//		  	  if(order==1)  {
//		  	  	//c.setFirstCardInRound(true);
//		  	  	setRoundCard(c);
//		  	  }
//		  	  c.setRound(game.getRound());
//		  	  c.setOrder (order++);
//		  	  
//		  	  // nächsten Spieler bestimmen...
//		  	  /** @todo der Rundengewinner kommt als nächstes dran */ 
//		  	  //
//		  	  spieler = getNextPlayer(spieler);
//			  	game.setCurrentPlayer(spieler);
//		  	}  // ------ nächste Karte -----------
//		  	
//	  	  //---- Stich beendet----------------------
//		  	// Calculate Stich Gewinner 
//		  	calculatePointsPerRound();  // Stichende: Punkte berechnen
//	  	  spieler = getNextPlayer();
//  	  }  // ------ nächster Stich ------- 
//	  	calculatePointsPerGame();  // Spielende: Punkte berechnen
//	  	GameLogger.printGame(game);
//	}
//
//
//	private void setRoundCard(PlayCard c) {
//		game.setRoundCard(c);
//	}
//
//
//	/**
//	 * Spielende feststellen:  wenn alle gegebenen Karten im Stich verwendet worden sind.
//	 * @return True, wenn alle gegebenen Karten im Stich verwendet worden sind.
//	 */
//	private boolean areCardsAvailable() {
//    for (PlayCard c:game.getCards()) {		
//		  if (c.getOwner()!=null && c.getRound()==0 /*&& !c.isOriginal()*/) {
//		  	System.out.println ("areCardsAvailable: YES");
//		  	return true;
//		  }
//    }
//  	System.out.println ("areCardsAvailable: NO");
//    return false;
//	}
//
//	
//	/**
//	 * Der nächste Ausspieler ist der letzte Rundengewinner
//	 * @return
//	 */
//	private Player getNextPlayer() {
//		return game.getLastRoundWinner();
//	}
//
//	/**
//	 * Finde nächsten Spieler im Uhrzeigersinn.
//	 */
//	private Player getNextPlayer (Player me) {
//		Player next=me;
//  	Iterator<Player> iter = game.getPlayers().iterator();
//  	while(iter.hasNext()) {
//  		if (iter.next().equals(me)){
//				if(iter.hasNext())   // nächster spieler im Uhrzeigersinn 
//  				next = iter.next();
//  			else                 // wieder der erste Spieler, wenn Liste zu Ende ist.
//  				next = game.getPlayers().iterator().next();
//  			break;
//  		}
//  	}
//		return next;
//	}
//
//	
//	private Set<Player> getTeamPlayers(int teamId) {
//		Set<Player> tp = new HashSet<Player>();
//		for (Player p:game.getPlayers()) {
//			if (p.getTeamId() == teamId)
//				tp.add(p);
//		}
//		return tp;
//	}
//
//	/**
//	 * Bei 1-3 Spielern: <br>
//	 * Gruppe 1: Mainplayer <br>
//	 * Gruppe 2: die restlichen Spieler <br>
//	 *  <br>
//	 * Bei 4 Spielern: <br>
//	 * Gruppe 1: Mainplayer + gegenübersitzender Spieler<br>
//	 * Gruppe 2: die restlichen Spieler <br>
//	 */
//	private void teamsVerteilen() {
//		if (game.getPlayers().size() <= 3) {
//			for (Player p:game.getPlayers()) {
//				if (p.equals(game.getMainPlayer())) {
//					p.setTeamId(1);
//				}
//				else
//					p.setTeamId(2);
//			}
//		}
//		else {
//			/**    Player 1(Team 1)  
//			 *   4(2) 2(2)
//			 *     3(1) 
//			 */
//			int t=1;
//			for (Player p:game.getPlayers()) {
//				p.setTeamId(t++ %2);
//			}
//		}
//	}
//
//	/**
//	 * send refresh command to all player! 
//	 * @throws GameException 
//	 */
//	private void refreshClients() throws GameException {
//		for (Player p:game.getPlayers()) {
//			pli(p).updateRound(game);
//		}
//	}
//
//	private void sleep(int i) {
//		try { Thread.sleep(i); } catch (InterruptedException e) { }
//	}
//
//	/**
//	 * - Stichgewinner ermitteln
//	 * - Mitspieler im gleichen Team ermitteln
//	 * - Punkte zählen und allen im Team addieren
//	 */
//	private void calculatePointsPerRound () {
//		int round = game.getRound();  // für diese Runde zählen
//		Player gewinner = null;
//		PlayCard gewinnerCard = null;
//		int points = 0;
//		for (PlayCard c : game.getCards()) {
//			if (c.getRound()==round) {
//			  if (gewinner==null) {
//			  	gewinner = c.getOwner();
//			  	gewinnerCard = c;
//			  }
//			  else {
//			    //int r1 = gewinnerCard.getCard().getRank() + (gewinnerCard.getCard().getColor() == game.getTrumpf() ? 100 : 0 );
//			    //int r2 = c.getCard().getRank() + (c.getCard().getColor() == game.getTrumpf() ? 100 : 0 );
//			  	int r1 = gewinnerCard.getRank(game.getTrumpf());
//			  	int r2 = c.getRank(game.getTrumpf());
//			  	if (r2>r1) {
//				  	gewinner = c.getOwner();
//				  	gewinnerCard = c;
//			    }	
//			  }
//			  points += c.getCard().getPoints();
//			  // Zusatzgewinne
//			  // 1) Jass
//			  if(c.isJass(game.getTrumpf())) {
//				  //c.getOwner().addPoints(20);
//			  	points += 20;
//			  }
//			  // 2) Mie
//			  if(c.isMie(game.getTrumpf())) {
//				  //c.getOwner().addPoints(14);
//			  	points += 14;
//			  }
//			}
//		}
//  	game.setRoundWinner (round, gewinner);
//  	// Punkte im Team incl. Gewinner verteilen
//  	for (Player p:getTeamPlayers(gewinner.getTeamId())) {
//  	  p.addPoints(points);  
//  	  log.fine ("Round "+game.getRound()+": "+points+" points for player "+p.getName());
//  	}
//	}
//
//	
//	/**
//	 * - Meldungen der einzelnen Spieler addieren
//	 * - Punkte für Meldungen addieren 
//	 */
//	private void calculatePointsPerGame() {
//		Player lastWinner = game.getLastRoundWinner();
//		lastWinner.addPoints(10);
//	}
//
//
//
//}
