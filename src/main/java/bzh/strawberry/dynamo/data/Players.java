package bzh.strawberry.dynamo.data;

/**
 * Crée par Eclixal
 * Le 09/05/2018.
 */
public class Players {

    private String max;
    private String online;

    public Players(String max) {
        this.max = max;
        this.online = "%online%";
    }

    public String getMax() {
        return max;
    }

    public String getOnline() {
        return online;
    }
}