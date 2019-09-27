package com.gmail.dzhivchik.dao;

import com.gmail.dzhivchik.domain.User;

import java.util.List;

public interface UserDAO {
    User save(User user);
    User get(String login);
    List<User> getUsersById(int[] cancel_share_for_users);
    List<User> getUsersByEmail(String[] cancel_share_for_users);
    void edit(User user);
    void delete(User user);
    List<User> getShareReceivers(String shareFor);
    User getUserReference(int userId);
}
