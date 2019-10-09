package com.gmail.dzhivchik.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter @Getter @ToString @NoArgsConstructor @JsonAutoDetect
public class SelectedContent {
    private List<Integer> selectedFiles;
    private List<Integer> selectedFolders;
}
