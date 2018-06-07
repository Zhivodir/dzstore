package com.gmail.dzhivchik.web;

import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.domain.enums.UserRoleEnum;
import com.gmail.dzhivchik.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by User on 03.02.2017.
 */

@Controller
@RequestMapping("/")
public class LoginController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/test1", method = RequestMethod.POST)
    public String androidTest1(@RequestParam("code") String[] code){
        String test = "111!!!!!!!!!!!" + code[0];
        return test;
    }

    @ResponseBody
    @RequestMapping(value = "/test2", method = RequestMethod.POST)
    public String androidTest2(@RequestParam("code") String[] code){
        String test = "222!!!!!!!!!!!" + code[0];
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

    @RequestMapping(value = "/create_new_user", method = RequestMethod.POST)
    public String createNewUser(Model model,
                                @RequestParam("login") String login,
                                @RequestParam("password") String password,
                                @RequestParam("email") String email){
        if(!login.trim().isEmpty() && !password.trim().isEmpty() && !email.trim().isEmpty()) {
            User user = new User(login, password, email, UserRoleEnum.USER);
            boolean wasAdded = userService.addUser(user);
            if(wasAdded) {
                return "redirect:/login";
            }
        }
        return "registration";
    }
}
