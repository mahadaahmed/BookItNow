package main.bookit.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Config {

    private static final Logger logger = LoggerFactory.getLogger(Config.class);
    private static Config instance;
    private final Properties properties;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            // Handle the exception, possibly rethrow or log it
            logger.error("PostgreSQL Driver not found", e);
            throw new ExceptionInInitializerError(e);
        }
    }

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
            logger.error("Could not load configuration file, error", ex);
            System.err.println("Could not load configuration file: " + ex.getMessage());
            // Handle error - maybe load default properties or terminate the application
        }
    }

    public Connection getConnection() throws SQLException {

        return DriverManager.getConnection(properties.getProperty("db.url"), properties.getProperty("db.user"), properties.getProperty("db.pass"));
    }

}
