package com.gmail.dzhivchik.web.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangesInAccessForUsers {
    private String shareForUsers;
    private int[] cancelShareForUsers;
}
