package com.gmail.dzhivchik.web;

import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.SpringSecurityUser;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.domain.enums.PageType;
import com.gmail.dzhivchik.service.Impl.ContentService;
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

@Controller
@RequestMapping("/")
public class ViewController {
    @Autowired
    private ViewControllerHelper viewHelper;
    @Autowired
    private ContentService contentService;
    @Autowired
    private LocaleResolver localeResolver;

    @RequestMapping(method = RequestMethod.GET)
    public String onIndex(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(value = "currentFolderID", required = false) Integer currentFolderID) {
        SpringSecurityUser user = (SpringSecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        viewHelper.prepareView(request, response, localeResolver, PageType.INDEX, user);

        request.setAttribute("parentsFolderID", null);
        request.setAttribute("currentFolderID", currentFolderID);
        return "index_commons";
    }


    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(value = "whatSearch", required = false) String whatSearch) {
        SpringSecurityUser user = (SpringSecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        viewHelper.prepareView(request, response, localeResolver, PageType.SEARCH, user);

        request.setAttribute("whatSearch", whatSearch);
        return "index_commons";
    }


    @RequestMapping(value = "/shared", method = RequestMethod.GET)
    public String shared(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(value = "currentFolderID", required = false) Integer currentFolderID) {
        SpringSecurityUser user = (SpringSecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        viewHelper.prepareView(request, response, localeResolver, PageType.SHARED, user);

        request.setAttribute("currentFolderID", currentFolderID);
        return "index_commons";
    }

    @RequestMapping(value = "/bin")
    public String toBin(HttpServletRequest request, HttpServletResponse response) {
        SpringSecurityUser user = (SpringSecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        viewHelper.prepareView(request, response, localeResolver, PageType.BIN, user);
        return "index_commons";
    }

    @RequestMapping(value = "/starred")
    public String onStarred(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(value = "currentFolderID", required = false) Integer currentFolderID) {
        SpringSecurityUser user = (SpringSecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        viewHelper.prepareView(request, response, localeResolver, PageType.STARRED, user);

        request.setAttribute("currentFolderID", currentFolderID);
        return "index_commons";
    }


    @RequestMapping(value = "/getContent/{currentFolderId}", method = RequestMethod.POST)
    public @ResponseBody DataTablesResponse<Content> getContent(@PathVariable int currentFolderId,  DataTablesRequest dtRequest) {
        SpringSecurityUser user = (SpringSecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getAllCurrentFolderContent(user.getId(), currentFolderId, dtRequest);
    }

    @RequestMapping(value = "/getContent/starred/{currentFolderId}", method = RequestMethod.POST)
    public @ResponseBody DataTablesResponse<Content> getStarredContent(@PathVariable int currentFolderId, @RequestBody DataTablesRequest dtRequest) {
        SpringSecurityUser user = (SpringSecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentFolderId != -1) {
            return getAllCurrentFolderContent(user.getId(), currentFolderId, dtRequest);
        }
        return getStarredContentList(user.getId(), dtRequest);
    }

    private DataTablesResponse<Content> getAllCurrentFolderContent(int userId, int currentFolderId, DataTablesRequest dtRequest) {
        Folder currentFolder = currentFolderId == -1 ? null : contentService.getFolder(currentFolderId);
        List<Content> data = contentService.getContent(userId, currentFolder);
        return formDataTablesResponse(data, dtRequest);
    }

    private DataTablesResponse<Content> getStarredContentList(int userId, DataTablesRequest dtRequest) {
        List<Content> data = contentService.getStarredContent(userId);
        return formDataTablesResponse(data, dtRequest);
    }

    @RequestMapping(value = "/getContent/bin", method = RequestMethod.POST)
    public @ResponseBody DataTablesResponse<Content> getBinContent(@RequestBody DataTablesRequest dtRequest) {
        SpringSecurityUser user = (SpringSecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getTargetContent(user.getId(), dtRequest);
    }

    private DataTablesResponse<Content> getTargetContent(int userId, DataTablesRequest dtRequest) {
        List<Content> data = contentService.getBinContent(userId);
        return formDataTablesResponse(data, dtRequest);
    }

    @RequestMapping(value = "/getContent/shared/{currentFolderId}", method = RequestMethod.POST)
    public @ResponseBody DataTablesResponse<Content> getSharedContent(@PathVariable int currentFolderId, @RequestBody DataTablesRequest dtRequest) {
        SpringSecurityUser user = (SpringSecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getSharedContent(user.getId(), currentFolderId, dtRequest);
    }

    private DataTablesResponse<Content> getSharedContent(int userId, int currentFolderId, DataTablesRequest dtRequest) {
        List<Content> data = contentService.getSharedContent(userId, currentFolderId == -1 ? null : currentFolderId);
        return formDataTablesResponse(data, dtRequest);
    }

    @RequestMapping(value = "/getContent/search/{searchQuery}", method = RequestMethod.POST)
    public @ResponseBody DataTablesResponse<Content> getSearchContent(@PathVariable String searchQuery, @RequestBody DataTablesRequest dtRequest) {
        SpringSecurityUser user = (SpringSecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getSearchContent(user.getId(), searchQuery, dtRequest);
    }

    private DataTablesResponse<Content> getSearchContent(int userId, String searchQuery, DataTablesRequest dtRequest) {
        List<Content> data = contentService.getSearchContent(userId, searchQuery);
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