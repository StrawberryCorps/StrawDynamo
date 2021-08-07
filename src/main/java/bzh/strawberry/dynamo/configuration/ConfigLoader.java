package bzh.strawberry.dynamo.configuration;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Crée par Eclixal
 * Le 09/05/2018.
 */
public class ConfigLoader {
    private static final Gson GSON = new Gson();

    private static final String LINE_SEPARATOR = System.lineSeparator();

    public static <T> T loadConfig(String name, Class<T> type) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(name));
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(LINE_SEPARATOR);
        }

        return GSON.fromJson(stringBuilder.toString(), type);
    }
}