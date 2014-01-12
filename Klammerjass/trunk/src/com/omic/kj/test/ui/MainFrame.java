package com.omic.kj.test.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.*;
import layout.TableLayout;

public class MainFrame extends JFrame {

	CardLayout cardLayout;
	JPanel cardPanel;

	MainFrame() {
		super();
	}

	public void start() {
		setTitle("Klammerjass V0.2");
		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);
		cardPanel.add("intro", createIntroScreen());
		cardPanel.add("options", createOptionsScreen());
		cardPanel.add("game", createGameScreen());
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(cardPanel);
		setSize(1000, 800);
		setVisible(true);
	}

	/**
	 * Screen: Intro
	 */
	private Component createIntroScreen() {
		final JButton btnNew, btnJoin, btnInfo;

		final JPanel p = new JPanel();
		Theme.style3(p);
		
		p.setLayout(new GridLayout(3, 1, 20, 20));
		p.setBorder(new EmptyBorder(50, 50, 50, 50));

		btnNew = new JButton(" NEW GAME ");
		btnNew.setActionCommand("new");
		btnJoin = new JButton(" JOIN GAME ");
		btnJoin.setActionCommand("join");
		btnInfo = new JButton(" INFO ");
		btnInfo.setActionCommand("info");

		final Controller ctl = new Controller();
		for (JButton b : new JButton[] { btnNew, btnJoin, btnInfo }) {
			p.add(Theme.style1(b));
			b.addActionListener(ctl);
		}
		return p;
	}

	/**
	 * Screen: Game Options
	 */
	private Component createOptionsScreen() {
		JPanel p = new JPanel();
		Theme.style3(p);
		
		TableLayout tl = new TableLayout(new double[][]//col/row
				{ { 50, TableLayout.PREFERRED, 20, TableLayout.FILL, 50 }, //
				{ 50, TableLayout.PREFERRED, 50, TableLayout.PREFERRED, 50, TableLayout.PREFERRED, 50, TableLayout.FILL, 50 } });
		p.setLayout(tl);

		final JLabel lName, lGegner, lPunkte;
		final JTextField tfName, tfGegner, tfPunkte;
		final JButton btnOk;
		lName = new JLabel("Dein Name:");
		lGegner = new JLabel("Mitspieler:");
		lPunkte = new JLabel("Max. Punkte:");
		tfName = new JTextField();
		tfGegner = new JTextField("3");
		tfPunkte = new JTextField("300");
		btnOk = new JButton("Weiter");
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnPanel.add(Theme.style1(btnOk));

		p.add(Theme.style1(lName), "1,1,r");
		p.add(Theme.style1(lGegner), "1,3,r");
		p.add(Theme.style1(lPunkte), "1,5,r");
		p.add(Theme.style2(tfName), "3,1");
		p.add(Theme.style2(tfGegner), "3,3");
		p.add(Theme.style2(tfPunkte), "3,5");
		p.add(Theme.style3(btnPanel),"1,7,3,7");
		return p;
	}

	/**
	 * Screen: Game Table
	 */
	private Component createGameScreen() {
		JPanel p = new JPanel();
		return p;
	}


	// ---------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------

	final class Controller implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("new")) {
				switchToScreen("options");
			}
		}

	}

	public void switchToScreen(String screen) {
		cardLayout.show(cardPanel, screen);
	}
}
