package bzh.strawberry.dynamo.strategy;

import bzh.strawberry.dynamo.utils.ProxyData;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Crée par Eclixal
 * Le 09/05/2018.
 */
public class RoundRobinBalancingStrategy extends BalancingStrategy {

    private List<ProxyData> proxyData;

    private AtomicInteger currentTarget = new AtomicInteger(0);

    public RoundRobinBalancingStrategy(List<ProxyData> proxyData) {
        super(proxyData);
        this.proxyData = proxyData;
    }

    @Override
    public synchronized ProxyData selectTarget(String originHost, int originPort) {
        int now = currentTarget.getAndIncrement();

        if (now >= proxyData.size()) {
            now = 0;
            currentTarget.set(0);
        }

        return proxyData.get(now);
    }

}