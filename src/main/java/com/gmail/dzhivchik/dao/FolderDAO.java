package com.gmail.dzhivchik.dao;

import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;

import java.util.List;

/**
 * Created by User on 27.02.2017.
 */
public interface FolderDAO {
    void createFolder(Folder folder);
    Folder[] deleteGroup(int[] checked_folders_id);
    Folder getFolder(int id);
    List<Folder> getList(User user, Folder parentFolder);
    List<Folder> getListFolderById(int[] listOfId);
    void changeStar(int[] checked_folders_id, boolean stateOfStar);
    List<Folder> getStarredList(User user);
    List<Folder> getSearchList(String whatSearch, User user);
    void renameFolder(int[] checked_folders_id, String newName);
    void share(List<Folder> targets);
    List<Folder> getSharedList(User user);
}
