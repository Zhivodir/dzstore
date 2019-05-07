package com.gmail.dzhivchik.web.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}