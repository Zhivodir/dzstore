package com.gmail.dzhivchik.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangesInAccessForUsers {
    private String shareForUsers;
    private String[] cancelShareForUsers;
}
