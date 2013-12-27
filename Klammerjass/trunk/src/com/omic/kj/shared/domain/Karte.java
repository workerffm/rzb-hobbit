package com.omic.kj.shared.domain;

public enum Karte {
    Karo7( 1,Farbe.Karo, Wert.Sieben, 0) ,
    Karo8 ( 2,Farbe.Karo, Wert.Acht, 0),
			Karo9( 3,Farbe.Karo, Wert.Neun, 0), 
			KaroB( 4,Farbe.Karo, Wert.Bube, 2),
			KaroD( 5,Farbe.Karo, Wert.Dame, 3),
			KaroK( 6,Farbe.Karo, Wert.Koenig, 4), 
			Karo10( 7,Farbe.Karo, Wert.Zehn, 10),
			KaroA( 8,Farbe.Karo, Wert.As, 11), 
			Herz7( 9,Farbe.Herz, Wert.Sieben, 0),
			Herz8(10,Farbe.Herz, Wert.Acht, 0),
			Herz9(11,Farbe.Herz, Wert.Neun, 0),
			HerzB(12,Farbe.Herz, Wert.Bube, 2),
			HerzD(13,Farbe.Herz, Wert.Dame, 3),
			Herzk(14,Farbe.Herz, Wert.Koenig, 4),
			Herz10(15,Farbe.Herz, Wert.Zehn, 10), 
			HerzA(16,Farbe.Herz, Wert.As, 11), 
			Pik7(17,Farbe.Pik, Wert.Sieben, 0),
			Pik8(18,Farbe.Pik, Wert.Acht, 0), 
			Pik9(19,Farbe.Pik, Wert.Neun, 0),
			PikB(20,Farbe.Pik, Wert.Bube, 2), 
			PikD(21,Farbe.Pik, Wert.Dame, 3),
			PikK(22,Farbe.Pik, Wert.Koenig, 4),
			Pik10(23,Farbe.Pik, Wert.Zehn, 10), 
			PikA(24,Farbe.Pik, Wert.As, 11),
			Kreuz7(25,Farbe.Kreuz, Wert.Sieben, 0),
			Kreuz8(26,Farbe.Kreuz, Wert.Acht, 0), 
			Kreuz9(27,Farbe.Kreuz, Wert.Neun, 0), 
			KreuzB(28,Farbe.Kreuz, Wert.Bube, 2),
			KreuzD(29,Farbe.Kreuz, Wert.Dame, 3),
			KreuzK(30,Farbe.Kreuz, Wert.Koenig, 4),
			Kreuz10(31,Farbe.Kreuz, Wert.Zehn, 10), 
			KreuzA(32,Farbe.Kreuz, Wert.As, 11); 

	private final Farbe color;
	private final Wert name;
	private final int points;
	private final int rank;

	private Karte(int rank, Farbe color, Wert name, int points) {
		this.rank = rank;
		this.color = color;
		this.name = name;
		this.points = points;
	}

	public Wert getName() {
		return name;
	}

	public int getPoints() {
		return points;
	}

	public Farbe getColor() {
		return color;
	}
	
	public int getRank() {
		return rank;
	}

}
