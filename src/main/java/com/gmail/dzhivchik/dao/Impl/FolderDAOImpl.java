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
    public List<Folder> getList(User user, Folder parentFolder) {
        int user_id = user.getId();
        Query query;
        if(parentFolder == null){
            query = entityManager.createQuery("SELECT c FROM Folder c WHERE c.user.id = :user_id AND c.parentFolder.id IS NULL", Folder.class);
            query.setParameter("user_id", user_id);
        }else{
            Integer parent_id = parentFolder.getId();
            query = entityManager.createQuery("SELECT c FROM Folder c WHERE c.user.id = :user_id AND c.parentFolder.id = :parent_id", Folder.class);
            query.setParameter("user_id", user_id);
            query.setParameter("parent_id", parent_id);
        }
        return (List<Folder>)query.getResultList();
    }

    @Override
    public Folder getFolder(int id) {
        Query query;
        query = entityManager.createQuery("SELECT f FROM Folder f WHERE f.id = :id", Folder.class);
        query.setParameter("id", id);
        List<Folder> temp = (List<Folder>)query.getResultList();
        return temp.get(0);
    }

    @Override
    public Folder[] deleteGroup(int[] checked_folders_id) {
        Folder[] folders = new Folder[checked_folders_id.length];
        int num = 0;
        for (Integer id : checked_folders_id) {
            Folder folder = entityManager.find(Folder.class, id);
            folders[num] = folder;
            entityManager.remove(folder);
            num++;
        }
        return folders;
    }

    @Override
    public List<Folder> getListFolderById(int[] listOfId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT f FROM Folder f WHERE ");
        for (int i = 0; i < listOfId.length; i++) {
            if(i == 0) {
                sb.append("f.id = " + listOfId[0]);
            }else{
                sb.append(" OR f.id = " + listOfId[i]);
            }
        }
        Query query = entityManager.createQuery(sb.toString(), Folder.class);
        return (List<Folder>)query.getResultList();
    }
}
