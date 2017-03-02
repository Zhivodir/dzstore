package com.gmail.dzhivchik.web;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by User on 26.01.2017.
 */

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    private ContentService contentService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/index")
    public String onIndex(Model model) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        model.addAttribute("listOfFiles", contentService.listOfFiles(user));
        model.addAttribute("listOfFolders", contentService.listOfFolders(user));
        return "index";
    }

    @RequestMapping(value = "/upload_file", method = RequestMethod.POST)
    public String uploadFile(@RequestParam MultipartFile file, Model model){
        if(!file.isEmpty()){
            String fileName = file.getOriginalFilename();
            long size = file.getSize();
            String type = "test";

            String login = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.getUser(login);

            com.gmail.dzhivchik.domain.File fileForDAO = new com.gmail.dzhivchik.domain.File(fileName, size, type, user);
            File convFile = new File(fileName);
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
        return "redirect:/index";
    }

    @RequestMapping(value = "/upload_folder", method = RequestMethod.POST)
    public String uploadFolder(@RequestParam MultipartFile[] files){
        if(files.length != 0){
            String login = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.getUser(login);

            for (MultipartFile file : files) {
                long size = file.getSize();
                String type = "test";
                String fileName = file.getOriginalFilename();
                File convFile = new File(fileName);

                com.gmail.dzhivchik.domain.File fileForDAO = new com.gmail.dzhivchik.domain.File(fileName, size, type, user);
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
        }
        return "redirect:/index";
    }
}
