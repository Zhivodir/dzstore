package com.gmail.dzhivchik.web;

import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.Sender;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.service.Impl.ContentService;
import com.gmail.dzhivchik.service.UserService;
import com.gmail.dzhivchik.web.dto.ChangesInAccessForUsers;
import com.gmail.dzhivchik.web.dto.PathElement;
import com.gmail.dzhivchik.web.dto.SelectedContent;
import com.gmail.dzhivchik.web.dto.datatables.DataTablesRequest;
import com.gmail.dzhivchik.web.dto.datatables.DataTablesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

import static com.gmail.dzhivchik.utils.SpringSecurityUtil.getSecurityUser;


@Controller
@RequestMapping("/")
public class ContentController {
    @Autowired
    private ContentService contentService;

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody String upload(@RequestParam(value = "file", required = false) MultipartFile file,
                @RequestParam(value = "files", required = false) MultipartFile[] files,
                @RequestParam(value = "structure", required = false) String structure,
                @RequestParam Integer currentFolderID) {
        contentService.uploadContent(file, files, structure, currentFolderID);
        return getBusySpace() + "";
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
        User user = userService.getReferenceUser(getSecurityUser().getId());
        Folder parentFolder = null;
        String path = null;
        if (currentFolderId != -1) {
            parentFolder = contentService.getFolder(currentFolderId);
            path = parentFolder.getPath() != null ? parentFolder.getPath() + "/" + parentFolder.getName() : parentFolder.getName();
        }
        Folder folder = new Folder(newFolderName, user, parentFolder, false, false, false, path);
        contentService.createFolder(folder);
        return "Ok";
    }

    @RequestMapping(value = "/renameContent", method = RequestMethod.POST)
    public ResponseEntity renameContent(@RequestParam String newName, @RequestParam String contentType, @RequestParam int contentId) {
        contentService.rename(contentType, contentId, newName, getSecurityUser().getId());
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

    @RequestMapping(value = "/isCurrentUserOwner", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Map isCurrentUserOwner(@RequestParam int folderId) {
        int currentUserId = getSecurityUser().getId();
        //ToDo - optimization
        Folder targetFolder = contentService.getFolder(folderId);
        boolean isOwner = currentUserId == targetFolder.getUser().getId();
        return Collections.singletonMap("isOwner", isOwner);
    }

    @RequestMapping(value = "/getPathToFolder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Map getPathToFolder(@RequestParam int folderId) {
        //ToDo - optimization
        Folder folder = contentService.getFolder(folderId);
        return Collections.singletonMap("response", contentService.getIdsAndPathesForFolders(folder));
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

    @RequestMapping(value = "/makeCopy", method = RequestMethod.POST)
    public @ResponseBody String makeCopy(@ModelAttribute SelectedContent selectedContent) {
        contentService.makeCopy(selectedContent.getSelectedFiles());
        return getBusySpace() + "";
    }

    @RequestMapping(value = "/addToMe", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public @ResponseBody String addToMe(@ModelAttribute SelectedContent selectedContent) {
        contentService.addToMe(selectedContent.getSelectedFiles(), selectedContent.getSelectedFolders());
        return getBusySpace() + "";
    }

    @RequestMapping(value = "/getUsersWithAccess/{contentType}/{contentId}", method = RequestMethod.POST)
    public @ResponseBody DataTablesResponse<User> loadListOfAccount(@RequestBody DataTablesRequest dtRequest,
                                               @PathVariable("contentType") String contentType, @PathVariable("contentId") int contentId) {
        List<User> users = new ArrayList<>();

        if (("folder").equals(contentType) && contentId != -1) {
            users = contentService.getFolder(contentId).getShareFor();
        } else if (("file").equals(contentType) && contentId != -1) {
            users = contentService.getFile(contentId).getShareFor();
        }
        return formDataTablesResponse(users, dtRequest);
    }

    private DataTablesResponse<User> formDataTablesResponse(List<User> data, DataTablesRequest dtRequest) {
        int total = data.size();

        DataTablesResponse<User> dtResponse = new DataTablesResponse<>();
        dtResponse.setDraw(dtRequest.getDraw());
        dtResponse.setRecordsTotal(total);
        dtResponse.setRecordsFiltered(total);
        dtResponse.setData(data);
        return dtResponse;
    }

    private void sendMessageToEmail() {
        Sender tlsSender = new Sender("dzhproject@gmail.com", "");
        tlsSender.send("This is Subject", "TLS: This is text!", "support@dzstore.com", "");
    }

    private long getBusySpace() {
        return contentService.getSizeBusyMemory(getSecurityUser().getId());
    }
}