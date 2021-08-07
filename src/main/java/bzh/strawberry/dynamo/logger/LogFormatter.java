package bzh.strawberry.dynamo.logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Crée par Eclixal
 * Le 09/05/2018.
 */
public class LogFormatter extends Formatter {

    private DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Override
    public String format(LogRecord record) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(dateFormat.format(new Date()));
        stringBuilder.append(" [").append(record.getLevel()).append("]");
        if(record.getParameters() == null && record.getMessage() != "")
            stringBuilder.append(": ").append(record.getMessage());
        else
            stringBuilder.append(" [").append(record.getMessage()).append("]").append(": ").append(record.getParameters()[0]);

        return stringBuilder.toString();
    }

}