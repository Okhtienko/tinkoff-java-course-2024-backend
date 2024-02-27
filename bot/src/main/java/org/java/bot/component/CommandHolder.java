package org.java.bot.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.java.bot.bot.Command;
import org.java.bot.bot.CommandManager;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommandHolder implements CommandManager {
    private final Map<String, Command> commands;

    public CommandHolder(List<Command> commands) {
        this.commands = new HashMap<>();
        commands.forEach(command -> this.commands.put(command.command(), command));
    }

    @Override
    public Command get(String name) {
        Command command = commands.get(name);
        log.info("Retrieving command for name: {}, found: {}", name, command);
        return command;
    }

    @Override
    public Map<String, Command> gets() {
        log.info("Returning all registered commands: {}", commands);
        return commands;
    }
}
