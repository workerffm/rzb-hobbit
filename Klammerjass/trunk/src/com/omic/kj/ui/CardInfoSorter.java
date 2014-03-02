package com.omic.kj.ui;

import java.util.Comparator;
import com.omic.kj.shared.domain.CardInfo;
import com.omic.kj.shared.domain.Farbe;

public class CardInfoSorter implements Comparator<CardInfo> {

	private final Farbe trumpf;

	public CardInfoSorter(Farbe trumpf) {
		this.trumpf = trumpf;
	}

	@Override
	public int compare(CardInfo o1, CardInfo o2) {
		if (o1 == o2)
			return 0;
		if (o1 == null && o2 != null)
			return -1;
		if (o2 == null && o1 != null)
			return 1;

		if (o1.getKarte().getRank(trumpf) < o2.getKarte().getRank(trumpf))
			return -1;
		if (o1.getKarte().getRank(trumpf) > o2.getKarte().getRank(trumpf))
			return 1;
		return 0;
	}

}
