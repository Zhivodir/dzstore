package com.gmail.dzhivchik.service.Impl;

import com.gmail.dzhivchik.dao.FileDAO;
import com.gmail.dzhivchik.dao.FolderDAO;
import com.gmail.dzhivchik.dao.UserDAO;
import com.gmail.dzhivchik.domain.File;
import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 31.01.2017.
 */

@Service
public class ContentService {
    private static String USERS_STORAGES = "C:/DevKit/Temp/dzstore/users_storages/";

    @Autowired
    private FileDAO fileDAO;

    @Autowired
    private FolderDAO folderDAO;

    @Autowired
    private UserDAO userDAO;


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
    public void uploadFolder(Folder folder){
        folderDAO.upload(folder);
    }


    @Transactional(readOnly=true)
    public List[] getContent(User user, Folder parentFolder){
        List[] content = new List[2];
        content[0] = fileDAO.getList(user, parentFolder);
        content[1] = folderDAO.getList(user, parentFolder);
        return content;
    }


    @Transactional
    public void deleteCheckedContent(int[] checked_files_id, int[] checked_folders_id, String login){
        if (checked_files_id != null) {
            fileDAO.deleteGroup(checked_files_id);
        }
        if (checked_folders_id != null) {
            folderDAO.deleteGroup(checked_folders_id);
        }
    }


    @Transactional
    public List<File> getListFilesById(int[] checked_files_id) {
        return fileDAO.getListFilesById(checked_files_id);
    }


    @Transactional
    public List<Folder> getListFolderById(int[] checked_folders_id) {
        return folderDAO.getListFoldersById(checked_folders_id);
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
    public void in_out_bin(int[] checked_files_id, int[] checked_folders_id, boolean stateOfInBinStatus) {
        if(checked_files_id != null){
            fileDAO.changeInBin(checked_files_id, stateOfInBinStatus);
        }
        if(checked_folders_id != null){
            folderDAO.changeInBin(checked_folders_id, stateOfInBinStatus);
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
    public List[] getBinContent(User user){
        List[] content = new List[2];
        content[0] = fileDAO.getBinList(user);
        content[1] = folderDAO.getBinList(user);
        return content;
    }


    @Transactional
    public List[] getListBySearch(String whatSearch, User user){
        List[] content = new List[2];
        content[0] = fileDAO.getSearchList(whatSearch, user);
        content[1] = folderDAO.getSearchList(whatSearch, user);
        return content;
    }


    @Transactional
    public void rename(String login, int[] checked_files_id, int[] checked_folders_id, String newName){
        StringBuilder sb = new StringBuilder();
        if(checked_files_id != null){
            //File fileForRename = fileDAO.getListFilesById(checked_files_id).get(0);
            fileDAO.renameFile(checked_files_id, newName);
        }else if(checked_folders_id != null){
            //Folder folderForRename = folderDAO.getFolder(checked_folders_id[0]);
            folderDAO.renameFolder(checked_folders_id, newName);
        }
    }

    @Transactional
    public void share(List<File> checked_files, List<Folder> checked_folders, String shareFor, boolean shareInFolder){
        List<User> receivers = userDAO.getShareReceivers(shareFor);
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        for (int i = 0; i < receivers.size(); i++) {
            if(receivers.get(i).getLogin().equals(login)){
                receivers.remove(receivers.get(i));
                i--;
            }
        }

        if(checked_files != null){
            for (File file : checked_files){
                file.addToShareFor(receivers);
                file.setShareInFolder(shareInFolder);
            }
            fileDAO.changeShare(checked_files);
        }

        if(checked_folders != null){
            for (Folder folder : checked_folders){
                folder.addToShareFor(receivers);
                share(folder.getFiles(), folder.getFolders(), shareFor, true);
                //file.setShareInFolder(shareInFolder);
            }
            folderDAO.changeShare(checked_folders);
        }
    }


    @Transactional
    public void cancelShare(int[] checked_files_id, int[] checked_folders_id, User user){
        List<User> receivers = new ArrayList<>();
        receivers.add(user);

        List<File> targetsFiles = null;
        if(checked_files_id != null){
            targetsFiles = fileDAO.getListFilesById(checked_files_id);
            for (File file : targetsFiles){
                file.removeFromShareFor(receivers);
            }
            fileDAO.changeShare(targetsFiles);
        }

        List<Folder> targetsFolder = null;
        if(checked_folders_id != null){
            targetsFolder = folderDAO.getListFoldersById(checked_folders_id);
            for (Folder folder : targetsFolder){
                folder.removeFromShareFor(receivers);
            }
            folderDAO.changeShare(targetsFolder);
        }
    }


    @Transactional
    public List[] getSharedContent(User user, Integer targetFolder){
        List[] content = new List[2];
        content[0] = fileDAO.getSharedList(user, targetFolder);
        content[1] = folderDAO.getSharedList(user, targetFolder);
        return content;
    }


    @Transactional
    public void addtome(int[] checked_files_id, int[] checked_folders_id, User user){
        if(checked_files_id != null) {
            List<File> listOfAddFiles = getListFilesById(checked_files_id);
            addSharedFileToMyStore(listOfAddFiles, user, null, null);
        }

        if(checked_folders_id != null) {
            List<Folder> ListOfAddFolders = getListFolderById(checked_folders_id);
            addSharedFolderToMyStore(ListOfAddFolders, user, null, null);
        }
    }


    @Transactional
    public void move_to(int[] checked_files_id, int[] checked_folders_id, User user, Folder move_to){
        if(checked_files_id != null) {
            fileDAO.move_to(checked_files_id, move_to);
        }
        if(checked_folders_id != null) {
            folderDAO.move_to(checked_folders_id, move_to);
        }
    }


    public long getSizeBusyMemory(User user){
        List<File> content = fileDAO.getAllList(user);
        long sumSize = 0;
        for(File file : content) {
            sumSize += file.getSize();
        }
        return sumSize;
    }



    public void createPathForElement(StringBuilder sb, Folder curFolder) {
        if (curFolder != null) {
            createPathForElement(sb, curFolder.getParentFolder());
            sb.append(curFolder.getName());
            sb.append("/");
        }
    }


    public void addSharedFileToMyStore(List<File> listOfAddFiles, User user, Folder curFolder, Folder addFolder) {
        long all = (long)10*1024*1024*1024;
        for(File file : listOfAddFiles){
            String fileName = file.getName();
            StringBuilder relativePath = new StringBuilder();
            createPathForElement(relativePath, curFolder);
            long size = file.getSize();
            if(size <= all - getSizeBusyMemory(user)){
                fileDAO.upload(new File(fileName, size, file.getType(), user, addFolder, false, false, file.getData(), false));
            }else{
                System.out.println("Havn't need memory");
            }
        }
    }

    public void addSharedFolderToMyStore(List<Folder> listOfAddFolders, User user, Folder shareFolder, Folder addFolder){
        for(Folder folder : listOfAddFolders) {
            folderDAO.createFolder(new Folder(folder.getName(), user, addFolder, false, false, false));
            Folder tf = folderDAO.getFolder(user, folder.getName(), addFolder);

            if(folder.getFiles().size() != 0) {
                addSharedFileToMyStore(folder.getFiles(), user, folder, tf);
            }

            if(folder.getFolders().size() != 0) {
                addSharedFolderToMyStore(folder.getFolders(), user, folder, tf);
            }
        }
    }

    public List[] getContentById(int[] checked_files_id, int[] checked_folders_id) {
        List[] content = new List[2];
        if(checked_files_id != null) {
            content[0] = getListFilesById(checked_files_id);
        }
        if(checked_folders_id != null) {
            content[1] = getListFolderById(checked_folders_id);
        }
        return content;
    }
}
