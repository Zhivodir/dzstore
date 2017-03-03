package com.gmail.dzhivchik.web;

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

import java.io.File;

/**
 * Created by User on 28.02.2017.
 */

@Controller
@RequestMapping("/")
public class FolderController {

    @Autowired
    private ContentService contentService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/folder", method = RequestMethod.GET)
    public String inFolder(Model model, @RequestParam Integer f){
        //id-shnik folder vnutr' kotoroy zaxodim
        model.addAttribute("f", f);
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        Folder currentFolder = contentService.getFolder(f);
        model.addAttribute("listOfFiles", contentService.listOfFiles(user, currentFolder));
        model.addAttribute("listOfFolders", contentService.listOfFolders(user, currentFolder));
        return "folder";
    }

    @RequestMapping(value = "/create_folder", method = RequestMethod.POST)
    public String createNewFolder(Model model, @RequestParam String nameOfFolder, @RequestParam Integer currentFolder){

        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        Folder curFolder = null;
        if(currentFolder != -1) {
            curFolder = contentService.getFolder(currentFolder);
        }
        System.out.println("Получаем обьект текущей папки: " + curFolder);
        File myPath = new File("c:/DevKit/Temp/dzstore/" + login + "/" + nameOfFolder);
        Folder folder = new Folder(nameOfFolder, user, curFolder);
        System.out.println("Получаем обьект новой папки: " + folder);
        contentService.createFolder(folder);
        myPath.mkdirs();
        if(currentFolder != -1){
            model.addAttribute("f", currentFolder);
            return "redirect:/folder";}
        return "redirect:/index";
    }
}
