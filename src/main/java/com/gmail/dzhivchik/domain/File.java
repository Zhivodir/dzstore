package com.gmail.dzhivchik.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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

    public File() {
    }

    public File(String name, long size, String type) {
        this.name = name;
        this.size = size;
        this.type = type;
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
}
