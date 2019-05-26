package com.gmail.dzhivchik.dao;

import com.gmail.dzhivchik.domain.File;
import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.web.dto.Content;

import java.util.List;


public interface FileDAO {
    void upload(File file);

    File getFile(int id);
    List<Content> getList(int userId, Folder parentFolder);
    List<Content> getBinList(int userId);
    List<Content> getStarredList(User user);
    List<Content> getSharedList(int userId, Integer targetFolder);
    List<Content> getSearchList(int userId, String whatSearch);
    //need full file's info
    List<File> getListFilesById(int[] listOfId);
    List<File> getAllList(User user);

    long getMemoryBusySize(int userId);
    File isFile(String name, boolean inbin, User user, Folder parentFolder);
    File[] deleteGroup(int[] checked_files_id);
    void renameFile(int userId, int folderId, String newName);
    void changeStar(int[] checked_files_id, boolean stateOfStar);
    void changeShare(List<File> targets);
    void changeInBin(int[] checked_files_id, boolean stateOfInBinStatus);
    void move_to(int[] checked_files_id, Folder target);
}
