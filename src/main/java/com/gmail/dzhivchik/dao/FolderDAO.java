package com.gmail.dzhivchik.dao;

import com.gmail.dzhivchik.domain.Folder;

import java.util.List;

/**
 * Created by User on 27.02.2017.
 */
public interface FolderDAO {
    Folder save(Folder folder);
    Folder isFolder(String name, boolean inbin, Folder parentFolder);
    Folder[] deleteGroup(int[] checked_folders_id);
    Folder getFolder(int id);
    Folder getFolder(String name, Folder parentFolder);
    List<Folder> getList(Folder parentFolder);
    List<Folder> getList(Folder parentFolder, String[] exceptionFolder);
    List<Folder> getListFoldersById(int[] listOfId);
    void changeStar(int[] checked_folders_id, boolean stateOfStar);
    List<Folder> getStarredList();
    List<Folder> getSearchList(String whatSearch);
    void renameFolder(int[] checked_folders_id, String newName);
    void changeShare(List<Folder> targets);
    List<Folder> getSharedList(Integer targetFolder);
    void changeInBin(int[] checked_folders_id, boolean stateOfInBinStatus);
    List<Folder> getBinList();
    void move_to(int[] checked_folders_id, Folder target);
}
