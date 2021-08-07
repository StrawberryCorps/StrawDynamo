package bzh.strawberry.dynamo.protocol;

import java.util.Arrays;
import java.util.List;

/**
 * Crée par Eclixal
 * Le 09/05/2018.
 */
public class Protocols {
    public static final int MINECRAFT_1_8 = 47;
    public static final int MINECRAFT_1_9 = 107;
    public static final int MINECRAFT_1_9_1 = 108;
    public static final int MINECRAFT_1_9_2 = 109;
    public static final int MINECRAFT_1_9_4 = 110;
    public static final int MINECRAFT_1_10 = 210;
    public static final int MINECRAFT_1_11 = 315;
    public static final int MINECRAFT_1_11_1 = 316;
    public static final int MINECRAFT_1_12 = 335;
    public static final int MINECRAFT_1_12_1 = 338;
    public static final int MINECRAFT_1_12_2 = 340;
    public static final int MINECRAFT_1_13 = 393;
    public static final int MINECRAFT_1_13_1 = 401;
    public static final int MINECRAFT_1_13_2 = 404;
    public static final int MINECRAFT_1_14_2 = 485;
    public static final int MINECRAFT_1_15_2 = 578;
    public static final int MINECRAFT_1_16_4 = 754;
    public static final int MINECRAFT_1_16_5 = 754;

    public static final List<Integer> SUPPORTED_VERSION = Arrays.asList(
            Protocols.MINECRAFT_1_16_4,
            Protocols.MINECRAFT_1_16_5
    );
}
