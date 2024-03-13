package org.java.bot.utils;

import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class CommandUtils {
    private static final  Set<String> COMMANDS = Set.of("/start", "/help");

    private CommandUtils() {

    }

    public static Boolean belong(String command) {
        Boolean exists = COMMANDS.contains(command);
        log.info("Command {} exists: {}", command, exists);
        return exists;
    }
}
