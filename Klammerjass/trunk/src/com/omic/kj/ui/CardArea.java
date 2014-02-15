package com.omic.kj.ui;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import com.omic.kj.shared.domain.CardInfo;
import com.omic.kj.shared.domain.Karte;
import com.omic.kj.ui.CardEvent.CardListener;
import com.omic.kj.ui.CardEvent.ChangeType;

public final class CardArea  {

	private final Logger log = Logger.getLogger("UI");

	public enum Style {
		/** Alle Karten übereinander */
		STACK, 
		/** Alle Karten etwas versetzt im Kreis, a  lá Stich */
		CROSS, 
		/** Alle Karten in einer Reihe */
		ROW
	};

	private final int BORDER = 10;
	private final int EXPOSE_OFFSET = 21;
	private final double overlapp = .4d; // x% overlapp to next card

	private boolean hidden;
	private boolean exposeSelectedCard;
	private Style style;
	private Karte selectedCard;
	private Point location;

	private final JComponent owner;
	private final List<CardInfo> cards;
	private final Dimension cardDimension;
	private final Map<Rectangle, Karte> cardAreas;
	private final Set<CardListener> listeners;

	CardArea(final JComponent owner) {
		super();
		this.owner = owner;
		cards = new ArrayList<>();
		listeners = new HashSet<>();
		cardAreas = new HashMap<>();
		cardDimension = CardImageCache.getCardDimension();
	}

	

	public void paint(Graphics2D g) {
		if (getLocation() != null && cards.size() > 0) {

			final int x = getLocation().x, y = getLocation().y;

			if (getStyle() == Style.ROW) {

				final int step = (int) Math.round((1d - this.overlapp) * cardDimension.width);
				int imgx = x - (step * cards.size()) / 2;

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
					final int selectOffset = (isExposeSelectedCard() && (card == this.selectedCard)) ? EXPOSE_OFFSET : 0;
					final int imgy = y - cardDimension.height - BORDER - selectOffset;
					g.drawImage(img, imgx, imgy, null);

					// Track area for mouse over events:
					final Rectangle r = new Rectangle(new Point(imgx, imgy), cardDimension);
					cardAreas.put(r, card);
					//log.info("selected card: "+selectedCard);
					imgx += step;
				}
			}   // ROW
			
			else if (getStyle() == Style.STACK) {
				
				int imgx =  x - cardDimension.width /2 ;
				int imgy =  y - cardDimension.height / 2;
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
			}  // STACK
			

			else if (getStyle() == Style.CROSS) {
				
				final AffineTransform saveAT = g.getTransform();
				int imgx =  x - cardDimension.width /2 ;
				int imgy =  y - cardDimension.height / 2;
				for (int n = 0; n < cards.size(); n++) {
					final CardInfo ci = cards.get(n);
					final Karte card = ci.getKarte();
					final Image img;
					if (hidden) {
						img = CardImageCache.getCoverImage();
					} else {
						img = CardImageCache.getImage(card);
					}
					g.rotate(Math.toRadians(5), x,y);
					g.drawImage(img, imgx, imgy, null);
				}
			  // !! Reset transformation !!
				g.setTransform(saveAT);
			}  // CROSS
    }

	}

	private Karte getSelectedCard(Point point) {
		for (Rectangle r : cardAreas.keySet()) {
			if (r.contains(point)) {
				return cardAreas.get(r);
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
	}

	public void addCard(CardInfo k) {
		this.cards.add(k);
	}

	public void addCards(Collection<CardInfo> k) {
		this.cards.addAll(k);
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
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



	// -- Listener & Event stuff --------------------------------------------------------------------
	
	public void addCardListener(final CardEvent.CardListener listener) {
		owner.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Karte nowSelectedCard = getSelectedCard(e.getPoint());
				if (nowSelectedCard != selectedCard) {
					selectedCard = nowSelectedCard;
					sendEvent(new CardEvent(selectedCard, ChangeType.CARD_SELECTED));
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}
		});
		
		owner.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				Karte nowSelectedCard = getSelectedCard(e.getPoint());
				selectedCard = null;
				sendEvent(new CardEvent(nowSelectedCard, ChangeType.CARD_CLICKED));
			}
		});
		listeners.add(listener);
	}

	protected void sendEvent(final CardEvent cardEvent) {
    for (final CardListener l:listeners) {
    	SwingUtilities.invokeLater(new Runnable(){
				@Override
				public void run() {
					l.cardChanged(cardEvent);
				}});
    }
	}

}
