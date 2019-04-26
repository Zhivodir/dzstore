package com.gmail.dzhivchik.domain;

import com.gmail.dzhivchik.domain.enums.UserRoleEnum;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
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

    public User(String login, String password, String email, UserRoleEnum role) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public User(int id, String login, String password, String email, UserRoleEnum role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
        this.role = role;
    }
}
