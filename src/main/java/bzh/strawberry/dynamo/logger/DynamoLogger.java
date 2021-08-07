package bzh.strawberry.dynamo.logger;

import jline.console.ConsoleReader;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Crée par Eclixal
 * Le 09/05/2018.
 */
public class DynamoLogger extends Logger {

    private final LogFormatter formatter = new LogFormatter();
    private final LogDispatcher dispatcher = new LogDispatcher(this);

    public DynamoLogger(ConsoleReader console) {
        super("Dynamo", null);
        try {
            LogWriter consoleHandler = new LogWriter(console);
            consoleHandler.setLevel(Level.ALL);
            consoleHandler.setFormatter(formatter);
            addHandler(consoleHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dispatcher.start();
    }

    @Override
    public void log(LogRecord record) {
        dispatcher.queue(record);
    }

    public void realLog(LogRecord logRecord) {
        super.log(logRecord);
    }
}