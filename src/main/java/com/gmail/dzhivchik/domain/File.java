package com.gmail.dzhivchik.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 24.01.2017.
 */

@Entity
@Table(name = "file")
public class File {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private long size;
    private String type;
    private boolean starred;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Folder parentFolder;

    public File() {
    }

    @ManyToMany
    @JoinTable(
            name="share_file_for_user",
            joinColumns = {@JoinColumn(name = "id_file", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "id_user", referencedColumnName = "id")})
    private List<User> shareFor = new ArrayList<>();

    public File(String name, long size, String type, User user, Folder parentFolder, boolean starred) {
        this.name = name;
        this.size = size;
        this.type = type;
        this.user = user;
        this.parentFolder = parentFolder;
        this.starred = starred;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }
    public void setSize(long size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) { this.type = type; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Folder getParentFolder() { return parentFolder; }
    public void setParentFolder(Folder parentFolder) { this.parentFolder = parentFolder; }

    public boolean isStarred() { return starred; }
    public void setStarred(boolean starred) { this.starred = starred; }

    public List<User> getShareFor() { return shareFor; }
    public void setShareFor(List<User> shareFor) { this.shareFor = shareFor; }
    public void addToShareFor(List<User> forAdd){
        for(User newUser : forAdd){
            if(!shareFor.contains(newUser)){
                shareFor.add(newUser);
            }
        }
    }
}
