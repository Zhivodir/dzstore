package com.gmail.dzhivchik.web;

import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.domain.enums.Language;
import com.gmail.dzhivchik.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/settings")
public class SettingsController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/changeLanguage", method = RequestMethod.POST)
    public String createNewUser(@RequestParam("typeOfView") String typeOfView, @RequestParam("language") Language language) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        user.setLanguage(language);
        userService.editUser(user);
        return "redirect:/";
    }
}
