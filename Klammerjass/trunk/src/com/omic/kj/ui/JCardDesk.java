package com.omic.kj.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JComponent;
import com.omic.kj.shared.domain.CardInfo;
import com.omic.kj.ui.CardArea.Style;
import com.omic.kj.ui.CardEvent.CardListener;

public class JCardDesk extends JComponent implements CardListener {

	private final Logger log = Logger.getLogger("UI");

	private final CardArea p1, p2, p3, p4, p5, p6, p7;

	private int originalPosition;

	JCardDesk(int numberOfPlayer) {
		p1 = new CardArea(this);
		p1.setStyle(Style.ROW);
		p1.setHidden(false);
		p1.addCardListener(this);
		p1.setExposeSelectedCard(true);

		p2 = new CardArea(this);
		p2.setStyle(Style.ROW);
		p2.setHidden(true);

		p3 = new CardArea(this);
		p3.setStyle(Style.ROW);
		p3.setHidden(true);

		p4 = new CardArea(this);
		p4.setStyle(Style.ROW);
		p4.setHidden(true);

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
	}

	@Override
	public void cardChanged(final CardEvent event) {
		if (event.getEvent() == CardEvent.ChangeType.CARD_CLICKED)
			log.info(event.toString());
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		final Dimension d = getSize();
		final Graphics2D g2 = (Graphics2D) g;
		final AffineTransform saveAT = g2.getTransform();
		g2.rotate(0, 0, 0);
		//g2.scale(0.8, 0.8);
		if (p1 != null) {
			p1.setLocation(new Point(d.width / 2, d.height));
			p1.paint(g2);
		}
		g2.rotate(Math.toRadians(90), d.width / 2, d.height / 2);
		if (p2 != null) {
			p2.setLocation(new Point(d.width / 2, d.height));
			p2.paint(g2);
		}
		g2.rotate(Math.toRadians(90), d.width / 2, d.height / 2);
		if (p3 != null) {
			p3.setLocation(new Point(d.width / 2, d.height));
			p3.paint(g2);
		}
		g2.rotate(Math.toRadians(90), d.width / 2, d.height / 2);
		if (p4 != null) {
			p4.setLocation(new Point(d.width / 2, d.height));
			p4.paint(g2);
		}
		// !! Reset transformation !!
		g2.setTransform(saveAT);
		if (p5 != null) {
			p5.setLocation(new Point(d.width / 2, d.height / 2));
			p5.paint(g2);
		}
		if (p6 != null) {
			p6.setLocation(new Point(10, d.height -150));
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
	}

	public void setCards(int deskPlaceId, List<CardInfo> cards) {
		if (deskPlaceId == 1) {
			p1.clearCards();
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

}
