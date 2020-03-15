package ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.dao;

import ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.models.Issue;
import ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.models.Project;
import ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.models.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class IssueDao {
    private final static Logger log = Logger.getLogger(IssueDao.class.getName());

    private final DataSource dataSource;

    private final String SELECT_TABLE_INFO_ISSUES =
            "select issue_id, summary, name, (project_id || '-' || CAST ( number as VARCHAR)) as number from \"BugTracker\".\"Issue\"";
    private final String SELECT_ISSUE_BY_ID =
            "select issue.issue_id as issue_id, issue.status_id, issue.summary as summary, issue.description as description, issue.priority_id as priority_id, issue.type_id as type_id, issue.number as number, " +
                    "buser.user_id as assignee_id, buser.name as assignee_name, " +
                    "b2user.user_id as author_id, b2user.name as author_name, " +
                    "project.project_id as project_id, project.name as project_name " +
                    "from \"BugTracker\".\"Issue\" issue " +
                    "inner join \"BugTracker\".\"User\" buser on issue.assignee_id = buser.user_id " +
                    "inner join \"BugTracker\".\"User\" b2user on issue.author_id = b2user.user_id " +
                    "inner join \"BugTracker\".\"Project\" project on issue.project_id = project.project_id " +
                    "where issue.project_id = ? and issue.number = ?";

    public IssueDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private boolean noneIsEmpty(String... s) {
        for (String s1 : s) {
            if (!s1.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private String[] splitId(String id) throws Exception {
        String[] parts = id.split("-");
        if (parts.length < 2) {
            throw new Exception("Failed to split id");
        }
        return parts;
    }

    public List<Issue> getAllIssues() {
        ArrayList<Issue> issues = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_TABLE_INFO_ISSUES)) {
            while (resultSet.next()) {
                String issueId = resultSet.getString("issue_id");
                String summary = resultSet.getString("summary");
                String number = resultSet.getString("number");
                if (noneIsEmpty(issueId, summary, number)) {
                    issues.add(new Issue(issueId, summary, number));
                }
            }
        } catch (SQLException e) {
            log.severe(String.format("Error getting issues: %s", e.getMessage()));
        }

        return issues;
    }

    public Issue getIssueByID(String id) throws Exception {
        List<Issue> issues = new ArrayList<>();

        String[] parts = splitId(id);
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ISSUE_BY_ID)) {
            statement.setString(1, parts[0]);
            statement.setString(2, parts[1]);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String statusId = resultSet.getString("status_id");
                    String typeId = resultSet.getString("type_id");
                    String priorityId = resultSet.getString("priority_id");
                    String issueId = resultSet.getString("issue_id");
                    String summary = resultSet.getString("summary");
                    String number = resultSet.getString("number");
                    String description = resultSet.getString("description");

                    String projectId = resultSet.getString("project_id");
                    String projectName = resultSet.getString("project_name");

                    String assigneeId = resultSet.getString("assignee_id");
                    String assigneeName = resultSet.getString("assignee_name");

                    String authorId = resultSet.getString("author_id");
                    String authorName = resultSet.getString("author_name");

                    boolean issueIsOk = noneIsEmpty(statusId, typeId, priorityId, issueId, summary, number, description);
                    boolean projectIsOk = noneIsEmpty(projectId, projectName);
                    boolean assigneeIsOk = noneIsEmpty(assigneeId, assigneeName);
                    boolean authorIsOk = noneIsEmpty(authorId, authorName);

                    if (issueIsOk && projectIsOk && assigneeIsOk && authorIsOk) {
                        Project project = new Project(projectId, projectName);
                        User assignee = new User(assigneeId, assigneeName);
                        User author = new User(authorId, assigneeName);
                        Issue issue = new Issue(issueId, summary, description, priorityId, typeId, statusId,
                                projectId + "-" + number, project, assignee, author);
                        issues.add(issue);
                    }
                }
            }
        } catch (SQLException e) {
            log.severe(String.format("Error getting issue by id: %s", e.getMessage()));
        }

        return issues.get(0);
    }
}