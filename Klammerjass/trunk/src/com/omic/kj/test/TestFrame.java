package com.omic.kj.test;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import com.omic.kj.shared.domain.GameHistory;
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
		a.add(new GameHistory(1,255,"Tom"));
		a.add(new GameHistory(2,13,"Markus"));
		a.setActiveGameInfo ("Spiel läuft", 600);
		a.setLocation(5, 5);
		//getContentPane().setLayout(new BorderLayout());
		getContentPane().add(a);
		//pack();
		setSize(900,900);
		//setResizable(false); // Resizable möglich, aber nur quadratisch möglich !!!
		setVisible(true);
	}

	
}
