package ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.dao;

import ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.models.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.dao.Util.noneIsEmpty;

public class UserDao {
    private final static Logger log = Logger.getLogger(UserDao.class.getName());

    private DataSource dataSource;

    private final String SELECT_TABLE_USERS = "select name, user_id from \"BugTracker\".\"User\"";
    private final String SELECT_USER_BY_ID =
            "select user_id, name, login, role_id, password from \"BugTracker\".\"User\" where user_id = ?";

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
                if (noneIsEmpty(userId, name)) {
                    users.add(new User(userId, name));
                }
            }
        } catch (SQLException e) {
            log.severe(String.format("Error fetching all users: %s", e.getMessage()));
        }

        return users;
    }

    public User getUserById(String id) {
        List<User> users = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID)) {
            preparedStatement.setObject(1, UUID.fromString(id));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String userId = resultSet.getString("user_id");
                    String name = resultSet.getString("name");
                    if (noneIsEmpty(userId, name)) {
                        users.add(new User(userId, name));
                    }
                }
            }
        } catch (SQLException e) {
            log.severe(String.format("Error fetching user by id: %s", e.getMessage()));
        }

        return users.get(0);
    }

}
