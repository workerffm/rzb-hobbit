package com.omic.kj.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import com.omic.kj.shared.domain.CardInfo;
import com.omic.kj.shared.domain.GameInfo;
import com.omic.kj.shared.domain.Karte;
import com.omic.kj.shared.domain.PlayerInfo;
import com.omic.kj.ui.CardArea.Style;
import com.omic.kj.ui.CardEvent.CardListener;

public class JDeskPanel extends JComponent implements CardListener {

	private final Logger log = Logger.getLogger("UI");

	private final Object selectCardMonitor = new Object();
	private final ExecutorService executor = Executors.newCachedThreadPool();

	private final CardArea p1, p2, p3, p4, p5, p6, p7;
	private final PlayerArea u1, u2, u3, u4;
	private int originalPosition;
	private Karte selectedCard;
	//private final Image backgroundImage;
	private final JGameInfoArea gameInfoArea = new JGameInfoArea();

	JDeskPanel(int numberOfPlayer) {
		super();
		//backgroundImage = getImage("/images/table.jpg");

		p1 = new CardArea(this);
		p1.setStyle(Style.ROW);
		p1.setHidden(false);
		p1.addCardListener(this);
		p1.setExposeSelectedCard(true);

		p2 = new CardArea(this);
		p2.setStyle(Style.ROW);
		p2.setHidden(true);
		p2.setOverlapp(0.6d);

		p3 = new CardArea(this);
		p3.setStyle(Style.ROW);
		p3.setHidden(true);
		p3.setOverlapp(0.6d);

		p4 = new CardArea(this);
		p4.setStyle(Style.ROW);
		p4.setHidden(true);
		p4.setOverlapp(0.6d);

		// Stich
		p5 = new CardArea(this);
		p5.setStyle(Style.CROSS);
		p5.setHidden(false);

		// Übrige Karten
		p6 = new CardArea(this);
		p6.setStyle(Style.STACK);
		p6.setHidden(true);

		// Original
		p7 = new CardArea(this);
		p7.setStyle(Style.STACK);
		p7.setHidden(false);

		Dimension d = new Dimension(100, 100);
		u1 = new PlayerArea(this, d, 1);
		u2 = new PlayerArea(this, d, 2);
		u3 = new PlayerArea(this, d, 3);
		u4 = new PlayerArea(this, d, 4);
		//d = getSize();

		//u1.setImage(getImage("/images/player1.png"));
		u2.setImage(getImage("/images/player2.png"));
		u3.setImage(getImage("/images/player3.png"));
		u4.setImage(getImage("/images/player4.png"));

		final MouseOnPlayerListener ml = new MouseOnPlayerListener();
		ml.registerComponent(u2);
		ml.registerComponent(u3);
		ml.registerComponent(u4);
		addMouseListener(ml);
		
		add(gameInfoArea);
	}

	final class MouseOnPlayerListener extends MouseAdapter {
		private final Set<PlayerArea> playerAreas = new HashSet<>();

		public void registerComponent(PlayerArea component) {
			playerAreas.add(component);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			for (final PlayerArea c : playerAreas) {
				if (c.getBounds().contains(e.getPoint())) {
					e.consume();
					log.info("Clicked player: " + c);
					showBubble(c.getPosition(), "Hey!");
					return;
				}
			}
			super.mouseClicked(e);
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
		repaint();
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

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		final Dimension d = getSize();
		final Graphics2D g2 = (Graphics2D) g;
		final AffineTransform saveAT = g2.getTransform();

		//g.drawImage(backgroundImage, 0, 0, null);
		
		gameInfoArea.setBounds(0, d.height-gameInfoArea.getPreferredSize().height,gameInfoArea.getPreferredSize().width, gameInfoArea.getPreferredSize().height );
		gameInfoArea.paint(g2);
		//g2.setTransform(saveAT);
		
		g2.rotate(0, 0, 0);
		//g2.scale(0.8, 0.8);
		if (p1 != null) {
			p1.setLocation(new Point(d.width / 2, d.height));
			p1.setOffset(new Point(0, -5));
			p1.paint(g2);
		}
		g2.rotate(Math.toRadians(90), d.width / 2, d.height / 2);
		if (p2 != null) {
			p2.setLocation(new Point(d.width / 2, d.height));
			p2.setOffset(new Point(-30, -80));
			p2.paint(g2);
		}
		g2.rotate(Math.toRadians(90), d.width / 2, d.height / 2);
		if (p3 != null) {
			p3.setLocation(new Point(d.width / 2, d.height));
			p3.setOffset(new Point(0, -5));
			p3.paint(g2);
		}
		g2.rotate(Math.toRadians(90), d.width / 2, d.height / 2);
		if (p4 != null) {
			p4.setLocation(new Point(d.width / 2, d.height));
			p4.setOffset(new Point(-30, -80));
			p4.paint(g2);
		}
		// !! Reset transformation !!
		g2.setTransform(saveAT);
		if (p5 != null) {
			p5.setLocation(new Point(d.width / 2, d.height / 2));
			p5.paint(g2);
		}
		if (p6 != null) {
			p6.setLocation(new Point(10, d.height - 150));
			p6.paint(g2);
		}
		if (p7 != null) {
			// Originalkarte etwas in Richtung Aufspieler verschieben
			final int dx, dy, D = 150;
			if (originalPosition == 1) {
				dx = 0;
				dy = D;
			} else if (originalPosition == 2) {
				dx = -D;
				dy = 0;
			} else if (originalPosition == 3) {
				dx = 0;
				dy = -D;
			} else if (originalPosition == 4) {
				dx = D;
				dy = 0;
			} else {
				dx = 0;
				dy = 0;
			}
			p7.setLocation(new Point(d.width / 2 + dx, d.height / 2 + dy));
			p7.paint(g2);
		}

		// User names
		//g2.setTransform(saveAT);
		u1.setLocation(new Point(d.width - 100, d.height - 100));
		u2.setLocation(new Point(5, d.height / 2 - 50));
		u3.setLocation(new Point(50, 20));
		u4.setLocation(new Point(d.width - 105, d.height / 2 - 50));
		u1.paint(g2);
		u2.paint(g2);
		u3.paint(g2);
		u4.paint(g2);
	}

	public void setCards(int deskPlaceId, List<CardInfo> cards) {
		if (deskPlaceId == 1) {
			p1.clearCards();
			Collections.sort(cards);
			p1.addCards(cards);
		} else if (deskPlaceId == 2) {
			p2.clearCards();
			p2.addCards(cards);
		} else if (deskPlaceId == 3) {
			p3.clearCards();
			p3.addCards(cards);
		} else if (deskPlaceId == 4) {
			p4.clearCards();
			p4.addCards(cards);
		} else if (deskPlaceId == 5) {
			p5.clearCards();
			p5.addCards(cards);
		} else if (deskPlaceId == 6) {
			p6.clearCards();
			p6.addCards(cards);
		} else if (deskPlaceId == 7) {
			p7.clearCards();
			p7.addCards(cards);
		}
		repaint();
	}

	public void setOriginalPosition(int originalPosition) {
		this.originalPosition = originalPosition;
	}

	private void setPlayerInfo(List<PlayerInfo> playerInfo) {
		for (PlayerInfo pi : playerInfo) {
			if (pi.getPosition() == 1) {
				u1.setName(pi.getName() + " (" + pi.getPunkte() + ")");
			} else if (pi.getPosition() == 2) {
				u2.setName(pi.getName() + " (" + pi.getPunkte() + ")");
			} else if (pi.getPosition() == 3) {
				u3.setName(pi.getName() + " (" + pi.getPunkte() + ")");
			} else if (pi.getPosition() == 4) {
				u4.setName(pi.getName() + " (" + pi.getPunkte() + ")");
			}
		}
		repaint();
	}

	private void setActivePosition(int activePlayerPosition) {
		u1.setActive(1 == activePlayerPosition);
		u2.setActive(2 == activePlayerPosition);
		u3.setActive(3 == activePlayerPosition);
		u4.setActive(4 == activePlayerPosition);
		repaint();
	}

	public void showBubble(int playerPosition, String message) {
		if (playerPosition == 1)
			new BubbleThread(u1, message).start();
		else if (playerPosition == 2)
			new BubbleThread(u2, message).start();
		else if (playerPosition == 3)
			new BubbleThread(u3, message).start();
		else if (playerPosition == 4)
			new BubbleThread(u4, message).start();
		repaint();
	}

	private final class BubbleThread extends Thread {

		private PlayerArea playerArea;
		private String message;

		public BubbleThread(PlayerArea playerArea, String message) {
			this.playerArea = playerArea;
			this.message = message;
		}

		@Override
		public void run() {
			setName("Bubble " + this.playerArea.getPosition());
			this.playerArea.setBubbleMessage(message);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.playerArea.setBubbleMessage(null);
			repaint();
		}

	}


	public void showGameInfo(final GameInfo gameInfo) {
		setPlayerInfo (gameInfo.getPlayerInfo());
		setActivePosition (gameInfo.getActivePlayerPosition());
		if(gameInfo.getGameHistory()!=null) {
			gameInfoArea.add(gameInfo.getGameHistory());
		}
		gameInfoArea.setActiveGameInfo("Spiel läuft", gameInfo.getMaxPoints());
	}

}
