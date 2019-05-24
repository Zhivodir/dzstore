package com.gmail.dzhivchik.web;

import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.domain.enums.PageType;
import com.gmail.dzhivchik.service.Impl.ContentService;
import com.gmail.dzhivchik.service.UserService;
import com.gmail.dzhivchik.service.ViewControllerHelper;
import com.gmail.dzhivchik.web.dto.Content;
import com.gmail.dzhivchik.web.dto.datatables.DataTablesRequest;
import com.gmail.dzhivchik.web.dto.datatables.DataTablesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/")
public class ViewController {
    @Autowired
    private ViewControllerHelper viewHelper;
    @Autowired
    private ContentService contentService;
    @Autowired
    private UserService userService;
    @Autowired
    private LocaleResolver localeResolver;

    @RequestMapping(method = RequestMethod.GET)
    public String onIndex(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(value = "currentFolderID", required = false) Integer currentFolderID) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        viewHelper.prepareLeftMenu(request, PageType.INDEX, user);
        viewHelper.prepareCommonData(request, PageType.INDEX, user);
        localeResolver.setLocale(request, response, new Locale(user.getLanguage().toString()));

        request.setAttribute("parentsFolderID", null);
        request.setAttribute("currentFolderID", currentFolderID);
        return "index_commons";
    }


    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(value = "whatSearch", required = false) String whatSearch) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        viewHelper.prepareLeftMenu(request, PageType.SEARCH, user);
        viewHelper.prepareCommonData(request, PageType.SEARCH, user);
        localeResolver.setLocale(request, response, new Locale(user.getLanguage().toString()));

        request.setAttribute("whatSearch", whatSearch);
        return "index_commons";
    }


    @RequestMapping(value = "/shared", method = RequestMethod.GET)
    public String shared(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(value = "currentFolderID", required = false) Integer currentFolderID) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        viewHelper.prepareLeftMenu(request, PageType.SHARED, user);
        viewHelper.prepareCommonData(request, PageType.SHARED, user);
        localeResolver.setLocale(request, response, new Locale(user.getLanguage().toString()));

        request.setAttribute("currentFolderID", currentFolderID);
        return "index_commons";
    }

    @RequestMapping(value = "/bin")
    public String toBin(HttpServletRequest request, HttpServletResponse response) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        viewHelper.prepareLeftMenu(request, PageType.BIN, user);
        viewHelper.prepareCommonData(request, PageType.BIN, user);
        localeResolver.setLocale(request, response, new Locale(user.getLanguage().toString()));
        return "index_commons";
    }

    @RequestMapping(value = "/starred")
    public String onStarred(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(value = "currentFolderID", required = false) Integer currentFolderID) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        viewHelper.prepareLeftMenu(request, PageType.STARRED, user);
        viewHelper.prepareCommonData(request, PageType.STARRED, user);
        localeResolver.setLocale(request, response, new Locale(user.getLanguage().toString()));
        request.setAttribute("currentFolderID", currentFolderID);
        return "index_commons";
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