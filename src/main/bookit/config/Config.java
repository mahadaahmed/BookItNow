package main.bookit.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static Config instance;
    private final Properties properties;

    private Config() {
        properties = new Properties();
        loadProperties();
    }

    public static synchronized Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                System.err.println("Unable to find config.properties");
                // Handle error - maybe load default properties or terminate the application
            }
        } catch (IOException ex) {
            System.err.println("Could not load configuration file: " + ex.getMessage());
            // Handle error - maybe load default properties or terminate the application
        }
    }

    public String getDbURL() {
        return properties.getProperty("db.url", System.getenv("DB_URL"));
    }

    public String getDbUSER() {
        return properties.getProperty("db.user", System.getenv("DB_USER"));
    }

    public String getDbPASS() {
        return properties.getProperty("db.pass", System.getenv("DB_PASS"));
    }
}
