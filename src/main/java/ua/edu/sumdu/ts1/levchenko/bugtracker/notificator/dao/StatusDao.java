package ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.dao;

import ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.models.Status;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.dao.Util.noneIsEmpty;

public class StatusDao {
    private final static Logger log = Logger.getLogger(StatusDao.class.getName());

    private DataSource dataSource;

    private final String SELECT_TABLE_STATUSES = "select name, status_id from \"BugTracker\".\"Status\"";

    public StatusDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Status> getAllStatuses() {
        List<Status> statuses = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_TABLE_STATUSES)) {
            while (resultSet.next()) {
                String statusId = resultSet.getString("status_id");
                String name = resultSet.getString("name");
                if (noneIsEmpty(statusId, name)) {
                    statuses.add(new Status(statusId, name));
                }
            }
        } catch (SQLException e) {
            log.severe(String.format("Error fetching all statuses: %s", e.getMessage()));
        }

        return statuses;
    }
}
