package bzh.strawberry.dynamo.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Crée par Eclixal
 * Le 09/05/2018.
 */
public class CommandManager {

    private HashMap<String, Command> commands = new HashMap<>();

    public CommandManager() {

    }

    public Command findCommand(String name) {
        if(commands.containsKey(name))
            return commands.get(name);
        else
            return commands.values().stream().filter((Command c) -> c.isValidAlias(name)).findFirst().orElse(null);
    }

    public void addCommand(Command command) {
        commands.put(command.getName(), command);
    }

    public List<Command> getCommands() {
        return new ArrayList<>(commands.values());
    }

}