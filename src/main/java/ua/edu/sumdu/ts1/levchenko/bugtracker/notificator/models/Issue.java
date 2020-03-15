package ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.models;

public class Issue {
    String id;
    String statusId;
    String priorityId;
    String typeId;
    String summary;
    String description;
    String number;
    Project project;
    User assignee;
    User author;

    public Issue(String issueId, String summary, String number) {
        this.id = issueId;
        this.summary = summary;
        this.number = number;
    }

    public Issue(String issueId,
                 String summary,
                 String description,
                 String priorityId,
                 String typeId,
                 String statusId,
                 String number,
                 Project project,
                 User assignee,
                 User author
    ) {
        this.id = issueId;
        this.summary = summary;
        this.description = description;
        this.priorityId = priorityId;
        this.typeId = typeId;
        this.statusId = statusId;
        this.number = number;
        this.project = project;
        this.assignee = assignee;
        this.author = author;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(String priorityId) {
        this.priorityId = priorityId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id='" + id + '\'' +
                ", statusId='" + statusId + '\'' +
                ", priorityId='" + priorityId + '\'' +
                ", summary='" + summary + '\'' +
                ", description='" + description + '\'' +
                ", project=" + project +
                ", assignee=" + assignee +
                ", author=" + author +
                '}';
    }
}