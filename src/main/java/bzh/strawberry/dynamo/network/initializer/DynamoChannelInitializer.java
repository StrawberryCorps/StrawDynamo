package bzh.strawberry.dynamo.network.initializer;

import bzh.strawberry.dynamo.Dynamo;
import bzh.strawberry.dynamo.network.handler.HandshakeHandler;
import bzh.strawberry.dynamo.network.handler.UpstreamHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import java.net.InetSocketAddress;
import java.util.logging.Level;

/**
 * Crée par Eclixal
 * Le 09/05/2018.
 */
public class DynamoChannelInitializer extends ChannelInitializer<SocketChannel> {

    private Dynamo dynamo;

    public DynamoChannelInitializer() {
        this.dynamo = Dynamo.getDynamo();
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        InetSocketAddress remoteAddress = ch.remoteAddress();

        HandshakeHandler handshakeHandler = new HandshakeHandler();
        p.addFirst(handshakeHandler);
        UpstreamHandler upstreamHandler = new UpstreamHandler(Dynamo.getDynamo().getBalancingStrategy());
        p.addLast(upstreamHandler);

        handshakeHandler.setUpstreamHandler(upstreamHandler);
        Dynamo.getLogger().log(Level.INFO, "Dynamo", "[" + remoteAddress.getAddress().getHostAddress() + "] -> Initialize connection");
    }
}