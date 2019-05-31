package com.gmail.dzhivchik.web;

import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.Sender;
import com.gmail.dzhivchik.domain.SpringSecurityUser;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.service.Impl.ContentService;
import com.gmail.dzhivchik.service.UserService;
import com.gmail.dzhivchik.web.dto.ChangesInAccessForUsers;
import com.gmail.dzhivchik.web.dto.SelectedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/")
public class ContentController {
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

    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public String actionsAboveCheckedFiles(@RequestParam(value = "selectedFiles", required = false) int[] checked_files_id,
                                           @RequestParam(value = "selectedFolders", required = false) int[] checked_folders_id,
                                           @RequestParam String typeOfView,
                                           @RequestParam(value = "currentFolderID", required = false) Integer currentFolderID,
                                           final RedirectAttributes redirectAttributes) {
        contentService.downloadContent(checked_files_id, checked_folders_id);
        redirectAttributes.addFlashAttribute("currentFolderID", currentFolderID);
        if (typeOfView.equals("index")) {
            return "redirect:/";
        }
        return "redirect:/" + typeOfView;
    }

    @RequestMapping(value = "/createFolder", method = RequestMethod.POST)
    public @ResponseBody String createNewFolder(@RequestParam int currentFolderId, @RequestParam String newFolderName) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        Folder curFolder = null;
        if (currentFolderId != -1) {
            curFolder = contentService.getFolder(currentFolderId);
        }
        Folder folder = new Folder(newFolderName, user, curFolder, false, false, false);
        contentService.createFolder(folder);
        return "Ok";
    }

    @RequestMapping(value = "/renameContent", method = RequestMethod.POST)
    public ResponseEntity renameContent(@RequestParam String newName, @RequestParam String contentType, @RequestParam int contentId) {
        SpringSecurityUser user = (SpringSecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        contentService.rename(contentType, contentId, newName, user.getId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/removeContent", method = RequestMethod.POST)
    public ResponseEntity removeContent(@ModelAttribute SelectedContent selectedContent, @RequestParam String typeOfView) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        if (typeOfView.equals("shared")) {
            contentService.removeFromShareWithMe(selectedContent.getSelectedFiles(), selectedContent.getSelectedFolders(), user);
        } else {
            contentService.removeInBin(selectedContent.getSelectedFiles(), selectedContent.getSelectedFolders(), true);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/restoreContent", method = RequestMethod.POST)
    public ResponseEntity restoreContent(@ModelAttribute SelectedContent selectedContent) {
        contentService.removeInBin(selectedContent.getSelectedFiles(), selectedContent.getSelectedFolders(), false);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteContent", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public @ResponseBody String deleteContent(@ModelAttribute SelectedContent selectedContent) {
        contentService.deleteCheckedContent(selectedContent.getSelectedFiles(), selectedContent.getSelectedFolders());
        return getBusySpace() + "";
    }

    @RequestMapping(value = "/changeStarState", method = RequestMethod.POST)
    public ResponseEntity addStar(@ModelAttribute SelectedContent selectedContent, @RequestParam boolean state) {
        contentService.changeStar(selectedContent.getSelectedFiles(), selectedContent.getSelectedFolders(), state);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/changeSharedList", method = RequestMethod.POST)
    public ResponseEntity shareContent(@ModelAttribute SelectedContent selectedContent, @ModelAttribute ChangesInAccessForUsers changesInAccess) {
        List[] content = contentService.getContentById(selectedContent.getSelectedFiles(), selectedContent.getSelectedFolders());
        if (changesInAccess.getCancelShareForUsers() != null && changesInAccess.getCancelShareForUsers().length != 0) {
            contentService.cancelShareForCheckedUsers(content[0], content[1], changesInAccess.getCancelShareForUsers());
        } else if (changesInAccess.getShareForUsers() != null) {
            contentService.shareForCheckedUsers(content[0], content[1], changesInAccess.getShareForUsers(), false);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/moveTo", method = RequestMethod.POST)
    public ResponseEntity replaceContent(@ModelAttribute SelectedContent selectedContent, @RequestParam int moveTo) {
        contentService.moveToFolder(selectedContent.getSelectedFiles(), selectedContent.getSelectedFolders(), moveTo);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/addToMe", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public @ResponseBody String addToMe(@ModelAttribute SelectedContent selectedContent) {
        contentService.addToMe(selectedContent.getSelectedFiles(), selectedContent.getSelectedFolders());
        return getBusySpace() + "";
    }

    private void sendMessageToEmail() {
        Sender tlsSender = new Sender("dzhproject@gmail.com", "");
        tlsSender.send("This is Subject", "TLS: This is text!", "support@dzstore.com", "");
    }

    private long getBusySpace(){
        SpringSecurityUser user = (SpringSecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return contentService.getSizeBusyMemory(user.getId());
    }
}