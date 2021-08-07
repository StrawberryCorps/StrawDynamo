package bzh.strawberry.dynamo.configuration;

import bzh.strawberry.dynamo.utils.ProxyData;

import java.util.ArrayList;
import java.util.List;

/**
 * Crée par Eclixal
 * Le 09/05/2018.
 */
public class DynamoConfig {

    private String host;
    private int port;
    private int threads;
    private int backlog;
    private List<ProxyData> proxyData = new ArrayList<>();

    public DynamoConfig(String host, int port, int threads, int backlog, List<ProxyData> proxyData) {
        this.host = host;
        this.port = port;
        this.threads = threads;
        this.backlog = backlog;
        this.proxyData = proxyData;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getThreads() {
        return threads;
    }

    public int getBacklog() {
        return backlog;
    }

    public List<ProxyData> getProxyDatas() {
        return proxyData;
    }
}