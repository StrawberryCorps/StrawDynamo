package bzh.strawberry.dynamo.network.handler;

import bzh.strawberry.dynamo.Dynamo;
import bzh.strawberry.dynamo.client.DynamoClient;
import bzh.strawberry.dynamo.strategy.BalancingStrategy;
import bzh.strawberry.dynamo.utils.ProxyData;
import bzh.strawberry.dynamo.utils.SocketUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Crée par Eclixal
 * Le 09/05/2018.
 */
public class UpstreamHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private DynamoClient dynamoClient;
    private BalancingStrategy strategy;
    private Channel upstreamChannel;
    private Channel downstreamChannel;
    private boolean downstreamConnected;
    private DownstreamHandler downstreamHandler;
    private long upstreamBytesIn;
    private long downstreamBytesOut;
    private List<ByteBuf> initialPackets = new ArrayList<>();

    public UpstreamHandler(BalancingStrategy strategy) {
        this.strategy = strategy;
    }

    public void connectDownstream(ByteBuf initPacket) {
        InetSocketAddress address = (InetSocketAddress) upstreamChannel.remoteAddress();
        ProxyData proxyData = this.strategy.selectTarget(address.getHostName(), address.getPort());

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(upstreamChannel.eventLoop())
                .channel(upstreamChannel.getClass())
                .option(ChannelOption.SO_TIMEOUT, 5000)
                .handler(downstreamHandler = new DownstreamHandler(dynamoClient, upstreamChannel));


        ChannelFuture f = bootstrap.connect(proxyData.getHost(), proxyData.getPort());
        downstreamChannel = f.channel();

        initialPackets.add(initPacket);

        f.addListener((future) -> {
            if (future.isSuccess()) {
                downstreamConnected = true;

                for (ByteBuf packet : initialPackets) {
                    downstreamChannel.writeAndFlush(packet);
                }

               Dynamo.getLogger().log(Level.INFO, "Proxy", "[" + dynamoClient.getName() + "] <-> [Dynamo] <-> [" + proxyData.getName() + "] pont de connexion lancé !");
            } else {
                future.cause().printStackTrace();
                upstreamChannel.close();
            }
        });
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        upstreamChannel = ctx.channel();
        upstreamChannel.read();
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        if(downstreamConnected) {
            System.out.println("messageReceived");
            downstreamChannel.writeAndFlush(byteBuf.retain()).addListener((future) -> {
                if(future.isSuccess())
                    ctx.channel().read();
                else {
                    System.out.println("CLOSE CHANNEL !");
                    ctx.channel().close();
                }
            });
        } else
            ctx.channel().read();

        upstreamBytesIn += byteBuf.readableBytes();
        downstreamBytesOut += byteBuf.readableBytes();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (downstreamChannel != null) {
            if (downstreamChannel.isActive()) {
                System.out.println("CRASH !");

                downstreamChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            }

            if(dynamoClient != null)
                Dynamo.getDynamo().removeClient(dynamoClient);

            upstreamBytesIn = 0;
            downstreamBytesOut = 0;

            Dynamo.getLogger().info("[" + ((dynamoClient != null) ? dynamoClient.getName() : SocketUtils.formatSocketAddress(upstreamChannel.remoteAddress())) + "] -> Deconnexion");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Channel ch = ctx.channel();
        if (ch.isActive()) {
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }

    public void addInitialPacket(ByteBuf buf) {
        if(!downstreamConnected)
            initialPackets.add(buf);
    }

    public Channel getUpstreamChannel() {
        return upstreamChannel;
    }

    public DownstreamHandler getDownstreamHandler() {
        return downstreamHandler;
    }

    public void setClient(DynamoClient dynamoClient) {
        this.dynamoClient = dynamoClient;
    }

    public long getUpstreamBytesIn() {
        return upstreamBytesIn;
    }

    public long getDownstreamBytesOut() {
        return downstreamBytesOut;
    }

    public boolean isDownstreamConnected() {
        return downstreamConnected;
    }
}