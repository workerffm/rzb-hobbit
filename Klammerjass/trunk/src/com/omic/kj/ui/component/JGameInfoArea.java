package com.omic.kj.ui.component;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import com.omic.kj.shared.domain.GameHistoryInfo;
import com.omic.kj.shared.domain.GameInfo;

/**
 * Show game history, the last 3 entries
 */
public class JGameInfoArea extends JComponent {

	final static Font smallFont = new Font("Bauhaus 93", Font.PLAIN, 16);
	final static Font largeFont = new Font("Bauhaus 93", Font.PLAIN, 24);
	final static Color bgColor = new Color(0x003399);
			
	private final List<GameHistoryInfo> gameHistory;
	private String activeGame;
	private GameInfo gameInfo;

	public JGameInfoArea() {
		setOpaque(false);
		this.gameHistory = new ArrayList<>();
		setPreferredSize(new Dimension(270, 160));
		setMinimumSize(new Dimension(270, 160));
		//add(new GameHistory (1,123,"Sample1"));
	}

	public void add(List<GameHistoryInfo> gameHistoryInfos) {
		gameHistory.clear();
		gameHistory.addAll(gameHistoryInfos);
		repaint();
	}
	

	public void add(GameHistoryInfo gameHistoryInfo) {
		gameHistory.add(gameHistoryInfo);
		repaint();
	}

	
	public void setActiveGameInfo (String activeGame, GameInfo gameInfo) {
		this.activeGame = activeGame;
		this.gameInfo = gameInfo;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		paint2((Graphics2D) g);
	}

	public void paint2(Graphics2D g) {
		final AffineTransform saveAT = g.getTransform();
		int x, y, w, h;
		x = 0;
		y = getHeight()-20;
		w = getWidth() - 8;
		h = 30;

//		g.setColor(new Color(0xffdd00));
//		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
//		g.fillRect(x, y-getHeight()+20, getWidth(), getHeight());

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		y -= 2;
		// Frame
		g.setColor(bgColor);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
		g.fillRoundRect(x+2, y, w, 20, 2, 2);
		// Text
		g.setColor(Color.white);
		g.setFont(smallFont);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
		g.drawString(activeGame+" - bis "+gameInfo.getMaxRunden()+" Runden", x + 4, y + 16);
		y -= 28;
		
		int showMax = 3;
		for (int i = this.gameHistory.size()-1; i >= 0 && showMax >= 1; i--,showMax--) {
			final GameHistoryInfo hist = this.gameHistory.get(i);
			y -= 4;
			// Frame
			g.setColor(bgColor);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
			g.fillRoundRect(x+2, y, w, h, 2, 2);
			// Text
			g.setColor(Color.white);
			g.setFont(largeFont);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
			g.drawString("Spiel " + hist.getSpielNr() + " - "+hist.getWinner()+" - "+hist.getPoints(), x + 4, y + 22);
			y -= 28;
		}
    g.setTransform(saveAT);
	}

}
