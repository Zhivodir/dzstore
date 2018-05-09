package com.gmail.dzhivchik.web.ajax;

import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.service.Impl.ContentService;
import com.gmail.dzhivchik.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ajax/content")
public class AjaxContentController {

    final static private long MAX_SIZE_OF_FILE = 5120;

    @Autowired
    private ContentService contentService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/create_folder", method = RequestMethod.POST)
    public Folder createFolder(Model model,
        @RequestParam String nameOfFolder,
        @RequestParam Integer currentFolder,
        @RequestParam String typeOfView) {

            String login = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.getUser(login);
            Folder curFolder = null;
            if (currentFolder != null) {
                curFolder = contentService.getFolder(currentFolder);
            }
            Folder folder = new Folder(nameOfFolder, user, curFolder, false, false, false);
            contentService.createFolder(folder);
        return null;
    }
}
