package com.gmail.dzhivchik.web.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString @NoArgsConstructor @JsonAutoDetect
public class SelectedContent {
    private int[] selectedFiles;
    private int[] selectedFolders;
}
