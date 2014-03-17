package com.omic.kj.shared.domain;

/**
 * Gewählte Optionen vom Startdialog: <br>
 * <code>
 * -------------------------------------------------
 * Spieler 
 *   Name: ____________
 *   Pwd : ____________
 *                                 [Login]
 * -------------------------------------------------
 * 
 * (X) Spiel gegen online spieler
 * ( ) Spiel gegen PC      
 *        Anzahl Gegner: [  1-3  ]
 * 
 * Spielen bis Punkte: ___________       
 *                                 [Start]
 * ------------------------------------------------- 
 * </code>
 *         <br>
 * @author cb3arbe <br>
 * @version 28.7.2011 arbeitm 1st draft <br>
 *
 */
public class GameSettings {

	private boolean option_playWithPc;
	private int anzPCs, maxPoints, maxRounds;
	
	
	public GameSettings (boolean playWithPC, int anzPCGegner, int maxPoints) {
		option_playWithPc = playWithPC;
		anzPCs = anzPCGegner;
		this.maxPoints = maxPoints;
		this.maxRounds = 9;
	}


	/**
	 * ( ) Spiel gegen PC   --> return true      
	 */
	public boolean isOption_PlayWithPC() {
		return option_playWithPc;
	}

	/**
	 * Anzahl Computer Gegner
	 */
	public int getComputerCount() {
		return anzPCs;
	}

	public int getMaximumPoints() {
		return maxPoints;
	}

	public void setOption_WITHPC (boolean b) {
		option_playWithPc=b;	
	}
	
	public String toString() {
		return "["
		  +"option_playWithPc="+option_playWithPc+","
		  +"anzPCs="+anzPCs+","
		  +"max_points="+maxPoints
		 +"]";
	}

}
