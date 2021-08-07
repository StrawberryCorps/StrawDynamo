package bzh.strawberry.dynamo.utils;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Crée par Eclixal
 * Le 10/05/2018.
 */
public class SocketUtils {

    public static String formatSocketAddress(SocketAddress address) {
        InetSocketAddress inetSocketAddress = (InetSocketAddress)address;
        return inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort();
    }
}