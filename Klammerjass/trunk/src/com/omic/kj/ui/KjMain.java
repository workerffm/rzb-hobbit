package com.omic.kj.ui;

import java.io.InputStream;
import java.util.logging.LogManager;

public class KjMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			
			InputStream is = ClassLoader.getSystemResourceAsStream("logging.properties");
			LogManager.getLogManager().readConfiguration(is);
			is.close();
			//log = Logger.getLogger("TestPlayer");
			
			MainFrame gui = new MainFrame();
			gui.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
