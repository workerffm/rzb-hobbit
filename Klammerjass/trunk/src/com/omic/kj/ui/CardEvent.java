package com.omic.kj.ui;

import com.omic.kj.shared.domain.Karte;

public final class CardEvent {

	public enum ChangeType {
		CARD_SELECTED, CARD_CLICKED
	};

	public interface CardListener {
		void cardChanged(CardEvent event);
	}

	private ChangeType event;
	private Karte card;

	public CardEvent(Karte card, ChangeType event) {
		this.setCard(card);
		this.setEvent(event);
	}

	@Override
	public String toString() {
		return "CardEvent [event=" + getEvent() + ", card=" + getCard() + "]";
	}

	public ChangeType getEvent() {
		return event;
	}

	public void setEvent(ChangeType event) {
		this.event = event;
	}

	public Karte getCard() {
		return card;
	}

	public void setCard(Karte card) {
		this.card = card;
	}

}
