package com.gmail.dzhivchik.dao.Impl;

import com.gmail.dzhivchik.dao.FileDAO;
import com.gmail.dzhivchik.domain.File;
import com.gmail.dzhivchik.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by User on 31.01.2017.
 */

@Repository
public class FileDAOImpl implements FileDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void upload(File file) {
        entityManager.persist(file);
    }

    @Override
    public void uploadGroup(File[] files) {
        for (File file : files) {
            entityManager.merge(file);
        }
    }

    @Override
    public void delete(File file) {

    }

    //PROBLEM : Many query to DB
    @Override
    public File[] deleteGroup(int[] checked_files_id) {
        File[] files = new File[checked_files_id.length];
        int num = 0;
        for (Integer id : checked_files_id) {
            File file = entityManager.find(File.class, id);
            files[num] = file;
            entityManager.remove(file);
            num++;
        }
        return files;
    }

    @Override
    public List<File> getList(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        int user_id = user.getId();

        Query query;
        query = entityManager.createQuery("SELECT c FROM File c WHERE c.user.id = :user_id", File.class);
        query.setParameter("user_id", user_id);
        return (List<File>)query.getResultList();
    }
}
