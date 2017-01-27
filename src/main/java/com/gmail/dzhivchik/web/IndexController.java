package com.gmail.dzhivchik.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;

/**
 * Created by User on 26.01.2017.
 */

@Controller
@RequestMapping("/")
public class IndexController {

    @RequestMapping("/")
    public String onIndex() {
        return "index";
    }

    @RequestMapping(value = "/create_folder", method = RequestMethod.POST)
    public String createNewFolder(@RequestParam String nameOfFolder){
        File myPath = new File("/" + nameOfFolder);
        myPath.mkdirs();
        return "index";
    }
}
