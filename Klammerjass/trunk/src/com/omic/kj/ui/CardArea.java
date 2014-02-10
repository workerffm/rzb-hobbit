package com.omic.kj.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import com.omic.kj.shared.domain.Karte;

public class CardArea {

	private final Logger log = Logger.getLogger("UI");
	
	public enum Style {
	  DECK, BID, HAND
	};

	private Style style;
	private boolean hidden;
	private Point location;
	private double overlapp;
	private final int BORDER = 10;
	private final List<Karte> cards;
	private final Dimension cardDimension;

	public CardArea() {
		cards = new ArrayList<>();
		cardDimension = CardImageCache.getCardDimension();
		overlapp = .4d; // x% overlapp to next card
	}

	public void paint(Graphics2D g) {
		if (getLocation() != null && cards.size() > 0) {

			int x = getLocation().x, y = getLocation().y;
			//int y = getLocation().x, x = getLocation().y;

//			if (getStyle() == Style.HORIZONTAL) {
//
//				//				int x = getLocation().x, y = getLocation().y;
//				//				final int step = (int) Math.round(Math.min(1,1-this.overlapp) * cardDimension.width);
//				//				x -= (step * cards.size()) / 2;
//				//				
//				//				for (int n = 0; n < cards.size(); n++) {
//				//					final Image img;
//				//					if (hidden) {
//				//						img = CardImageCache.getCoverImage();
//				//					} else {
//				//						img = CardImageCache.getImage(cards.get(n));
//				//					}
//				//					g.drawImage(img, x, y-cardDimension.height - BORDER, null);
//				//					x += step;
//				//				}
//				final Graphics2D g2 = (Graphics2D) g;
//				g2.rotate(0, 0, 0);
//			} // horizontal
//
//			else if (getStyle() == Style.VERTICAL) {
//
//				//int x = getLocation().x, y = getLocation().y;
//				final Graphics2D g2 = (Graphics2D) g;
//				g2.rotate(-Math.toRadians(90), x, y);
//			}

		 if(getStyle()== Style.HAND) {
			
				final int step = (int) Math.round((1d - this.overlapp) * cardDimension.width);
				x -= (step * cards.size()) / 2;
	
				for (int n = 0; n < cards.size(); n++) {
					final Image img;
					if (hidden) {
						img = CardImageCache.getCoverImage();
					} else {
						img = CardImageCache.getImage(cards.get(n));
					}
					g.drawImage(img,  x, y - cardDimension.height - BORDER,null);
					if(cards.get(n)==Karte.Pik10) {
						log.info("x="+x+", y="+ (y - cardDimension.height - BORDER));
					}
					x += step;
				}
		 }
		}
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

	public void addCard(Karte k) {
		this.cards.add(k);
	}

	public void addCards(Collection<Karte> k) {
		this.cards.addAll(k);
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public double getOverlapp() {
		return overlapp;
	}

	public void setOverlapp(double overlapp) {
		this.overlapp = overlapp;
	}

	public Style getStyle() {
		return style;
	}

	public void setStyle(Style style) {
		this.style = style;
	}

}
