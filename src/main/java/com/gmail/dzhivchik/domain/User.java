package com.gmail.dzhivchik.domain;

import com.gmail.dzhivchik.domain.enums.UserRoleEnum;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 24.01.2017.
 */

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    private String login;
    private String password;
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    private boolean avatar;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();

    @ManyToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<File> filesSharedWithMe;

    @ManyToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Folder> foldersSharedWithMe;

    public User() {
    }

    public User(String login, String password, UserRoleEnum role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public User(String login, String password, String email, UserRoleEnum role, boolean avatar) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.role = role;
        this.avatar = avatar;
    }

    public User(int id, String login, String password, String email, UserRoleEnum role, boolean avatar) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
        this.role = role;
        this.avatar = avatar;
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

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public List<File> getFiles() { return files; }
    public void setFiles(List<File> files) { this.files = files; }

    public boolean isAvatar() { return avatar; }
    public void setAvatar(boolean avatar) { this.avatar = avatar; }
}
