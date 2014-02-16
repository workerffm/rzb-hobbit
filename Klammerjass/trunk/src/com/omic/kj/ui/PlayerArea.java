package com.omic.kj.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.JComponent;

/**
 * Small panel with photo, name and punkte, etc.
 */
public final class PlayerArea extends JComponent {

	//private final Logger log = Logger.getLogger("UI");

	private final int BORDER = 10;

	private final JComponent owner;
	private boolean active;
	private String name;
	private Image image;
	private int position;

	PlayerArea(final JComponent owner, Dimension dimension, int position) {
		super();
		this.owner = owner;
		setSize(dimension);
		this.position = position;
	}

	public void paint(Graphics2D g) {
		if (getLocation() != null) {

			final int x = getLocation().x, y = getLocation().y;
			
			g.setColor(this.active ? new Color(0x03572E) : Color.black);
			g.fillRect(x, y+getSize().height-20, getSize().width, 20);
			//g.fillRect(x, y, getSize().width, getSize().height);
      
			if(image!=null) {		
      	int imgx=x, imgy=y;
      	if (position==1){ imgx +=10; imgy +=10;}
      	if (position==2){ imgx +=0; imgy -=0;}
      	if (position==3){ imgx +=10; imgy +=10;}
      	if (position==4){ imgx +=20; imgy +=0;}
			  g.drawImage(image, imgx, imgy, null);
      }
			g.setColor(this.active? Color.white : Color.white);
			g.setFont(new Font("Verdana", Font.BOLD, 12));
			g.drawString(this.name!=null?this.name:"", x+5, y+getSize().height-5);
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

}
