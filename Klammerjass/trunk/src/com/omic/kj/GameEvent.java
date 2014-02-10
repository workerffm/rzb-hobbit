package com.omic.kj;

import com.omic.kj.shared.domain.PlayerResponse;

/**
 * Gamecontroller event, contains next state for FSM or response from a player.
 */
class GameEvent {
  private EventType event;
  private PlayerResponse playerResponse;
	private GameState state;
  
	public GameEvent(EventType event, GameState state) {
		this.event = event;
		this.setState(state);
	}
	public GameEvent() {
	}
	public EventType getEvent() {
		return event;
	}
	public void setEvent(EventType event) {
		this.event = event;
	}
	public PlayerResponse getPlayerResponse() {
		return playerResponse;
	}
	public void setPlayerResponse(PlayerResponse playerResponse) {
		this.playerResponse = playerResponse;
	}
	public GameState getState() {
		return state;
	}
	public void setState(GameState state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "GameEvent [event=" + event + ", playerResponse=" + playerResponse + ", state=" + state + "]";
	}

	
}
