package bzh.strawberry.dynamo.task;

import bzh.strawberry.dynamo.utils.ProxyData;
import bzh.strawberry.dynamo.Dynamo;
import bzh.strawberry.dynamo.strategy.BalancingStrategy;

import java.util.*;
import java.util.logging.Level;

/**
 * Crée par Eclixal
 * Le 09/05/2018.
 */
public class ProxyCheck extends TimerTask {

    private BalancingStrategy balancingStrategy;

    private List<ProxyData> offlineProxys = Collections.synchronizedList(new ArrayList<>());

    public ProxyCheck(BalancingStrategy balancingStrategy) {
        this.balancingStrategy = balancingStrategy;
    }

    @Override
    public void run() {
        Iterator<ProxyData> iterator = balancingStrategy.getTargets().iterator();
        while (iterator.hasNext()) {
            ProxyData proxyData = iterator.next();
            if(!proxyData.isAvailable()) {
                iterator.remove();
                offlineProxys.add(proxyData);
                Dynamo.getLogger().log(Level.INFO,"ProxyKeepAlive", proxyData.getName() + " ne répond plus depuis 1000ms !");
            }
        }

        Iterator<ProxyData> offlineIterator = offlineProxys.iterator();
        while (offlineIterator.hasNext()) {
            ProxyData offlineTargetData = offlineIterator.next();
            if(offlineTargetData.isAvailable()) {
                offlineIterator.remove();
                balancingStrategy.addTarget(offlineTargetData);
                Dynamo.getLogger().log(Level.INFO,"ProxyKeepAlive", offlineTargetData.getName() + " répond de nouveau !");
            }
        }
    }
}