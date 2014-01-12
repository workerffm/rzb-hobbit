package com.omic.kj.test.ui;

public class KjMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainFrame gui = new MainFrame();
			gui.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
