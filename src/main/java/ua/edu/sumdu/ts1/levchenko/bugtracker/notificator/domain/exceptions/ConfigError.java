package ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.domain.exceptions;

public class ConfigError extends Error {
    public ConfigError(String message) {
        super(message);
    }
}
