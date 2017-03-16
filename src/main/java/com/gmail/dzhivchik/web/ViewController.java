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

import java.util.List;

/**
 * Created by User on 15.03.2017.
 */

@Controller
@RequestMapping("/")
public class ViewController {
    @Autowired
    private ContentService contentService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/index")
    public String onIndex(Model model) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        model.addAttribute("content", contentService.getContent(user, null));
        return "index";
    }

    @RequestMapping(value = "/folder", method = RequestMethod.GET)
    public String inFolder(Model model, @RequestParam Integer f){
        //id-shnik folder vnutr' kotoroy zaxodim
        model.addAttribute("f", f);
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        Folder currentFolder = contentService.getFolder(f);
        List<Folder> list = currentFolder.getFolders();
        for (Folder folder:list) {
            System.out.println(folder.getName());
        }
        model.addAttribute("content", contentService.getContent(user, currentFolder));
        return "folder";
    }

    @RequestMapping(value = "/starred")
    public String onStarred(Model model) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        model.addAttribute("content", contentService.getStarredContent(user));
        return "starred";
    }

}
