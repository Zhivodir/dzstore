package com.gmail.dzhivchik.service;


import com.gmail.dzhivchik.config.ServerConfig;
import com.gmail.dzhivchik.domain.Leaf;
import com.gmail.dzhivchik.domain.SpringSecurityUser;
import com.gmail.dzhivchik.domain.enums.PageType;
import com.gmail.dzhivchik.service.Impl.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Service
public class ViewControllerHelper {
    @Autowired
    private ContentService contentService;


    public void prepareView(HttpServletRequest request, HttpServletResponse response, LocaleResolver localeResolver,
                            PageType pageType, SpringSecurityUser user) {
        prepareLeftMenu(request, pageType, user);
        prepareCommonData(request, pageType, user);
        localeResolver.setLocale(request, response, new Locale(user.getLanguage().toString()));
    }

    public void prepareCommonData(HttpServletRequest request, PageType pageType, SpringSecurityUser user) {
        request.setAttribute("typeOfView", pageType.toString().toLowerCase());
        request.setAttribute("user", user);
        request.setAttribute("busySpace", contentService.getSizeBusyMemory(user.getId()));
        request.setAttribute("availableSpace", ServerConfig.AVAILABLE_SPACE);
    }

    public void prepareLeftMenu(HttpServletRequest request, PageType pageType, SpringSecurityUser user) {
        List<Leaf> leftMenuPoints = new ArrayList<>();
        ResourceBundle texts = ResourceBundle.getBundle("lang/texts", new Locale(user.getLanguage().toString().toLowerCase()));
        leftMenuPoints.add(new Leaf("/mydisk", texts.getString("view.type.my.space"), "fa-home", pageType == PageType.MYDISK));
        leftMenuPoints.add(new Leaf("/shared", texts.getString("view.type.shared.for.me"), "fa-users", pageType == PageType.SHARED));
        leftMenuPoints.add(new Leaf("/starred", texts.getString("view.type.starred"), "fa-star", pageType == PageType.STARRED));
        leftMenuPoints.add(new Leaf("/bin", texts.getString("view.type.bin"), "fa-bitbucket", pageType == PageType.BIN));
        request.setAttribute("leftMenuPoints", leftMenuPoints);
    }
}