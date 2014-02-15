package com.omic.kj.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import layout.TableLayout;

public class JStatusPanel extends JPanel {

	private JLabel[] tf;

	public JStatusPanel(){
		setLayout(new TableLayout(new double[][]{{0.25,0.25,0.25,0.25},{TableLayout.FILL}}));
		tf = new JLabel[] {
			new JLabel(),
			new JLabel(),
			new JLabel(),
			new JLabel()
		};
		BevelBorder b = new BevelBorder(BevelBorder.LOWERED);
		tf[0].setBorder (b);
		tf[1].setBorder (b);
		tf[2].setBorder (b);
		tf[3].setBorder (b);
		add(tf[0], "0,0");
		add(tf[1], "1,0");
		add(tf[2], "2,0");
		add(tf[3], "3,0");
	}
	
	
	void setTrumpf(String s){
		tf[1].setText("Trumpf ist "+s);
	}
	void setAufspieler(String s){
		tf[0].setText("Aufspieler: "+s);
	}
	void setPunkte(String s){
		tf[2].setText(s+" Punkte");
	}
	void setRunde(String s){
		tf[3].setText("Runde "+s);
	}
	
}
