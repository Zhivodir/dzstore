package com.gmail.dzhivchik.domain;

/**
 * Created by User on 24.01.2017.
 */
public class File {
    private long id;
    private String name;
    private long size;

    public File() {
    }

    public File(long id, String name, long size) {
        this.id = id;
        this.name = name;
        this.size = size;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
}
