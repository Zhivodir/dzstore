package com.gmail.dzhivchik.dao;

import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;

import java.util.List;

/**
 * Created by User on 27.02.2017.
 */
public interface FolderDAO {
    void createFolder(Folder folder);
    //void delete(Folder folder);
    //Folder[] deleteGroup(int[] checked_files_id);

    Folder getFolder(int id);
    List<Folder> getList(User user);
}
