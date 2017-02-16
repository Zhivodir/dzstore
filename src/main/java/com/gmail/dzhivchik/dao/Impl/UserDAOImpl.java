package com.gmail.dzhivchik.dao.Impl;

import com.gmail.dzhivchik.dao.UserDAO;
import com.gmail.dzhivchik.domain.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by User on 09.02.2017.
 */

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

        List<User> temp = (List<User>)query.getResultList();
        return temp.get(0);
    }

    @Override
    public void editUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public void delete(User user) {

    }
}
