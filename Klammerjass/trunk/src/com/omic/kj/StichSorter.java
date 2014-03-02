package com.omic.kj;

import java.util.Comparator;

public class StichSorter implements Comparator<PlayCard> {

	@Override
	public int compare(PlayCard o1, PlayCard o2) {
		if (o1 == o2)
			return 0;
		if (o1 == null && o2 != null)
			return -1;
		if (o2 == null && o1 != null)
			return 1;

		if (o1.getBidNr() < o2.getBidNr())
			return -1;
		if (o1.getBidNr() < o2.getBidNr())
			return 1;
		return 0;
	}

}
