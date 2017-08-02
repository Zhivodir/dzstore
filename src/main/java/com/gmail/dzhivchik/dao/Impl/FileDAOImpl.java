package com.gmail.dzhivchik.dao.Impl;

import com.gmail.dzhivchik.dao.FileDAO;
import com.gmail.dzhivchik.domain.File;
import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
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
        File searchFile = isFile(file.getName(), file.isInbin(), file.getUser(), file.getParentFolder());
        if(searchFile != null){
            Query query = entityManager.createQuery("UPDATE File f SET f.size = :size, f.inbin = :inbin WHERE f.id = :id");
            query.setParameter("size", file.getSize());
            query.setParameter("inbin", false);
            query.setParameter("id", searchFile.getId());
            query.executeUpdate();
        } else {
            entityManager.persist(file);
        }
    }

    @Override
    public void uploadGroup(File[] files) {
        for (File file : files) {
            entityManager.merge(file);
        }
    }

    public File isFile(String name, boolean inbin, User user, Folder parentFolder) {
        Query query;
        if(parentFolder == null){
            query = entityManager.createQuery("SELECT f FROM File f WHERE f.name = :name " +
                    "AND f.inbin = :inbin AND f.user = :user AND f.parentFolder IS NULL", File.class);
            query.setParameter("name", name);
            query.setParameter("inbin", inbin);
            query.setParameter("user", user);
        }else{
            query = entityManager.createQuery("SELECT f FROM File f WHERE f.name = :name " +
                    "AND f.inbin = :inbin AND f.user = :user AND f.parentFolder = :parentFolder", File.class);
            query.setParameter("name", name);
            query.setParameter("inbin", inbin);
            query.setParameter("user", user);
            query.setParameter("parentFolder", parentFolder);
        }
        List<File> resultList = ((List<File>)query.getResultList());
        if (resultList.size() != 0){
            return resultList.get(0);
        }
        return null;
    }

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
            query = entityManager.createQuery("SELECT f FROM File f WHERE f.user.id = :user_id AND f.parentFolder.id IS NULL AND f.inbin <> 1", File.class);
            query.setParameter("user_id", user_id);
        }else{
            Integer parent_id = parentFolder.getId();
            query = entityManager.createQuery("SELECT f FROM File f WHERE f.user.id = :user_id AND f.parentFolder.id = :parent_id AND f.inbin <> 1", File.class);
            query.setParameter("user_id", user_id);
            query.setParameter("parent_id", parent_id);
        }
        return (List<File>)query.getResultList();
    }

    @Override
    public List<File> getListFilesById(int[] listOfId) {
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
        query.executeUpdate();
    }

    @Override
    public List<File> getStarredList(User user) {
        int user_id = user.getId();
        Query query = entityManager.createQuery("SELECT f FROM File f WHERE f.user.id = :user_id AND f.starred = 1 AND f.inbin <> 1", File.class);
        query.setParameter("user_id", user_id);
        Query query2 = entityManager.createQuery("SELECT f FROM File f INNER JOIN f.shareFor user " +
                "WHERE user = :user AND f.inbin <> 1 AND f.starred = 1", File.class);
        query2.setParameter("user", user);
        List<File> result = new ArrayList<>();
        result.addAll((List<File>)query.getResultList());
        result.addAll((List<File>)query2.getResultList());
        return result;
    }

    @Override
    public List<File> getSearchList(String whatSearch, User user) {
        int user_id = user.getId();
        Query query = entityManager.createQuery("SELECT f FROM File f WHERE f.user.id = :user_id AND UPPER(f.name) LIKE :whatSearch AND f.inbin <> 1", File.class);
        query.setParameter("user_id", user_id);
        query.setParameter("whatSearch", "%" + whatSearch.toUpperCase() + "%");
        return (List<File>)query.getResultList();
    }

    @Override
    public void renameFile(int[] checked_files_id, String newName) {
        Query query = entityManager.createQuery("UPDATE File f SET f.name = :newName WHERE f.id = :id");
        query.setParameter("newName", newName);
        query.setParameter("id", checked_files_id[0]);
        query.executeUpdate();
    }

    @Override
    public void changeShare(List<File> targets) {
        for (File file : targets) {
            entityManager.merge(file);
        }
    }

    @Override
    public List<File> getSharedList(User user, Integer targetFolder) {
        Query query;
        if(targetFolder == null ) {
            query = entityManager.createQuery("SELECT f FROM File f INNER JOIN f.shareFor user " +
                    "WHERE user = :user AND f.inbin <> 1 AND f.shareInFolder = false", File.class);
        } else {
            query = entityManager.createQuery("SELECT f FROM File f INNER JOIN f.shareFor user " +
                    "WHERE user = :user AND f.inbin <> 1 AND f.shareInFolder = true AND f.parentFolder.id = :targetFolder", File.class);
            query.setParameter("targetFolder", targetFolder);
        }
        query.setParameter("user", user);
        return  (List<File>)query.getResultList();
    }

    @Override
    public List<File> getAllList(User user) {
        int user_id = user.getId();
        Query query = entityManager.createQuery("SELECT f FROM File f WHERE f.user.id = :user_id", File.class);
        query.setParameter("user_id", user_id);
        return (List<File>)query.getResultList();
    }

    public List<File> getBinList(User user){
        int user_id = user.getId();
        Query query = entityManager.createQuery("SELECT f FROM File f WHERE f.user.id = :user_id AND f.inbin = 1", File.class);
        query.setParameter("user_id", user_id);
        return (List<File>)query.getResultList();
    }

    @Override
    public void changeInBin(int[] checked_files_id, boolean stateOfInBinStatus){
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE File f SET f.inbin = :stateOfInBinStatus WHERE ");
        for (int i = 0; i < checked_files_id.length; i++) {
            if(i == 0) {
                sb.append("f.id = " + checked_files_id[0]);
            }else{
                sb.append(" OR f.id = " + checked_files_id[i]);
            }
        }
        Query query = entityManager.createQuery(sb.toString());
        query.setParameter("stateOfInBinStatus", stateOfInBinStatus);
        query.executeUpdate();
    }

    @Override
    public File getFile(User user, String name, Folder parentFolder) {
        Query query;
        if(parentFolder == null) {
            query = entityManager.createQuery("SELECT f FROM File f WHERE f.name = :name " +
                    "AND f.user = :user " +
                    "AND f.parentFolder IS NULL", File.class);
            query.setParameter("name", name);
            query.setParameter("user", user);
        } else {
            query = entityManager.createQuery("SELECT f FROM File f WHERE f.name = :name " +
                    "AND f.user = :user " +
                    "AND f.parentFolder = :parentFolder", File.class);
            query.setParameter("name", name);
            query.setParameter("user", user);
            query.setParameter("parentFolder", parentFolder);
        }
        List<File> temp = (List<File>)query.getResultList();
        return temp.get(0);
    }

    @Override
    public void move_to(int[] checked_files_id, Folder target) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE File f SET f.parentFolder = :target WHERE ");
        for (int i = 0; i < checked_files_id.length; i++) {
            if(i == 0) {
                sb.append("f.id = " + checked_files_id[0]);
            }else{
                sb.append(" OR f.id = " + checked_files_id[i]);
            }
        }
        Query query = entityManager.createQuery(sb.toString());
        query.setParameter("target", target);
        query.executeUpdate();
    }
}
