package bzh.strawberry.dynamo;

import bzh.strawberry.dynamo.command.Command;

import java.util.logging.Level;

/**
 * Crée par Eclixal
 * Le 09/05/2018.
 */
public class Main {

    public static void main(String[] mainArgs) throws Exception {
        Dynamo dynamo = new Dynamo();
        new Thread(dynamo, "Dynamo").start();

        while (dynamo.isLaunch()) {
            String line = dynamo.getConsoleReader().readLine(">");
            if(line == "")
                continue;
            String[] splitted = line.split(" ");
            int length = splitted.length - 1;
            String[] args = new String[0];
            if(length > 1) {
                args = new String[length];
                System.arraycopy(splitted, 1, args, 0, length);
            }
            String name = splitted[0];
            Command command = dynamo.getCommandManager().findCommand(name);
            if(command != null)
                command.execute(args);
            else
                Dynamo.getLogger().log(Level.INFO, "Commande introuvable !");
        }
    }
}