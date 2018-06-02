package com.gmail.dzhivchik.util;


import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.to.UserTo;

public class UserUtil {
    public static UserTo asTo(User user) {
        return new UserTo(user.getId(), user.getLogin(), user.getEmail(), user.getPassword());
    }
}
