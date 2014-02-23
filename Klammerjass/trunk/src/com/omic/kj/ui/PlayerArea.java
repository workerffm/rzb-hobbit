package com.omic.kj.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import javax.swing.JComponent;

/**
 * Small panel with photo, name and punkte, etc.
 */
public final class PlayerArea extends JComponent {

	//private final Logger log = Logger.getLogger("UI");

	private final int BORDER = 10;
	private final static Color bubbleColor = new Color(0xF3F5B5);
	private final static Color activeColor = new Color(0x03572E);
	private final static Font smallFont = new Font("Bauhaus 93", Font.PLAIN, 13);
	private final static Font largeFont = new Font("Bauhaus 93", Font.PLAIN, 20);
	private final static Composite transparent80 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f);
	
	private final JComponent owner;
	private boolean active;
	private String name;
	private Image image;
	private int position;
	private String bubbleMessage;

	PlayerArea(final JComponent owner, Dimension dimension, int position) {
		super();
		this.owner = owner;
		this.position = position;
		setSize(dimension);
	}

	public void paint(Graphics2D g) {
		if (getLocation() != null) {
			final AffineTransform saveAT = g.getTransform();
			final int x = getLocation().x, y = getLocation().y;
			
			if(image!=null) {		
      	int imgx=x, imgy=y;
      	if (getPosition()==1){ imgx +=10; imgy +=10;}
      	if (getPosition()==2){ imgx +=0; imgy -=0;}
      	if (getPosition()==3){ imgx +=10; imgy +=10;}
      	if (getPosition()==4){ imgx +=20; imgy +=0;}
      	g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			  g.drawImage(image, imgx, imgy, null);
      }
			if(this.active) {
			  g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
			  g.setColor(this.active ? activeColor : Color.black);
		  	g.fillRect(x, y+getSize().height-20, getSize().width, 20);
			}
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		 	g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			g.setColor(this.active? Color.white : Color.white);
			g.setFont(smallFont);
			g.drawString(this.name!=null?this.name:"", x+5, y+getSize().height-5);
			
			if(this.bubbleMessage!=null){
				g.setColor(bubbleColor);
				Composite save = g.getComposite();
				g.setComposite(transparent80);
			  g.fillRoundRect(x, y-15, 80, 40, 15,15);	
			  int a=x-20;
			  int b=y+40-15;
			  g.fillPolygon(new int[]{a+40 ,a+55, a+57, a+60 ,a+63 ,a+60 ,a+49 ,a+45}, 
			  		new int[] { b+0,b+0,b+4,b+8,b+9,b+10,b+8,b+4},
			  		8);
			  
				g.setColor(Color.black);
				g.setFont(largeFont);
			  g.drawString(this.bubbleMessage, x+5, y+12);
			  g.setComposite(save);
			}
			g.setTransform(saveAT);
		}
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return this.active;
	}

  public void setImage(Image image) {
		this.image = image;
	}

	public void setBubbleMessage(final String bubbleMessage) {
		this.bubbleMessage = bubbleMessage;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}
