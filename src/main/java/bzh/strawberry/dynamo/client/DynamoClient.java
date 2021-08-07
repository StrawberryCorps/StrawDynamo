package bzh.strawberry.dynamo.client;

import bzh.strawberry.dynamo.network.handler.DownstreamHandler;
import bzh.strawberry.dynamo.network.handler.UpstreamHandler;

/**
 * Crée par Eclixal
 * Le 09/05/2018.
 */
public class DynamoClient {
    private String name;

    private DownstreamHandler downstreamHandler;

    private UpstreamHandler upstreamHandler;

    public DynamoClient(String name, DownstreamHandler downstreamHandler, UpstreamHandler upstreamHandler) {
        this.name = name;
        this.downstreamHandler = downstreamHandler;
        this.upstreamHandler = upstreamHandler;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DownstreamHandler getDownstreamHandler() {
        return downstreamHandler;
    }

    public UpstreamHandler getUpstreamHandler() {
        return upstreamHandler;
    }

    public long getUpstreamBytesIn() {
        return upstreamHandler.getUpstreamBytesIn();
    }

    public long getDownstreamBytesOut() {
        return upstreamHandler.getDownstreamBytesOut();
    }

    public long getUpstreamBytesOut() {
        return downstreamHandler.getUpstreamBytesOut();
    }

    public long getDownstreamBytesIn() {
        return downstreamHandler.getDownstreamBytesIn();
    }
}