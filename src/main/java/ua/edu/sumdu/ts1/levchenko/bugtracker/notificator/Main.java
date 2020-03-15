package ua.edu.sumdu.ts1.levchenko.bugtracker.notificator;

import ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.domain.Config;
import ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.domain.DataSourceProvider;
import ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.domain.Notificator;

public class Main {
    public static void main(String[] args) {
        String configFilename = args.length > 0 ? args[0] : "config.properties";
        Config config = new Config(configFilename);
        DataSourceProvider dataSourceProvider = new DataSourceProvider(config);
        Notificator notificator = new Notificator(config, dataSourceProvider);
        notificator.sendNotifications();
    }
}
