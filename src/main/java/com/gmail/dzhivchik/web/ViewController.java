package com.gmail.dzhivchik.web;

import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.domain.enums.PageType;
import com.gmail.dzhivchik.service.Impl.ContentService;
import com.gmail.dzhivchik.service.UserService;
import com.gmail.dzhivchik.web.dto.Content;
import com.gmail.dzhivchik.web.dto.datatables.DataTablesRequest;
import com.gmail.dzhivchik.web.dto.datatables.DataTablesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class ViewController {
    @Autowired
    private ContentService contentService;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String onIndex(Model model, @RequestParam(value = "currentFolderID", required = false) Integer currentFolderID) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        model.addAttribute("pageType", PageType.COMMON);
        model.addAttribute("parentsFolderID", null);
        model.addAttribute("currentFolderID", currentFolderID);
        model.addAttribute("user", user);
        model.addAttribute("busySpace", showBusySpace(user));
        model.addAttribute("typeOfView", "index");
        return "index_commons";
    }


    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(Model model, @RequestParam(value = "whatSearch", required = false) String whatSearch) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        model.addAttribute("pageType", PageType.SEARCH);
        model.addAttribute("user", user);
        model.addAttribute("whatSearch", whatSearch);
        model.addAttribute("busySpace", showBusySpace(user));
        model.addAttribute("typeOfView", "search");
        return "index_commons";
    }


    @RequestMapping(value = "/shared", method = RequestMethod.GET)
    public String shared(Model model, @RequestParam(value = "currentFolderID", required = false) Integer currentFolderID) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        model.addAttribute("pageType", PageType.SHARED);
        model.addAttribute("currentFolderID", currentFolderID);
        model.addAttribute("user", user);
        model.addAttribute("busySpace", showBusySpace(user));
        model.addAttribute("typeOfView", "shared");
        return "index_commons";
    }

    @RequestMapping(value = "/bin")
    public String toBin(Model model) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        model.addAttribute("pageType", PageType.BIN);
        model.addAttribute("user", user);
        model.addAttribute("busySpace", showBusySpace(user));
        model.addAttribute("typeOfView", "bin");
        return "index_commons";
    }

    @RequestMapping(value = "/starred")
    public String onStarred(Model model, @RequestParam(value = "currentFolderID", required = false) Integer currentFolderID) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        model.addAttribute("pageType", PageType.STARRED);
        model.addAttribute("user", user);
        model.addAttribute("busySpace", showBusySpace(user));
        model.addAttribute("typeOfView", "starred");
        model.addAttribute("currentFolderID", currentFolderID);
        return "index_commons";
    }


    private String[] showBusySpace(User user) {
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
        ostatok = filesSize % delitel;
        size = (int) Math.round(Double.parseDouble(wholePart + "." + ostatok));

        switch (pow) {
            case 0:
                if (filesSize != 0) {
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
        //Доступное по условиям тарифа. Пока захардкожено
        sizes[1] = "10 Gb";
        return sizes;
    }

    @RequestMapping(value = "/getContent/{currentFolderId}", method = RequestMethod.POST)
    public @ResponseBody DataTablesResponse<Content> getContent(@PathVariable int currentFolderId, @RequestBody DataTablesRequest dtRequest) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        return getAllCurrentFolderContent(user, currentFolderId, dtRequest);
    }

    private DataTablesResponse<Content> getAllCurrentFolderContent(User user, int currentFolderId, DataTablesRequest dtRequest) {
        Folder currentFolder = currentFolderId == -1 ? null : contentService.getFolder(currentFolderId);
        List<Content> data = contentService.getContent(user, currentFolder);
        return formDataTablesResponse(data, dtRequest);
    }

    @RequestMapping(value = "/getContent/starred/{currentFolderId}", method = RequestMethod.POST)
    public @ResponseBody DataTablesResponse<Content> getStarredContent(@PathVariable int currentFolderId, @RequestBody DataTablesRequest dtRequest) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        if (currentFolderId != -1) {
            return getAllCurrentFolderContent(user, currentFolderId, dtRequest);
        }
        return getStarredContent(user, dtRequest);
    }

    private DataTablesResponse<Content> getStarredContent(User user, DataTablesRequest dtRequest) {
        List<Content> data = contentService.getStarredContent(user);
        return formDataTablesResponse(data, dtRequest);
    }

    @RequestMapping(value = "/getContent/bin", method = RequestMethod.POST)
    public @ResponseBody DataTablesResponse<Content> getBinContent(@RequestBody DataTablesRequest dtRequest) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        return getTargetContent(user, dtRequest);
    }

    private DataTablesResponse<Content> getTargetContent(User user, DataTablesRequest dtRequest) {
        List<Content> data = contentService.getBinContent(user);
        return formDataTablesResponse(data, dtRequest);
    }

    @RequestMapping(value = "/getContent/shared/{currentFolderId}", method = RequestMethod.POST)
    public @ResponseBody DataTablesResponse<Content> getSharedContent(@PathVariable int currentFolderId, @RequestBody DataTablesRequest dtRequest) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        return getSharedContent(user, currentFolderId, dtRequest);
    }

    private DataTablesResponse<Content> getSharedContent(User user, int currentFolderId, DataTablesRequest dtRequest) {
        List<Content> data = contentService.getSharedContent(user, currentFolderId == -1 ? null : currentFolderId);
        return formDataTablesResponse(data, dtRequest);
    }

    @RequestMapping(value = "/getContent/search/{searchQuery}", method = RequestMethod.POST)
    public @ResponseBody DataTablesResponse<Content> getSearchContent(@PathVariable String searchQuery, @RequestBody DataTablesRequest dtRequest) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        return getSearchContent(user, searchQuery, dtRequest);
    }

    private DataTablesResponse<Content> getSearchContent(User user, String searchQuery, DataTablesRequest dtRequest) {
        List<Content> data = contentService.getSearchContent(user, searchQuery);
        return formDataTablesResponse(data, dtRequest);
    }

    private DataTablesResponse<Content> formDataTablesResponse(List<Content> data, DataTablesRequest dtRequest) {
        int total = data.size();

        DataTablesResponse<Content> dtResponse = new DataTablesResponse<>();
        dtResponse.setDraw(dtRequest.getDraw());
        dtResponse.setRecordsTotal(total);
        dtResponse.setRecordsFiltered(total);
        dtResponse.setData(data);
        return dtResponse;
    }
}