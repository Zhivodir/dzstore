package com.gmail.dzhivchik.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.service.Impl.ContentService;
import com.gmail.dzhivchik.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

/**
 * Created by User on 15.05.2017.
 */

@Controller
@RequestMapping("/")
public class AjaxControllerForTree {

    @Autowired
    private ContentService contentService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/ajax/load_tree_of_catalog", method = RequestMethod.GET)
    public String loadTree(Model model,
                           @RequestParam(value = "id", required = false) String id,
                           @RequestParam(value = "fields", required = false) String fields
    ){
        System.out.println(fields);
        String[] exceptionFolders = fields.split(",");
        for(String str:exceptionFolders){
            System.out.println(str);
        }
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        List[] content = null;
        if(id == "") {
            content = contentService.getContent(user, null, exceptionFolders);
        } else {
            content = contentService.getContent(user, contentService.getFolder(Integer.valueOf(id)), exceptionFolders);
        }
//        List<File> filesForTree = content[0];
//        List<Folder> foldersForTree = content[1];
//
        StringWriter writer = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(writer, content);
        } catch (IOException e){e.printStackTrace();}
        String result = writer.toString();
        return result;
    }
}