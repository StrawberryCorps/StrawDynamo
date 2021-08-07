package bzh.strawberry.dynamo.network.handler;

import bzh.strawberry.dynamo.client.DynamoClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Crée par Eclixal
 * Le 09/05/2018.
 */
public class DownstreamHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private DynamoClient dynamoClient;
    private Channel upstreamChannel;
    private long upstreamBytesOut;
    private long downstreamBytesIn;

    public DownstreamHandler(DynamoClient dynamoClient, Channel upstreamChannel) {
        this.dynamoClient = dynamoClient;
        this.upstreamChannel = upstreamChannel;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.read();
        ctx.write(Unpooled.EMPTY_BUFFER);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        upstreamChannel.writeAndFlush(byteBuf.retain()).addListener((future) -> {
            if(future.isSuccess())
                ctx.channel().read();
            else
                ctx.channel().close();
        });

        upstreamBytesOut += byteBuf.readableBytes();
        downstreamBytesIn += byteBuf.readableBytes();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (upstreamChannel != null) {
            if (upstreamChannel.isActive())
                upstreamChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            upstreamBytesOut = 0;
            downstreamBytesIn = 0;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Channel ch = ctx.channel();
        if (ch.isActive())
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    public long getUpstreamBytesOut() {
        return upstreamBytesOut;
    }

    public long getDownstreamBytesIn() {
        return downstreamBytesIn;
    }
}