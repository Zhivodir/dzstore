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


    @RequestMapping(method = RequestMethod.GET)
    public String onIndex(Model model,
                          @RequestParam(value = "currentFolderID", required = false) Integer currentFolderID) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        if(currentFolderID != null && currentFolderID > 0) {
            Folder currentFolder = contentService.getFolder(currentFolderID);
            List<Folder> forRelativePath = new ArrayList<>();
            getListRelativePath(currentFolder, forRelativePath);
            Collections.reverse(forRelativePath);
            forRelativePath.add(currentFolder);
            model.addAttribute("listForRelativePath", forRelativePath);
            model.addAttribute("content", contentService.getContent(user, currentFolder));
        }else{
            model.addAttribute("content", contentService.getContent(user, null));
        }
        model.addAttribute("currentFolderID", currentFolderID);
        model.addAttribute("user", user);
        model.addAttribute("busySpace", showBusySpace(user));
        model.addAttribute("typeOfView", "index");
        return "index";
    }


    @RequestMapping(value = "/starred")
    public String onStarred(Model model,
                            @RequestParam(value = "currentFolderID", required = false) Integer currentFolderID) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        model.addAttribute("content", contentService.getStarredContent(user));
        model.addAttribute("user", user);
        model.addAttribute("busySpace", showBusySpace(user));
        model.addAttribute("typeOfView", "starred");
        model.addAttribute("currentFolderID", currentFolderID);
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
        model.addAttribute("typeOfView", "search");
        return "search";
    }


    @RequestMapping(value = "/shared", method = RequestMethod.GET)
    public String shared(Model model,
                         @RequestParam(value = "currentFolderID", required = false) Integer currentFolderID) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        if(currentFolderID != null && currentFolderID > 0) {
            Folder currentFolder = contentService.getFolder(currentFolderID);
            List<Folder> forRelativePath = new ArrayList<>();
            getListRelativePath(currentFolder, forRelativePath);
            Collections.reverse(forRelativePath);
            forRelativePath.add(currentFolder);
            model.addAttribute("listForRelativePath", forRelativePath);
        }
        model.addAttribute("currentFolderID", currentFolderID);
        model.addAttribute("content", contentService.getSharedContent(user, currentFolderID));
        model.addAttribute("user", user);
        model.addAttribute("busySpace", showBusySpace(user));
        model.addAttribute("typeOfView", "shared");
        return "shared";
    }

    @RequestMapping(value = "/bin")
    public String toBin(Model model) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        model.addAttribute("content", contentService.getBinContent(user));
        model.addAttribute("user", user);
        model.addAttribute("busySpace", showBusySpace(user));
        model.addAttribute("typeOfView", "bin");
        return "bin";
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
        long ostatok = 0;
        String[] sizes = new String[2];
        int size = 0;
        long wholePart = filesSize;
        long delitel = 1;
        int pow = 0;

        while (wholePart / 1024 > 0) {
            wholePart = wholePart / 1024;
            delitel = delitel * 1024;
            pow++;
        }
        ostatok = filesSize%delitel;
        size = (int)Math.round(Double.parseDouble(wholePart + "." + ostatok));

        switch (pow){
            case 0:
                if(filesSize != 0){
                    sizes[0] = size + " bytes";
                }
                break;
            case 1:
                sizes[0] = size + " Kb";
                break;
            case 2:
                sizes[0] = size + " Mb";
                break;
            case 3:
                sizes[0] = size + " Gb";
                break;
        }
        //Доступное по условиям тарифа. Пока захардкодено
        sizes[1] = "10 Gb";
        return sizes;
    }
}
