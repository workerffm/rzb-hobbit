package com.omic.kj.ui;

import com.omic.kj.shared.domain.Karte;

public final class CardEvent {

	private ChangeType event;
	private Karte card;

	public CardEvent(Karte card, ChangeType event) {
		this.card = card;
		this.event = event;
	}

	@Override
	public String toString() {
		return "CardEvent [event=" + event + ", card=" + card + "]";
	}

	public enum ChangeType {
		CARD_SELECTED, CARD_CLICKED
	};

	public interface CardListener {
		void cardChanged(CardEvent event);
	}

}
