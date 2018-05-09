package com.gmail.dzhivchik.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 27.02.2017.
 */

@JsonAutoDetect
@Entity
@Table(name = "folders")
public class Folder {
    @Id
    @GeneratedValue
    private Integer id;
    @JsonProperty("title")
    private String name;
    @JsonIgnore
    private boolean starred;
    @JsonIgnore
    private boolean inbin;
    @JsonIgnore
    private boolean shareInFolder;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Folder parentFolder;

    @JsonIgnore
    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.ALL)
    private List<Folder> folders = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name="share_folder_for_user",
            joinColumns = {@JoinColumn(name = "id_folder", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "id_user", referencedColumnName = "id")})
    private List<User> shareFor = new ArrayList<>();

    public Folder() {
    }

    public Folder(String name, User user, Folder parentFolder,
                  boolean starred, boolean inbin, boolean shareInFolder) {
        this.name = name;
        this.user = user;
        this.parentFolder = parentFolder;
        this.starred = starred;
        this.inbin = inbin;
        this.shareInFolder = shareInFolder;
    }

    public Integer getId() { return id; }
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

    public List<User> getShareFor() { return shareFor; }
    public void setShareFor(List<User> shareFor) { this.shareFor = shareFor; }
    public void addToShareFor(List<User> forAdd){
        for(User newUser : forAdd){
            if(!shareFor.contains(newUser)){
                shareFor.add(newUser);
            }
        }
    }
    public void removeFromShareFor(List<User> forCancelShare){
        for(User newUser : forCancelShare){
            shareFor.remove(newUser);
        }
    }

    public boolean isInbin() { return inbin; }
    public void setInbin(boolean inbin) { this.inbin = inbin; }

    public boolean isShareInFolder() { return shareInFolder; }
    public void setShareInFolder(boolean shareInFolder) { this.shareInFolder = shareInFolder; }

    @Override
    public String toString() {
        return "Folder{" +
                "name='" + name + '\'' +
                ", parentFolder=" + parentFolder +
                ", files=" + files +
                ", folders=" + folders +
                '}';
    }
}
