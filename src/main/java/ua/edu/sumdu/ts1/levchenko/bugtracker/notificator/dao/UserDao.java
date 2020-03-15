package ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.dao;

import ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.models.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.dao.Util.noneIsEmpty;

public class UserDao {
    private final static Logger log = Logger.getLogger(UserDao.class.getName());

    private DataSource dataSource;

    private final String SELECT_TABLE_USERS = "select name, user_id, email from \"BugTracker\".\"User\"";

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_TABLE_USERS)) {
            while (resultSet.next()) {
                String userId = resultSet.getString("user_id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                if (noneIsEmpty(userId, name, email)) {
                    users.add(new User(userId, name, email));
                }
            }
        } catch (SQLException e) {
            log.severe(String.format("Error fetching all users: %s", e.getMessage()));
        }

        return users;
    }
}
