package com.gmail.dzhivchik.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Leaf {
    private String path;
    private String name;
    private String icon;
    private boolean active;
}
