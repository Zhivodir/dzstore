package com.gmail.dzhivchik.service.Impl;

import com.gmail.dzhivchik.dao.FileDAO;
import com.gmail.dzhivchik.dao.FolderDAO;
import com.gmail.dzhivchik.dao.UserDAO;
import com.gmail.dzhivchik.domain.File;
import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
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
    public void deleteCheckedContent(int[] checked_files_id, int[] checked_folders_id, String login){
        if (checked_files_id != null) {
            File[] files = fileDAO.deleteGroup(checked_files_id);;
            deleteListOfFiles(Arrays.asList(files), login);
        }
        if (checked_folders_id != null) {
            Folder[] folders = folderDAO.deleteGroup(checked_folders_id);
            deleteListOfFolders(Arrays.asList(folders), login);
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
            File fileForRename = fileDAO.getListFilesById(checked_files_id).get(0);
            fileDAO.renameFile(checked_files_id, newName);
            createPathForElement(sb, fileForRename.getParentFolder());
            java.io.File file = new java.io.File("c:/DevKit/Temp/dzstore/users_storages/" + login + "/" + sb.toString() + fileForRename.getName());
            if(file.exists()){
                file.renameTo(new java.io.File("c:/DevKit/Temp/dzstore/users_storages/" + login + "/" + sb.toString() + newName));
            }
            else{
                System.out.println("File not found!");
            }
        }else if(checked_folders_id != null){
            Folder folderForRename = folderDAO.getFolder(checked_folders_id[0]);
            folderDAO.renameFolder(checked_folders_id, newName);
            createPathForElement(sb, folderForRename);
            java.io.File file = new java.io.File("c:/DevKit/Temp/dzstore/users_storages/" + login + "/" + sb.toString().substring(0,sb.toString().length()-1));
            if(file.exists()){
                file.renameTo(new java.io.File(file.getPath().substring(0, file.getPath().lastIndexOf("\\")+1) + newName));
            }
            else{
                System.out.println("File not found!");
            }
        }
    }

    @Transactional
    public void share(int[] checked_files_id, int[] checked_folders_id, String shareFor){
        List<User> receivers = userDAO.getShareReceivers(shareFor);

        List<File> targetsFiles = null;
        if(checked_files_id != null){
            targetsFiles = fileDAO.getListFilesById(checked_files_id);
            for (File file : targetsFiles){
                file.addToShareFor(receivers);
            }
            fileDAO.changeShare(targetsFiles);
        }

        List<Folder> targetsFolder = null;
        if(checked_folders_id != null){
            targetsFolder = folderDAO.getListFoldersById(checked_folders_id);
            for (Folder folder : targetsFolder){
                folder.addToShareFor(receivers);
            }
            folderDAO.changeShare(targetsFolder);
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
    public List[] getSharedContent(User user){
        List[] content = new List[2];
        content[0] = fileDAO.getSharedList(user);
        content[1] = folderDAO.getSharedList(user);
        return content;
    }

    @Transactional
    public void addtome(int[] checked_files_id, int[] checked_folders_id, User user){
        StringBuilder sb = new StringBuilder();
        if(checked_files_id != null) {
            List<File> listOfAddFiles = getListFilesById(checked_files_id);
            addSharedFileToMyStore(sb, listOfAddFiles, user, null, null);
        }

        if(checked_folders_id != null) {
            List<Folder> ListOfAddFolders = getListFolderById(checked_folders_id);
            addSharedFolderToMyStore(sb, ListOfAddFolders, user, null, null);
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

    public void deleteListOfFiles(List<File> files, String login){
        if (files.size() != 0) {
            for (File file : files) {
                Folder temp = file.getParentFolder();
                if(temp != null) {
                    StringBuilder sb = new StringBuilder();
                    createPathForElement(sb, temp);
                    new java.io.File("c:/DevKit/Temp/dzstore/users_storages/" + login + "/" + sb.toString() + "/" + file.getName()).delete();
                }
                else {
                    new java.io.File("c:/DevKit/Temp/dzstore/users_storages/" + login + "/" + file.getName()).delete();
                }
            }
        }
    }

    public void deleteListOfFolders(List<Folder> folders, String login){
        for(Folder folder : folders) {
            List<Folder> subfolder = folder.getFolders();
            List<File> files = folder.getFiles();
            if(subfolder.size() != 0){
                deleteListOfFolders(subfolder, login);
                deleteContentOfFolder(files, login, folder);
            } else {
                deleteContentOfFolder(files, login, folder);
            }
        }
    }

    public void deleteContentOfFolder(List<File> files, String login, Folder folder){
        deleteListOfFiles(files, login);
        Folder temp = folder.getParentFolder();
        if (temp != null) {
            StringBuilder sb = new StringBuilder();
            createPathForElement(sb, temp);
            new java.io.File("c:/DevKit/Temp/dzstore/users_storages/" + login + "/" + sb.toString() + "/" + folder.getName()).delete();
        } else {
            new java.io.File("c:/DevKit/Temp/dzstore/users_storages/" + login + "/" + folder.getName()).delete();
        }
    }

    public void createPathForElement(StringBuilder sb, Folder curFolder) {
        if (curFolder != null) {
            createPathForElement(sb, curFolder.getParentFolder());
            sb.append(curFolder.getName());
            sb.append("/");
        }
    }


    public void addSharedFileToMyStore(StringBuilder sb, List<File> listOfAddFiles, User user, Folder curFolder, Folder addFolder) {
        long all = (long)10*1024*1024*1024;
        String login = user.getLogin();
        for(File file : listOfAddFiles){
            String fileName = file.getName();
            String oldOwner = file.getUser().getLogin();
            StringBuilder relativePath = new StringBuilder();
            createPathForElement(relativePath, curFolder);
            String pathForSource = "c:/DevKit/Temp/dzstore/users_storages/" + oldOwner + "/" + relativePath.toString() + "/" + fileName;
            String pathForDest = "c:/DevKit/Temp/dzstore/users_storages/" + login + "/" + sb.toString() + fileName;
            long size = file.getSize();
            if(size <= all - getSizeBusyMemory(user)){
                String type = "test";
                File fileForDAO = new File(fileName, size, type, user, addFolder, false, false);
                java.io.File source = new java.io.File(pathForSource);
                java.io.File dest = new java.io.File(pathForDest);
                try {
                    fileDAO.upload(fileForDAO);
                    copy(source, dest);
                }catch (IOException e){e.printStackTrace();}
            }else{
                System.out.println("Havn't nedd memory");
            }
        }
    }

    public void addSharedFolderToMyStore(StringBuilder sb, List<Folder> listOfAddFolders, User user, Folder shareFolder, Folder addFolder){
        for(Folder folder : listOfAddFolders) {
            sb.append(folder.getName() + "/");
            String newFolder = "c:/DevKit/Temp/dzstore/users_storages/" +
                    user.getLogin() + "/" + sb.toString();
            Folder folderForDAO = new Folder(folder.getName(), user, addFolder, false, false);
            folderDAO.createFolder(folderForDAO);
            Folder tf = folderDAO.getFolder(user, folder.getName(), addFolder);
            java.io.File file = new java.io.File(newFolder);
            file.mkdirs();

            if(folder.getFiles().size() != 0) {
                sb.append("/");
                addSharedFileToMyStore(sb, folder.getFiles(), user, folder,tf);
                sb.delete(sb.toString().length() - 1, sb.length());
            }

            if(folder.getFolders().size() != 0) {
                addSharedFolderToMyStore(sb, folder.getFolders(), user, folder, tf);
                sb.delete(sb.lastIndexOf("/") + 1, sb.length());
            }
        }
    }

    public static void copy(java.io.File source, java.io.File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }
}
