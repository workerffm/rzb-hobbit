package com.omic.kj.ui.component;

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
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import com.omic.kj.shared.domain.CardInfo;
import com.omic.kj.shared.domain.Karte;
import com.omic.kj.ui.CardImageCache;
import com.omic.kj.ui.component.CardEvent.CardListener;
import com.omic.kj.ui.component.CardEvent.ChangeType;

public class JCardPanel extends JPanel {

	public enum Style {
		/** Alle Karten übereinander */
		STACK,
		/** Alle Karten etwas versetzt im Kreis, a  lá Stich */
		CROSS,
		/** Alle Karten in einer Reihe */
		ROW
	};

	private final int EXPOSE_OFFSET = 21;
	private final int CROSS_OFFSET = 45;
	private final double DEFAULT_OVERLAPP = .45d; // x% overlapp to next card

	private boolean hidden;
	private boolean exposeSelectedCard;
	private Style style;
	private Karte selectedCard;
	private Point offset;
	private int rotationQuadrant;

	private final List<CardInfo> cards;
	private final Dimension cardDimension;
	private final List<Entry<Rectangle, Karte>> cardAreas;
	private final Set<CardListener> listeners;
	private double crossRotation;
	private double overlapp;

	public JCardPanel() {
		super();
		setOpaque(false);
		hidden = false;
		exposeSelectedCard = false;
		cards = new ArrayList<>();
		listeners = new HashSet<>();
		cardAreas = new ArrayList<>();
		cardDimension = CardImageCache.getCardDimension();
		overlapp = DEFAULT_OVERLAPP;
		initCardListener(this);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		paint2((Graphics2D) g);
	}

	private void paint2(Graphics2D g) {
		if (cards.size() > 0) {

			final AffineTransform saveAT = g.getTransform();
			final int x = getLocation().x, y = getLocation().y;

			if (getStyle() == Style.ROW) {

				cardAreas.clear();

				int imgx = 0;
				int imgy = 0;
				final int stepx, stepy;
				final Dimension d;

				if (rotationQuadrant == 0) {
					stepx = (int) Math.round((1d - this.getOverlapp()) * cardDimension.width);
					stepy = 0;
					d = new Dimension(stepx * (cards.size() - 1) + cardDimension.width, cardDimension.height + EXPOSE_OFFSET);
				} else if (rotationQuadrant == 1) {
					stepx = 0;
					stepy = (int) Math.round((1d - this.getOverlapp()) * cardDimension.width);
					d = new Dimension(cardDimension.height + EXPOSE_OFFSET, stepy * (cards.size() - 1) + cardDimension.width);
				} else if (rotationQuadrant == 2) {
					stepx = -(int) Math.round((this.getOverlapp()) * cardDimension.width);
					stepy = 0;
					d = new Dimension(-stepx * (cards.size() - 1) + cardDimension.width, cardDimension.height + EXPOSE_OFFSET);
					imgx = d.width - cardDimension.width;
				} else if (rotationQuadrant == 3) {
					stepx = 0;
					stepy = -(int) Math.round((1d - this.getOverlapp()) * cardDimension.width);
					d = new Dimension(cardDimension.height /*+ EXPOSE_OFFSET*/, -stepy * (cards.size() - 1) + cardDimension.width);
					imgy = d.height - cardDimension.width;
				} else {
					System.err.println("Invalid rotation");
					stepx = stepy = 0;
					d = null;
				}
				setPreferredSize(d);
				setBounds(x, y, x + d.width, y + d.height);
				//System.out.println("rot=" + getRotationQuadrant() + ", x=" + x + ", y=" + y + ", d.w=" + d.width + ", d.h=" + d.height + ", stepx=" + stepx + ", stepy=" + stepy);

				for (int n = 0; n < cards.size(); n++) {
					final CardInfo ci = cards.get(n);
					final Karte card = ci.getKarte();
					final BufferedImage img;
					if (hidden) {
						img = CardImageCache.getCoverImage();
					} else {
						img = CardImageCache.getImage(card);
					}
					final int selectOffset = rotationQuadrant == 0 && (isExposeSelectedCard() && (card == this.selectedCard)) ? 0 : EXPOSE_OFFSET;

					final AffineTransformOp opRotated;
					{
						final AffineTransform affineTransform = AffineTransform.getQuadrantRotateInstance(rotationQuadrant, cardDimension.width / 2, cardDimension.height / 2);
						int corr = 0;
						if (rotationQuadrant == 0) {
							corr = 0;
						} else if (rotationQuadrant == 1) {
							corr = Math.abs(cardDimension.height - cardDimension.width) / 2;
							affineTransform.translate(-corr, -corr);
						} else if (rotationQuadrant == 2) {
							corr = 0;
							affineTransform.translate(-corr, -corr);
						} else if (rotationQuadrant == 3) {
							corr = -Math.abs(cardDimension.height - cardDimension.width) / 2;
							affineTransform.translate(-corr - 1, -corr);
						}
						opRotated = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
					}
					BufferedImage newImage = opRotated.filter(img, null);
					g.drawImage(newImage, imgx, imgy + selectOffset, null);

					//System.out.println("corr=" + corr + ", rot=" + getRotationQuadrant() + ", x=" + x + ", y=" + y + ", d.w=" + d.width + ", d.h=" + d.height + ", stepx=" + stepx + ", stepy=" + stepy);

					// Save area for mouse over events:
					if (rotationQuadrant == 0) {
						final Rectangle r = new Rectangle(new Point(imgx, imgy), cardDimension);
						cardAreas.add(0, new AbstractMap.SimpleImmutableEntry<>(r, card));
					}
					// Prepare next card
					imgx += stepx;
					imgy += stepy;
				}
				g.setTransform(saveAT);
			} // ROW

			else if (getStyle() == Style.STACK) {
				//setBounds(x, y, x + d.width, y + d.height);
				Dimension d = new Dimension(200,200);
				setPreferredSize(d);
				//setBounds(x, y, x + d.width, y + d.height);
				
				int imgx =  cardDimension.width / 2;
				int imgy =  cardDimension.height / 2;
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

				setPreferredSize( new Dimension(400,400));
				int rot = 20;
				for (int n = 0; n < cards.size(); n++,rot+=75) {
					final CardInfo ci = cards.get(n);
					final Karte card = ci.getKarte();
					final BufferedImage img;
					BufferedImage img1;
					if (hidden) {
						img1 = CardImageCache.getCoverImage();
					} else {
						img1 = CardImageCache.getImage(card);
					}
				
					double crossRotation = Math.toRadians(rot);
					int h = img1.getHeight();
					int w = img1.getWidth();
					double d = Math.sqrt(h * h + w * w);
					//System.out.println("h=" + h + ",w=" + w + ",d=" + d);

					BufferedImage img2;
					{
						AffineTransform t2 = new AffineTransform();
						t2.translate(d - w, d - h);
						AffineTransformOp op = new AffineTransformOp(t2, AffineTransformOp.TYPE_BICUBIC);
						img2 = op.filter(img1, null);
					}

					BufferedImage img3;
					{
						AffineTransform t1 = new AffineTransform();
						t1.rotate(crossRotation, d,d);
						t1.translate(0,h/4);
						AffineTransformOp op = new AffineTransformOp(t1, AffineTransformOp.TYPE_BICUBIC);
						img3 = op.filter(img2, null);
					}

					g.drawImage(img3, (int)Math.round(getWidth()/2-d), (int)Math.round(getHeight()/2-d), null);
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
		this.crossRotation = Math.toRadians(70);
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
		if (overlapp < 0d || overlapp > 1d)
			throw new IllegalArgumentException("overlapp");
		this.overlapp = overlapp;
	}

	public int getRotationQuadrant() {
		return rotationQuadrant;
	}

	public void setRotationQuadrant(int rotationQuadrant) {
		if (rotationQuadrant < 0 || rotationQuadrant > 3)
			throw new IllegalArgumentException("rotationQuadrant");
		this.rotationQuadrant = rotationQuadrant;
	}

	// -- Listener & Event stuff --------------------------------------------------------------------
	public void addCardListener(final CardEvent.CardListener listener) {
		listeners.add(listener);
	}

	private void initCardListener(final Component owner) {
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
			public void mouseDragged(MouseEvent e) {}
		});

		owner.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {
				selectedCard = null;
				sendEvent(new CardEvent(selectedCard, ChangeType.CARD_SELECTED));
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (JCardPanel.this.getRotationQuadrant() == 0) {
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
