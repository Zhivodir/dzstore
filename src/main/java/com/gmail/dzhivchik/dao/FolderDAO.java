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
    List<Content> getStarredList(User user);
    List<Content> getBinList(User user);
    List<Content> getSharedList(User user, Integer targetFolder);
    List<Content> getSearchList(String whatSearch, User user);
    List<Folder> getListFoldersById(int[] listOfId);

    void changeStar(int[] checked_folders_id, boolean stateOfStar);
    void renameFolder(User userWhoWantRename, int fileId, String newName);
    void changeShare(List<Folder> targets);
    void changeInBin(int[] checked_folders_id, boolean stateOfInBinStatus);
    void move_to(int[] checked_folders_id, Folder target);
}
