package ua.edu.sumdu.ts1.levchenko.bugtracker.notificator;

import ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.domain.Config;
import ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.domain.EmailNotificator;

public class Main {
    public static void main(String[] args) {
        String configFilename = args.length == 2 ? args[1] : "config.properties";
        Config config = new Config(configFilename);
        EmailNotificator notificator = new EmailNotificator(config);
        notificator.sendNotifications();
    }
}
