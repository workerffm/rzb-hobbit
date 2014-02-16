package com.omic.kj.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.logging.Logger;
import javax.swing.JComponent;

public final class UserArea {

	private final Logger log = Logger.getLogger("UI");

	private final int BORDER = 10;

	private final JComponent owner;
	private Point location;
	private boolean active;
	private String name;

	UserArea(final JComponent owner) {
		super();
		this.owner = owner;
	}

	public void paint(Graphics2D g) {
		if (getLocation() != null) {
			final int x = getLocation().x, y = getLocation().y;
			g.setColor(this.active ? Color.red : Color.black);
			g.drawString(this.name + (this.active ? " *" : "  "), x, y);
		}
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
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

}
