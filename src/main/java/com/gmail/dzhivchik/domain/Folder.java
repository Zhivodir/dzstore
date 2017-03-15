package com.gmail.dzhivchik.domain;

import javax.persistence.*;
import java.util.List;

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
    private boolean starred;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Folder parentFolder;

    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.ALL)
    private List<File> files;

    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.ALL)
    private List<Folder> folders;

    public Folder() {
    }

    public Folder(String name, User user, Folder parentFolder, boolean starred) {
        this.name = name;
        this.user = user;
        this.parentFolder = parentFolder;
        this.starred = starred;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Folder getParentFolder() { return parentFolder; }
    public void setParentFolder(Folder parentFolder) { this.parentFolder = parentFolder; }

    public List<File> getFiles() { return files; }
    public void setFiles(List<File> files) { this.files = files; }

    public List<Folder> getFolders() { return folders; }
    public void setFolders(List<Folder> folders) { this.folders = folders; }

    public boolean isStarred() { return starred; }
    public void setStarred(boolean starred) { this.starred = starred; }
}
