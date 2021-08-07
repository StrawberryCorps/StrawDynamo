package bzh.strawberry.dynamo.network.handler;

import bzh.strawberry.dynamo.Dynamo;
import bzh.strawberry.dynamo.client.DynamoClient;
import bzh.strawberry.dynamo.protocol.Protocol;
import bzh.strawberry.dynamo.protocol.ProtocolState;
import bzh.strawberry.dynamo.protocol.Protocols;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;
import java.util.logging.Level;

/**
 * Crée par Eclixal
 * Le 09/05/2018.
 */
public class HandshakeHandler extends MessageToMessageDecoder<ByteBuf> {

    private UpstreamHandler upstreamHandler;
    private ProtocolState protocolMode = ProtocolState.HANDSHAKE;
    private boolean downstreamInitialized = false;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) {
        ByteBuf copy = byteBuf.copy();
        int protocolClient = 47;
        System.out.println(Protocol.readVarInt(byteBuf));

        int packetId = Protocol.readVarInt(byteBuf);
        if(packetId == 0) {
            if(protocolMode == ProtocolState.HANDSHAKE) {
                protocolClient = Protocol.readVarInt(byteBuf);
                String host = Protocol.readString(byteBuf);
                System.out.println(host + " / " + protocolClient);
                byteBuf.readUnsignedShort();
                protocolMode = ProtocolState.valueOf((byte) Protocol.readVarInt(byteBuf));
            }

            if(protocolMode == ProtocolState.STATUS) {
                ByteBuf responseBuffer = Unpooled.buffer();
                String response = Dynamo.getDynamo().getStatusMessage();
                response = response.replace("%protocolUser%", String.valueOf((Protocols.SUPPORTED_VERSION.contains(protocolClient)) ? protocolClient : 47)).replace("%online%", "" + Dynamo.getDynamo().getClientsOnline());
                Protocol.writeVarInt(3 + response.length(), responseBuffer);
                Protocol.writeVarInt(0, responseBuffer);
                Protocol.writeString(response, responseBuffer);
                channelHandlerContext.writeAndFlush(responseBuffer);

                ByteBuf pongBuffer = Unpooled.buffer();
                Protocol.writeVarInt(9, pongBuffer);
                Protocol.writeVarInt(1, pongBuffer);
                pongBuffer.writeLong(0);
                channelHandlerContext.writeAndFlush(pongBuffer);
                channelHandlerContext.close();
            }

            if(protocolMode == ProtocolState.LOGIN) {
                if(byteBuf.readableBytes() == 0) {
                    upstreamHandler.connectDownstream(copy);
                    downstreamInitialized = true;
                    out.add(copy.retain());
                    return;
                }

                String name = Protocol.readString(byteBuf);

                if(!downstreamInitialized)
                    upstreamHandler.connectDownstream(copy);
                else
                    upstreamHandler.addInitialPacket(copy);

                DynamoClient dynamoClient = new DynamoClient(name, upstreamHandler.getDownstreamHandler(), upstreamHandler);
                Dynamo.getDynamo().addClient(dynamoClient);
                upstreamHandler.setClient(dynamoClient);

                channelHandlerContext.channel().pipeline().remove(this);

                Dynamo.getLogger().log(Level.INFO, "Handshake", "Connection de: " + name);
                out.add(copy.retain());
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Channel ch = ctx.channel();
        if (ch.isActive()) {
            cause.printStackTrace();
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }

    public void setUpstreamHandler(UpstreamHandler upstreamHandler) {
        this.upstreamHandler = upstreamHandler;
    }
}