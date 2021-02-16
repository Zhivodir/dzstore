package com.gmail.dzhivchik.domain;

import com.gmail.dzhivchik.domain.enums.Language;
import com.gmail.dzhivchik.domain.enums.UserRoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    private String login;

    @NotBlank
    @Size(min = 6, max = 12)
    private String password;

    @Email
    @NotBlank
    private String email;

    @Enumerated(EnumType.STRING)
    private Language language;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();

    @ManyToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<File> filesSharedWithMe = new ArrayList<>();

    @ManyToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Folder> foldersSharedWithMe = new ArrayList<>();

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

    public User(String login, String password, String email, UserRoleEnum role, Language language) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.role = role;
        this.language = language;
    }

    public User(int id, String login, String password, String email, UserRoleEnum role, Language language) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
        this.role = role;
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(login, user.login) &&
                Objects.equals(password, user.password) &&
                Objects.equals(email, user.email) &&
                language == user.language &&
                role == user.role &&
                Objects.equals(files, user.files) &&
                Objects.equals(filesSharedWithMe, user.filesSharedWithMe) &&
                Objects.equals(foldersSharedWithMe, user.foldersSharedWithMe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, email, language, role, files, filesSharedWithMe, foldersSharedWithMe);
    }
}
