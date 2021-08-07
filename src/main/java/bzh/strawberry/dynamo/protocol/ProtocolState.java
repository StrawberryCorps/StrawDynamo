package bzh.strawberry.dynamo.protocol;

/**
 * Crée par Eclixal
 * Le 09/05/2018.
 */
public enum ProtocolState {

    HANDSHAKE((byte)0), STATUS((byte)1), LOGIN((byte)2);

    private byte id;

    ProtocolState(byte id) {
        this.id = id;
    }

    public static ProtocolState valueOf(byte id) {
        return values()[id];
    }

    public byte getId() {
        return id;
    }
}