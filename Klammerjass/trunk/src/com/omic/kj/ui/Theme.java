package com.omic.kj.ui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import javax.swing.border.*;

public class Theme {
	final static Font ftLarge = new Font("Arial", Font.PLAIN, 80);

	static JComponent style1(JComponent c) {
		c.setOpaque(true);
		c.setForeground(Color.orange);
		c.setBackground(Color.black);
		c.setFont(ftLarge);
		if (c instanceof JButton) {
			c.setBorder(new CompoundBorder(new EmptyBorder(50, 50, 50, 50), new LineBorder(Color.orange, 10,true)));
		}
		return c;
	}

	static JComponent style2(JComponent c) {
		c.setOpaque(false);
		c.setForeground(Color.blue);
		//c.setBackground(Color.blue);
		c.setFont(ftLarge);
		if (c instanceof JButton) {
			c.setBorder(new CompoundBorder(new EmptyBorder(50, 50, 50, 50), new LineBorder(Color.orange, 10,true)));
		}
		return c;
	}

	public static JComponent style3(JComponent p) {
		p.setOpaque(true);
		p.setBackground(Color.black);
		return p;
	}

	public static JComponent style4(JComponent p) {
		p.setOpaque(true);
		p.setFocusable(!true);
		p.setForeground(Color.white);
		p.setBackground(new Color(0x003399));
		return p;
	}

	
}
