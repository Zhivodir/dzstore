package com.gmail.dzhivchik.dao.Impl;

import com.gmail.dzhivchik.AuthorizedUser;
import com.gmail.dzhivchik.dao.UserDAO;
import com.gmail.dzhivchik.domain.User;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserDAOImpl implements UserDAO {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public void addUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public User getUser(String login) {
        Query query;
        query = entityManager.
                createQuery("SELECT u FROM User u WHERE u.login = :login", User.class);
        query.setParameter("login", login);
        User user = (User) query.getSingleResult();
        return user;
    }

    @Override
    public List<User> getUsersById(int[] cancel_share_for_users) {
        List<Integer> list = Arrays.stream(cancel_share_for_users).boxed().collect(Collectors.toList());
        Query query = entityManager.
                createQuery("SELECT u FROM User u WHERE u.id IN :list", User.class);
        query.setParameter("list", list);
        List<User> resultList = (List<User>) query.getResultList();
        return resultList;
    }

    @Override
    public void editUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public void delete(User user) {
    }

    @Override
    public List<User> getShareReceivers(String shareFor) {
        String[] tempListForShare = shareFor.split(",");
        List<String> forEmail = new ArrayList<>();
        List<String> forLogin = new ArrayList<>();
        for (String receiver : tempListForShare) {
            receiver = receiver.trim();
            if (receiver.contains("@")) forEmail.add(receiver);
            else forLogin.add(receiver);
        }
        Query query = entityManager.createQuery("SELECT u FROM User u " +
                "WHERE u.email IN :forEmail OR u.login IN :forLogin", User.class);
        query.setParameter("forEmail", forEmail);
        query.setParameter("forLogin", forLogin);
        return (List<User>) query.getResultList();
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = entityManager.createQuery("SELECT u FROM User u " +
                "WHERE u.email =:email", User.class)
                .setParameter("email", email)
                .getResultList();
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public long getBusySize() {
        int user_id = AuthorizedUser.id();
        Query query = entityManager.createQuery("SELECT SUM(f.size) FROM File f WHERE f.user.id = :user_id", Long.class);
        query.setParameter("user_id", user_id);
        return (Long)query.getSingleResult();
    }
}
