package com.gmail.dzhivchik.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MyController {

    @RequestMapping("/")
    public String onIndex() {
        return "index";
    }


}
