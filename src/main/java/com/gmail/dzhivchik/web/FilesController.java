package com.gmail.dzhivchik.web;

import com.gmail.dzhivchik.domain.File;
import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.service.ContentService;
import com.gmail.dzhivchik.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
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
    private ServletContext context;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(Model model,
                         @RequestParam(value = "file", required = false) MultipartFile file,
                         @RequestParam(value = "files", required = false) MultipartFile[] files,
                         @RequestParam Integer currentFolder) {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);

        Folder curFolder = null;
        if (currentFolder != -1) {
            curFolder = contentService.getFolder(currentFolder);
        }

        if (file != null) {
            uploadFile(file, user, curFolder, login);
        }

        if (files != null) {
            for (MultipartFile currentFile : files) {
                uploadFile(currentFile, user, curFolder, login);
            }
        }

        if (currentFolder != -1) {
            model.addAttribute("f", currentFolder);
            return "redirect:/folder";
        }
        return "redirect:/index";
    }


    @RequestMapping(value = "/actions_above_checked_files", method = RequestMethod.POST)
    public String actionsAboveCheckedFiles(Model model,
                                           @RequestParam(value = "checked_files_id", required = false) int[] checked_files_id,
                                           @RequestParam(value = "checked_folders_id", required = false) int[] checked_folders_id,
                                           @RequestParam(value = "download", required = false) String download,
                                           @RequestParam(value = "delete", required = false) String delete,
                                           @RequestParam(value = "starred", required = false) String starred,
                                           @RequestParam(value = "removeStar", required = false) String removeStar,
                                           @RequestParam Integer currentFolder) {
        if (checked_files_id != null || checked_folders_id != null) {
            String login = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.getUser(login);

            if (starred != null) {
                changeStar(checked_files_id, checked_folders_id, true);
            }

            if (removeStar != null) {
                changeStar(checked_files_id, checked_folders_id, false);
            }

            if (delete != null) {
                deleteContent(model, checked_files_id, checked_folders_id, login);
            }

            if (download != null) {
                List<File> listCheckedFiles = new ArrayList<>();
                if (checked_files_id != null) {
                    listCheckedFiles = contentService.getListById(checked_files_id);
                }

                List<Folder> listCheckedFolder = new ArrayList<>();
                if (checked_folders_id != null) {
                    listCheckedFolder = contentService.getListFolderById(checked_folders_id);
                }
                downloadContent(user, login, currentFolder, listCheckedFiles, listCheckedFolder);
            }
        }

        if (currentFolder == null) {
            return "redirect:/starred";
        } else if (currentFolder != -1) {
            model.addAttribute("f", currentFolder);
            return "redirect:/folder";
        }
        return "redirect:/index";
    }


    public void uploadFile(MultipartFile file, User user, Folder curFolder, String login) {
        String fileName = file.getOriginalFilename();
        long size = file.getSize();
        String type = "test";
        com.gmail.dzhivchik.domain.File fileForDAO = new com.gmail.dzhivchik.domain.File(fileName, size, type, user, curFolder, false);
        java.io.File convFile = new java.io.File(fileName);
        contentService.uploadFile(fileForDAO);
        StringBuilder sb = new StringBuilder();
        createPathForFile(sb, curFolder);
        try {
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream("c:/DevKit/Temp/dzstore/" + login + "/" + sb.toString() + "/" + convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createPathForFile(StringBuilder sb, Folder curFolder){
        if(curFolder != null){
            sb.append(curFolder.getName());
            sb.append("/");
            createPathForFile(sb, curFolder.getParentFolder());
        }
    }


    public void deleteContent(Model model, int[] checked_files_id, int[] checked_folders_id, String login) {
        if (checked_files_id != null) {
            File[] files = contentService.deleteCheckedFiles(checked_files_id);

            if (files.length != 0) {
                for (File file : files) {
                    new java.io.File("c:/DevKit/Temp/dzstore/" + login + "/" + file.getName()).delete();
                }
            }
        }
        if (checked_folders_id != null) {
            Folder[] folders = contentService.deleteCheckedFolders(checked_folders_id);
        }
    }


    public void downloadContent(User user, String login, Integer currentFolder, List<File> listCheckedFiles, List<Folder> listCheckedFolder) {
        String filesPath = null;
        Folder curFolder = null;
        if (currentFolder == null || currentFolder == -1) {
            filesPath = "c:/DevKit/Temp/dzstore/" + login + "/";
        } else {
            curFolder = contentService.getFolder(currentFolder);
            filesPath = "c:/DevKit/Temp/dzstore/" + login + "/" + curFolder + "/";
        }
        String filesPathForDownload = null;
        if ((listCheckedFolder.size() != 0) || (listCheckedFiles.size() > 1)) {
            try {
                StringBuilder structure = new StringBuilder();
                ZipOutputStream out = new ZipOutputStream(new FileOutputStream("c:/DevKit/Temp/dzstore/Temp/archive.zip"));
                prepareToDownload(user, out, listCheckedFiles, listCheckedFolder, filesPath, structure);
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            filesPathForDownload = "c:/DevKit/Temp/dzstore/Temp/archive.zip";
        } else if (listCheckedFiles.size() == 1) {
            filesPathForDownload = listCheckedFiles.get(0).getName();
        }
        downloadFunction(filesPathForDownload);
    }

    public void prepareToDownload(User user, ZipOutputStream out, List<File> listCheckedFiles, List<Folder> listCheckedFolder, String filesPath, StringBuilder structure) throws FileNotFoundException, IOException {
        int BUFFER_SIZE = 4096;
        if (listCheckedFiles.size() != 0) {
            for (File file : listCheckedFiles) {
                String fullPath = filesPath + file.getName();
                out.putNextEntry(new ZipEntry(structure.toString() + file.getName()));
                FileInputStream fis = new FileInputStream(new java.io.File(fullPath));
                byte[] buffer = new byte[BUFFER_SIZE];
                int len;
                while ((len = fis.read(buffer)) >= 0)
                    out.write(buffer, 0, len);
                fis.close();
            }
        }

        if (listCheckedFolder.size() != 0) {
            for (Folder folder : listCheckedFolder) {
                System.out.println(folder.getFiles().size() + "   " + folder.getFolders().size());
                if (folder.getFiles().size() == 0 && folder.getFolders().size() == 0) {
                    structure.append(folder.getName() + "/");
                    out.putNextEntry(new ZipEntry(structure.toString() + "/"));
                }
                prepareToDownload(user, out, folder.getFiles(), folder.getFolders(), filesPath, structure);
            }
        }
    }

    public void downloadFunction(String archivePath) {
        int BUFFER_SIZE = 4096;
        java.io.File downloadFile = new java.io.File(archivePath);
        String mimeType = context.getMimeType(archivePath);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        try {
            FileInputStream inputStream = new FileInputStream(downloadFile);
            httpServletResponse.setContentType(mimeType);
            httpServletResponse.setContentLength((int) downloadFile.length());
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"",
                    downloadFile.getName());
            httpServletResponse.setHeader(headerKey, headerValue);
            OutputStream outStream = httpServletResponse.getOutputStream();

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        downloadFile.delete();
    }

    public void changeStar(int[] checked_files_id, int[] checked_folders_id, boolean stateOfStar) {
        contentService.changeStar(checked_files_id, checked_folders_id, stateOfStar);
    }
}