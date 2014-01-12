package com.omic.kj.test.ui;

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
			c.setBorder(new CompoundBorder(new EmptyBorder(50, 50, 50, 50), new LineBorder(Color.orange, 10)));
		}
		return c;
	}

	static JComponent style2(JComponent c) {
		c.setOpaque(true);
		c.setForeground(Color.orange);
		c.setBackground(Color.blue);
		c.setFont(ftLarge);
		if (c instanceof JButton) {
			c.setBorder(new CompoundBorder(new EmptyBorder(50, 50, 50, 50), new LineBorder(Color.orange, 10)));
		}
		return c;
	}

	public static JComponent style3(JComponent p) {
		p.setOpaque(true);
		p.setBackground(Color.black);
		return p;
		
	}

}
