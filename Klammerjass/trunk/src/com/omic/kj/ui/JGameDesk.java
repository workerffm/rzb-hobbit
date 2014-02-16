package com.omic.kj.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import java.util.concurrent.Future;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import com.omic.kj.shared.domain.CardInfo;
import com.omic.kj.shared.domain.Farbe;
import com.omic.kj.shared.domain.Karte;
import com.omic.kj.shared.domain.ResponseCode;

public class JGameDesk extends JPanel {

	private final JStatusPanel statusPanel;
	private final JCardDesk cardDesk;
	private final JButton b1, b2, b3;

	JGameDesk() {
		super(new BorderLayout());
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
		cardDesk = new JCardDesk(4);
		add(cardDesk);
		add(playerPanel, BorderLayout.SOUTH);
	}

	public void setStatus(String aufspieler, Farbe trumpf, int punkte, int runde) {
		statusPanel.setAufspieler(aufspieler);
		statusPanel.setTrumpf(trumpf != null ? trumpf.name() : "");
		statusPanel.setPunkte(punkte + "");
		statusPanel.setRunde(runde + "");
	}

	public void setCards(int deskPlaceId, List<CardInfo> cards) {
		cardDesk.setCards(deskPlaceId, cards);
	}

	public void setOriginalPosition(int position) {
		cardDesk.setOriginalPosition(position);
	}

	public void setUserInfo(int position, String playerName) {
		cardDesk.setUserInfo(position, playerName);
		b1.setEnabled(position==1);
	  b2.setEnabled(position==1);
	  b3.setEnabled(position==1);
	}

	public ResponseCode askUser(String message, ResponseCode[] allowedResponse) {
    ResponseCode r;
    int n = JOptionPane.showOptionDialog(this, message, "Frage", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, allowedResponse, null); 
    r = allowedResponse[n];
		return r;
	}

	public Future<Karte> askForCard() {
    return cardDesk.askForCard();		
	}

}
