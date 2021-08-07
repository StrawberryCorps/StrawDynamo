package bzh.strawberry.dynamo.protocol;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

/**
 * Crée par Eclixal
 * Le 09/05/2018.
 */
public class Protocol {
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    public static void writeString(String s, ByteBuf buf) {
        byte[] b = s.getBytes(UTF_8);
        writeVarInt( b.length, buf );
        buf.writeBytes( b );
    }

    public static String readString(ByteBuf buf) {
        int len = readVarInt(buf);
        byte[] b = new byte[len];
        buf.readBytes(b);
        return new String(b, UTF_8);
    }

    public static void writeVarInt(int value, ByteBuf output) {
        int part;
        while (true) {
            part = value & 0x7F;
            value >>>= 7;
            if ( value != 0 )
                part |= 0x80;

            output.writeByte(part);
            if (value == 0)
                break;
        }
    }

    public static int readVarInt(ByteBuf input) {
        return readVarInt(input, 5);
    }

    public static int readVarInt(ByteBuf input, int maxBytes) {
        int out = 0;
        int bytes = 0;
        byte in;
        while (true) {
            in = input.readByte();
            out |= (in & 0x7F) << (bytes++ * 7);
            if (bytes > maxBytes)
                throw new RuntimeException("VarInt too big");

            if ((in & 0x80) != 0x80)
                break;
        }

        return out;
    }
}
