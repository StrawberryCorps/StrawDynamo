package bzh.strawberry.dynamo.command;

import bzh.strawberry.dynamo.Dynamo;

/**
 * Crée par Eclixal
 * Le 11/05/2018.
 */
public class HelpCommand extends Command {

    public HelpCommand(String name, String[] aliases, String description) {
        super(name, aliases, description);
    }

    @Override
    public boolean execute(String[] args) {
        Dynamo.getLogger().info("------------ Commandes ------------");
        for (Command command : Dynamo.getDynamo().getCommandManager().getCommands()) {
            Dynamo.getLogger().info("- " + command.getName() + " [" + String.join(", ", command.getAliases()) + "] - " + command.getDescription());
        }
        Dynamo.getLogger().info("-----------------------------------");
        return true;
    }
}
