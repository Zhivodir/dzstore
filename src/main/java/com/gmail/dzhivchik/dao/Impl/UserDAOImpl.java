package com.gmail.dzhivchik.dao.Impl;

import com.gmail.dzhivchik.dao.UserDAO;
import com.gmail.dzhivchik.domain.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
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

        List<User> temp = (List<User>) query.getResultList();
        if(temp.size() < 1){
            return null;
        }
        return temp.get(0);
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
        Query query = entityManager.
                createQuery(preparationShareRequest(shareFor), User.class);
        return (List<User>) query.getResultList();
    }


    public String preparationShareRequest(String shareFor) {
        String[] tempListForShare = shareFor.split(",");
        List<String> forEmail = new ArrayList<>();
        List<String> forLogin = new ArrayList<>();
        for (String receiver : tempListForShare) {
            if (receiver.trim().contains("@")) {
                forEmail.add(receiver);
            } else {
                forLogin.add(receiver);
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT u FROM User u WHERE ");
        if (forEmail.size() != 0) {
            for (int i = 0; i < forEmail.size(); i++) {
                sb.append("u.email = '" + forEmail.get(i) + "' ");
                if (i != forEmail.size() - 1) {
                    sb.append("OR ");
                }
            }
        }
        if (forLogin.size() != 0) {
            if (forEmail.size() != 0) {
                sb.append("OR ");
            }
            for (int j = 0; j < forLogin.size(); j++) {
                sb.append("u.login = '" + forLogin.get(j) + "' ");
                if (j != forLogin.size() - 1) {
                    sb.append("OR ");
                }
            }
        }
        return sb.toString();
    }
}
