package com.gmail.dzhivchik.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@JsonAutoDetect
@Entity
@Table(name = "folders")
public class Folder {
    @Id
    @GeneratedValue
    private Integer id;
    @NotBlank
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
    private String path;

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

    public Folder(String name, User user, Folder parentFolder,
                  boolean starred, boolean inbin, boolean shareInFolder, String path) {
        this.name = name;
        this.user = user;
        this.parentFolder = parentFolder;
        this.starred = starred;
        this.inbin = inbin;
        this.shareInFolder = shareInFolder;
        this.path = path;
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

    public void addFile(File file){
        files.add(file);
    }

    public void addFolder(Folder folder){
        folders.add(folder);
    }

    public int getNestedFilesQuantity(){
        return files.size();
    }

    public int getNestedFoldersQuantity(){
        return folders.size();
    }

    public String getFullPath(){
        if(parentFolder != null) {
            return parentFolder.getPath() != null ? parentFolder.getPath() + "/" + parentFolder.getName() : parentFolder.getName();
        }
        return name;

    }

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