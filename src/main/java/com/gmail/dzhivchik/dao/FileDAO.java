package com.gmail.dzhivchik.dao;

import com.gmail.dzhivchik.domain.File;
import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.web.dto.Content;

import java.util.List;


public interface FileDAO {
    void save(File file);

    File getFile(int id);
    List<Content> getList(int userId, Folder parentFolder);
    List<Content> getBinList(int userId);
    List<Content> getStarredList(int userId);
    List<Content> getSharedList(int userId, Integer targetFolder);
    List<Content> getSearchList(int userId, String whatSearch);
    List<File> getListFilesById(int[] listOfId);

    long getMemoryBusySize(int userId);
    File isFile(String name, boolean inbin, User user, Folder parentFolder);
    File[] deleteGroup(int[] checked_files_id);
    void renameFile(int userId, int folderId, String newName);
    void changeStar(int[] checked_files_id, boolean stateOfStar);
    void changeShare(List<File> targets);
    void changeInBin(int[] checked_files_id, boolean stateOfInBinStatus);
    void moveTo(int[] checked_files_id, Folder target);
}
