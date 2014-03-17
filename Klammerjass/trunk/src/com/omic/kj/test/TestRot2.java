package com.omic.kj.test;

import java.awt.BorderLayout;
import java.io.IOException;
import javax.swing.JFrame;
import com.omic.kj.shared.domain.CardInfo;
import com.omic.kj.shared.domain.Karte;
import com.omic.kj.ui.component.JCardPanel;
import com.omic.kj.ui.component.JCardPanel.Style;

class TestRot2 extends JFrame {

	public static void main(String s[]) throws IOException {
		TestRot2 f = new TestRot2();
		f.setVisible(true);
	}

	TestRot2() {
		setTitle("Testframe");

		JCardPanel pSouth = new JCardPanel();
		pSouth.setRotationQuadrant(0);
		pSouth.setStyle(Style.ROW);
		pSouth.setExposeSelectedCard(true);
		pSouth.addCard(new CardInfo(Karte.Pik8));
		pSouth.addCard(new CardInfo(Karte.Pik9));
		pSouth.addCard(new CardInfo(Karte.PikA));
		
		JCardPanel pWest = new JCardPanel();
		pWest.setRotationQuadrant(1);
		pWest.setStyle(Style.ROW);
		pWest.setHidden(true);
		pWest.addCard(new CardInfo(Karte.Herz8));
		pWest.addCard(new CardInfo(Karte.Herz9));
		pWest.addCard(new CardInfo(Karte.Herz10));

		JCardPanel pNorth = new JCardPanel();
		pNorth.setRotationQuadrant(2);
		pNorth.setHidden(true);
		pNorth.setStyle(Style.ROW);
		pNorth.addCard(new CardInfo(Karte.Pik8));
		pNorth.addCard(new CardInfo(Karte.Pik9));
		pNorth.addCard(new CardInfo(Karte.PikA));
		
		JCardPanel pEast = new JCardPanel();
		pEast.setRotationQuadrant(3);
		pEast.setStyle(Style.ROW);
		pEast.setExposeSelectedCard(true);
		pEast.addCard(new CardInfo(Karte.Pik8));
		pEast.addCard(new CardInfo(Karte.Pik9));
		pEast.addCard(new CardInfo(Karte.PikA));
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(pSouth,BorderLayout.SOUTH);
		getContentPane().add(pNorth,BorderLayout.NORTH);
		getContentPane().add(pEast,BorderLayout.EAST);
		getContentPane().add(pWest,BorderLayout.WEST);

		setSize(900, 900);
		setVisible(true);
	}

}
