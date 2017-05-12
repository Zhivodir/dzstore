package com.gmail.dzhivchik.dao;

import com.gmail.dzhivchik.domain.File;
import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;

import java.util.List;

/**
 * Created by User on 31.01.2017.
 */
public interface FileDAO {
    void upload(File file);
    void uploadGroup(File[] files);
    File[] deleteGroup(int[] checked_files_id);
    List<File> getList(User user, Folder parentFolder);
    List<File> getListFilesById(int[] listOfId);
    List<File> getAllList(User user);
    File getFile(User user, String name, Folder parentFolder);
    void changeStar(int[] checked_files_id, boolean stateOfStar);
    List<File> getStarredList(User user);
    List<File> getSearchList(String whatSearch, User user);
    void renameFile(int[] checked_files_id, String newName);
    void changeShare(List<File> targets);
    List<File> getSharedList(User user);
    void changeInBin(int[] checked_files_id, boolean stateOfInBinStatus);
    List<File> getBinList(User user);
}
