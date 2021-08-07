package bzh.strawberry.dynamo.database;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * Crée par Eclixal
 * Le 24/10/2017.
 */
public class JedisManager {

    private final String host;
    private final int port;
    private final String password;
    private final String user;
    private JedisPool jedisPool;

    public JedisManager(String host, int port, String password, String user) {
        this.host = host;
        this.port = port;
        this.password = password;
        this.user = user;
    }

    private Jedis init() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(1024);

        jedisPool = new JedisPool(config, host, port, 5000, password);
        return jedisPool.getResource();
    }

    public Jedis getRessource() {
        return this.init();
    }

    public void close() {
        if (jedisPool != null) {
            jedisPool.close();
            jedisPool.destroy();
        }
    }

    public void closeChannel(Jedis jedis) {
        if (jedis != null) jedis.close();
    }

    public boolean ping() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(1024);
        config.setMaxWaitMillis(5000);
        jedisPool = new JedisPool(config, host, port, 5000, password);
        try {
            Jedis jedis = jedisPool.getResource();
            jedis.close();
            jedisPool.close();
            jedisPool.destroy();
            return true;
        } catch (JedisConnectionException e) {
            return false;
        }
    }

}