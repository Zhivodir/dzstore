package com.gmail.dzhivchik.service.Impl;

import com.gmail.dzhivchik.config.TempContentConfig;
import com.gmail.dzhivchik.dao.FileDAO;
import com.gmail.dzhivchik.dao.FolderDAO;
import com.gmail.dzhivchik.dao.UserDAO;
import com.gmail.dzhivchik.domain.File;
import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.dto.Content;
import com.gmail.dzhivchik.dto.PathElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.SecureRandom;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.gmail.dzhivchik.config.TempContentConfig.*;
import static com.gmail.dzhivchik.utils.MemoryUtils.GBYTE;
import static com.gmail.dzhivchik.utils.SpringSecurityUtil.getSecurityUser;


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
    public Folder getFolder(int id) {
        return folderDAO.getFolder(id);
    }

    public Folder getReferenceFolder(int id) {
        return folderDAO.getReferenceFolder(id);
    }

    @Transactional
    public File getFile(Integer id) {
        return fileDAO.getFile(id);
    }

    @Transactional
    public void uploadFile(MultipartFile file, Integer currentFolderId) {
        User currentUser = userDAO.getUserReference(getSecurityUser().getId());
        Folder parentFolder = null;

        if (file != null && file.getSize() < TempContentConfig.MAX_SIZE_OF_FILE) {
            if (currentFolderId != -1) {
                parentFolder = getReferenceFolder(currentFolderId);
            }
            uploadFile(file, currentUser, parentFolder);
        }
    }

    @Transactional
    public void uploadFolder(MultipartFile[] files, String structure, Integer currentFolderId) {
        User currentUser = userDAO.getUserReference(getSecurityUser().getId());
        Folder parentFolder = null;

        if (files != null && structure != null) {
            if (structure.startsWith(",")) {
                structure = structure.substring(1);
            }
            String nameOfUploadFolder = structure.substring(0, structure.indexOf("/"));
            String pathToUploadFolder = nameOfUploadFolder;

            if (currentFolderId != -1) {
                parentFolder = getFolder(currentFolderId);
                pathToUploadFolder = parentFolder.getFullPath();
            }

            Folder uploadFolder = new Folder(nameOfUploadFolder, currentUser, parentFolder, false, false, false, pathToUploadFolder);
            String[] pathes = structure.replaceAll(nameOfUploadFolder + "/", "").split(";");
            try {
                prepareNewFolderForUpload(files, pathToUploadFolder, pathes, currentUser, uploadFolder);
            } catch (IOException e) {
                //ToDo - реакция на ошибку
            }
            folderDAO.save(uploadFolder);
        }
    }

    @Transactional
    public void download(List<Integer> checkedFilesId, List<Integer> checkedFoldersId) {
        List<File> listCheckedFiles = new ArrayList<>();
        if (checkedFilesId != null) {
            listCheckedFiles = getListFilesById(checkedFilesId);
        }

        List<Folder> listCheckedFolder = new ArrayList<>();
        if (checkedFoldersId != null) {
            listCheckedFolder = getListFolderById(checkedFoldersId);
        }
        downloadContent(listCheckedFiles, listCheckedFolder);
    }

    @Transactional(readOnly = true)
    public List<Content> getContent(int userId, int currentFolderId) {
        Folder currentFolder = currentFolderId == -1 ? null : getReferenceFolder(currentFolderId);
        List<Content> content = folderDAO.getList(userId, currentFolder);
        content.addAll(fileDAO.getList(userId, currentFolder));
        return content;
    }

    @Transactional
    public void deleteCheckedContent(List<Integer> checkedFilesId, List<Integer> checkedFoldersId) {
        if (checkedFilesId != null) {
            fileDAO.deleteGroup(checkedFilesId);
        }
        if (checkedFoldersId != null) {
            folderDAO.deleteGroup(checkedFoldersId);
        }
    }

    @Transactional
    public List<File> getListFilesById(List<Integer> checkedFilesId) {
        return fileDAO.getListFilesById(checkedFilesId);
    }

    @Transactional
    public List<Folder> getListFolderById(List<Integer> checkedFoldersId) {
        return folderDAO.getListFoldersById(checkedFoldersId);
    }

    @Transactional
    public void changeStar(List<Integer> checkedFilesId, List<Integer> checkedFoldersId, boolean stateOfStar) {
        if (checkedFilesId != null) {
            fileDAO.changeStar(checkedFilesId, stateOfStar);
        }
        if (checkedFoldersId != null) {
            folderDAO.changeStar(checkedFoldersId, stateOfStar);
        }
    }

    @Transactional
    public void removeInBin(List<Integer> checkedFilesId, List<Integer> checkedFoldersId, boolean stateOfInBinStatus) {
        if (checkedFilesId != null) {
            fileDAO.changeInBin(checkedFilesId, stateOfInBinStatus);
        }
        if (checkedFoldersId != null) {
            folderDAO.changeInBin(checkedFoldersId, stateOfInBinStatus);
        }
    }

    @Transactional
    public void makeCopy(List<Integer> selectedFiles, User user) {
        List<File> copyContent = fileDAO.getListFilesById(selectedFiles);
        copyContent.stream().forEach(file -> fileDAO.save(File.makeCopy(file, user)));
    }

    @Transactional
    public List<Content> getStarredContent(int userId) {
        List<Content> content = folderDAO.getStarredList(userId);
        content.addAll(fileDAO.getStarredList(userId));
        return content;
    }

    @Transactional
    public List<Content> getBinContent(int userId) {
        List<Content> content = folderDAO.getBinList(userId);
        content.addAll(fileDAO.getBinList(userId));
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
    public void shareForCheckedUsers(List<File> checkedFiles, List<Folder> checkedFolders, String shareFor, boolean shareInFolder) {
        List<User> receivers = userDAO.getShareReceivers(shareFor);
        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        for (int i = 0; i < receivers.size(); i++) {
            if (receivers.get(i).getLogin().equals(login)) {
                receivers.remove(receivers.get(i));
                i--;
            }
        }

        if (checkedFiles != null) {
            for (File file : checkedFiles) {
                file.addToShareFor(receivers);
                file.setShareInFolder(shareInFolder);
            }
            fileDAO.changeShare(checkedFiles);
        }

        if (checkedFolders != null) {
            for (Folder folder : checkedFolders) {
                folder.addToShareFor(receivers);
                folder.setShareInFolder(shareInFolder);
                shareForCheckedUsers(folder.getFiles(), folder.getFolders(), shareFor, true);
            }
            folderDAO.changeShare(checkedFolders);
        }
    }

    @Transactional
    public void cancelShareForCheckedUsers(List<File> checkedFiles, List<Folder> checkedFolders, String[] cancelShareForUsers) {
        List<User> receivers = userDAO.getUsersByEmail(cancelShareForUsers);

        if (checkedFiles != null) {
            for (File file : checkedFiles) {
                file.removeFromShareFor(receivers);
            }
            fileDAO.changeShare(checkedFiles);
        }

        if (checkedFolders != null) {
            for (Folder folder : checkedFolders) {
                folder.removeFromShareFor(receivers);
            }
            folderDAO.changeShare(checkedFolders);
        }
    }

    @Transactional
    public void removeFromShareWithMe(List<Integer> checkedFilesId, List<Integer> checkedFoldersId, User user) {
        List<User> receivers = new ArrayList<>();
        receivers.add(user);

        List<File> targetsFiles = null;
        if (checkedFilesId != null) {
            targetsFiles = fileDAO.getListFilesById(checkedFilesId);
            for (File file : targetsFiles) {
                file.removeFromShareFor(receivers);
            }
            fileDAO.changeShare(targetsFiles);
        }

        List<Folder> targetsFolder = null;
        if (checkedFoldersId != null) {
            targetsFolder = folderDAO.getListFoldersById(checkedFoldersId);
            for (Folder folder : targetsFolder) {
                folder.removeFromShareFor(receivers);
            }
            folderDAO.changeShare(targetsFolder);
        }
    }

    @Transactional
    public void addToMe(List<Integer> checkedFilesId, List<Integer> checkedFoldersId) {
        User currentUser = getCurrentUser();
        if (checkedFilesId != null) {
            List<File> listOfAddFiles = getListFilesById(checkedFilesId);
            addSharedFileToMyStore(listOfAddFiles, currentUser, null, null);
        }

        if (checkedFoldersId != null) {
            List<Folder> ListOfAddFolders = getListFolderById(checkedFoldersId);
            addSharedFolderToMyStore(ListOfAddFolders, currentUser, null);
        }
    }

    @Transactional
    public void moveToFolder(List<Integer> checkedFilesId, List<Integer> checkedFoldersId, int move_to) {
        Folder target = move_to == -1 ? null : getFolder(move_to);
        if (checkedFilesId != null) {
            fileDAO.moveTo(checkedFilesId, target);
        }
        if (checkedFoldersId != null) {
            folderDAO.moveTo(checkedFoldersId, target);
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
                fileDAO.save(new File(file.getName(), file.getSize(), file.getType(), user, addFolder, false, false, file.getData(), false));
            } else {
                System.out.println("Havn't need memory");
            }
        }
    }

    private void addSharedFolderToMyStore(List<Folder> listOfAddFolders, User user, Folder addFolder) {
        for (Folder folder : listOfAddFolders) {
            folderDAO.save(new Folder(folder.getName(), user, addFolder, false, false, false, folder.getPath()));
            Folder intoFolder = folderDAO.getFolder(user, folder.getName(), addFolder);

            if (folder.getNestedFilesQuantity() != 0) {
                addSharedFileToMyStore(folder.getFiles(), user, folder, intoFolder);
            }

            if (folder.getNestedFoldersQuantity() != 0) {
                addSharedFolderToMyStore(folder.getFolders(), user, intoFolder);
            }
        }
    }

    public List[] getContentById(List<Integer> checkedFilesId, List<Integer> checkedFoldersId) {
        List[] content = new List[2];
        if (checkedFilesId != null) {
            content[0] = getListFilesById(checkedFilesId);
        }
        if (checkedFoldersId != null) {
            content[1] = getListFolderById(checkedFoldersId);
        }
        return content;
    }

    private User getCurrentUser() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return userDAO.get(login);
    }

    private void uploadFile(MultipartFile file, User user, Folder curFolder) {
        long size = file.getSize();
        long allAvailableSize = (long) 10 * GBYTE;
        if (size <= allAvailableSize - getSizeBusyMemory(user.getId())) {
            try {
                fileDAO.save(new File(file, user, curFolder));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Havn't need memory");
        }
    }

    private void prepareNewFolderForUpload(MultipartFile[] files, String mainPath, String[] pathes, User user, Folder currentFolder) throws IOException {
        Map<String, Folder> map = new HashMap<>();

        for (int i = 0; i < pathes.length; i++) {
            if (isExceedMaximumFileSize(files[i].getSize())) {
                continue;
            } else if (!isInNestedFolder(pathes[i])) {
                currentFolder.addFile(new File(files[i], user, currentFolder));
            } else {
                String pathToParentFolder = pathes[i].substring(0, pathes[i].lastIndexOf("/"));
                int indexOfParentFolderName = pathToParentFolder.lastIndexOf("/") + 1;
                String parentFolderNameAndFileName = pathes[i].substring(indexOfParentFolderName);
                String parentFolderName = parentFolderNameAndFileName.substring(0, parentFolderNameAndFileName.indexOf("/"));

                if (!map.containsKey(pathToParentFolder)) {
                    Folder parentFolderForFolderWithThisFile = currentFolder;
                    //если добавляемая пользователем папка не родительская для
                    // текущей папки ,то определяем родительскую и достаем её из карты
                    if (indexOfParentFolderName > 0) {
                        String parentFolderPath = pathToParentFolder.substring(0, pathToParentFolder.indexOf("/"));
                        parentFolderForFolderWithThisFile = map.get(parentFolderPath);
                    }

                    String path = "";
                    if (mainPath != null) {
                        path = mainPath  + "/" + pathToParentFolder;
                    } else {
                        path = pathToParentFolder;
                    }

                    Folder newFolder = new Folder(parentFolderName, user, parentFolderForFolderWithThisFile, false, false, false, path);
                    parentFolderForFolderWithThisFile.addFolder(newFolder);
                    map.put(pathToParentFolder, newFolder);
                }
                Folder parentFolder = map.get(pathToParentFolder);
                parentFolder.addFile(new File(files[i], user, parentFolder));
            }
        }
    }

    private boolean isInNestedFolder(String path) {
        return path.contains("/");
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
            //ToDo - обработку ошибок
        }
    }

    private void downloadSingleFile(File file) {
        httpServletResponse.setContentType(file.getType());
        httpServletResponse.setContentLength((int) file.getSize());
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + file.getName());

        try {
            OutputStream os = httpServletResponse.getOutputStream();
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
                    byte[] readBuffer = new byte[BUFFER_SIZE];
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
                    if (folder.getNestedFilesQuantity() == 0 && folder.getNestedFoldersQuantity() == 0) {
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

    private static SecureRandom rnd = new SecureRandom();

    private String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(SYMBOL_FOR_ARCHIEVE_NAME.charAt(rnd.nextInt(SYMBOL_FOR_ARCHIEVE_NAME.length())));
        return sb.toString();
    }


    public List<PathElement> getIdsAndPathesForFolders(Folder folder) {
        int userId = folder.getUser().getId();
        String path = folder.getPath();
        StringBuilder sb = new StringBuilder(path);
        List<String> pathes = new ArrayList<>();
        String folderName = path;

        while (sb.length() > 0) {
            pathes.add(folderName);
            int index = sb.lastIndexOf("/");

            if (index != -1) {
                folderName = sb.substring(0, index);
                sb.delete(index, sb.length());
            } else {
                folderName = sb.substring(0, sb.length());
                sb.delete(0, sb.length());
            }
        }
        Collections.reverse(pathes);
        return folderDAO.getPathElements(pathes, userId);
    }
}