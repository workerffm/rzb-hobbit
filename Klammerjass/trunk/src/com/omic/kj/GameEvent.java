package com.omic.kj;

import com.omic.kj.shared.domain.PlayerResponse;

class GameEvent {
  private Event event;
  private PlayerResponse playerResponse;
	private GameState state;
  
	public GameEvent(Event event, GameState state) {
		this.event = event;
		this.state = state;
	}
	public GameEvent() {
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	public PlayerResponse getPlayerResponse() {
		return playerResponse;
	}
	public void setPlayerResponse(PlayerResponse playerResponse) {
		this.playerResponse = playerResponse;
	}
}
