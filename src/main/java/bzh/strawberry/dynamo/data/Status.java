package bzh.strawberry.dynamo.data;

/**
 * Crée par Eclixal
 * Le 09/05/2018.
 */
public class Status {

    private Version version;
    private Players players;
    private String description = "§cEcliServ! Le meilleur serveur au monde !\n";

    public Status(String description, Players players, Version version) {
        this.description += description;
        this.players = players;
        this.version = version;
    }

    public String getDescription() {
        return description.replace("&", "\\u00A7").replace("§", "\\u00A7");
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Players getPlayers() {
        return players;
    }

    public void setPlayers(Players players) {
        this.players = players;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }
}