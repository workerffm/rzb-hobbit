package com.omic.kj.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import com.omic.kj.shared.domain.CardInfo;
import com.omic.kj.shared.domain.Karte;
import com.omic.kj.ui.CardEvent.CardListener;
import com.omic.kj.ui.CardEvent.ChangeType;

public final class JCardArea extends JComponent {

	private final Logger log = Logger.getLogger("UI");

	public enum Style {
		/** Alle Karten übereinander */
		STACK,
		/** Alle Karten etwas versetzt im Kreis, a  lá Stich */
		CROSS,
		/** Alle Karten in einer Reihe */
		ROW
	};

	//private final int BORDER =80;
	private final int EXPOSE_OFFSET = 21;
	private final int CROSS_OFFSET = 45;
	private final double DEFAULT_OVERLAPP = .45d; // x% overlapp to next card

	private boolean hidden;
	private boolean exposeSelectedCard;
	private Style style;
	private Karte selectedCard;
	private Point offset;

	private final List<CardInfo> cards;
	private final Dimension cardDimension;
	private final List<Entry<Rectangle, Karte>> cardAreas;
	private final Set<CardListener> listeners;
	private double crossRotation;
	private double overlapp;
	private double rotationInRadians;

	public JCardArea() {
		super();
		cards = new ArrayList<>();
		listeners = new HashSet<>();
		cardAreas = new ArrayList<>();
		cardDimension = CardImageCache.getCardDimension();
		overlapp = DEFAULT_OVERLAPP;
		initCardListener(this);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.white);
		g.fillRect(0, 0, getBounds().width, getBounds().height);
		paint2((Graphics2D) g);
	}

	private void paint2(Graphics2D g) {
		if (cards.size() > 0) {

			final AffineTransform saveAT = g.getTransform();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			///final int x = getLocation().x, y = getLocation().y;
			final int x = 0, y = 0;

			if (getStyle() == Style.ROW) {

				final int step = (int) Math.round((1d - this.getOverlapp()) * cardDimension.width);
				final Dimension d = new Dimension((step - 1) * cards.size() + cardDimension.width, cardDimension.height + EXPOSE_OFFSET);
				setSize(Math.max(d.width,d.height), Math.max(d.width,d.height));
				if (this.rotationInRadians != 0d) {
					g.rotate(this.rotationInRadians,0,0);
					g.translate(0, -d.height / 1);
				  //g.translate(0, d.height/2);
				}

				int imgx = x;//- (step * cards.size()) / 2 + (offset != null ? offset.x : 0);

				cardAreas.clear();
				for (int n = 0; n < cards.size(); n++) {
					final CardInfo ci = cards.get(n);
					final Karte card = ci.getKarte();
					final Image img;
					if (hidden) {
						img = CardImageCache.getCoverImage();
					} else {
						img = CardImageCache.getImage(card);
					}
					final int selectOffset = (isExposeSelectedCard() && (card == this.selectedCard)) ? 0 : EXPOSE_OFFSET;
					//final int imgy = y - cardDimension.height - selectOffset + (offset != null ? offset.y : 0);
					final int imgy = y + selectOffset + (offset != null ? offset.y : 0);
					g.drawImage(img, imgx, imgy, null);

					// Track area for mouse over events:
					final Rectangle r = new Rectangle(new Point(imgx, imgy), cardDimension);
					cardAreas.add(0, new AbstractMap.SimpleImmutableEntry<>(r, card));
					//log.info("selected card: "+selectedCard);
					imgx += step;
				}
				g.setTransform(saveAT);
				g.setColor(Color.blue);
				g.fillOval(d.width / 2, d.height / 2,33,33);
				g.setColor(Color.red);
			} // ROW

			else if (getStyle() == Style.STACK) {

				int imgx = x - cardDimension.width / 2;
				int imgy = y - cardDimension.height / 2;
				for (int n = 0; n < cards.size(); n++) {
					final CardInfo ci = cards.get(n);
					final Karte card = ci.getKarte();
					final Image img;
					if (hidden) {
						img = CardImageCache.getCoverImage();
					} else {
						img = CardImageCache.getImage(card);
					}
					g.drawImage(img, imgx, imgy, null);
				}
			} // STACK

			else if (getStyle() == Style.CROSS) {

				int imgx = x - cardDimension.width / 2;
				int imgy = y - cardDimension.height / 2;
				for (int n = 0; n < cards.size(); n++) {
					final CardInfo ci = cards.get(n);
					final Karte card = ci.getKarte();
					final Image img;
					if (hidden) {
						img = CardImageCache.getCoverImage();
					} else {
						img = CardImageCache.getImage(card);
					}
					final int p = ci.getPlayerPosition();
					g.rotate(crossRotation, x, y);
					g.drawImage(img, imgx + CROSS_OFFSET, imgy + CROSS_OFFSET, null);
				}
			} // CROSS

			// !! Reset transformation !!
			g.setTransform(saveAT);
		}

	}

	private Karte getSelectedCard(Point point) {
		for (final Entry<Rectangle, Karte> e : cardAreas) {
			Rectangle r = e.getKey();
			if (r.contains(point)) {
				return e.getValue();
			}
		}
		return null;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public void clearCards() {
		this.cards.clear();
		this.crossRotation = Math.toRadians(50); // + (5 - Math.random() * 10));
	}

	public void addCard(CardInfo k) {
		this.cards.add(k);
	}

	public void addCards(Collection<CardInfo> k) {
		this.cards.addAll(k);
	}

	public Style getStyle() {
		return style;
	}

	public void setStyle(Style style) {
		this.style = style;
	}

	public boolean isExposeSelectedCard() {
		return exposeSelectedCard;
	}

	public void setExposeSelectedCard(boolean exposeSelectedCard) {
		this.exposeSelectedCard = exposeSelectedCard;
	}

	public Point getOffset() {
		return offset;
	}

	public void setOffset(Point offset) {
		this.offset = offset;
	}

	public double getOverlapp() {
		return overlapp;
	}

	public void setOverlapp(double overlapp) {
		this.overlapp = overlapp;
	}

	public void setRotation(double rotationInRadians) {
		this.rotationInRadians = rotationInRadians;
	}

	// -- Listener & Event stuff --------------------------------------------------------------------
	public void addCardListener(final CardEvent.CardListener listener) {
		listeners.add(listener);
	}

	private void initCardListener(final Component owner) {
		owner.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if (JCardArea.this.rotationInRadians == 0d) {
					Karte nowSelectedCard = getSelectedCard(e.getPoint());
					if (nowSelectedCard != selectedCard) {
						selectedCard = nowSelectedCard;
						sendEvent(new CardEvent(selectedCard, ChangeType.CARD_SELECTED));
					}
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {}
		});

		owner.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (JCardArea.this.rotationInRadians == 0d) {
					Karte nowSelectedCard = getSelectedCard(e.getPoint());
					selectedCard = null;
					if (nowSelectedCard != null) {
						sendEvent(new CardEvent(nowSelectedCard, ChangeType.CARD_CLICKED));
					}
				}
			}
		});
	}

	private void sendEvent(final CardEvent cardEvent) {
		for (final CardListener l : listeners) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					l.cardChanged(cardEvent);
				}
			});
		}
		if (isExposeSelectedCard()) {
			repaint();
		}
	}

}
