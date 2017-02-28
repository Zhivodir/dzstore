package com.gmail.dzhivchik.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by User on 28.02.2017.
 */

@Controller
@RequestMapping("/")
public class FolderController {

    @RequestMapping(value = "/index/folder", method = RequestMethod.GET)
    public String inFolder(@RequestParam String f){
        if(f != null){
            System.out.println(f);
        }
        return "index";
    }
}
