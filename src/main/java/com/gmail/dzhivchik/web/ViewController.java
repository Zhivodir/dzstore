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

import java.util.ArrayList;
import java.util.Collections;
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
        model.addAttribute("user", user);
        model.addAttribute("busySpace", showBusySpace(user));
        return "index";
    }

    @RequestMapping(value = "/folder", method = RequestMethod.GET)
    public String inFolder(Model model, @RequestParam Integer f) {
        //id-shnik folder vnutr' kotoroy zaxodim
        model.addAttribute("f", f);
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        Folder currentFolder = contentService.getFolder(f);
        List<Folder> list = currentFolder.getFolders();
        List<Folder> forRelativePath = new ArrayList<>();
        getListRelativePath(currentFolder, forRelativePath);
        Collections.reverse(forRelativePath);
        forRelativePath.add(currentFolder);
        model.addAttribute("content", contentService.getContent(user, currentFolder));
        model.addAttribute("listForRelativePath", forRelativePath);
        model.addAttribute("user", user);
        model.addAttribute("busySpace", showBusySpace(user));
        return "folder";
    }

    @RequestMapping(value = "/starred")
    public String onStarred(Model model) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        model.addAttribute("content", contentService.getStarredContent(user));
        model.addAttribute("user", user);
        model.addAttribute("busySpace", showBusySpace(user));
        return "starred";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(Model model, @RequestParam(value = "whatSearch", required = false) String whatSearch) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        if (whatSearch != null) {
            model.addAttribute("content", contentService.getListBySearch(whatSearch, user));
            model.addAttribute("user", user);
        }
        model.addAttribute("busySpace", showBusySpace(user));
        return "search";
    }

    @RequestMapping(value = "/shared")
    public String shared(Model model) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        model.addAttribute("content", contentService.getSharedContent(user));
        model.addAttribute("user", user);
        model.addAttribute("busySpace", showBusySpace(user));
        return "shared";
    }

    public void getListRelativePath(Folder currentFolder, List<Folder> forRelativePath) {
        Folder parentFolder = currentFolder.getParentFolder();

        if (parentFolder != null) {
            forRelativePath.add(parentFolder);
            getListRelativePath(parentFolder, forRelativePath);
        }
    }

    public String[] showBusySpace(User user) {
        long filesSize = contentService.getSizeBusyMemory(user);
        String[] sizes = new String[2];
        if(filesSize/(1024*1024*1024) > 0){
            sizes[0] = filesSize/(1024*1024*1024) + "."
                    + String.valueOf(filesSize%(1024*1024*1024)).substring(0,2) + " Gb";
        }else if(filesSize/(1024*1024) > 0){
            sizes[0] = filesSize/(1024*1024)+ "."
                    + String.valueOf(filesSize%(1024*1024)).substring(0,2) + " Mb";
        }else if(filesSize/1024 > 0){
            sizes[0] = filesSize/1024 + "."
                    + String.valueOf(filesSize%1024).substring(0,2) + " Kb";
        }else{
            sizes[0] = filesSize + " bytes";
        }
        //Доступное по условиям тарифа. Пока захардкодено
        sizes[1] = "10 Gb";
        return sizes;
    }
}
