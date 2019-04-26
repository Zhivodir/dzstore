package com.gmail.dzhivchik.web;

import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.Sender;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/")
public class ContentController {
    final static private long MAX_SIZE_OF_FILE = 5120;

    @Autowired
    private ContentService contentService;

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(Model model,
                         @RequestParam(value = "file", required = false) MultipartFile file,
                         @RequestParam(value = "files", required = false) MultipartFile[] files,
                         @RequestParam String typeOfView,
                         @RequestParam(value = "structure", required = false) String structure,
                         @RequestParam Integer currentFolderID) {

        contentService.uploadContent(file, files, structure, currentFolderID);

        model.addAttribute("currentFolderID", currentFolderID);
        if (typeOfView.equals("index")) {
            return "redirect:/";
        }
        return "redirect:/" + typeOfView;
    }


    @RequestMapping(value = "/actions_above_checked_files", method = RequestMethod.POST)
    public String actionsAboveCheckedFiles(@RequestParam(value = "checked_files_id", required = false) int[] checked_files_id,
                                           @RequestParam(value = "checked_folders_id", required = false) int[] checked_folders_id,
                                           @RequestParam(value = "cancel_share_for_users", required = false) int[] cancel_share_for_users,
                                           @RequestParam(value = "move_to", required = false) String move_to,
                                           @RequestParam(value = "name", required = false) String name,
                                           @RequestParam(value = "shareFor", required = false) String shareFor,
                                           @RequestParam(value = "action", required = false) String action,
                                           @RequestParam String typeOfView,
                                           @RequestParam(value = "currentFolderID", required = false) Integer currentFolderID,
                                           final RedirectAttributes redirectAttributes) {

        if (checked_files_id != null || checked_folders_id != null) {
            String login = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.getUser(login);

            switch (action) {
                case "remove":
                    if (typeOfView.equals("shared")) {
                        contentService.removeFromShareWithMe(checked_files_id, checked_folders_id, user);
                    } else {
                        contentService.removeInBin(checked_files_id, checked_folders_id, true);
                    }
                    break;
                case "rename":
                    if (name != null &&
                            ((checked_files_id != null && checked_files_id.length == 1) && checked_folders_id == null) ||
                            ((checked_folders_id != null && checked_folders_id.length == 1) && checked_files_id == null)) {
                        contentService.rename(checked_files_id, checked_folders_id, name);
                    }
                    break;
                case "share":
                    List[] content = contentService.getContentById(checked_files_id, checked_folders_id);
                    if (cancel_share_for_users != null && cancel_share_for_users.length != 0) {
                        contentService.cancelShareForCheckedUsers(content[0], content[1], cancel_share_for_users);
                    } else if (shareFor != null) {
                        contentService.shareForCheckedUsers(content[0], content[1], shareFor, false);
                    }
                    //sendMessageToEmail();
                    break;
                case "replace":
                    contentService.removeToFolder(checked_files_id, checked_folders_id, move_to);
                    break;
                case "Download":
                    contentService.downloadContent(checked_files_id, checked_folders_id);
                    break;
                case "delete":
                    contentService.deleteCheckedContent(checked_files_id, checked_folders_id);
                    break;
                case "Star":
                    contentService.changeStar(checked_files_id, checked_folders_id, true);
                    break;
                case "Remove star":
                    contentService.changeStar(checked_files_id, checked_folders_id, false);
                    break;
                case "Restore":
                    contentService.removeInBin(checked_files_id, checked_folders_id, false);
                    break;
                case "Add to me":
                    contentService.addToMe(checked_files_id, checked_folders_id);
                    break;
            }
        }
        redirectAttributes.addFlashAttribute("currentFolderID", currentFolderID);
        if (typeOfView.equals("index")) {
            return "redirect:/";
        }
        return "redirect:/" + typeOfView;
    }

    @RequestMapping(value = "/create_folder", method = RequestMethod.POST)
    public String createNewFolder(@RequestParam String nameOfFolder,
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
        if(typeOfView.equals("index")){
            return "redirect:/";
        }
        return "redirect:/" + typeOfView;
    }

    private void sendMessageToEmail() {
        Sender tlsSender = new Sender("dzhproject@gmail.com", "");
        tlsSender.send("This is Subject", "TLS: This is text!", "support@dzstore.com", "");
    }
}
