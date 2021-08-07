package bzh.strawberry.dynamo.command;

/**
 * Crée par Eclixal
 * Le 09/05/2018.
 */
public abstract class Command {

    private String name;
    private String[] aliases;
    private String description;

    public Command(String name, String[] aliases, String description) {
        this.name = name;
        this.aliases = aliases;
        this.description = description;
    }

    public boolean isValidAlias(String cmd) {
        for (String alias : aliases) {
            if(alias.equals(cmd))
                return true;
        }
        return false;
    }

    public abstract boolean execute(String[] args);

    public String getName() {
        return name;
    }

    public String[] getAliases() {
        return aliases;
    }

    public String getDescription() {
        return description;
    }
}