package com.omic.kj.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import layout.TableLayout;
import com.omic.kj.shared.domain.CardInfo;
import com.omic.kj.shared.domain.GameInfo;
import com.omic.kj.shared.domain.Karte;
import com.omic.kj.shared.domain.PlayerInfo;
import com.omic.kj.ui.component.BubbleThread;
import com.omic.kj.ui.component.CardEvent;
import com.omic.kj.ui.component.CardEvent.CardListener;
import com.omic.kj.ui.component.JCardPanel;
import com.omic.kj.ui.component.JGameInfoArea;
import com.omic.kj.ui.component.JPlayer;

/**
 * Klammerjass
 *
 * @version 01.03.2014 Markus create file
 *
 */
public class JDeskPanel extends JPanel implements CardListener {

	private final Logger log = Logger.getLogger("UI");

	private final Object selectCardMonitor = new Object();
	private final ExecutorService executor = Executors.newCachedThreadPool();

	private final JPlayer[] player;
	private final JCardPanel[] cardPanel;
	private final JCardPanel stichPanel;
	private final JGameInfoArea gameInfoArea = new JGameInfoArea();
	
	private int originalPosition;
	private Karte selectedCard;

	JDeskPanel(int numberOfPlayer) {
		super();
		setOpaque(false);
		this.player = new JPlayer[numberOfPlayer];
		this.cardPanel = new JCardPanel[numberOfPlayer];
		
	   
		// PLAYER 1
		{
			final int playerNr = 1;
			final JCardPanel cardPanel = new JCardPanel();
			cardPanel.setRotationQuadrant(playerNr-1);
			cardPanel.setStyle(JCardPanel.Style.ROW);
			cardPanel.setExposeSelectedCard(true);
		
			final JPlayer player = new JPlayer(playerNr);
			player.setImage(getImage("/images/player"+playerNr+".png"));
    	
		  this.player[playerNr-1] = player;
		  this.cardPanel[playerNr-1] = cardPanel;
		  
		  // Register to events from main player
		  cardPanel.addCardListener(this);
		}

		// PLAYER 2
		{
			final int playerNr = 2;
			final JCardPanel cardPanel = new JCardPanel();
			cardPanel.setRotationQuadrant(playerNr-1);
			cardPanel.setStyle(JCardPanel.Style.ROW);
			cardPanel.setExposeSelectedCard(false);
			cardPanel.setOverlapp(0.7d);
		
			final JPlayer player = new JPlayer(playerNr);
			player.setImage(getImage("/images/player"+playerNr+".png"));

			final MouseOnPlayerListener ml = new MouseOnPlayerListener();
			ml.registerComponent(player);

		  this.player[playerNr-1] = player;
		  this.cardPanel[playerNr-1] = cardPanel;
		}
    
		// PLAYER 3
		{
			final int playerNr = 3;
			final JCardPanel cardPanel = new JCardPanel();
			cardPanel.setRotationQuadrant(playerNr-1);
			cardPanel.setStyle(JCardPanel.Style.ROW);
			cardPanel.setExposeSelectedCard(false);
		
			final JPlayer player = new JPlayer(playerNr);
			player.setImage(getImage("/images/player"+playerNr+".png"));

			final MouseOnPlayerListener ml = new MouseOnPlayerListener();
			ml.registerComponent(player);

		  this.player[playerNr-1] = player;
		  this.cardPanel[playerNr-1] = cardPanel;
		}
    
		// PLAYER 4
		{
			final int playerNr = 4;
			final JCardPanel cardPanel = new JCardPanel();
			cardPanel.setRotationQuadrant(playerNr-1);
			cardPanel.setStyle(JCardPanel.Style.ROW);
			cardPanel.setExposeSelectedCard(false);
			cardPanel.setOverlapp(0.7d);
		
			final JPlayer player = new JPlayer(playerNr);
			player.setImage(getImage("/images/player"+playerNr+".png"));

			final MouseOnPlayerListener ml = new MouseOnPlayerListener();
			ml.registerComponent(player);

		  this.player[playerNr-1] = player;
		  this.cardPanel[playerNr-1] = cardPanel;
		}
		
		// STICH
		{
			this.stichPanel = new JCardPanel();
			this.stichPanel.setStyle(JCardPanel.Style.CROSS);
			this.stichPanel.setExposeSelectedCard(false);
			this.stichPanel.setHidden(false);
		}
		
		//setLayout(new BorderLayout());
		//add(gameInfoArea);
		
		// links + recchts
		final JPanel pRL = new JPanel();
		{
			final TableLayout tl = new TableLayout(new double[][] { { TableLayout.PREFERRED,TableLayout.PREFERRED,TableLayout.FILL,TableLayout.PREFERRED,TableLayout.PREFERRED }, { TableLayout.FILL } });
			pRL.setLayout(tl);
			pRL.setOpaque(false);
			pRL.add(this.player[1], "0,0,c,c");
			pRL.add(this.cardPanel[1], "1,0,c,c");
			pRL.add(this.cardPanel[3], "3,0,c,c");
			pRL.add(this.player[3], "4,0,c,c");
		}
		
		// oben + unten
		final JPanel pOU = new JPanel();
		{
			final TableLayout tl = new TableLayout(new double[][] {  { TableLayout.FILL }, { TableLayout.PREFERRED,TableLayout.PREFERRED,TableLayout.FILL,TableLayout.PREFERRED,TableLayout.PREFERRED } });
			pOU.setLayout(tl);
			pOU.setOpaque(false);
			pOU.add(this.player[2], "0,0,c,c");
			pOU.add(this.cardPanel[2], "0,1,c,c");
			pOU.add(this.cardPanel[0], "0,3,c,c");
			pOU.add(this.player[0], "0,4,c,c");
		}
		
		final JPanel pStich = new JPanel();
		{
		  final TableLayout tl = new TableLayout(new double[][] { { TableLayout.FILL }, { TableLayout.FILL } });
		  pStich.setLayout(tl);
			pStich.add(this.stichPanel, "0,0,c,c");
			pStich.setOpaque(false);
		}
		
		final JPanel pInfo = new JPanel();
		{
		  final TableLayout tl = new TableLayout(new double[][] { { TableLayout.PREFERRED,TableLayout.FILL }, { TableLayout.FILL,TableLayout.PREFERRED } });
		  pInfo.setLayout(tl);
		  pInfo.setOpaque(false);
		  pInfo.add(this.gameInfoArea, "0,1");
		}
		
		final TableLayout tl = new TableLayout(new double[][] { { TableLayout.FILL }, { TableLayout.FILL } });
		setLayout(tl);
		add(pOU,"0,0");
		add(pRL,"0,0");
		add(pInfo,"0,0");
		add(pStich,"0,0");
		
		

//		// Übrige Karten
//		p6 = new CardArea(this);
//		p6.setStyle(Style.STACK);
//		p6.setHidden(true);
//
//		// Original
//		p7 = new CardArea(this);
//		p7.setStyle(Style.STACK);
//		p7.setHidden(false);
		
	}
	
		
	final class MouseOnPlayerListener extends MouseAdapter {
		public void registerComponent(JPlayer player) {
			player.addMouseListener(this);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			int playerNr = ((JPlayer)e.getSource()).getPosition();
					log.info("Clicked player: " + playerNr);
					showBubble(playerNr, "Hey!");
					e.consume();
		}
	}

	private Image getImage(String filename) {
		try {
			InputStream is = this.getClass().getResourceAsStream(filename);
			BufferedImage img = ImageIO.read(is);
			is.close();
			return img;
		} catch (IOException e) {
			log.log(Level.WARNING, "Playerimage error", e);
		}
		return null;
	}

	@Override
	public void cardChanged(final CardEvent event) {
		if (event.getEvent() == CardEvent.ChangeType.CARD_CLICKED) {
			log.info(event.toString());
			synchronized (selectCardMonitor) {
				selectedCard = event.getCard();
				selectCardMonitor.notify();
			}
		}
		//repaint();
	}

	public Future<Karte> askForCard() {
		final Callable<Karte> c = new Callable<Karte>() {
			@Override
			public Karte call() throws Exception {
				synchronized (selectCardMonitor) {
					selectedCard = null;
					while (selectedCard == null) {
						selectCardMonitor.wait();
					}
					return selectedCard;
				}
			}
		};
		Future<Karte> f = executor.submit(c);
		return f;
	}


	/**
	 * @param deskPlaceId 1..4 player (1=my player), 5=Stich, 6=Stack, 7=Original
	 * @param cards
	 */
	public void setCards(int deskPlaceId, List<CardInfo> cards) {
		if(deskPlaceId >= 1 && deskPlaceId <= 4){
		  final JCardPanel c = this.cardPanel[deskPlaceId-1];
		c.clearCards();
		c.addCards(cards);
		} else if (deskPlaceId == 5) {
			this.stichPanel.clearCards();
			this.stichPanel.addCards(cards);
		}
//		} else if (deskPlaceId == 6) {
//			p6.clearCards();
//			p6.addCards(cards);
//		} else if (deskPlaceId == 7) {
//			p7.clearCards();
//			p7.addCards(cards);
//		}
		repaint();
	}

	public void setOriginalPosition(int originalPosition) {
		this.originalPosition = originalPosition;
	}

	private void setPlayerInfo(List<PlayerInfo> playerInfo) {
		for (PlayerInfo pi : playerInfo) {
			final JPlayer p = this.player[pi.getPosition()-1];
		  p.setName(pi.getName() + " (" + pi.getPunkte() + "/" + pi.getKleinesHolz()+")");
		}
		repaint();
	}
	

	public void showGameInfo(final GameInfo gameInfo) {
		setPlayerInfo (gameInfo.getPlayerInfo());
		setActivePosition (gameInfo.getActivePlayerPosition());
		if(gameInfo.getGameHistory()!=null) {
			gameInfoArea.add(gameInfo.getGameHistory());
		}
		gameInfoArea.setActiveGameInfo("Spiel läuft", gameInfo);
		repaint();
	}

	private void setActivePosition(int activePlayerPosition) {
		for (JPlayer p:this.player) {
			p.setActive(activePlayerPosition == p.getPosition());
		}
		repaint();
	}

	public void showBubble(int playerPosition, String message) {
		new BubbleThread(this.player[playerPosition-1], message).start();
	}
}
