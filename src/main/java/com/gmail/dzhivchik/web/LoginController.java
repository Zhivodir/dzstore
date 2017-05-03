package com.gmail.dzhivchik.web;

import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.domain.enums.UserRoleEnum;
import com.gmail.dzhivchik.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by User on 03.02.2017.
 */

@Controller
@RequestMapping("/")
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(){
        return "login";
    }

    @RequestMapping(value = "/registration")
    public String registration(){
        return "registration";
    }

    @RequestMapping(value = "/create_new_user", method = RequestMethod.POST)
    public String cresteNewUser(@RequestParam("login") String login,
                                @RequestParam("password") String password,
                                @RequestParam("email") String email){
        User user = new User(login, password, email, UserRoleEnum.USER);
        userService.addUser(user);
        new java.io.File("c:/DevKit/Temp/dzstore/users_storages/" + user.getLogin()).mkdir();
        return "redirect:/login";
    }

    @RequestMapping(value = "/newImageForProfile", method = RequestMethod.POST)
    public String changeProfileImage(
            @RequestParam(value = "file", required = false) MultipartFile file){
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        java.io.File convFile = new java.io.File(login + ".jpg");
        try {
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream("c:/DevKit/Temp/dzstore/users_image_for_profile/" + convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/index";
    }
}
