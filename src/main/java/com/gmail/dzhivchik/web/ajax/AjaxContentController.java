package com.gmail.dzhivchik.web.ajax;

import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.Sender;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.service.Impl.ContentService;
import com.gmail.dzhivchik.service.UserService;
import com.gmail.dzhivchik.to.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Content createFolder(@RequestParam String nameOfFolder,
                             @RequestParam Integer currentFolder,
                             @RequestParam String typeOfView) {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        Folder curFolder = null;
        if (currentFolder != null) {
            curFolder = contentService.getFolder(currentFolder);
        }
        Folder folder = new Folder(nameOfFolder, user, curFolder, false, false, false);
        return fromFolderToContent(contentService.createFolder(folder));
    }

    @ResponseBody
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public void remove(@RequestParam(value = "checked_files_id", required = false) int[] checked_files_id,
                       @RequestParam(value = "checked_folders_id", required = false) int[] checked_folders_id,
                       @RequestParam String typeOfView) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        if (typeOfView.equals("shared")) {
            contentService.removeFromShareWithMe(checked_files_id, checked_folders_id, user);
        } else {
            contentService.removeInBin(checked_files_id, checked_folders_id, true);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/restore", method = RequestMethod.POST)
    public void restore(@RequestParam(value = "checked_files_id", required = false) int[] checked_files_id,
                        @RequestParam(value = "checked_folders_id", required = false) int[] checked_folders_id) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        contentService.removeInBin(checked_files_id, checked_folders_id, false);
    }

    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void delete(@RequestParam(value = "checked_files_id", required = false) int[] checked_files_id,
                       @RequestParam(value = "checked_folders_id", required = false) int[] checked_folders_id) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        contentService.deleteCheckedContent(checked_files_id, checked_folders_id);
    }

    @ResponseBody
    @RequestMapping(value = "/rename", method = RequestMethod.POST)
    public void rename(@RequestParam(value = "checked_files_id", required = false) int[] checked_files_id,
                       @RequestParam(value = "checked_folders_id", required = false) int[] checked_folders_id,
                       @RequestParam(value = "name", required = false) String name) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        if (name != null &&
                ((checked_files_id != null && checked_files_id.length == 1) && checked_folders_id == null) ||
                ((checked_folders_id != null && checked_folders_id.length == 1) && checked_files_id == null)) {
            contentService.rename(checked_files_id, checked_folders_id, name);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/replace", method = RequestMethod.POST)
    public void replace(@RequestParam(value = "checked_files_id", required = false) int[] checked_files_id,
                        @RequestParam(value = "checked_folders_id", required = false) int[] checked_folders_id,
                        @RequestParam(value = "move_to", required = false) String move_to) {
        contentService.removeToFolder(checked_files_id, checked_folders_id, move_to);
    }

    @ResponseBody
    @RequestMapping(value = "/addStar", method = RequestMethod.POST)
    public void addStar(@RequestParam(value = "checked_files_id", required = false) int[] checked_files_id,
                        @RequestParam(value = "checked_folders_id", required = false) int[] checked_folders_id) {
        contentService.changeStar(checked_files_id, checked_folders_id, true);
    }

    @ResponseBody
    @RequestMapping(value = "/removeStar", method = RequestMethod.POST)
    public void removeStar(@RequestParam(value = "checked_files_id", required = false) int[] checked_files_id,
                           @RequestParam(value = "checked_folders_id", required = false) int[] checked_folders_id) {
        contentService.changeStar(checked_files_id, checked_folders_id, false);
    }

    @ResponseBody
    @RequestMapping(value = "/share", method = RequestMethod.POST)
    public void cancelShare(@RequestParam(value = "checked_files_id", required = false) int[] checked_files_id,
                            @RequestParam(value = "checked_folders_id", required = false) int[] checked_folders_id,
                            @RequestParam(value = "cancel_share_for_users", required = false) int[] cancel_share_for_users,
                            @RequestParam(value = "shareFor", required = false) String shareFor) {
        List[] content = contentService.getContentById(checked_files_id, checked_folders_id);
        if (cancel_share_for_users != null && cancel_share_for_users.length != 0) {
            contentService.cancelShareForCheckedUsers(content[0], content[1], cancel_share_for_users);
        }
        if (shareFor != null) {
            contentService.shareForCheckedUsers(content[0], content[1], shareFor, false);
        }
        //sendMessageToEmail();
    }

    @ResponseBody
    @RequestMapping(value = "/addToMe", method = RequestMethod.POST)
    public void addToMe(@RequestParam(value = "checked_files_id", required = false) int[] checked_files_id,
                        @RequestParam(value = "checked_folders_id", required = false) int[] checked_folders_id) {
        contentService.addToMe(checked_files_id, checked_folders_id);
    }

    private void sendMessageToEmail() {
        Sender tlsSender = new Sender("dzhproject@gmail.com", "");
        tlsSender.send("This is Subject", "TLS: This is text!", "support@dzstore.com", "");
    }

    private Content fromFolderToContent(Folder folder){
        return new Content(folder.getId(), folder.getName(), 0, folder.getUser().getLogin(), "");
    }
}
