package com.gmail.dzhivchik.service;

import com.gmail.dzhivchik.domain.User;

/**
 * Created by User on 03.02.2017.
 */

public interface UserService {
    User getUser(String login);
}
