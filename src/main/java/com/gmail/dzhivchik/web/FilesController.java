package com.gmail.dzhivchik.web;

import com.gmail.dzhivchik.domain.File;
import com.gmail.dzhivchik.service.ContentService;
import com.gmail.dzhivchik.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by User on 21.02.2017.
 */

@Controller
@RequestMapping("/")
public class FilesController {

    @Autowired
    private ContentService contentService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/actions_above_checked_files", method = RequestMethod.POST)
    public String actionsAboveCheckedFiles(@RequestParam int[] checked_files_id){
        File[] files = contentService.deleteCheckedFiles(checked_files_id);
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        if(files.length != 0){
            for (File file:files) {
                new java.io.File("c:/DevKit/Temp/dzstore/" + login + "/" + file.getName()).delete();
            }
        }
        return "redirect:/index";
    }
}
