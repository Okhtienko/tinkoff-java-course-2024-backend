package org.java.bot.service;

import org.java.bot.util.BotState;
import org.springframework.stereotype.Service;

@Service
public class BotStateService {
    private final ThreadLocal<BotState> botState;

    public BotStateService() {
        botState = ThreadLocal.withInitial(() -> BotState.WAITING_COMMAND);
    }

    public BotState get() {
        return botState.get();
    }

    public void set(BotState newState) {
        botState.set(newState);
    }

    public void reset() {
        botState.remove();
    }
}
