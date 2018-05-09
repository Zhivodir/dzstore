package com.gmail.dzhivchik.dao;

import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;

import java.util.List;

/**
 * Created by User on 27.02.2017.
 */
public interface FolderDAO {
    Folder save(Folder folder);
    Folder isFolder(String name, boolean inbin, User user, Folder parentFolder);
    Folder[] deleteGroup(int[] checked_folders_id);
    Folder getFolder(int id);
    Folder getFolder(User user, String name, Folder parentFolder);
    List<Folder> getList(User user, Folder parentFolder);
    List<Folder> getList(User user, Folder parentFolder, String[] exceptionFolder);
    List<Folder> getListFoldersById(int[] listOfId);
    void changeStar(int[] checked_folders_id, boolean stateOfStar);
    List<Folder> getStarredList(User user);
    List<Folder> getSearchList(String whatSearch, User user);
    void renameFolder(User userWhoWantRename, int[] checked_folders_id, String newName);
    void changeShare(List<Folder> targets);
    List<Folder> getSharedList(User user, Integer targetFolder);
    void changeInBin(int[] checked_folders_id, boolean stateOfInBinStatus);
    List<Folder> getBinList(User user);
    void move_to(int[] checked_folders_id, Folder target);
}
