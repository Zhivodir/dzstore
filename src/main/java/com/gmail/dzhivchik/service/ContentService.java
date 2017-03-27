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

    @Transactional
    public Folder[] deleteCheckedFolders(int[] checked_folders_id) {
        return folderDAO.deleteGroup(checked_folders_id);
    }

    @Transactional
    public List<File> getListById(int[] checked_files_id) {
        return fileDAO.getListById(checked_files_id);
    }

    @Transactional
    public List<Folder> getListFolderById(int[] checked_folders_id) {
        return folderDAO.getListFolderById(checked_folders_id);
    }

    @Transactional
    public void changeStar(int[] checked_files_id, int[] checked_folders_id, boolean stateOfStar) {
        if(checked_files_id != null){
            fileDAO.changeStar(checked_files_id, stateOfStar);
        }
        if(checked_folders_id != null){
            folderDAO.changeStar(checked_folders_id, stateOfStar);
        }
    }

    @Transactional
    public List[] getStarredContent(User user){
        List[] content = new List[2];
        content[0] = fileDAO.getStarredList(user);
        content[1] = folderDAO.getStarredList(user);
        return content;
    }

    @Transactional
    public List[] getListBySearch(String whatSearch, User user){
        List[] content = new List[2];
        content[0] = fileDAO.getSearchList(whatSearch, user);
        content[1] = folderDAO.getStarredList(user);
        return content;
    }

    @Transactional
    public void rename(String login, int[] checked_files_id, int[] checked_folders_id, String newName){
        StringBuilder sb = new StringBuilder();
        if(checked_files_id != null){
            File fileForRename = fileDAO.getListById(checked_files_id).get(0);
            fileDAO.renameFile(checked_files_id, newName);
            createPathForFile(sb, fileForRename.getParentFolder(), 0);
            java.io.File file = new java.io.File("c:/DevKit/Temp/dzstore/" + login + "/" + sb.toString() + fileForRename.getName());
            if(file.exists()){
                file.renameTo(new java.io.File("c:/DevKit/Temp/dzstore/" + login + "/" + sb.toString() + newName));
            }
            else{
                System.out.println("File not found!");
            }
        }else if(checked_folders_id != null){
            Folder folderForRename = folderDAO.getFolder(checked_folders_id[0]);
            folderDAO.renameFolder(checked_folders_id, newName);
            createPathForFile(sb, folderForRename, 0);
            java.io.File file = new java.io.File("c:/DevKit/Temp/dzstore/" + login + "/" + sb.toString() + folderForRename.getName());
            if(file.exists()){
                file.renameTo(new java.io.File("c:/DevKit/Temp/dzstore/" + login + "/" + sb.toString() + newName));
            }
            else{
                System.out.println("File not found!");
            }
        }
    }

    public void createPathForFile(StringBuilder sb, Folder curFolder, int deep) {
        System.out.println(deep);
        if (curFolder != null) {
            deep++;
            createPathForFile(sb, curFolder.getParentFolder(), deep);
            if(deep != 1) {
                sb.append(curFolder.getName());
                sb.append("/");
                deep--;
            }
        }
    }
}
