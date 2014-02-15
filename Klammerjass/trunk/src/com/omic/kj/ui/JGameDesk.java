package com.omic.kj.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import com.omic.kj.shared.domain.CardInfo;
import com.omic.kj.shared.domain.Farbe;
import com.omic.kj.shared.domain.Karte;

public class JGameDesk extends JPanel {

	private final JStatusPanel statusPanel;
	private final JCardDesk cardDesk;

	JGameDesk() {
		super(new BorderLayout());
		statusPanel = new JStatusPanel();
		JButton b1 = new JButton("Bella"), b2 = new JButton("50"), b3 = new JButton("Terz");
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
		statusPanel.setTrumpf(trumpf!=null?trumpf.name():"");
		statusPanel.setPunkte(punkte+"");
		statusPanel.setRunde(runde+"");
	}

	public void setCards(int deskPlaceId, List<CardInfo> cards) {
		cardDesk.setCards (deskPlaceId, cards);
	}

	public void setOriginalPosition(int originalPosition) {
		cardDesk.setOriginalPosition(originalPosition);
	}
}
