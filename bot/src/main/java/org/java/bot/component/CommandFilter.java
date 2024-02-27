package org.java.bot.component;

import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.java.bot.bot.CommandValidator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommandFilter implements CommandValidator {
    private Set<String> commands;

    public CommandFilter() {
        this.commands = Set.of("/start", "/help");
    }

    @Override
    public Boolean exists(String command) {
        Boolean exists = commands.contains(command);
        log.info("Command {} exists: {}", command, exists);
        return exists;
    }
}
