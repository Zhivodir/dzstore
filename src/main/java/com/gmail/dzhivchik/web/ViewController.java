package com.gmail.dzhivchik.web;

import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.enums.PageType;
import com.gmail.dzhivchik.service.Impl.ContentService;
import com.gmail.dzhivchik.service.ViewControllerHelper;
import com.gmail.dzhivchik.web.dto.Content;
import com.gmail.dzhivchik.web.dto.datatables.DataTablesRequest;
import com.gmail.dzhivchik.web.dto.datatables.DataTablesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static com.gmail.dzhivchik.utils.SpringSecurityUtil.getSecurityUser;

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
        viewHelper.prepareView(request, response, localeResolver, PageType.MYDISK, getSecurityUser());

        request.setAttribute("parentsFolderID", null);
        request.setAttribute("currentFolderID", currentFolderID);
        return "index_commons";
    }

    @RequestMapping(value = "/getContent/mydisk/{currentFolderId}", method = RequestMethod.POST)
    public @ResponseBody DataTablesResponse<Content> getContent(@PathVariable int currentFolderId,  DataTablesRequest dtRequest) {
        return getAllCurrentFolderContent(getSecurityUser().getId(), currentFolderId, dtRequest);
    }

    @RequestMapping(value = "/getContent/starred/{currentFolderId}", method = RequestMethod.POST)
    public @ResponseBody DataTablesResponse<Content> getStarredContent(@PathVariable int currentFolderId, @RequestBody DataTablesRequest dtRequest) {
        if (currentFolderId != -1) {
            return getAllCurrentFolderContent(getSecurityUser().getId(), currentFolderId, dtRequest);
        }
        return getStarredContentList(getSecurityUser().getId(), dtRequest);
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
        return getTargetContent(getSecurityUser().getId(), dtRequest);
    }

    private DataTablesResponse<Content> getTargetContent(int userId, DataTablesRequest dtRequest) {
        List<Content> data = contentService.getBinContent(userId);
        return formDataTablesResponse(data, dtRequest);
    }

    @RequestMapping(value = "/getContent/shared/{currentFolderId}", method = RequestMethod.POST)
    public @ResponseBody DataTablesResponse<Content> getSharedContent(@PathVariable int currentFolderId, @RequestBody DataTablesRequest dtRequest) {
        return getSharedContent(getSecurityUser().getId(), currentFolderId, dtRequest);
    }

    private DataTablesResponse<Content> getSharedContent(int userId, int currentFolderId, DataTablesRequest dtRequest) {
        List<Content> data = contentService.getSharedContent(userId, currentFolderId == -1 ? null : currentFolderId);
        return formDataTablesResponse(data, dtRequest);
    }

    @RequestMapping(value = "/getContent/search/{searchQuery}", method = RequestMethod.POST)
    public @ResponseBody DataTablesResponse<Content> getSearchContent(@PathVariable String searchQuery, @RequestBody DataTablesRequest dtRequest) {
        return getSearchContent(getSecurityUser().getId(), searchQuery, dtRequest);
    }

    private DataTablesResponse<Content> getSearchContent(int userId, String searchQuery, DataTablesRequest dtRequest) {
        List<Content> data = !searchQuery.isEmpty() ? contentService.getSearchContent(userId, searchQuery) : new ArrayList<>();
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