package com.gmail.dzhivchik.service;


import com.gmail.dzhivchik.MemoryUtils;
import com.gmail.dzhivchik.domain.Leaf;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.domain.enums.PageType;
import com.gmail.dzhivchik.service.Impl.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service
public class ViewControllerHelper {
    @Autowired
    private ContentService contentService;

    public void prepareCommonData(Model model, PageType pageType, User user) {
        model.addAttribute("typeOfView", pageType.toString().toLowerCase());
        model.addAttribute("user", user);
        model.addAttribute("busySpace", MemoryUtils.showBusySpace(contentService.getSizeBusyMemory(user)));
    }

    public void prepareLeftMenu(Model model, PageType pageType) {
        List<Leaf> leftMenuPoints = new ArrayList<>();
        leftMenuPoints.add(new Leaf("/", "My store", "glyphicon-home", pageType == PageType.INDEX));
        leftMenuPoints.add(new Leaf("/shared", "Shared with me", "glyphicon-eye-open", pageType == PageType.SHARED));
        leftMenuPoints.add(new Leaf("/starred", "Starred", "glyphicon-star", pageType == PageType.STARRED));
        leftMenuPoints.add(new Leaf("/bin", "Bin", "glyphicon-trash", pageType == PageType.BIN));
        model.addAttribute("leftMenuPoints", leftMenuPoints);
    }
}
