package com.omic.kj.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JComponent;
import com.omic.kj.shared.domain.Karte;
import com.omic.kj.ui.CardArea.Style;
import com.omic.kj.ui.CardEvent.CardListener;

public class JCardDesk extends JComponent implements CardListener {

	private final Logger log = Logger.getLogger("UI");

	private CardArea p1, p2, p3, p4;

	JCardDesk(int numberOfPlayer) {
		p1 = new CardArea(this);
		p1.setStyle(Style.HAND);
		p1.setHidden(false);
		p1.addCardListener(this);
		p1.setExposeSelectedCard(true);

		p2 = new CardArea(this);
		p2.setStyle(Style.HAND);
		p2.setHidden(!true);

		p3 = new CardArea(this);
		p3.setStyle(Style.HAND);
		p3.setHidden(!true);

		p4 = new CardArea(this);
		p4.setStyle(Style.HAND);
		p4.setHidden(!true);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		final Dimension d = getSize();
		final Graphics2D g2 = (Graphics2D) g;
		g2.rotate(0, 0, 0);
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
	}

	@Override
	public void cardChanged(final CardEvent event) {
		if(event.getEvent()==CardEvent.ChangeType.CARD_CLICKED)
		  log.info(event.toString());
		repaint();
	}

	public void setCards(int position, List<Karte> cards) {
		if (position == 1) {
			p1.clearCards();
		  p1.addCards(cards);
		}
		else if (position == 2) {
			p2.clearCards();
		  p2.addCards(cards);
		}
		else if (position == 3) {
			p3.clearCards();
		  p3.addCards(cards);
		}
		else if (position == 4) {
			p4.clearCards();
		  p4.addCards(cards);
		}
		repaint();
	}

}
