package com.omic.kj.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.logging.Logger;
import javax.swing.JPanel;
import com.omic.kj.shared.domain.Karte;
import com.omic.kj.ui.CardArea.Style;

public class GameDesk extends JPanel {

	private final Logger log = Logger.getLogger("UI");
	
	private CardArea p1,p2,p3,p4;

	GameDesk() throws InterruptedException {
		//super(new BorderLayout());
		//		JButton b1 = new JButton("Bella"), b2=new JButton("50"), b3=new JButton("Terz");
		//		JPanel infoPanel = new JPanel();
		//		JPanel btnPanel = new JPanel();
		//		btnPanel.setLayout(new GridLayout(3,1));
		//		btnPanel.add(b1);
		//		btnPanel.add(b2);
		//		btnPanel.add(b3);
		//
		//		PSpace playerPanel = new PSpace();
		//		add(btnPanel, BorderLayout.WEST);
		//		add(playerPanel);
		//add(infoPanel);
		//add (new Strawberry());
		//add (new PSpace());
		//setPreferredSize(new Dimension(500, 500));
  
		p1 = new CardArea();
		p1.setStyle(Style.HAND);
		p1.setHidden(false);
		
		p2 = new CardArea();
		p2.setStyle(Style.HAND);
		p2.setHidden(!true);
		p2.addCard(Karte.Karo10);
		p2.addCard(Karte.KaroD);
		p2.addCard(Karte.Pik10);
		
		p3 = new CardArea();
		p3.setStyle(Style.HAND);
		p3.setHidden(!true);
		p3.addCard(Karte.Pik8);
		p3.addCard(Karte.Pik7);
		
		p4 = new CardArea();
		p4.setStyle(Style.HAND);
		p4.setHidden(!true);
		p4.addCard(Karte.Kreuz10);
		p4.addCard(Karte.HerzA);
		p4.addCard(Karte.KaroD);
		p4.addCard(Karte.KreuzA);
		p4.addCard(Karte.Herzk);
		p4.addCard(Karte.Karo9);

		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					p1.addCard(Karte.Herz7);
					repaint();
					Thread.sleep(1000);
					p1.addCard(Karte.Herz8);
					repaint();
					Thread.sleep(1000);
					p1.addCard(Karte.Herz9);
					repaint();
					Thread.sleep(1000);
					p1.setHidden(!true);
					repaint();
				
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}}).start();
	}
	
	
	@Override
	public void paint(Graphics g) {
		final Dimension d = getSize();
		final Graphics2D g2 = (Graphics2D) g;
		g2.rotate(0,0,0);
		if(p1!=null) {
		  p1.setLocation(new Point(d.width/2,d.height));
			p1.paint(g2);
		}
		g2.rotate(Math.toRadians(90), d.width/2,d.height/2);
		if(p2!=null) {
		  p2.setLocation(new Point(d.width/2,d.height));
			p2.paint(g2);
		}
		g2.rotate(Math.toRadians(90), d.width/2,d.height/2);
		if(p3!=null) {
		  p3.setLocation(new Point(d.width/2,d.height));
			p3.paint(g2);
		}
		g2.rotate(Math.toRadians(90), d.width/2,d.height/2);
		if(p4!=null) {
		  p4.setLocation(new Point(d.width/2,d.height));
			p4.paint(g2);
		}
	}
	
}
