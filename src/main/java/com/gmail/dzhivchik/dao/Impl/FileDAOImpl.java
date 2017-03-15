package com.gmail.dzhivchik.dao.Impl;

import com.gmail.dzhivchik.dao.FileDAO;
import com.gmail.dzhivchik.domain.File;
import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Arrays;
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
    public List<File> getList(User user, Folder parentFolder) {
        int user_id = user.getId();

        Query query;
        if(parentFolder == null){
            query = entityManager.createQuery("SELECT f FROM File f WHERE f.user.id = :user_id AND f.parentFolder.id IS NULL", File.class);
            query.setParameter("user_id", user_id);
        }else{
            Integer parent_id = parentFolder.getId();
            query = entityManager.createQuery("SELECT f FROM File f WHERE f.user.id = :user_id AND f.parentFolder.id = :parent_id", File.class);
            query.setParameter("user_id", user_id);
            query.setParameter("parent_id", parent_id);
        }
        return (List<File>)query.getResultList();
    }

    @Override
    public List<File> getListById(int[] listOfId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT f FROM File f WHERE ");
        for (int i = 0; i < listOfId.length; i++) {
            if(i == 0) {
                sb.append("f.id = " + listOfId[0]);
            }else{
                sb.append(" OR f.id = " + listOfId[i]);
            }
        }
        Query query = entityManager.createQuery(sb.toString(), File.class);
        return (List<File>)query.getResultList();
    }

    @Override
    public void changeStar(int[] checked_files_id, boolean stateOfStar) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE File f SET f.starred = :stateOfStar WHERE ");
        for (int i = 0; i < checked_files_id.length; i++) {
            if(i == 0) {
                sb.append("f.id = " + checked_files_id[0]);
            }else{
                sb.append(" OR f.id = " + checked_files_id[i]);
            }
        }
        Query query = entityManager.createQuery(sb.toString());
        query.setParameter("stateOfStar", stateOfStar);
        int result = query.executeUpdate();
    }
}
