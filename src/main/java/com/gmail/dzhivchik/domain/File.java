package com.gmail.dzhivchik.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 24.01.2017.
 */

@JsonAutoDetect
@Entity
@Table(name = "files")
public class File implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;
    @JsonProperty("title")
    private String name;
    @JsonIgnore
    private long size;
    private String type;
    @JsonIgnore
    private boolean starred;
    @JsonIgnore
    private boolean inbin;
    @JsonIgnore
    private byte[] data;
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
    @ManyToMany
    @JoinTable(
            name="share_file_for_user",
            joinColumns = {@JoinColumn(name = "id_file", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "id_user", referencedColumnName = "id")})
    private List<User> shareFor = new ArrayList<>();

    public File() {
    }

    public File(String name, long size, String type, User user, Folder parentFolder,
                boolean starred, boolean inbin, byte[] data, boolean shareInFolder) {
        this.name = name;
        this.size = size;
        this.type = type;
        this.user = user;
        this.parentFolder = parentFolder;
        this.starred = starred;
        this.inbin = inbin;
        this.data = data;
        this.shareInFolder = shareInFolder;
    }

    public Integer getId() { return id; }
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
    public void removeFromShareFor(List<User> forCancelShare){
        for(User newUser : forCancelShare){
            shareFor.remove(newUser);
        }
    }

    public boolean isInbin() { return inbin; }
    public void setInbin(boolean inbin) { this.inbin = inbin; }

    public byte[] getData() { return data; }
    public void setData(byte[] data) { this.data = data; }

    public boolean isShareInFolder() { return shareInFolder; }
    public void setShareInFolder(boolean shareInFolder) { this.shareInFolder = shareInFolder; }

    @Override
    public String toString() {
        return "File{" +
                "name='" + name + '\'' +
                ", size=" + size +
                '}';
    }
}
