package com.gmail.dzhivchik.web.ajax;

import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.service.Impl.ContentService;
import com.gmail.dzhivchik.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ajax/content")
public class AjaxContentController {

    final static private long MAX_SIZE_OF_FILE = 5120;

    @Autowired
    private ContentService contentService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/createFolder", method = RequestMethod.POST)
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

    //Надо что бы отправлял статус и на основе статусе уже либо удалять из DOM либо нет
    @ResponseBody
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public void remove(Model model,
                          @RequestParam(value = "checked_files_id", required = false) int[] checked_files_id,
                          @RequestParam(value = "checked_folders_id", required = false) int[] checked_folders_id,
                          @RequestParam String typeOfView){
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        if (typeOfView.equals("shared")) {
            contentService.removeFromShareWithMe(checked_files_id, checked_folders_id, user);
        } else {
            contentService.removeInBin(checked_files_id, checked_folders_id, true);
        }
    }
}
