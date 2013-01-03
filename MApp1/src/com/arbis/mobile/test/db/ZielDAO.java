package com.arbis.mobile.test.db;

import java.util.List;

import com.arbis.mobile.test.domain.Ziel;

public class ZielDAO {

	public static List<Ziel> populateList(final List<Ziel> l) {
		l.clear();
		l.add(new Ziel("REWE CITY", "Weberstrasse 118, 53113 Bonn"));
		l.add(new Ziel("REWE", "Heerstraﬂe 40-42, 53111 Bonn"));
		l.add(new Ziel("REWE", "Kˆlnstraﬂe 154, 53111 Bonn"));
		l.add(new Ziel("REWE", "Immenburgstraﬂe 42, 53121 Bonn"));
		l.add(new Ziel("REWE CITY", "K¸dinghovener Straﬂe 141, 53225 Bonn"));
		l.add(new Ziel("REWE", "Rˆckumstrasse 21, 53121 Bonn"));
		l.add(new Ziel("REWE CITY", "Rˆmerstraﬂe 214, 53117 Bonn"));
		l.add(new Ziel("REWE", "Pleimesstraﬂe 3, 53129 Bonn"));
		l.add(new Ziel("REWE", "Pariser Straﬂe 34, 53117 Bonn"));
		l.add(new Ziel("REWE", "Rochusstrasse 187-189, 53123 Bonn"));
		l.add(new Ziel("REWE", "Am Schickshof 6, 53123 Bonn-Duisdorf"));
		l.add(new Ziel("REWE CITY", "Am Buschhof 19-21, 53227 Bonn"));
		l.add(new Ziel("REWE", "Zeil 106-110, 60313 Frankfurt am Main"));
		l.add(new Ziel("REWE CITY", "Oeder Weg 17-19, 60318 Frankfurt"));
		l.add(new Ziel("REWE", "Schweizer Straﬂe 33, 60594 Frankfurt"));
		l.add(new Ziel("REWE", "Walther-v-Cronberg-Platz 10, 60594 Frankfurt"));
		l.add(new Ziel("REWE", "Dreieichstr. 59, 60594 Frankfurt"));
		l.add(new Ziel("REWE", "Gr¸neburgweg 12, 60322 Frankfurt"));
		l.add(new Ziel("REWE", "Textorstraﬂe / Bruchstraﬂe 31, 60594 Frankfurt am Main"));
		l.add(new Ziel("REWE", "Eiserne Hand 18-20, 60318 Frankfurt"));
		l.add(new Ziel("REWE CITY", "Karlstraﬂe 4, 60329 Frankfurt"));
		l.add(new Ziel("REWE CITY", "Wittelsbacher Allee 30, 60316 Frankfurt / Ostend"));
		l.add(new Ziel("REWE", "Berger Str. 96, 60316 Frankfurt / Nordend-Ost"));
		l.add(new Ziel("REWE", "Eckenheimer Landstraﬂe 97, 60318 Frankfurt"));
		return l;
	}

}
