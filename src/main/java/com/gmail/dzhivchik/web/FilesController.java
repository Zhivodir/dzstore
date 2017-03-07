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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(Model model,
                         @RequestParam(value="file", required=false) MultipartFile file,
                         @RequestParam(value="files", required=false) MultipartFile[] files,
                         @RequestParam Integer currentFolder){

        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);

        Folder curFolder = null;
        if(currentFolder != -1) {
            curFolder = contentService.getFolder(currentFolder);
        }

        if(file != null){
            uploadFile(file, user, curFolder, login);
        }

        if(files != null){
            for (MultipartFile currentFile : files) {
                uploadFile(currentFile, user, curFolder, login);
            }
        }

        if(currentFolder != -1){
            model.addAttribute("f", currentFolder);
            return "redirect:/folder";}
        return "redirect:/index";
    }


    @RequestMapping(value = "/actions_above_checked_files", method = RequestMethod.POST)
    public String actionsAboveCheckedFiles(Model model,
                                           @RequestParam(value="checked_files_id", required=false) int[] checked_files_id,
                                           @RequestParam(value="checked_folders_id", required=false) int[] checked_folders_id,
                                           @RequestParam(value="download", required=false) String download,
                                           @RequestParam(value="delete", required=false) String delete,
                                           @RequestParam Integer currentFolder){

        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        if(delete != null){
            deleteContent(model, checked_files_id, checked_folders_id, login);
        }

        if(download != null){
            System.out.println("download");
        }

        if(currentFolder != -1){
            model.addAttribute("f", currentFolder);
            return "redirect:/folder";}
        return "redirect:/index";
    }


    public void uploadFile(MultipartFile file, User user, Folder curFolder, String login){
        String fileName = file.getOriginalFilename();
        long size = file.getSize();
        String type = "test";
        com.gmail.dzhivchik.domain.File fileForDAO = new com.gmail.dzhivchik.domain.File(fileName, size, type, user, curFolder);
        java.io.File convFile = new java.io.File(fileName);
        contentService.uploadFile(fileForDAO);
        try {
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream("c:/DevKit/Temp/dzstore/" + login + "/" + convFile);
            fos.write(file.getBytes());
            fos.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }


    public void deleteContent(Model model, int[] checked_files_id, int[] checked_folders_id, String login){
        if(checked_files_id != null) {
            File[] files = contentService.deleteCheckedFiles(checked_files_id);

            if (files.length != 0) {
                for (File file : files) {
                    new java.io.File("c:/DevKit/Temp/dzstore/" + login + "/" + file.getName()).delete();
                }
            }
        }
        if(checked_folders_id != null){
            Folder[] folders = contentService.deleteCheckedFolders(checked_folders_id);
        }
    }
}



