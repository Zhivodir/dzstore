package com.gmail.dzhivchik.dao;

import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.dto.Content;
import com.gmail.dzhivchik.dto.PathElement;

import java.util.List;

public interface FolderDAO {
    Folder save(Folder folder);
    Folder isFolder(String name, boolean inbin, User user, Folder parentFolder);
    void deleteGroup(List<Integer> checkedFoldersId);

    Folder getReferenceFolder(int id);
    Folder getFolder(int id);
    Folder getFolder(User user, String name, Folder parentFolder);

    List<PathElement> getPathElements(List<String> pathes, int userId);

    List<Content> getList(int userId, Folder parentFolder);
    List<Content> getStarredList(int userId);
    List<Content> getBinList(int userId);
    List<Content> getSharedList(int userId, Integer targetFolder);
    List<Content> getSearchList(int userId, String whatSearch);
    List<Folder> getListFoldersById(List<Integer> listOfId);

    void changeStar(List<Integer> checkedFoldersId, boolean stateOfStar);
    void renameFolder(int userId, int fileId, String newName);
    void changeShare(List<Folder> targets);
    void changeInBin(List<Integer> checkedFoldersId, boolean stateOfInBinStatus);
    void moveTo(List<Integer> checkedFoldersId, Folder target);
}
