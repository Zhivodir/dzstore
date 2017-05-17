package com.gmail.dzhivchik.web;

import com.gmail.dzhivchik.domain.File;
import com.gmail.dzhivchik.domain.Folder;
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
    @RequestMapping(value = "/ajax/load_tree_of_catalog", method = RequestMethod.GET
//            ,headers = {"Content-type=application/json"}
    )
    public String loadTree(Model model,
                           @RequestParam(value = "id", required = false) String id
                           //            @RequestBody Integer id
    ){
        System.out.println("!!!!!!" + id + "!!!!!!");
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        List[] content = contentService.getContent(user, null);
        List<File> filesForTree = content[0];
        List<Folder> foldersForTree = content[1];


//        $node[] = "{ id: $id, title: 'Node $id', isFolder: $isFolder}";

        return "redirect:/index";
    }
}