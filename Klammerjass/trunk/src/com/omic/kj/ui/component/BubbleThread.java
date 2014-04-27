package com.omic.kj.ui.component;


public final class BubbleThread extends Thread {
	private JPlayer playerArea;
	private String message;

	public BubbleThread(JPlayer playerArea, String message) {
		this.playerArea = playerArea;
		this.message = message;
	}

	@Override
	public void run() {
		setName("Bubble " + this.playerArea.getPosition());
		this.playerArea.setBubbleMessage(message);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.playerArea.setBubbleMessage(null);
	}
}
