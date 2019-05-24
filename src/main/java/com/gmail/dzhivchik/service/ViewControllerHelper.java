package com.gmail.dzhivchik.service;


import com.gmail.dzhivchik.MemoryUtils;
import com.gmail.dzhivchik.domain.Leaf;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.domain.enums.PageType;
import com.gmail.dzhivchik.service.Impl.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Service
public class ViewControllerHelper {
    @Autowired
    private ContentService contentService;

    public void prepareCommonData(HttpServletRequest request, PageType pageType, User user) {
        request.setAttribute("typeOfView", pageType.toString().toLowerCase());
        request.setAttribute("user", user);
        request.setAttribute("busySpace", MemoryUtils.showBusySpace(contentService.getSizeBusyMemory(user)));
    }

    public void prepareLeftMenu(HttpServletRequest request, PageType pageType, User user) {
        List<Leaf> leftMenuPoints = new ArrayList<>();
        ResourceBundle texts = ResourceBundle.getBundle("lang/texts", new Locale(user.getLanguage().toString().toLowerCase()));
        leftMenuPoints.add(new Leaf("/", texts.getString("view.type.my.space"), "glyphicon-home", pageType == PageType.INDEX));
        leftMenuPoints.add(new Leaf("/shared", texts.getString("view.type.shared.for.me"), "glyphicon-eye-open", pageType == PageType.SHARED));
        leftMenuPoints.add(new Leaf("/starred", texts.getString("view.type.starred"), "glyphicon-star", pageType == PageType.STARRED));
        leftMenuPoints.add(new Leaf("/bin", texts.getString("view.type.bin"), "glyphicon-trash", pageType == PageType.BIN));
        request.setAttribute("leftMenuPoints", leftMenuPoints);
    }
}