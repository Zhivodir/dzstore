package com.gmail.dzhivchik.dao;

import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.web.dto.Content;

import java.util.List;

public interface FolderDAO {
    Folder save(Folder folder);
    Folder isFolder(String name, boolean inbin, User user, Folder parentFolder);
    Folder[] deleteGroup(int[] checked_folders_id);

    Folder getReferenceFolder(int id);
    Folder getFolder(int id);
    Folder getFolder(User user, String name, Folder parentFolder);

    List<Content> getList(int userId, Folder parentFolder);
    List<Content> getStarredList(int userId);
    List<Content> getBinList(int userId);
    List<Content> getSharedList(int userId, Integer targetFolder);
    List<Content> getSearchList(int userId, String whatSearch);
    List<Folder> getListFoldersById(int[] listOfId);

    void changeStar(int[] checked_folders_id, boolean stateOfStar);
    void renameFolder(int userId, int fileId, String newName);
    void changeShare(List<Folder> targets);
    void changeInBin(int[] checked_folders_id, boolean stateOfInBinStatus);
    void move_to(int[] checked_folders_id, Folder target);
}
