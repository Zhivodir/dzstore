package com.gmail.dzhivchik.web;

import com.gmail.dzhivchik.domain.File;
import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.service.Impl.ContentService;
import com.gmail.dzhivchik.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by User on 21.02.2017.
 */

@Controller
@RequestMapping("/")
public class FilesController {

    @Autowired
    private ContentService contentService;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(Model model,
                         @RequestParam(value = "file", required = false) MultipartFile file,
                         @RequestParam(value = "files", required = false) MultipartFile[] files,
                         @RequestParam String typeOfView,
                         @RequestParam(value = "structure", required = false) String structure,
                         @RequestParam Integer currentFolderID) {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);

        Folder curFolder = null;
        if (currentFolderID != null) {
            curFolder = contentService.getFolder(currentFolderID);
        }

        if (file != null) {
            uploadFile(file, user, curFolder, login);
        }

        if (files != null) {
            String[] pathes = null;
            if(structure.startsWith(",")){
                structure = structure.substring(1);
            }
            String targetFolderName = structure.substring(0, structure.indexOf("/"));
            structure = structure.replaceAll(targetFolderName + "/", "");
            Folder targetFolder = new Folder(targetFolderName, user, curFolder, false, false, false);
            pathes = structure.split(";");
            prepareNewFolderForUpload(files, pathes, user, targetFolder, structure);
            contentService.uploadFolder(targetFolder);
        }
        model.addAttribute("currentFolderID", currentFolderID);
        if(typeOfView.equals("index")){
            return "redirect:/";
        }
        return "redirect:/" + typeOfView;
    }


    @RequestMapping(value = "/actions_above_checked_files", method = RequestMethod.POST)
    public String actionsAboveCheckedFiles(Model model,
                                           @RequestParam(value = "checked_files_id", required = false) int[] checked_files_id,
                                           @RequestParam(value = "checked_folders_id", required = false) int[] checked_folders_id,
                                           @RequestParam(value = "move_to", required = false) String move_to,
                                           @RequestParam(value = "replace", required = false) String replace,
                                           @RequestParam(value = "download", required = false) String download,
                                           @RequestParam(value = "remove", required = false) String remove,
                                           @RequestParam(value = "delete", required = false) String delete,
                                           @RequestParam(value = "restore", required = false) String restore,
                                           @RequestParam(value = "starred", required = false) String starred,
                                           @RequestParam(value = "removestar", required = false) String removestar,
                                           @RequestParam(value = "rename", required = false) String rename,
                                           @RequestParam(value = "name", required = false) String name,
                                           @RequestParam(value = "share", required = false) String share,
                                           @RequestParam(value = "shareFor", required = false) String shareFor,
                                           @RequestParam(value = "addtome", required = false) String addtome,
                                           @RequestParam String typeOfView,
                                           @RequestParam(value = "currentFolderID", required = false) Integer currentFolderID,
                                           final RedirectAttributes redirectAttributes) {

        if (checked_files_id != null || checked_folders_id != null) {
            String login = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.getUser(login);

            if (rename != null && name != null) {
                if (((checked_files_id != null && checked_files_id.length == 1) && checked_folders_id == null) ||
                        ((checked_folders_id != null && checked_folders_id.length == 1) && checked_files_id == null)) {
                    contentService.rename(login, checked_files_id, checked_folders_id, name);
                }
            }

            if (starred != null) {
                contentService.changeStar(checked_files_id, checked_folders_id, true);
            }

            if (removestar != null) {
                contentService.changeStar(checked_files_id, checked_folders_id, false);
            }

            if (remove != null) {
                if(typeOfView.equals("shared")){
                    contentService.cancelShare(checked_files_id, checked_folders_id, user);
                }else {
                    contentService.in_out_bin(checked_files_id, checked_folders_id, true);
                }
            }

            if (delete != null) {
                contentService.deleteCheckedContent(checked_files_id, checked_folders_id, login);
            }

            if (restore != null) {
                contentService.in_out_bin(checked_files_id, checked_folders_id, false);
            }

            if (download != null) {
                List<File> listCheckedFiles = new ArrayList<>();
                if (checked_files_id != null) {
                    listCheckedFiles = contentService.getListFilesById(checked_files_id);
                }

                List<Folder> listCheckedFolder = new ArrayList<>();
                if (checked_folders_id != null) {
                    listCheckedFolder = contentService.getListFolderById(checked_folders_id);
                }
                downloadContent(listCheckedFiles, listCheckedFolder);
            }

            if(share != null) {
                List[] content = contentService.getContentById(checked_files_id, checked_folders_id);
                contentService.share(content[0], content[1], shareFor, false);
            }

            if(addtome != null) {
                contentService.addtome(checked_files_id, checked_folders_id, user);
            }

            if(replace != null) {
                if(move_to.equals("tree")){
                    contentService.move_to(checked_files_id, checked_folders_id, user, null);
                }else {
                    Folder target = contentService.getFolder(Integer.valueOf(move_to));
                    contentService.move_to(checked_files_id, checked_folders_id, user, target);
                }
            }
        }
        redirectAttributes.addFlashAttribute("currentFolderID", currentFolderID);
        if(typeOfView.equals("index")){
            return "redirect:/";
        }
        return "redirect:/" + typeOfView;
    }


    public void uploadFile(MultipartFile file, User user, Folder curFolder, String login){
        String fileName = file.getOriginalFilename();
        long size = file.getSize();
        long all = (long)10*1024*1024*1024;
        String mimeType = file.getContentType();
        if(size <= all - contentService.getSizeBusyMemory(user)){
            String type = mimeType;
            File fileForDAO = null;
            try {
                fileForDAO = new File(fileName, size, type, user, curFolder, false, false, file.getBytes(), false);
            }catch (IOException e){e.printStackTrace();}
            contentService.uploadFile(fileForDAO);
        }else{
            System.out.println("Havn't need memory");
        }
    }

    public void prepareNewFolderForUpload(MultipartFile[] files, String[] pathes, User user, Folder curFolder, String structure){
        Map<String, Folder> map = new HashMap<>();
        try {
            for (int i = 0; i < pathes.length; i++) {
                if (!pathes[i].contains("/")) {
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
                        map.get(pathToFolderWithThisFile).getFiles().add(new File(targetFolderNameAndFileName.substring(targetFolderNameAndFileName.lastIndexOf("/")+1), files[i].getSize(), files[i].getContentType(),
                                user, map.get(pathToFolderWithThisFile), false, false, files[i].getBytes(), false));
                    } else {
                        //если такой папки еще нет
                        //изначально считаеться ,что добавляемая пользователем папка родительская для текущей папки
                        Folder parentFolderForFolderWithThisFile = curFolder;


                        //если добавляемая пользователем папка не родительская для
                        // текущей папки ,то определяем родительскую и достаем её из карты
                        if(startOfTargetFolderName > 0) {
                            String parentFolderPath = pathToFolderWithThisFile.substring(0, pathToFolderWithThisFile.indexOf("/"));
                            parentFolderForFolderWithThisFile = map.get(parentFolderPath);
                        }
                        Folder newFolder = new Folder(targetFolderName, user, parentFolderForFolderWithThisFile, false, false, false);
//                        newFolder.getFiles().add(new File(targetFolderNameAndFileName.substring(targetFolderNameAndFileName.lastIndexOf("/")+1), files[i].getSize(), files[i].getContentType(),
//                                user, map.get(pathToFolderWithThisFile), false, false, files[i].getBytes(), false));
                        parentFolderForFolderWithThisFile.getFolders().add(newFolder);
                        map.put(pathToFolderWithThisFile, newFolder);
                    }
                }
            }
        }catch (IOException e){e.printStackTrace();}
    }


                         /*   DOWNLOAD  */

    public void downloadContent(List<File> listCheckedFiles, List<Folder> listCheckedFolder) {
        if ((listCheckedFolder.size() != 0) || (listCheckedFiles.size() > 1)) {
            downloadSeveralFiles(listCheckedFiles, listCheckedFolder);
        } else if ((listCheckedFiles.size() == 1)&&(listCheckedFolder.size() == 0)) {
            downloadSingleFile(listCheckedFiles.get(0));
        }
    }


    public void downloadSeveralFiles(List<File> listCheckedFiles, List<Folder> listCheckedFolder){
        int BUFFER_SIZE = 1024;
        long size = 0;
        try {
            StringBuilder structure = new StringBuilder();
            String archiveName = randomString(8) + ".zip";
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(archiveName));

            prepareZipFileForDownload(size, out, listCheckedFiles, listCheckedFolder, structure);
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
        }catch(IOException e){e.printStackTrace();}
    }


    public void downloadSingleFile(File file){
        httpServletResponse.setContentType(file.getType());
        httpServletResponse.setContentLength((int)file.getSize());
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
        OutputStream os = null;
        try {
            os = httpServletResponse.getOutputStream();
            os.write(file.getData());
            os.flush();
            os.close();
        }catch (IOException e){e.printStackTrace();}
    }


    public void prepareZipFileForDownload(long size, ZipOutputStream out, List<File> listCheckedFiles,
                                          List<Folder> listCheckedFolder, StringBuilder structure) throws IOException{
        if (listCheckedFiles.size() != 0) {
            for (File file : listCheckedFiles) {
                if (!file.isInbin()) {
                    ZipEntry entry = (new ZipEntry(structure.toString() + file.getName()));
                    entry.setSize(file.getSize());
                    out.putNextEntry(entry);

                    InputStream is = new ByteArrayInputStream(file.getData());
                    int bytesIn;
                    byte[] readBuffer = new byte[512];
                    while((bytesIn = is.read(readBuffer)) != -1){
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
                    }else {
                        prepareZipFileForDownload(size, out, folder.getFiles(), folder.getFolders(), structure);
                    }
                    structure.delete(structure.toString().lastIndexOf(folder.getName()), structure.length());
                }
            }
        }
    }

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }
}
