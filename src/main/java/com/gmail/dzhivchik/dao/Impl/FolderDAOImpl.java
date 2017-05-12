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
//        int user_id = user.getId();
//        Query query;
//        if(parentFolder == null){
//            query = entityManager.createQuery("SELECT c FROM Folder c WHERE c.user.id = :user_id AND c.parentFolder.id IS NULL AND c.inbin <> 1", Folder.class);
//            query.setParameter("user_id", user_id);
//        }else{
//            Integer parent_id = parentFolder.getId();
//            query = entityManager.createQuery("SELECT c FROM Folder c WHERE c.user.id = :user_id AND c.parentFolder.id = :parent_id AND c.inbin <> 1", Folder.class);
//            query.setParameter("user_id", user_id);
//            query.setParameter("parent_id", parent_id);
//        }
//        return (List<Folder>)query.getResultList();
        int user_id = user.getId();
        Query query;
        if(parentFolder == null){
            query = entityManager.createQuery("SELECT c FROM Folder c WHERE c.user = :user AND c.parentFolder IS NULL AND c.inbin <> 1", Folder.class);
            query.setParameter("user", user);
        }else{
            query = entityManager.createQuery("SELECT c FROM Folder c WHERE c.user = :user AND c.parentFolder = :parent AND c.inbin <> 1", Folder.class);
            query.setParameter("user", user);
            query.setParameter("parent", parentFolder);
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
    public Folder getFolder(User user, String name, Folder parentFolder) {
        Query query;
        if(parentFolder == null) {
            query = entityManager.createQuery("SELECT f FROM Folder f WHERE f.name = :name " +
                    "AND f.user = :user " +
                    "AND f.parentFolder IS NULL", Folder.class);
            query.setParameter("name", name);
            query.setParameter("user", user);
        } else {
            query = entityManager.createQuery("SELECT f FROM Folder f WHERE f.name = :name " +
                    "AND f.user = :user " +
                    "AND f.parentFolder = :parentFolder", Folder.class);
            query.setParameter("name", name);
            query.setParameter("user", user);
            query.setParameter("parentFolder", parentFolder);
        }
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
    public List<Folder> getListFoldersById(int[] listOfId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT f FROM Folder f WHERE ");
        for (int i = 0; i < listOfId.length; i++) {
            System.out.println(i);
            if(i == 0) {
                sb.append("f.id = " + listOfId[0]);
            }else{
                sb.append(" OR f.id = " + listOfId[i]);
            }
        }
        Query query = entityManager.createQuery(sb.toString(), Folder.class);
        return (List<Folder>)query.getResultList();
    }


    @Override
    public void changeStar(int[] checked_folders_id, boolean stateOfStar) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE Folder f SET f.starred = :stateOfStar WHERE ");
        for (int i = 0; i < checked_folders_id.length; i++) {
            if(i == 0) {
                sb.append("f.id = " + checked_folders_id[0]);
            }else{
                sb.append(" OR f.id = " + checked_folders_id[i]);
            }
        }
        Query query = entityManager.createQuery(sb.toString());
        query.setParameter("stateOfStar", stateOfStar);
        int result = query.executeUpdate();
    }

    @Override
    public List<Folder> getStarredList(User user) {
        int user_id = user.getId();
        Query query = entityManager.createQuery("SELECT f FROM Folder f WHERE f.user.id = :user_id AND f.starred = 1  AND f.inbin <> 1", Folder.class);
        query.setParameter("user_id", user_id);
        return (List<Folder>)query.getResultList();
    }

    @Override
    public List<Folder> getSearchList(String whatSearch, User user) {
        int user_id = user.getId();
        Query query = entityManager.createQuery("SELECT f FROM Folder f WHERE f.user.id = :user_id AND UPPER(f.name) LIKE :whatSearch  AND f.inbin <> 1", Folder.class);
        query.setParameter("user_id", user_id);
        query.setParameter("whatSearch", "%" + whatSearch.toUpperCase() + "%");
        return (List<Folder>)query.getResultList();
    }

    @Override
    public void renameFolder(int[] checked_folders_id, String newName) {
        Query query = entityManager.createQuery("UPDATE Folder f SET f.name = :newName WHERE f.id = :id");
        query.setParameter("newName", newName);
        query.setParameter("id", checked_folders_id[0]);
        int result = query.executeUpdate();
    }

    @Override
    public void changeShare(List<Folder> targets) {
        for (Folder folder : targets) {
            entityManager.merge(folder);
        }
    }

    @Override
    public List<Folder> getSharedList(User user) {
        Query query = entityManager.createQuery("SELECT f FROM Folder f INNER JOIN f.shareFor user WHERE user = :user AND f.inbin <> 1", Folder.class);
        query.setParameter("user", user);
        return  (List<Folder>)query.getResultList();
    }

    @Override
    public void changeInBin(int[] checked_folders_id, boolean stateOfInBinStatus) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE Folder f SET f.inbin = :stateOfInBinStatus WHERE ");
        for (int i = 0; i < checked_folders_id.length; i++) {
            if(i == 0) {
                sb.append("f.id = " + checked_folders_id[0]);
            }else{
                sb.append(" OR f.id = " + checked_folders_id[i]);
            }
        }
        Query query = entityManager.createQuery(sb.toString());
        query.setParameter("stateOfInBinStatus", stateOfInBinStatus);
        query.executeUpdate();
    }

    @Override
    public List<Folder> getBinList(User user) {
        int user_id = user.getId();
        Query query = entityManager.createQuery("SELECT f FROM Folder f WHERE f.user.id = :user_id AND f.inbin = 1", Folder.class);
        query.setParameter("user_id", user_id);
        return (List<Folder>)query.getResultList();
    }
}
