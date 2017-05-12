package com.gmail.dzhivchik.web;

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

    @RequestMapping(value = "/create_folder", method = RequestMethod.POST)
    public String createNewFolder(Model model,
                                  @RequestParam String nameOfFolder,
                                  @RequestParam Integer currentFolder) {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        Folder curFolder = null;
        if (currentFolder != -1) {
            curFolder = contentService.getFolder(currentFolder);
        }
        Folder folder = new Folder(nameOfFolder, user, curFolder, false, false);
        StringBuilder sb = new StringBuilder();
        createPathForFile(sb, curFolder);
        File myPath = new File("c:/DevKit/Temp/dzstore/users_storages/" + login + "/" + sb.toString() + "/" + nameOfFolder);
        contentService.createFolder(folder);
        myPath.mkdirs();
        if (currentFolder != -1) {
            model.addAttribute("f", currentFolder);
            return "redirect:/folder";
        }
        return "redirect:/index";
    }


    public void createPathForFile(StringBuilder sb, Folder curFolder) {
        if (curFolder != null) {
            createPathForFile(sb, curFolder.getParentFolder());
            sb.append(curFolder.getName());
            sb.append("/");
        }
    }
}
