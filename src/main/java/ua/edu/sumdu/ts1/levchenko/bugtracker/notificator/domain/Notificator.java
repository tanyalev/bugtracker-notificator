package ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.domain;

public class EmailNotificator {
    private Config config;
    private DataSourceProvider dataSourceProvider;

    public EmailNotificator(Config config, DataSourceProvider dataSourceProvider) {
        this.config = config;
        this.dataSourceProvider = dataSourceProvider;
    }

    public void sendNotifications() {

    }
}
