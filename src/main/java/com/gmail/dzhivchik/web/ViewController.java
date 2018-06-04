package com.gmail.dzhivchik.web;

import com.gmail.dzhivchik.AuthorizedUser;
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
            List[] content = null;
            Integer parentsFolderID = null;
            if(currentFolderID != null && currentFolderID > 0) {
                Folder currentFolder = contentService.getFolder(currentFolderID);
                Folder parentsFolder = currentFolder.getParentFolder();
                List<Folder> forRelativePath = new ArrayList<>();
                getListRelativePath(currentFolder, forRelativePath);
                Collections.reverse(forRelativePath);
                forRelativePath.add(currentFolder);
                model.addAttribute("listForRelativePath", forRelativePath);
                content = contentService.getContent(currentFolder);
                if(parentsFolder != null) {
                    parentsFolderID = parentsFolder.getId();
                }
            } else {
                content = contentService.getContent(null);
            }
            model.addAttribute("parentsFolderID", parentsFolderID);
            model.addAttribute("content", content);
            model.addAttribute("currentFolderID", currentFolderID);
            model.addAttribute("user", user);
            model.addAttribute("busySpace", AuthorizedUser.getShowBusySize());
            model.addAttribute("typeOfView", "index");
            return "index";
    }


    @RequestMapping(value = "/starred")
    public String onStarred(Model model,
                            @RequestParam(value = "currentFolderID", required = false) Integer currentFolderID) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        model.addAttribute("content", contentService.getStarredContent());
        model.addAttribute("user", user);
        model.addAttribute("busySpace", AuthorizedUser.getShowBusySize());
        model.addAttribute("typeOfView", "starred");
        model.addAttribute("currentFolderID", currentFolderID);
        return "starred";
    }


    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(Model model, @RequestParam(value = "whatSearch", required = false) String whatSearch) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        if (whatSearch != null) {
            model.addAttribute("content", contentService.getListBySearch(whatSearch));
            model.addAttribute("user", user);
        }
        model.addAttribute("busySpace", AuthorizedUser.getShowBusySize());
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
        model.addAttribute("content", contentService.getSharedContent(currentFolderID));
        model.addAttribute("user", user);
        model.addAttribute("busySpace", AuthorizedUser.getShowBusySize());
        model.addAttribute("typeOfView", "shared");
        return "shared";
    }

    @RequestMapping(value = "/bin")
    public String toBin(Model model) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        model.addAttribute("content", contentService.getBinContent());
        model.addAttribute("user", user);
        model.addAttribute("busySpace", AuthorizedUser.getShowBusySize());
        model.addAttribute("typeOfView", "bin");
        return "bin";
    }


    private void getListRelativePath(Folder currentFolder, List<Folder> forRelativePath) {
        Folder parentFolder = currentFolder.getParentFolder();
        if (parentFolder != null) {
            forRelativePath.add(parentFolder);
            getListRelativePath(parentFolder, forRelativePath);
        }
    }
}
