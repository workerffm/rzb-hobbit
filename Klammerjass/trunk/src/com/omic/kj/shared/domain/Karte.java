package com.omic.kj.shared.domain;

/**
 * Klammerjass : Beim Trumpf ist die 10 eingereiht!
 */
public enum Karte {
	Karo7( 1,Farbe.Karo, Kartenwert.Sieben, 0) ,
	Karo8 ( 2,Farbe.Karo, Kartenwert.Acht, 0),
	Karo9( 3,Farbe.Karo, Kartenwert.Neun, 0), 
	KaroB( 4,Farbe.Karo, Kartenwert.Bube, 2),
	KaroD( 5,Farbe.Karo, Kartenwert.Dame, 3),
	KaroK( 6,Farbe.Karo, Kartenwert.Koenig, 4), 
	Karo10( 7,Farbe.Karo, Kartenwert.Zehn, 10),
	KaroA( 8,Farbe.Karo, Kartenwert.As, 11), 
	Herz7( 9,Farbe.Herz, Kartenwert.Sieben, 0),
	Herz8(10,Farbe.Herz, Kartenwert.Acht, 0),
	Herz9(11,Farbe.Herz, Kartenwert.Neun, 0),
	HerzB(12,Farbe.Herz, Kartenwert.Bube, 2),
	HerzD(13,Farbe.Herz, Kartenwert.Dame, 3),
	Herzk(14,Farbe.Herz, Kartenwert.Koenig, 4),
	Herz10(15,Farbe.Herz, Kartenwert.Zehn, 10), 
	HerzA(16,Farbe.Herz, Kartenwert.As, 11), 
	Pik7(17,Farbe.Pik, Kartenwert.Sieben, 0),
	Pik8(18,Farbe.Pik, Kartenwert.Acht, 0), 
	Pik9(19,Farbe.Pik, Kartenwert.Neun, 0),
	PikB(20,Farbe.Pik, Kartenwert.Bube, 2), 
	PikD(21,Farbe.Pik, Kartenwert.Dame, 3),
	PikK(22,Farbe.Pik, Kartenwert.Koenig, 4),
	Pik10(23,Farbe.Pik, Kartenwert.Zehn, 10), 
	PikA(24,Farbe.Pik, Kartenwert.As, 11),
	Kreuz7(25,Farbe.Kreuz, Kartenwert.Sieben, 0),
	Kreuz8(26,Farbe.Kreuz, Kartenwert.Acht, 0), 
	Kreuz9(27,Farbe.Kreuz, Kartenwert.Neun, 0), 
	KreuzB(28,Farbe.Kreuz, Kartenwert.Bube, 2),
	KreuzD(29,Farbe.Kreuz, Kartenwert.Dame, 3),
	KreuzK(30,Farbe.Kreuz, Kartenwert.Koenig, 4),
	Kreuz10(31,Farbe.Kreuz, Kartenwert.Zehn, 10), 
	KreuzA(32,Farbe.Kreuz, Kartenwert.As, 11); 

	private final Farbe farbe;
	private final Kartenwert wert;
	private final int punkte;
	private final int rank;

	private Karte(int rank, Farbe farbe, Kartenwert wert, int punkte) {
		this.rank = rank;
		this.farbe = farbe;
		this.wert = wert;
		this.punkte = punkte;
	}

	public Farbe getFarbe() {
		return farbe;
	}

	public Kartenwert getWert() {
		return wert;
	}

	public int getPunkte() {
		return punkte;
	}

	public int getRank() {
		return rank;
	}


}
