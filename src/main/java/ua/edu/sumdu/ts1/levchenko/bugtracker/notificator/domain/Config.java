package ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.domain;

import ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.domain.exceptions.ConfigError;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class Config {
    private final static Logger log = Logger.getLogger(Config.class.getName());

    private String dbHost;
    private String dbPort;
    private String dbName;
    private String dbUsername;
    private String dbPassword;

    private String emailUsername;
    private String emailPassword;

    public Config(String filename) {
        log.info(String.format("Loading config from \"%s\"", filename));
        Properties configuration = loadConfigFromFile(filename);
        loadConfigFromProperties(configuration);
        log.fine("Configuration loaded!");
    }

    private Properties loadConfigFromFile(String filename) {
        Properties configuration = new Properties();

        try (InputStream fileInputStream = new FileInputStream(filename)) {
            configuration.load(fileInputStream);
        } catch (FileNotFoundException e) {
            String message = String.format("Config file \"%s\" not found!", filename);
            log.severe(message);
            ConfigError error = new ConfigError(message);
            error.addSuppressed(e);
            throw error;
        } catch (IOException e) {
            String message = String.format("Error reading config file \"%s\": %s", filename, e.getMessage());
            log.severe(message);
            ConfigError error = new ConfigError(message);
            error.addSuppressed(e);
            throw error;
        }

        return configuration;
    }

    private void loadConfigFromProperties(Properties config) {
        dbHost = config.getProperty("db.host");
        dbPort = config.getProperty("db.port");
        dbName = config.getProperty("db.name");
        dbUsername = config.getProperty("db.username");
        dbPassword = config.getProperty("db.password");

        emailUsername = config.getProperty("email.username");
        emailPassword = config.getProperty("email.password");
    }

    public String getDbHost() {
        return dbHost;
    }

    public String getDbPort() {
        return dbPort;
    }

    public String getDbName() {
        return dbName;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getEmailUsername() {
        return emailUsername;
    }

    public String getEmailPassword() {
        return emailPassword;
    }
}
