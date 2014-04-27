package com.omic.kj.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

class TestRot extends JFrame {

	public static void main(String s[]) throws IOException {
		TestRot f = new TestRot();
		f.start();
	}

	TestRot() {
		super();
	}

	public void start() throws IOException {
		setTitle("Testframe");

		TestPanel p = new TestPanel();
		p.setOpaque(!true);
		p.setBackground(Color.yellow);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(p);
		getContentPane().add(new JLabel("west"), BorderLayout.WEST);
		getContentPane().add(new JLabel("east"), BorderLayout.EAST);

		setSize(900, 900);
		setVisible(true);
	}

	private class TestPanel extends JComponent {
		private BufferedImage img1, img2;

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
		
			int rot = 20;
			for (int n=0; n < 4; n++,rot+=75) {
				double crossRotation = Math.toRadians(rot);
				int h = img1.getHeight();
				int w = img1.getWidth();
				double d = Math.sqrt(h * h + w * w);
				System.out.println("h=" + h + ",w=" + w + ",d=" + d);

				{
					AffineTransform t2 = new AffineTransform();
					t2.translate(d - w, d - h);
					AffineTransformOp op = new AffineTransformOp(t2, AffineTransformOp.TYPE_BILINEAR);
					img2 = op.filter(img1, null);
				}

				BufferedImage img3;
				{
					AffineTransform t1 = new AffineTransform();
					t1.rotate(crossRotation, d,d);
					t1.translate(0,h/4);
					AffineTransformOp op = new AffineTransformOp(t1, AffineTransformOp.TYPE_BILINEAR);
					img3 = op.filter(img2, null);
				}

				g2.drawImage(img3, (int)Math.round(getWidth()/2-d), (int)Math.round(getHeight()/2-d), null);
			}
		}
	}

}
