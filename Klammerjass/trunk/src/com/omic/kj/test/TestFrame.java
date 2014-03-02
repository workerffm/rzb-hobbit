package com.omic.kj.test;

import javax.swing.JFrame;
import com.omic.kj.shared.domain.CardInfo;
import com.omic.kj.shared.domain.GameHistoryInfo;
import com.omic.kj.shared.domain.Karte;
import com.omic.kj.ui.CardEvent;
import com.omic.kj.ui.CardEvent.CardListener;
import com.omic.kj.ui.JCardArea;
import com.omic.kj.ui.JCardArea.Style;
import com.omic.kj.ui.JGameInfoArea;

class TestFrame extends JFrame {

	 public static void main(String s[]) {
		 TestFrame f = new TestFrame();
		 f.start();
	 }
	
	TestFrame() {
		super();
	}

	public void start()  {
		setTitle("Testframe");
		
		JGameInfoArea a = new JGameInfoArea();
		a.add(new GameHistoryInfo(1,255,"Tom"));
		a.add(new GameHistoryInfo(2,13,"Markus"));
		a.setActiveGameInfo ("Spiel läuft", 600);
		a.setLocation(5, 5);
		
		
		JCardArea ca = new JCardArea();
		ca.setBounds(200,200,444,444);
		//ca.setLocation(200,200);
		ca.setExposeSelectedCard(true);
		ca.addCard(new CardInfo(Karte.Herz10).setPlayerPosition(1));
		ca.addCard(new CardInfo(Karte.Herz7).setPlayerPosition(1));
		ca.addCard(new CardInfo(Karte.Herz8).setPlayerPosition(1));
		ca.addCard(new CardInfo(Karte.HerzB).setPlayerPosition(1));
		ca.setStyle(Style.ROW);
		ca.setRotation(Math.toRadians(-90));
		ca.addCardListener(new CardListener() {
			@Override
			public void cardChanged(CardEvent event) {
				System.out.println("card event "+event);
			}
		});
		
		getContentPane().setLayout(null);
		//getContentPane().add(a);
		getContentPane().add(ca);

		setSize(900,900);
		setVisible(true);
	}

	
}
