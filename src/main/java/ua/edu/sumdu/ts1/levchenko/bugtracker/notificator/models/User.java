package ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.models;

public class User {
    private String name;
    private String userId;
    private String email;

    public User(String userId, String name, String email) {
        this.name = name;
        this.userId = userId;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}