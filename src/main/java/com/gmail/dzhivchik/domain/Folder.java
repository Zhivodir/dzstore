package com.gmail.dzhivchik.domain;

import javax.persistence.*;

/**
 * Created by User on 27.02.2017.
 */

@Entity
@Table(name = "folders")
public class Folder {
    @Id
    @GeneratedValue
    private int id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Folder() {
    }

    public Folder(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    @Override
    public String toString() {
        return "Folder{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", user=" + user +
                '}';
    }
}
