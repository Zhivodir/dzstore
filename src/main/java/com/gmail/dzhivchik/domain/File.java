package com.gmail.dzhivchik.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
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
    private boolean starred;
    @JsonIgnore
    private boolean inbin;
    @JsonIgnore
    private boolean shareInFolder;

    @JsonIgnore
    private byte[] data;
    @JsonIgnore
    private long size;
    private String type;

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

    @Override
    public String toString() {
        return "File{" +
                "name='" + name + '\'' +
                ", size=" + size +
                '}';
    }
}
