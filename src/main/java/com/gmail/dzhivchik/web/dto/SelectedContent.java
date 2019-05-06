package com.gmail.dzhivchik.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class SelectedContent {
    private int[] selectedFiles;
    private int[] selectedFolders;
}
