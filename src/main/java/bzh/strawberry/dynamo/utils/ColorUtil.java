package bzh.strawberry.dynamo.utils;

/**
 * Crée par Eclixal
 * Le 09/05/2018.
 */
public enum  ColorUtil {

    BLACK( '0', "\\u00A70" ),
    DARK_BLUE( '1', "\\u00A71" ),
    DARK_GREEN( '2', "\\u00A72" ),
    DARK_AQUA( '3', "\\u00A73" ),
    DARK_RED( '4', "\\u00A74" ),
    DARK_PURPLE( '5', "\\u00A75" ),
    GOLD( '6', "\\u00A76" ),
    GRAY( '7', "\\u00A77" ),
    DARK_GRAY( '8', "\\u00A78" ),
    BLUE( '9', "\\u00A79" ),
    GREEN( 'a', "\\u00A7a" ),
    AQUA( 'b', "\\u00A7b" ),
    RED( 'c', "\\u00A7c" ),
    LIGHT_PURPLE( 'd', "\\u00A7d" ),
    YELLOW( 'e', "\\u00A7e" ),
    WHITE( 'f', "\\u00A7f" ),
    MAGIC( 'k', "\\u00A7k" ),
    BOLD( 'l', "\\u00A7l" ),
    STRIKETHROUGH( 'm', "\\u00A7m" ),
    UNDERLINE( 'n', "\\u00A7n" ),
    ITALIC( 'o', "\\u00A7o" ),
    RESET( 'r', "\\u00A7r" );

    private char code;
    private String color;

    ColorUtil(char code, String color) {
        this.code = code;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

}