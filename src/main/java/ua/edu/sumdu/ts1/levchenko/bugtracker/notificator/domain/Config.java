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
    private int dbPort;
    private String dbName;
    private String dbUsername;
    private String dbPassword;

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

    }

    public String getDbHost() {
        return dbHost;
    }

    public int getDbPort() {
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
}
