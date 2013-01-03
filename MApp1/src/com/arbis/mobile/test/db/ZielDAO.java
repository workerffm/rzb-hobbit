package com.arbis.mobile.test.db;

import java.util.List;

import com.arbis.mobile.test.domain.Ziel;

public class ZielDAO {

	public static List<Ziel> populateList(final List<Ziel> l) {
		l.clear();
		l.add(new Ziel("REWE CITY", "Weberstrasse 118, 53113 Bonn"));
		l.add(new Ziel("REWE", "Heerstra�e 40-42, 53111 Bonn"));
		l.add(new Ziel("REWE", "K�lnstra�e 154, 53111 Bonn"));
		l.add(new Ziel("REWE", "Immenburgstra�e 42, 53121 Bonn"));
		l.add(new Ziel("REWE CITY", "K�dinghovener Stra�e 141, 53225 Bonn"));
		l.add(new Ziel("REWE", "R�ckumstrasse 21, 53121 Bonn"));
		l.add(new Ziel("REWE CITY", "R�merstra�e 214, 53117 Bonn"));
		l.add(new Ziel("REWE", "Pleimesstra�e 3, 53129 Bonn"));
		l.add(new Ziel("REWE", "Pariser Stra�e 34, 53117 Bonn"));
		l.add(new Ziel("REWE", "Rochusstrasse 187-189, 53123 Bonn"));
		l.add(new Ziel("REWE", "Am Schickshof 6, 53123 Bonn-Duisdorf"));
		l.add(new Ziel("REWE CITY", "Am Buschhof 19-21, 53227 Bonn"));
		l.add(new Ziel("REWE", "Zeil 106-110, 60313 Frankfurt am Main"));
		l.add(new Ziel("REWE CITY", "Oeder Weg 17-19, 60318 Frankfurt"));
		l.add(new Ziel("REWE", "Schweizer Stra�e 33, 60594 Frankfurt"));
		l.add(new Ziel("REWE", "Walther-v-Cronberg-Platz 10, 60594 Frankfurt"));
		l.add(new Ziel("REWE", "Dreieichstr. 59, 60594 Frankfurt"));
		l.add(new Ziel("REWE", "Gr�neburgweg 12, 60322 Frankfurt"));
		l.add(new Ziel("REWE", "Textorstra�e / Bruchstra�e 31, 60594 Frankfurt am Main"));
		l.add(new Ziel("REWE", "Eiserne Hand 18-20, 60318 Frankfurt"));
		l.add(new Ziel("REWE CITY", "Karlstra�e 4, 60329 Frankfurt"));
		l.add(new Ziel("REWE CITY", "Wittelsbacher Allee 30, 60316 Frankfurt / Ostend"));
		l.add(new Ziel("REWE", "Berger Str. 96, 60316 Frankfurt / Nordend-Ost"));
		l.add(new Ziel("REWE", "Eckenheimer Landstra�e 97, 60318 Frankfurt"));
		return l;
	}

}
