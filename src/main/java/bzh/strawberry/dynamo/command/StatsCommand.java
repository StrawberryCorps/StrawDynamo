package bzh.strawberry.dynamo.command;

import bzh.strawberry.dynamo.Dynamo;
import bzh.strawberry.dynamo.client.DynamoClient;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Crée par Eclixal
 * Le 11/05/2018.
 */
public class StatsCommand  extends Command {

    public StatsCommand(String name, String[] aliases, String description) {
        super(name, aliases, description);
    }

    @Override
    public boolean execute(String[] args) {
        List<DynamoClient> dynamoClients = Dynamo.getDynamo().getDynamoClients();

        long totalUpstreamBytesIn = 0;
        long totalDownstreamBytesOut = 0;
        long totalUpstreamBytesOut = 0;
        long totalDownstreamBytesIn = 0;

        for (DynamoClient dynamoClient : dynamoClients) {
            totalUpstreamBytesIn += dynamoClient.getUpstreamBytesIn();
            totalDownstreamBytesOut += dynamoClient.getDownstreamBytesOut();
            totalUpstreamBytesOut += dynamoClient.getUpstreamBytesOut();
            totalDownstreamBytesIn += dynamoClient.getDownstreamBytesIn();
        }

        Dynamo.getLogger().info("---------- Statistiques ----------");
        Dynamo.getLogger().info("Joueurs connectées: " + dynamoClients.size());
        Dynamo.getLogger().info("Proxy: " + Dynamo.getDynamo().getBalancingStrategy().getTargets().size());
        Dynamo.getLogger().info("Total Upstream IN: " + convertBytes(totalUpstreamBytesIn));
        Dynamo.getLogger().info("Total Upstream OUT: " + convertBytes(totalUpstreamBytesOut));
        Dynamo.getLogger().info("Total Downstream IN: " + convertBytes(totalDownstreamBytesIn));
        Dynamo.getLogger().info("Total Downstream OUT: " + convertBytes(totalDownstreamBytesOut));
        Dynamo.getLogger().info("-----------------------------------");
        return true;
    }

    private String convertBytes(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}