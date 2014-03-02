package com.omic.kj.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.text.NumberFormat;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import com.omic.kj.shared.domain.GameSettings;
import layout.TableLayout;

class MainFrame extends JFrame {

	CardLayout cardLayout;
	JPanel cardPanel;
	private JGamePanel gamedesk;
	private JTextField tfName, tfGegner, tfPunkte;

	MainFrame() {
		super();
	}

	public void start() throws Exception {
		setTitle("Klammern V0.4");
		/*
		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);
		cardPanel.add("intro", createIntroScreen());
		cardPanel.add("options", createOptionsScreen());
		cardPanel.add("game", createGameScreen());
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(cardPanel);
		*/

		final GraphicsDevice myDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		if (false && myDevice.isFullScreenSupported()){
			setUndecorated(true);
			myDevice.setFullScreenWindow(this);
		}
		else {
			setSize(900, 900);
			setResizable(false); // Resizable möglich, aber nur quadratisch möglich !!!
		}
		
		gamedesk = new JGamePanel();
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(gamedesk);

		setVisible(true);
		startGame();
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

		final JButton btnOk;
		lName = new JLabel("Dein Name:");
		lGegner = new JLabel("Mitspieler:");
		lPunkte = new JLabel("Max. Punkte:");
		tfName = new JTextField();

		final NumberFormat fGegner = NumberFormat.getIntegerInstance();
		fGegner.setMinimumIntegerDigits(3);
		fGegner.setMaximumIntegerDigits(3);
		tfGegner = new JFormattedTextField(fGegner);

		final NumberFormat fPunkte = NumberFormat.getIntegerInstance();
		fPunkte.setMinimumIntegerDigits(300);
		fPunkte.setMaximumIntegerDigits(3000);
		tfPunkte = new JFormattedTextField(fPunkte);

		btnOk = new JButton("Weiter");
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnPanel.add(Theme.style1(btnOk));

		p.add(Theme.style1(lName), "1,1,r");
		p.add(Theme.style1(lGegner), "1,3,r");
		p.add(Theme.style1(lPunkte), "1,5,r");
		p.add(Theme.style2(tfName), "3,1");
		p.add(Theme.style2(tfGegner), "3,3");
		p.add(Theme.style2(tfPunkte), "3,5");
		p.add(Theme.style3(btnPanel), "1,7,3,7");
		return p;
	}

	/**
	 * Screen: Game Table
	 */
	private Component createGameScreen() {
		JPanel p = new JPanel();
		return p;
	}

	private void startGame() {
		try {
			//GameSettings settings = new GameSettings(true, getInt(tfGegner), getInt(tfPunkte));
			final GameSettings settings = new GameSettings(true, 3, 222);

			final LocalPlayer g = new LocalPlayer();
			g.setGameDesk(this.gamedesk);
			g.run(settings);

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private int getInt(JTextField tf) {
		return Integer.parseInt(tf.getText());
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
