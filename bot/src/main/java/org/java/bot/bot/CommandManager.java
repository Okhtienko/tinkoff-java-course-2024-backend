package org.java.bot.bot;

import java.util.Map;

public interface CommandManager {
    Command get(String name);

    Map<String, Command> gets();
}
