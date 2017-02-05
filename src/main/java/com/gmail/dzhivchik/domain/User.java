package com.gmail.dzhivchik.domain;

/**
 * Created by User on 24.01.2017.
 */

//@Entity
public class User {
  //  @Id
    //@GeneratedValue
    private int id;
    private String login;
    private String email;
    private String password;

    public User() {
    }

    public User(int id, String login, String email, String password) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
