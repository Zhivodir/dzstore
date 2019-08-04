package com.gmail.dzhivchik.web.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

@Setter @Getter @ToString @NoArgsConstructor @JsonAutoDetect
public class SelectedContent {
    private int[] selectedFiles;
    private int[] selectedFolders;
}
