package com.omic.kj.test;

import javax.swing.JFrame;
import com.omic.kj.shared.domain.GameHistoryInfo;
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
		//getContentPane().setLayout(new BorderLayout());
		getContentPane().add(a);
		//pack();
		setSize(900,900);
		//setResizable(false); // Resizable möglich, aber nur quadratisch möglich !!!
		setVisible(true);
	}

	
}
