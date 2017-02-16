package com.gmail.dzhivchik.domain;

import com.gmail.dzhivchik.domain.enums.UserRoleEnum;

import javax.persistence.*;

/**
 * Created by User on 24.01.2017.
 */

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue
    private long id;

    private String login;
    private String password;
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    //private List<File> files;

    public User() {
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(String login, String password, String email, UserRoleEnum role) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public String getLogin() { return login; }
    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public UserRoleEnum getRole() { return role; }
    public void setRole(UserRoleEnum role) { this.role = role; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
}
