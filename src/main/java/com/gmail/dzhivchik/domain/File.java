package com.gmail.dzhivchik.domain;

import javax.persistence.*;

/**
 * Created by User on 24.01.2017.
 */

@Entity
@Table(name = "file_test")
public class File {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private long size;
    private String type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    @ManyToOne
//    @JoinColumn(name = "parent_id")
//    private Folder parentFolder;

    public File() {
    }

    public File(String name, long size, String type, User user) {
        this.name = name;
        this.size = size;
        this.type = type;
        this.user = user;
//        this.parentFolder = parentFolder;
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
    public void setType(String type) {
        this.type = type;
    }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

//    public Folder getParentFolder() { return parentFolder; }
//    public void setParentFolder(Folder parentFolder) { this.parentFolder = parentFolder; }

//    @Override
//    public String toString() {
//        return "File{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", size=" + size +
//                ", type='" + type + '\'' +
//                ", user=" + user +
//                '}';
//    }
}
