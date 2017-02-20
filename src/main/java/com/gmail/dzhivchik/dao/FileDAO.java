package com.gmail.dzhivchik.dao;

import com.gmail.dzhivchik.domain.File;
import com.gmail.dzhivchik.domain.User;

import java.util.List;

/**
 * Created by User on 31.01.2017.
 */
public interface FileDAO {
    void upload(File file);
    void uploadGroup(File[] files);
    void delete(File file);
    void deleteGroup(File[] files);
    List<File> getList(User user);
}
