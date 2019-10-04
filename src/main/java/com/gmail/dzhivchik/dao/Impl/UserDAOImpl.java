package com.gmail.dzhivchik.dao.Impl;

import com.gmail.dzhivchik.dao.UserDAO;
import com.gmail.dzhivchik.domain.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Repository
public class UserDAOImpl implements UserDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User getUserReference(int userId) {
        return entityManager.getReference(User.class, userId);
    }


    @Override
    public User save(User user) {
        if (user.getId() == null) {
            entityManager.persist(user);
            return user;
        } else {
            return entityManager.merge(user);
        }
    }

    public User get(int userId) {
        return entityManager.find(User.class, userId);
    }

    @Override
    public User get(String login) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.login = :login", User.class)
                .setParameter("login", login)
                .getSingleResult();
    }

    @Override
    public List<User> getUsersById(int[] cancelShareForUsers) {
        if (cancelShareForUsers == null || cancelShareForUsers.length == 0) {
            return new ArrayList<User>();
        }

        List<Integer> list = Arrays.stream(cancelShareForUsers).boxed().collect(Collectors.toList());
        return entityManager.createQuery("SELECT u FROM User u WHERE u.id in (:list)", User.class)
                .setParameter("list", list)
                .getResultList();
    }

    @Override
    public List<User> getUsersByEmail(String[] cancelShareForUsers) {
        List<String> list = Stream.of(cancelShareForUsers).collect(Collectors.toList());
        return entityManager.createQuery("SELECT u FROM User u WHERE u.email in (:list)", User.class)
                .setParameter("list", list)
                .getResultList();
    }

    @Override
    public void edit(User user) {
        entityManager.merge(user);
    }

    @Override
    public void delete(User user) {

    }

    @Override
    public List<User> getShareReceivers(String shareFor) {
        String[] tempListForShare = shareFor.split(",");
        List<String> emails = new ArrayList<>();
        List<String> logins = new ArrayList<>();
        for (String receiver : tempListForShare) {
            receiver = receiver.trim();
            if (receiver.contains("@")) {
                emails.add(receiver);
            } else {
                logins.add(receiver);
            }
        }

        return entityManager.createQuery("SELECT u FROM User u WHERE u.login in (:logins) AND u.email in (:emails)", User.class)
                .setParameter("logins", logins)
                .setParameter("emails", emails)
                .getResultList();
    }
}
