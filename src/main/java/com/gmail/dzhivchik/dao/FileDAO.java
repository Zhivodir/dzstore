package com.gmail.dzhivchik.dao;

import com.gmail.dzhivchik.domain.File;
import com.gmail.dzhivchik.domain.Folder;

import java.util.List;

/**
 * Created by User on 31.01.2017.
 */
public interface FileDAO {
    void upload(File file);
    void uploadGroup(File[] files);
    File getFile(int id);
    File isFile(String name, boolean inbin, Folder parentFolder);
    void deleteGroup(int[] checked_files_id);
    List<File> getList(Folder parentFolder);
    List<File> getListFilesById(int[] listOfId);
    List<File> getAllList();
    File getFile(String name, Folder parentFolder);
    void changeStar(int[] checked_files_id, boolean stateOfStar);
    List<File> getStarredList();
    List<File> getSearchList(String whatSearch);
    void renameFile(int[] checked_files_id, String newName);
    void changeShare(List<File> targets);
    List<File> getSharedList(Integer targetFolder);
    void changeInBin(int[] checked_files_id, boolean stateOfInBinStatus);
    List<File> getBinList();
    void move_to(int[] checked_files_id, Folder target);
}
