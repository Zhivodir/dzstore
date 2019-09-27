package com.gmail.dzhivchik.web;

import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.domain.enums.Language;
import com.gmail.dzhivchik.domain.enums.UserRoleEnum;
import com.gmail.dzhivchik.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/")
public class LoginController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public String androidTest(@RequestParam("code") String[] code){
        String test = "!!!!!!!!!!!!!!!" + code[0];
        return test;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(){
        return "login";
    }

    @RequestMapping(value = "/registration")
    public String registration(){
        return "registration";
    }

    @RequestMapping(value = "/createNewUser", method = RequestMethod.POST)
    public String createNewUser(@RequestParam("login") String login,
                                @RequestParam("password") String password,
                                @RequestParam("email") String email,
                                @RequestParam("language") String language){
        if(!login.trim().isEmpty() && !password.trim().isEmpty() && !email.trim().isEmpty()) {
            User user = new User(login, password, email, UserRoleEnum.USER, Language.valueOf(language));
            boolean wasAdded = userService.create(user);
            if(wasAdded) {
                return "redirect:/login";
            }
        }
        return "registration";
    }
}
