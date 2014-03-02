package com.omic.kj.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Future;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import com.omic.kj.shared.domain.CardInfo;
import com.omic.kj.shared.domain.Farbe;
import com.omic.kj.shared.domain.GameInfo;
import com.omic.kj.shared.domain.Karte;
import com.omic.kj.shared.domain.PlayerInfo;
import com.omic.kj.shared.domain.ResponseCode;

public class JGamePanel extends JPanel {

	private final JStatusPanel statusPanel;
	private final JDeskPanel cardDesk;
	private final JButton b1, b2, b3;
	private final Image backgroundImage;

	JGamePanel() {
		super();
		this.cardDesk = new JDeskPanel(4);
		this.statusPanel = new JStatusPanel();

		this.backgroundImage = getImage("/images/table.jpg");

		final JPanel playerPanel = new JPanel(new BorderLayout());
		{
			b1 = new JButton("Belle");
			b3 = new JButton("Terz");
			b2 = new JButton("50");
			JPanel btnPanel = new JPanel();
			btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			btnPanel.add(Theme.style4(b1));
			btnPanel.add(Theme.style4(b3));
			btnPanel.add(Theme.style4(b2));
			btnPanel.setOpaque(false);
			playerPanel.add(btnPanel);
			playerPanel.add(statusPanel, BorderLayout.SOUTH);
			playerPanel.setOpaque(false);
		}

		setLayout(new BorderLayout());
		add(cardDesk, BorderLayout.CENTER);
		add(playerPanel, BorderLayout.SOUTH);
		setOpaque(false);
	}

	public void setStatus(PlayerInfo pi) {
		statusPanel.setText(0, "Geber: " + pi.getGeber());
		statusPanel.setText(1, "Aufspieler: " + pi.getAufspieler());
		statusPanel.setText(2, pi.getTrumpf() != null ? getTrumpfChar(pi.getTrumpf()) + " " + pi.getTrumpf().name() + " ist Trumpf" : "");
		statusPanel.setText(3, pi.getRunde() > 0 ? pi.getRunde() + ". Runde" : "");
		//cardDesk.setActivePosition (pi);
	}

	private String getTrumpfChar(Farbe t) {
		switch (t) {
		case Karo:
			return "\u2666";
		case Herz:
			return "\u2665";
		case Pik:
			return "\u2660";
		case Kreuz:
			return "\u2663";
		}
		return "";
	}

	public void setCards(int deskPlaceId, List<CardInfo> cards) {
		cardDesk.setCards(deskPlaceId, cards);
	}

	public void setOriginalPosition(int position) {
		cardDesk.setOriginalPosition(position);
	}

	public Future<Karte> askForCard() {
		return cardDesk.askForCard();
	}

	public void showGameInfo(GameInfo gameInfo) {
		cardDesk.showGameInfo(gameInfo);
	}

	public ResponseCode askUser(String message, ResponseCode[] allowedResponse) {
		int n = JOptionPane.showOptionDialog(this, message, "Frage", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, allowedResponse, null);
		if (n < 0)
			return null;
		ResponseCode r = allowedResponse[n];
		return r;
	}

	public Farbe askUserFarbe(String message, Farbe ersteFarbe) {
		final Farbe[] farben;
		if (ersteFarbe == null)
			farben = Farbe.values();
		else {
			// Farbe der ersten Karte / ersten Frage nicht mehr anzeigen.
			farben = new Farbe[Farbe.values().length - 1];
			int n = 0;
			for (Farbe f : Farbe.values()) {
				if (f != ersteFarbe) {
					farben[n++] = f;
				}
			}
		}
		int n = JOptionPane.showOptionDialog(this, message, "Frage", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, farben, null);
		if (n < 0)
			return null;
		return farben[n];
	}

	public void showBubble(int playerPosition, String message) {
		cardDesk.showBubble(playerPosition, message);
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(backgroundImage, 0, 0, null);
		super.paint(g);
	}

	private Image getImage(String filename) {
		try {
			InputStream is = this.getClass().getResourceAsStream(filename);
			BufferedImage img = ImageIO.read(is);
			is.close();
			return img;
		} catch (IOException e) {
			//log.log(Level.WARNING, "Playerimage error", e);
		}
		return null;
	}

}
