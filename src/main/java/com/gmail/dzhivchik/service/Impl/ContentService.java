package com.gmail.dzhivchik.service.Impl;

import com.gmail.dzhivchik.config.TempContentConfig;
import com.gmail.dzhivchik.dao.FileDAO;
import com.gmail.dzhivchik.dao.FolderDAO;
import com.gmail.dzhivchik.dao.UserDAO;
import com.gmail.dzhivchik.domain.File;
import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.web.dto.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.gmail.dzhivchik.MemoryUtils.GBYTE;


@Service
public class ContentService {

    @Autowired
    private FileDAO fileDAO;

    @Autowired
    private FolderDAO folderDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private HttpServletResponse httpServletResponse;


    @Transactional
    public Folder createFolder(Folder folder) {
        return folderDAO.save(folder);
    }

    @Transactional
    public Folder getFolder(Integer id) {
        return folderDAO.getFolder(id);
    }

    @Transactional
    public File getFile(Integer id) {
        return fileDAO.getFile(id);
    }

    @Transactional
    public void uploadContent(MultipartFile file, MultipartFile[] files, String structure, Integer currentFolderID){
        User currentUser = getCurrentUser();

        Folder curFolder = null;
        if (currentFolderID != -1) {
            curFolder = getFolder(currentFolderID);
        }

        if (file != null && file.getSize() < TempContentConfig.MAX_SIZE_OF_FILE) {
            uploadFile(file, currentUser, curFolder);
        }

        if (files != null && structure != null) {
            String[] pathes = null;
            if (structure.startsWith(",")) {
                structure = structure.substring(1);
            }
            String targetFolderName = structure.substring(0, structure.indexOf("/"));
            structure = structure.replaceAll(targetFolderName + "/", "");
            Folder targetFolder = new Folder(targetFolderName, currentUser, curFolder, false, false, false);
            pathes = structure.split(";");
            prepareNewFolderForUpload(files, pathes, currentUser, targetFolder, structure);
            folderDAO.save(targetFolder);
        }
    }

    @Transactional
    public void downloadContent(int[] checked_files_id, int[] checked_folders_id){
        List<File> listCheckedFiles = new ArrayList<>();
        if (checked_files_id != null) {
            listCheckedFiles = getListFilesById(checked_files_id);
        }

        List<Folder> listCheckedFolder = new ArrayList<>();
        if (checked_folders_id != null) {
            listCheckedFolder = getListFolderById(checked_folders_id);
        }
        downloadContent(listCheckedFiles, listCheckedFolder);
    }

    @Transactional(readOnly = true)
    public List<Content> getContent(int userId, Folder parentFolder) {
        List<Content> content = folderDAO.getList(userId, parentFolder);
        content.addAll(fileDAO.getList(userId, parentFolder));
        return content;
    }

    @Transactional
    public void deleteCheckedContent(int[] checked_files_id, int[] checked_folders_id) {
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
    public List<Folder> getListFolderById(int[] checked_folders_id) { return folderDAO.getListFoldersById(checked_folders_id); }

    @Transactional
    public void changeStar(int[] checked_files_id, int[] checked_folders_id, boolean stateOfStar) {
        if (checked_files_id != null) {
            fileDAO.changeStar(checked_files_id, stateOfStar);
        }
        if (checked_folders_id != null) {
            folderDAO.changeStar(checked_folders_id, stateOfStar);
        }
    }

    @Transactional
    public void removeInBin(int[] checked_files_id, int[] checked_folders_id, boolean stateOfInBinStatus) {
        if (checked_files_id != null) {
            fileDAO.changeInBin(checked_files_id, stateOfInBinStatus);
        }
        if (checked_folders_id != null) {
            folderDAO.changeInBin(checked_folders_id, stateOfInBinStatus);
        }
    }

    @Transactional
    public List<Content> getStarredContent(User user) {
        List<Content> content = folderDAO.getStarredList(user);
        content.addAll(fileDAO.getStarredList(user));
        return content;
    }

    @Transactional
    public List<Content> getBinContent(int userId) {
        Instant start = Instant.now();
        List<Content> content = folderDAO.getBinList(userId);
        content.addAll(fileDAO.getBinList(userId));
        Instant before = Instant.now();
        long ns = Duration.between(start, before).toNanos();
        System.out.println("Duration: " + ns);
        return content;
    }

    @Transactional
    public List<Content> getSharedContent(int userId, Integer targetFolder) {
        List<Content> content = folderDAO.getSharedList(userId, targetFolder);
        content.addAll(fileDAO.getSharedList(userId, targetFolder));
        return content;
    }

    @Transactional
    public List<Content> getSearchContent(int userId, String whatSearch) {
        List<Content> content = folderDAO.getSearchList(userId, whatSearch);
        content.addAll(fileDAO.getSearchList(userId, whatSearch));
        return content;
    }

    @Transactional
    public void rename(String contentType, int contentId, String newName, int userId) {
        if (contentType.equals("file")) {
            fileDAO.renameFile(userId, contentId, newName);
        } else {
            folderDAO.renameFolder(userId, contentId, newName);
        }
    }

    @Transactional
    public void shareForCheckedUsers(List<File> checked_files, List<Folder> checked_folders, String shareFor, boolean shareInFolder) {
        List<User> receivers = userDAO.getShareReceivers(shareFor);
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        for (int i = 0; i < receivers.size(); i++) {
            if (receivers.get(i).getLogin().equals(login)) {
                receivers.remove(receivers.get(i));
                i--;
            }
        }

        if (checked_files != null) {
            for (File file : checked_files) {
                file.addToShareFor(receivers);
                file.setShareInFolder(shareInFolder);
            }
            fileDAO.changeShare(checked_files);
        }

        if (checked_folders != null) {
            for (Folder folder : checked_folders) {
                folder.addToShareFor(receivers);
                folder.setShareInFolder(shareInFolder);
                shareForCheckedUsers(folder.getFiles(), folder.getFolders(), shareFor, true);
            }
            folderDAO.changeShare(checked_folders);
        }
    }

    @Transactional
    public void cancelShareForCheckedUsers(List<File> checked_files, List<Folder> checked_folders, int[] cancel_share_for_users) {
        List<User> receivers = userDAO.getUsersById(cancel_share_for_users);

        if (checked_files != null) {
            for (File file : checked_files) {
                file.removeFromShareFor(receivers);
            }
            fileDAO.changeShare(checked_files);
        }

        if (checked_folders != null) {
            for (Folder folder : checked_folders) {
                folder.removeFromShareFor(receivers);
            }
        }
    }

    @Transactional
    public void removeFromShareWithMe(int[] checked_files_id, int[] checked_folders_id, User user) {
        List<User> receivers = new ArrayList<>();
        receivers.add(user);

        List<File> targetsFiles = null;
        if (checked_files_id != null) {
            targetsFiles = fileDAO.getListFilesById(checked_files_id);
            for (File file : targetsFiles) {
                file.removeFromShareFor(receivers);
            }
            fileDAO.changeShare(targetsFiles);
        }

        List<Folder> targetsFolder = null;
        if (checked_folders_id != null) {
            targetsFolder = folderDAO.getListFoldersById(checked_folders_id);
            for (Folder folder : targetsFolder) {
                folder.removeFromShareFor(receivers);
            }
            folderDAO.changeShare(targetsFolder);
        }
    }

    @Transactional
    public void addToMe(int[] checked_files_id, int[] checked_folders_id) {
        User currentUser = getCurrentUser();
        if (checked_files_id != null) {
            List<File> listOfAddFiles = getListFilesById(checked_files_id);
            addSharedFileToMyStore(listOfAddFiles, currentUser, null, null);
        }

        if (checked_folders_id != null) {
            List<Folder> ListOfAddFolders = getListFolderById(checked_folders_id);
            addSharedFolderToMyStore(ListOfAddFolders, currentUser, null, null);
        }
    }

    @Transactional
    public void removeToFolder(int[] checked_files_id, int[] checked_folders_id, String move_to) {
        Folder target = null;
        if (!move_to.equals("tree")) {
            target = getFolder(Integer.valueOf(move_to));
        }
        if (checked_files_id != null) {
            fileDAO.move_to(checked_files_id, target);
        }
        if (checked_folders_id != null) {
            folderDAO.move_to(checked_folders_id, target);
        }
    }

    public long getSizeBusyMemory(int userId) {
        return fileDAO.getMemoryBusySize(userId);
    }

    private void createPathForElement(StringBuilder sb, Folder curFolder) {
        if (curFolder != null) {
            createPathForElement(sb, curFolder.getParentFolder());
            sb.append(curFolder.getName());
            sb.append("/");
        }
    }

    private void addSharedFileToMyStore(List<File> listOfAddFiles, User user, Folder curFolder, Folder addFolder) {
        long all = (long) 10 * GBYTE;
        for (File file : listOfAddFiles) {
            StringBuilder relativePath = new StringBuilder();
            createPathForElement(relativePath, curFolder);
            if (file.getSize() <= all - getSizeBusyMemory(user.getId())) {
                fileDAO.upload(new File(file.getName(), file.getSize(), file.getType(), user, addFolder, false, false, file.getData(), false));
            } else {
                System.out.println("Havn't need memory");
            }
        }
    }

    private void addSharedFolderToMyStore(List<Folder> listOfAddFolders, User user, Folder shareFolder, Folder addFolder) {
        for (Folder folder : listOfAddFolders) {
            folderDAO.save(new Folder(folder.getName(), user, addFolder, false, false, false));
            Folder tf = folderDAO.getFolder(user, folder.getName(), addFolder);

            if (folder.getFiles().size() != 0) {
                addSharedFileToMyStore(folder.getFiles(), user, folder, tf);
            }

            if (folder.getFolders().size() != 0) {
                addSharedFolderToMyStore(folder.getFolders(), user, folder, tf);
            }
        }
    }

    public List[] getContentById(int[] checked_files_id, int[] checked_folders_id) {
        List[] content = new List[2];
        if (checked_files_id != null) {
            content[0] = getListFilesById(checked_files_id);
        }
        if (checked_folders_id != null) {
            content[1] = getListFolderById(checked_folders_id);
        }
        return content;
    }

    private User getCurrentUser() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return userDAO.getUser(login);
    }

    private void uploadFile(MultipartFile file, User user, Folder curFolder) {
        String fileName = file.getOriginalFilename();
        long size = file.getSize();
        long all = (long) 10 * GBYTE;
        String mimeType = file.getContentType();
        if (size <= all - getSizeBusyMemory(user.getId())) {
            String type = mimeType;
            File fileForDAO = null;
            try {
                fileForDAO = new File(fileName, size, type, user, curFolder, false, false, file.getBytes(), false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileDAO.upload(fileForDAO);
        } else {
            System.out.println("Havn't need memory");
        }
    }

    private void prepareNewFolderForUpload(MultipartFile[] files, String[] pathes, User user, Folder curFolder, String structure) {
        Map<String, Folder> map = new HashMap<>();
        try {
            for (int i = 0; i < pathes.length; i++) {
                if (files[i].getSize() > TempContentConfig.MAX_SIZE_OF_FILE) {
                    continue;
                } else if (!pathes[i].contains("/")) {
                    File file = new File(pathes[i], files[i].getSize(), files[i].getContentType(),
                            user, curFolder, false, false, files[i].getBytes(), false);
                    curFolder.getFiles().add(file);
                } else if (pathes[i].contains("/")) {
                    String pathToFolderWithThisFile = pathes[i].substring(0, pathes[i].lastIndexOf("/"));
                    //точка начала имени родительской папки файла
                    int startOfTargetFolderName = pathToFolderWithThisFile.lastIndexOf("/") + 1;
                    String targetFolderNameAndFileName = pathes[i].substring(startOfTargetFolderName);
                    String targetFolderName = targetFolderNameAndFileName.substring(0, targetFolderNameAndFileName.indexOf("/"));
                    if (map.containsKey(pathToFolderWithThisFile)) {
                        //если папка уже есть в нее просто добавляеться файл
                        map.get(pathToFolderWithThisFile).getFiles().add(new File(targetFolderNameAndFileName.substring(targetFolderNameAndFileName.lastIndexOf("/") + 1), files[i].getSize(), files[i].getContentType(),
                                user, map.get(pathToFolderWithThisFile), false, false, files[i].getBytes(), false));
                    } else {
                        //если такой папки еще нет
                        //изначально считаеться ,что добавляемая пользователем папка родительская для текущей папки
                        Folder parentFolderForFolderWithThisFile = curFolder;

                        //если добавляемая пользователем папка не родительская для
                        // текущей папки ,то определяем родительскую и достаем её из карты
                        if (startOfTargetFolderName > 0) {
                            String parentFolderPath = pathToFolderWithThisFile.substring(0, pathToFolderWithThisFile.indexOf("/"));
                            parentFolderForFolderWithThisFile = map.get(parentFolderPath);
                        }
                        Folder newFolder = new Folder(targetFolderName, user, parentFolderForFolderWithThisFile, false, false, false);
                        parentFolderForFolderWithThisFile.getFolders().add(newFolder);
                        map.put(pathToFolderWithThisFile, newFolder);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

                             /*   DOWNLOAD  */

    private void downloadContent(List<File> listCheckedFiles, List<Folder> listCheckedFolder) {
        if ((listCheckedFolder.size() != 0) || (listCheckedFiles.size() > 1)) {
            downloadSeveralFiles(listCheckedFiles, listCheckedFolder);
        } else if ((listCheckedFiles.size() == 1) && (listCheckedFolder.size() == 0)) {
            downloadSingleFile(listCheckedFiles.get(0));
        }
    }

    private void downloadSeveralFiles(List<File> listCheckedFiles, List<Folder> listCheckedFolder) {
        int BUFFER_SIZE = 1024;
        try {
            StringBuilder structure = new StringBuilder();
            String archiveName = randomString(8) + ".zip";
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(archiveName));

            prepareZipFileForDownload(out, listCheckedFiles, listCheckedFolder, structure);
            out.flush();
            out.close();

            java.io.File tempFile = new java.io.File(archiveName);
            FileInputStream inputStream = new FileInputStream(tempFile);
            httpServletResponse.setContentType("application/zip");
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + archiveName);
            OutputStream os = httpServletResponse.getOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush();
            os.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void downloadSingleFile(File file) {
        httpServletResponse.setContentType(file.getType());
        httpServletResponse.setContentLength((int) file.getSize());
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
        OutputStream os = null;
        try {
            os = httpServletResponse.getOutputStream();
            os.write(file.getData());
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void prepareZipFileForDownload(ZipOutputStream out, List<File> listCheckedFiles, List<Folder> listCheckedFolder, StringBuilder structure) throws IOException {
        if (listCheckedFiles.size() != 0) {
            for (File file : listCheckedFiles) {
                if (!file.isInbin()) {
                    ZipEntry entry = (new ZipEntry(structure.toString() + file.getName()));
                    entry.setSize(file.getSize());
                    out.putNextEntry(entry);

                    InputStream is = new ByteArrayInputStream(file.getData());
                    int bytesIn;
                    byte[] readBuffer = new byte[512];
                    while ((bytesIn = is.read(readBuffer)) != -1) {
                        out.write(readBuffer, 0, bytesIn);
                    }
                    out.closeEntry();
                    is.close();
                }
            }
        }

        if (listCheckedFolder.size() != 0) {
            for (Folder folder : listCheckedFolder) {
                if (!folder.isInbin()) {
                    structure.append(folder.getName() + "/");
                    if (folder.getFiles().size() == 0 && folder.getFolders().size() == 0) {
                        out.putNextEntry(new ZipEntry(structure.toString()));
                        out.closeEntry();
                    } else {
                        prepareZipFileForDownload(out, folder.getFiles(), folder.getFolders(), structure);
                    }
                    structure.delete(structure.toString().lastIndexOf(folder.getName()), structure.length());
                }
            }
        }
    }
    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static SecureRandom rnd = new SecureRandom();

    private String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
}
