package com.gmail.dzhivchik.dao;

import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.web.dto.Content;

import java.util.List;

public interface FolderDAO {
    Folder save(Folder folder);
    Folder isFolder(String name, boolean inbin, User user, Folder parentFolder);
    Folder[] deleteGroup(int[] checked_folders_id);
    Folder getFolder(int id);
    Folder getFolder(User user, String name, Folder parentFolder);
    List<Content> getList(User user, Folder parentFolder);
    List<Folder> getList(User user, Folder parentFolder, String[] exceptionFolder);
    List<Folder> getListFoldersById(int[] listOfId);
    void changeStar(int[] checked_folders_id, boolean stateOfStar);
    List<Folder> getStarredList(User user);
    List<Folder> getSearchList(String whatSearch, User user);
    void renameFolder(User userWhoWantRename, int fileId, String newName);
    void changeShare(List<Folder> targets);
    List<Folder> getSharedList(User user, Integer targetFolder);
    void changeInBin(int[] checked_folders_id, boolean stateOfInBinStatus);
    List<Folder> getBinList(User user);
    void move_to(int[] checked_folders_id, Folder target);
}
