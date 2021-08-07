package bzh.strawberry.dynamo.utils;

import java.io.IOException;
import java.net.Socket;

/**
 * Crée par Eclixal
 * Le 09/05/2018.
 */
public class ProxyData {

    private String name;
    private String host;
    private int port;

    public ProxyData(int port) {
        this.host = "0.0.0.0";
        this.port = port;
    }

    public ProxyData(String name, String host, int port) {
        this.host = host;
        this.port = port;
        this.name = name;
    }

    public boolean isAvailable() {
        try (Socket s = new Socket(host, port)) {
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}