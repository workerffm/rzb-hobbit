package com.omic.kj.shared.domain;

/** Anweisung an Spieler */
public enum CommandCode {
	gameinfo,
	playerinfo,
	//roundinfo,
	say, 
	frageOriginal,
	frageKleines,
	frageTrumpffarbe,
	frageBesser,
	zeigeFuenfzig,
	zeigeTerz, 
	tauscheSieben, 
	spieleKarte,
	gameFinish;
}
