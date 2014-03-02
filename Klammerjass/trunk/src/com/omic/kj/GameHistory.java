package com.omic.kj;

import java.util.Date;
import com.omic.kj.shared.domain.Farbe;

public class GameHistory {

	private final int partieNr;
	private final int rundenNr;
	private final int spielNr;
	private final Date start;
	private final Date end;
	private final Player geber;
	private final Player trumpfPlayer;
	private final Farbe trumpfFarbe;
	private final int beet;
  private String playerName1, playerName2, playerName3, playerName4;
  private int playerPoints1, playerPoints2, playerPoints3, playerPoints4;
	private int teamGew1, teamGew2, teamPoints1, teamPoints2;
  

	public GameHistory(int partieNr, int rundenNr, int spielNr, Date start, Date end, Player geber, Player trumpfPlayer, Farbe trumpfFarbe, int beet) {
		this.partieNr = partieNr;
		this.rundenNr = rundenNr;
		this.spielNr = spielNr;
		this.start = start;
		this.end = end;
		this.geber = geber;
		this.trumpfPlayer = trumpfPlayer;
		this.trumpfFarbe = trumpfFarbe;
		this.beet = beet;
	}

	public int getPartieNr() {
		return partieNr;
	}

	public int getRundenNr() {
		return rundenNr;
	}

	public int getSpielNr() {
		return spielNr;
	}

	public Date getEnd() {
		return end;
	}

	public Player getGeber() {
		return geber;
	}

	public Player getTrumpfPlayer() {
		return trumpfPlayer;
	}

	public Farbe getTrumpfFarbe() {
		return trumpfFarbe;
	}

	public int getBeet() {
		return beet;
	}

	public String getPlayerName1() {
		return playerName1;
	}

	public void setPlayerName1(String playerName1) {
		this.playerName1 = playerName1;
	}

	public String getPlayerName2() {
		return playerName2;
	}

	public void setPlayerName2(String playerName2) {
		this.playerName2 = playerName2;
	}

	public String getPlayerName3() {
		return playerName3;
	}

	public void setPlayerName3(String playerName3) {
		this.playerName3 = playerName3;
	}

	public String getPlayerName4() {
		return playerName4;
	}

	public void setPlayerName4(String playerName4) {
		this.playerName4 = playerName4;
	}

	public int getPlayerPoints1() {
		return playerPoints1;
	}

	public void setPlayerPoints1(int playerPoints1) {
		this.playerPoints1 = playerPoints1;
	}

	public int getPlayerPoints2() {
		return playerPoints2;
	}

	public void setPlayerPoints2(int playerPoints2) {
		this.playerPoints2 = playerPoints2;
	}

	public int getPlayerPoints3() {
		return playerPoints3;
	}

	public void setPlayerPoints3(int playerPoints3) {
		this.playerPoints3 = playerPoints3;
	}

	public int getPlayerPoints4() {
		return playerPoints4;
	}

	public void setPlayerPoints4(int playerPoints4) {
		this.playerPoints4 = playerPoints4;
	}

	public int getTeamGew1() {
		return teamGew1;
	}

	public void setTeamGew1(int teamGew1) {
		this.teamGew1 = teamGew1;
	}

	public int getTeamGew2() {
		return teamGew2;
	}

	public void setTeamGew2(int teamGew2) {
		this.teamGew2 = teamGew2;
	}

	public int getTeamPoints1() {
		return teamPoints1;
	}

	public void setTeamPoints1(int teamPoints1) {
		this.teamPoints1 = teamPoints1;
	}

	public int getTeamPoints2() {
		return teamPoints2;
	}

	public void setTeamPoints2(int teamPoints2) {
		this.teamPoints2 = teamPoints2;
	}

	public Date getStart() {
		return start;
	}

}
