package ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.domain;

import ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.dao.IssueDao;
import ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.dao.StatusDao;
import ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.dao.UserDao;
import ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.models.Email;
import ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.models.Issue;
import ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.models.Status;
import ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.models.User;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

public class Notificator {
    private Config config;
    private EmailSender emailSender;

    private StatusDao statusDao;
    private UserDao userDao;
    private IssueDao issueDao;

    private List<Status> statuses;
    private List<User> users;
    private List<Issue> issues;

    private final static Status STATUS_NOT_FOUND = new Status("-1", "STATUS NOT FOUND");

    public Notificator(Config config, DataSourceProvider dataSourceProvider) {
        this.config = config;
        this.emailSender = new EmailSender(config.getEmailUsername(), config.getEmailPassword());

        DataSource dataSource = dataSourceProvider.provideDataSource();
        this.statusDao = new StatusDao(dataSource);
        this.userDao = new UserDao(dataSource);
        this.issueDao = new IssueDao(dataSource);
    }

    private void updateIssue(Issue issue) {
        Issue fullIssue = issueDao.getIssueByID(issue.getId());
        if (Objects.nonNull(fullIssue)) {
            issue.setDescription(fullIssue.getDescription());
            issue.setStatusId(fullIssue.getStatusId());
            issue.setProject(fullIssue.getProject());
            issue.setAuthor(fullIssue.getAuthor());
            issue.setAssignee(fullIssue.getAssignee());
        }
    }

    private String generateIssueReport(Issue issue) {
        Status issueStatus = statuses.parallelStream()
                .filter(status -> issue.getStatusId().equals(status.getStatusId()))
                .findFirst()
                .orElse(STATUS_NOT_FOUND);
        return String.format("---\n#%s \"%s\" [%s]\n%s\nAuthor: %s, Assignee: %s, Project: %s.\n---\n",
                issue.getNumber(), issue.getSummary(), issueStatus.getStatusName(), issue.getDescription(),
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
        statuses = statusDao.getAllStatuses();
        users = userDao.getAllUsers();
        issues = issueDao.getAllIssues();

        issues.parallelStream().forEach(this::updateIssue);
        users.parallelStream().map(this::generateEmail).forEach(emailSender::send);
    }
}
