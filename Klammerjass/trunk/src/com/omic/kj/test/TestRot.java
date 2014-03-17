package com.omic.kj.test;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

class TestRot extends JFrame {

	 public static void main(String s[]) throws IOException {
		 TestRot f = new TestRot();
		 f.start();
	 }
	
	TestRot() {
		super();
	}

	public void start() throws IOException  {
		setTitle("Testframe");
		
		TestPanel p = new TestPanel();
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(p);

		setSize(900,900);
		setVisible(true);
	}

	
	private class TestPanel extends JComponent{
		private Image img1,img2;

		TestPanel() throws IOException {
			InputStream is = this.getClass().getResourceAsStream("/karo0.gif");
			img1 = ImageIO.read(is);
			is.close();
			is = this.getClass().getResourceAsStream("/karo1.gif");
			img2 = ImageIO.read(is);
			is.close();
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			final Graphics2D g2 = (Graphics2D) g;
			final AffineTransform t = g2.getTransform();
			
			g2.translate(getWidth()/2, getHeight()-img1.getHeight(null));
			g2.drawImage(img1, 0, 0, null);
			g2.setTransform(t);
			
			AffineTransform r = AffineTransform.getQuadrantRotateInstance(1);
			g2.setTransform(r);
			g2.translate(getHeight()/2, -img2.getHeight(null));
			g2.drawImage(img2, 0, 0, null);
			g2.setTransform(t);

		  r = AffineTransform.getQuadrantRotateInstance(2);
			g2.setTransform(r);
			g2.translate(-getWidth()/2, -img2.getHeight(null));
			g2.drawImage(img2, 0, 0, null);
			g2.setTransform(t);
	
		  r = AffineTransform.getQuadrantRotateInstance(3);
			g2.setTransform(r);
			g2.translate(-getHeight()/2, getWidth()-img1.getHeight(null));
			g2.drawImage(img2, 0, 0, null);
			g2.setTransform(t);

		}
		
	}
	
}
