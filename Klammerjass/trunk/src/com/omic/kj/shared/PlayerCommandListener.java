package com.omic.kj.shared;

import com.omic.kj.shared.domain.PlayerCommand;

public interface PlayerCommandListener {
  void onMessage (PlayerCommand command);
}
