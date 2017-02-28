package com.gmail.dzhivchik.dao.Impl;

import com.gmail.dzhivchik.dao.FolderDAO;
import com.gmail.dzhivchik.domain.File;
import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by User on 27.02.2017.
 */

@Repository
public class FolderDAOImpl implements FolderDAO{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void createFolder(Folder folder) {
        entityManager.persist(folder);
    }

    @Override
    public List<Folder> getList(User user) {
        int user_id = user.getId();

        Query query;
        query = entityManager.createQuery("SELECT c FROM Folder c WHERE c.user.id = :user_id", Folder.class);
        query.setParameter("user_id", user_id);
        return (List<Folder>)query.getResultList();
    }
}
