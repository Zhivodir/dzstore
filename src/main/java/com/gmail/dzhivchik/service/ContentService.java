package com.gmail.dzhivchik.service;

import com.gmail.dzhivchik.dao.FileDAO;
import com.gmail.dzhivchik.dao.FolderDAO;
import com.gmail.dzhivchik.domain.File;
import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by User on 31.01.2017.
 */

@Service
public class ContentService {
    @Autowired
    private FileDAO fileDAO;

    @Autowired
    private FolderDAO folderDAO;

    @Transactional
    public void createFolder(Folder folder){
        folderDAO.createFolder(folder);
    }

    @Transactional
    public Folder getFolder(Integer id){ return folderDAO.getFolder(id); }

    @Transactional
    public void uploadFile(File file){
        fileDAO.upload(file);
    }

    @Transactional
    public void uploadFolder(File[] files){
        fileDAO.uploadGroup(files);
    }

    @Transactional(readOnly=true)
    public List[] getContent(User user, Folder parentFolder){
        List[] content = new List[2];
        content[0] = fileDAO.getList(user, parentFolder);
        content[1] = folderDAO.getList(user, parentFolder);
        return content;
    }

    @Transactional
    public File[] deleteCheckedFiles(int[] checked_files_id) {
        return fileDAO.deleteGroup(checked_files_id);
    }
}
