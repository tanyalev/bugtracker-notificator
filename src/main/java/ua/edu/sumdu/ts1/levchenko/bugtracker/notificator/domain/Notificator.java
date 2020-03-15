package ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.domain;

import ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.dao.IssueDao;
import ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.dao.UserDao;
import ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.models.Email;
import ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.models.Issue;
import ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.models.User;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

public class Notificator {
    private Config config;
    private EmailSender emailSender;

    private IssueDao issueDao;
    private UserDao userDao;

    private List<User> users;
    private List<Issue> issues;

    public Notificator(Config config, DataSourceProvider dataSourceProvider) {
        this.config = config;
        this.emailSender = new EmailSender(config.getEmailUsername(), config.getEmailPassword());

        DataSource dataSource = dataSourceProvider.provideDataSource();
        this.issueDao = new IssueDao(dataSource);
        this.userDao = new UserDao(dataSource);
    }

    private void updateUser(User user) {
        User fullUser = userDao.getUserById(user.getUserId());
        user.setName(fullUser.getName());
        user.setLastName(fullUser.getLastName());
        user.setEmail(fullUser.getEmail());
        user.setLoginName(fullUser.getLoginName());
        user.setPassword(fullUser.getPassword());
        user.setRoleId(fullUser.getRoleId());
    }

    private User getUserInfo(String userId) {
        return users.parallelStream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst()
                .orElseGet(null);
    }

    private void updateIssueUsers(Issue issue) {
        Issue fullIssue = issueDao.getIssueByID(issue.getId());
        if (Objects.nonNull(fullIssue)) {
            issue.setAuthor(getUserInfo(fullIssue.getAuthor().getUserId()));
            issue.setAssignee(getUserInfo(fullIssue.getAssignee().getUserId()));
        }
    }

    private String generateIssueReport(Issue issue) {
        return String.format("---\n#%s [%s]\n%s\nAuthor: %s, Assignee: %s, Project: %s.\n---\n",
                issue.getNumber(), issue.getSummary(), issue.getDescription(),
                issue.getAuthor().getName(), issue.getAssignee().getName(), issue.getProject().getProjectName());
    }

    private Email generateEmail(User user) {
        String reports = issues.parallelStream()
                .filter(issue ->
                        issue.getAuthor().getUserId().equals(user.getUserId()) ||
                        issue.getAssignee().getUserId().equals(user.getUserId()))
                .map(this::generateIssueReport)
                .collect(Collectors.joining());
        String text = String.format("Dear %s, there is your issues reports:\n %s", user.getName(), reports);
        return new Email("REPORT", text, config.getEmailUsername(), user.getEmail());
    }

    public void sendNotifications() {
        users = userDao.getAllUsers();
        issues = issueDao.getAllIssues();

        users.parallelStream().forEach(this::updateUser);
        issues.parallelStream().forEach(this::updateIssueUsers);

        users.parallelStream().map(this::generateEmail).forEach(email -> emailSender.send(email));
    }
}
