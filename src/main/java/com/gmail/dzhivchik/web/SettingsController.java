package com.gmail.dzhivchik.web;

import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.domain.enums.Language;
import com.gmail.dzhivchik.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static com.gmail.dzhivchik.utils.SpringSecurityUtil.getSecurityUser;

@Controller
@RequestMapping("/settings")
public class SettingsController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/{typeOfView}/{language}", method = RequestMethod.GET)
    public String createNewUser(@PathVariable("typeOfView") String typeOfView, @PathVariable("language") Language language) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(login);
        user.setLanguage(language);
        userService.editUser(user);
        getSecurityUser().setLanguage(language);
        return "redirect:/";
    }
}
