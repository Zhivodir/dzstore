package com.gmail.dzhivchik.dao.Impl;

import com.gmail.dzhivchik.dao.FileDAO;
import com.gmail.dzhivchik.domain.File;
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

    @Override
    public void deleteGroup(File[] files) {

    }

    @Override
    public List<File> getList() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Query query;
        query = entityManager.createQuery("SELECT c FROM File c", File.class);
        return (List<File>)query.getResultList();
    }
}
