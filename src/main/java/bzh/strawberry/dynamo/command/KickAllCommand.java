package bzh.strawberry.dynamo.command;

import bzh.strawberry.dynamo.Dynamo;

/**
 * Crée par Eclixal
 * Le 11/05/2018.
 */
public class KickAllCommand extends Command {

    public KickAllCommand(String name, String[] aliases, String description) {
        super(name, aliases, description);
    }

    @Override
    public boolean execute(String[] args) {
        if(Dynamo.getDynamo().getClientsOnline() == 0) {
            Dynamo.getLogger().info("Aucun joueur n'est en ligne !");
            return true;
        }
        Dynamo.getLogger().info("Kick de " + Dynamo.getDynamo().getClientsOnline() + " joueurs!");
        Dynamo.getDynamo().getDynamoClients().forEach(dynamoClient -> dynamoClient.getUpstreamHandler().getUpstreamChannel().close());
        return true;
    }
}
