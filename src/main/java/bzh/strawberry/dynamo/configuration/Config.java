package bzh.strawberry.dynamo.configuration;

import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Crée par Eclixal
 * Le 09/05/2018.
 */
public class Config {

    public static DynamoConfig loadConfig(String path) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("config.conf"));
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(System.lineSeparator());
        }

        return new GsonBuilder().setPrettyPrinting().create().fromJson(stringBuilder.toString(), DynamoConfig.class);
    }
}