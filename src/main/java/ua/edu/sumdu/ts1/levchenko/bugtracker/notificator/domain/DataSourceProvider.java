package ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.domain;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.logging.Logger;

public class DataSourceProvider {
    private final static Logger log = Logger.getLogger(DataSourceProvider.class.getName());

    private Config config;
    private PGSimpleDataSource dataSource;

    public DataSourceProvider(Config config) {
        this.config = config;
    }

    public DataSource provideDataSource() {
        if (Objects.nonNull(dataSource)) {
            return dataSource;
        }

        String serverName = String.format("%s:%s", config.getDbHost(), config.getDbPort());

        dataSource = new PGSimpleDataSource();
        dataSource.setServerName(serverName);
        dataSource.setUser(config.getDbUsername());
        dataSource.setPassword(config.getDbPassword());
        dataSource.setDatabaseName(config.getDbName());

        log.fine("Data source successfully created!");

        return dataSource;
    }
}
