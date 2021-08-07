package bzh.strawberry.dynamo.command;

import bzh.strawberry.dynamo.Dynamo;

/**
 * Crée par Eclixal
 * Le 09/05/2018.
 */
public class EndCommand extends Command {

    public EndCommand(String name, String[] aliases, String description) {
        super(name, aliases, description);
    }

    @Override
    public boolean execute(String[] args) {
        Dynamo.getDynamo().kill();
        return true;
    }
}
