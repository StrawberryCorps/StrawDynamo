package bzh.strawberry.dynamo.logger;

import jline.console.ConsoleReader;
import org.fusesource.jansi.Ansi;

import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Crée par Eclixal
 * Le 09/05/2018.
 */
public class LogWriter extends Handler {

    private ConsoleReader console;

    public LogWriter(ConsoleReader console) {
        this.console = console;
    }

    private void println(String line) {
        try {
            console.print(ConsoleReader.RESET_LINE + line.replaceAll("\\p{C}", "") + Ansi.ansi().reset().toString() + "\n\r");
            console.drawLine();
            console.flush();
        } catch ( IOException ex ) {

        }
    }

    @Override
    public void publish(LogRecord record) {
        if(isLoggable(record)) {
            println(getFormatter().format(record));
        }
    }

    @Override
    public void flush() {}

    @Override
    public void close() throws SecurityException {}
}