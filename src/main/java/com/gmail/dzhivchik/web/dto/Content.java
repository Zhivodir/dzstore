package com.gmail.dzhivchik.web.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonAutoDetect
@EqualsAndHashCode
public class Content {
    private int id;
    private String name;
    private long size;
    private String owner;
    private String type;
    private boolean starred;
    private boolean shared;
    private boolean inbin;

    public Content(int id, String name, String owner, boolean starred, boolean shared, boolean inbin) {
        this.id = id;
        this.name = name;
        this.size = 0;
        this.owner = owner;
        this.type = "folder";
        this.starred = starred;
        this.shared = shared;
        this.inbin = inbin;
    }

    public Content(int id, String name, long size, String owner, String type, boolean starred, boolean shared, boolean inbin) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.owner = owner;
        this.type = type;
        this.starred = starred;
        this.shared = shared;
        this.inbin = inbin;
    }
}