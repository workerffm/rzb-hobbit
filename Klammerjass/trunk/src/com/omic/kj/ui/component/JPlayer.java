package com.omic.kj.ui.component;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import javax.swing.JComponent;

/**
 * Show player with image, bubble, points...
 */
public class JPlayer extends JComponent {

	private final int BORDER = 10;
	private final static Color bubbleColor = new Color(0xF3F5B5);
	private final static Color activeColor = Color.yellow; //new Color(0x03572E);
	private final static Font smallFont = new Font("Bauhaus 93", Font.PLAIN, 13);
	private final static Font largeFont = new Font("Bauhaus 93", Font.PLAIN, 20);
	private final static Composite transparent80 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f);

	private final BlinkThread blinkThread;
	private boolean active, blinking;
	private String name;
	private Image image;
	private int position;
	private String bubbleMessage;

	public JPlayer(int position) {
		super();
		this.position = position;
		setPreferredSize(new Dimension(100, 100));
		blinking = true;
		blinkThread = new BlinkThread();
		blinkThread.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		paint2((Graphics2D) g);
	}

	public void paint2(Graphics2D g) {
		if (getLocation() != null) {
			final AffineTransform saveAT = g.getTransform();
			//final int x = getLocation().x, y = getLocation().y;
			final int x = 0, y = 0;

			if (image != null) {
				int imgx = x, imgy = y;
				if (getPosition() == 1) {
					imgx += 10;
					imgy += 10;
				}
				if (getPosition() == 2) {
					imgx += 0;
					imgy -= 0;
				}
				if (getPosition() == 3) {
					imgx += 10;
					imgy += 10;
				}
				if (getPosition() == 4) {
					imgx += 20;
					imgy += 0;
				}
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
				g.drawImage(image, imgx, imgy, null);
			}
			if (this.active) {
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
				g.setColor(this.active && blinking ? activeColor : Color.black);
				g.fillRect(x, y + getSize().height - 20, getSize().width, 20);
			}
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			g.setColor(this.active && blinking ? Color.black : Color.white);
			g.setFont(smallFont);
			g.drawString(this.name != null ? this.name : "", x + 5, y + getSize().height - 5);

			if (this.bubbleMessage != null) {
				g.setColor(bubbleColor);
				Composite save = g.getComposite();
				g.setComposite(transparent80);
				g.fillRoundRect(x, y - 15, 80, 40, 15, 15);
				int a = x - 20;
				int b = y + 40 - 15;
				g.fillPolygon(new int[] { a + 40, a + 55, a + 57, a + 60, a + 63, a + 60, a + 49, a + 45 }, new int[] { b + 0, b + 0, b + 4, b + 8, b + 9, b + 10, b + 8, b + 4 }, 8);

				g.setColor(Color.black);
				g.setFont(largeFont);
				g.drawString(this.bubbleMessage, x + 5, y + 12);
				g.setComposite(save);
			}
			g.setTransform(saveAT);
		}
	}

	public void setActive(boolean active) {
		this.active = active;
		repaint();
	}

	public void setName(String name) {
		this.name = name;
		repaint();
	}

	public boolean isActive() {
		return this.active;
	}

	public void setImage(Image image) {
		this.image = image;
		repaint();
	}

	public void setBubbleMessage(final String bubbleMessage) {
		this.bubbleMessage = bubbleMessage;
		repaint();
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
		repaint();
	}

	private final class BlinkThread extends Thread {
		public void run() {
			try {
				for (;;) {
					if (!JPlayer.this.active) {
						synchronized (this) {
							wait();
						}
					}
					if (JPlayer.this.active) {
						JPlayer.this.blinking = !JPlayer.this.blinking;
						synchronized (this) {
							wait(800);
						}
						JPlayer.this.repaint();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
