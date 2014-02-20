package com.omic.kj.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.List;
import java.util.concurrent.Future;
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

	JGamePanel() {
		super(new BorderLayout());
		
		setBackground(Color.black);
		statusPanel = new JStatusPanel();
		b1 = new JButton("Bella");
		b2 = new JButton("50");
		b3 = new JButton("Terz");
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		btnPanel.add(b1);
		btnPanel.add(b2);
		btnPanel.add(b3);

		//btnPanel.setOpaque(false);
		JPanel playerPanel = new JPanel(new BorderLayout());
		playerPanel.add(btnPanel);
		playerPanel.add(statusPanel, BorderLayout.SOUTH);
		//JPanel p = new JPanel(new BorderLayout());
		cardDesk = new JDeskPanel(4);
		add(cardDesk);
		add(playerPanel, BorderLayout.SOUTH);
	}

	public void setStatus(PlayerInfo pi) {
		statusPanel.setText(0,"Geber: "+pi.getGeber());
		statusPanel.setText(1,"Aufspieler: "+pi.getAufspieler());
		statusPanel.setText(2,pi.getTrumpf()!= null ? getTrumpfChar(pi.getTrumpf())+" "+pi.getTrumpf().name()+" ist Trumpf" : "");
		statusPanel.setText(3,pi.getRunde()>0 ? pi.getRunde() + ". Runde":"");
		//cardDesk.setActivePosition (pi);
	}

	private String getTrumpfChar(Farbe t) {
		switch (t) {
		case Karo: return "\u2666";		
		case Herz: return "\u2665";		
		case Pik: return "\u2660";		
		case Kreuz: return "\u2663";		
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

	public void setPlayerInfo(GameInfo gameInfo) {
		cardDesk.setPlayerInfo (gameInfo.getPlayerInfo());
		cardDesk.setActivePosition (gameInfo.getActivePlayerPosition());
	}

	public ResponseCode askUser(String message, ResponseCode[] allowedResponse) {
    int n = JOptionPane.showOptionDialog(this, message, "Frage", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, allowedResponse, null); 
    if (n<0) return null;
    ResponseCode r = allowedResponse[n];
		return r;
	}

	public void showBubble(int playerPosition, String message) {
		cardDesk.showBubble(playerPosition, message);
	}


}
