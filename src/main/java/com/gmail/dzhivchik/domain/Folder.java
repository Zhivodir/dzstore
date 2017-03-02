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

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Folder parentFolder;

    public Folder() {
    }

    public Folder(String name, User user, Folder parentFolder) {
        this.name = name;
        this.user = user;
        this.parentFolder = parentFolder;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Folder getParentFolder() { return parentFolder; }
    public void setParentFolder(Folder parentFolder) { this.parentFolder = parentFolder; }

    @Override
    public String toString() {
        return "Folder{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", user=" + user +
                ", parentFolder=" + parentFolder +
                '}';
    }
}
