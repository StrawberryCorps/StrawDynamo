package bzh.strawberry.dynamo.strategy;

import bzh.strawberry.dynamo.utils.ProxyData;

import java.util.Collections;
import java.util.List;

/**
 * Crée par Eclixal
 * Le 09/05/2018.
 */
public abstract class BalancingStrategy {

    private List<ProxyData> proxyData;

    public BalancingStrategy(List<ProxyData> proxyData) {
        this.proxyData = Collections.synchronizedList(proxyData);
    }

    public abstract ProxyData selectTarget(String originHost, int originPort);

    public synchronized void addTarget(ProxyData proxyData) {
        this.proxyData.add(proxyData);
    }

    public List<ProxyData> getTargets() {
        return proxyData;
    }
}