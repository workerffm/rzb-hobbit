package com.omic.kj;

import com.omic.kj.shared.domain.PlayerCommand;

public interface CommandListener {
  void toPlayer (PlayerCommand command);
}
