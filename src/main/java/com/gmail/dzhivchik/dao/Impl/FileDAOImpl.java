package com.gmail.dzhivchik.dao.Impl;

import com.gmail.dzhivchik.dao.FileDAO;
import com.gmail.dzhivchik.domain.File;
import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.web.dto.Content;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Repository
public class FileDAOImpl implements FileDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public File getFile(int id) {
        return entityManager.createQuery("SELECT f FROM File f WHERE f.id = :id", File.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public void save(File file) {
        File searchFile = isFile(file.getName(), file.isInbin(), file.getUser(), file.getParentFolder());
        if (searchFile != null) {
            entityManager.createQuery("UPDATE File f SET f.size = :size, f.inbin = :inbin WHERE f.id = :id")
                    .setParameter("size", file.getSize())
                    .setParameter("inbin", false)
                    .setParameter("id", searchFile.getId())
                    .executeUpdate();
        } else {
            entityManager.persist(file);
        }
    }

    public File isFile(String name, boolean inbin, User user, Folder parentFolder) {
        Query query;
        if (parentFolder == null) {
            query = entityManager.createQuery("SELECT f FROM File f WHERE f.name = :name " +
                    "AND f.inbin = :inbin AND f.user = :user AND f.parentFolder IS NULL", File.class);
        } else {
            query = entityManager.createQuery("SELECT f FROM File f WHERE f.name = :name " +
                    "AND f.inbin = :inbin AND f.user = :user AND f.parentFolder = :parentFolder", File.class)
                    .setParameter("parentFolder", parentFolder);
        }
        List<File> resultList = query.setParameter("name", name)
                .setParameter("inbin", inbin)
                .setParameter("user", user)
                .getResultList();
        if (resultList.size() != 0) {
            return resultList.get(0);
        }
        return null;
    }

    @Override
    public void deleteGroup(List<Integer> checkedFilesId) {
        for (Integer id : checkedFilesId) {
            entityManager.remove(getReferenceFile(id));
        }
    }

    public File getReferenceFile(int id) {
        return entityManager.getReference(File.class, id);
    }

    @Override
    public List<Content> getList(int userId, Folder parentFolder) {
        Query query;
        if (parentFolder == null) {
            query = entityManager.createQuery("SELECT new com.gmail.dzhivchik.web.dto.Content(f.id, f.name, f.size, f.user.login, f.type, f.starred, f.shareInFolder, f.inbin) FROM File f " +
                    "WHERE f.user.id = :userId AND f.parentFolder.id IS NULL AND f.inbin <> 1", Content.class);
        } else {
            Integer parent_id = parentFolder.getId();
            query = entityManager.createQuery("SELECT new com.gmail.dzhivchik.web.dto.Content(f.id, f.name, f.size, f.user.login, f.type, f.starred, f.shareInFolder, f.inbin) FROM File f " +
                    "WHERE f.user.id = :userId AND f.parentFolder.id = :parent_id AND f.inbin <> 1", Content.class)
                    .setParameter("parent_id", parent_id);
        }
        return query.setParameter("userId", userId).getResultList();
    }

    @Override
    public List<Content> getStarredList(int userId) {
        Query query = entityManager.createQuery("SELECT new com.gmail.dzhivchik.web.dto.Content(f.id, f.name, f.size, f.user.login, f.type, f.starred, f.shareInFolder, f.inbin) FROM File f " +
                "WHERE f.user.id = :userId AND f.starred = 1 AND f.inbin <> 1", Content.class)
                .setParameter("userId", userId);

        Query query2 = entityManager.createQuery("SELECT new com.gmail.dzhivchik.web.dto.Content(f.id, f.name, f.size, f.user.login, f.type, f.starred, f.shareInFolder, f.inbin) FROM File f " +
                "INNER JOIN f.shareFor user " +
                "WHERE f.user.id = :userId AND f.inbin <> 1 AND f.starred = 1", Content.class)
                .setParameter("userId", userId);

        List<Content> result = new ArrayList<>();
        result.addAll(query.getResultList());
        result.addAll(query2.getResultList());
        return result.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<Content> getSharedList(int userId, Integer targetFolder) {
        Query query;
        if (targetFolder == null) {
            query = entityManager.createQuery("SELECT new com.gmail.dzhivchik.web.dto.Content(f.id, f.name, f.size, f.user.login, f.type, f.starred, f.shareInFolder, f.inbin) FROM File f " +
                    "INNER JOIN f.shareFor user " +
                    "WHERE user.id = :userId AND f.inbin <> 1 AND f.shareInFolder = false", Content.class);
        } else {
            query = entityManager.createQuery("SELECT new com.gmail.dzhivchik.web.dto.Content(f.id, f.name, f.size, f.user.login, f.type, f.starred, f.shareInFolder, f.inbin) FROM File f " +
                    "INNER JOIN f.shareFor user " +
                    "WHERE user.id = :userId AND f.inbin <> 1 AND f.shareInFolder = true AND f.parentFolder.id = :targetFolder", Content.class);
            query.setParameter("targetFolder", targetFolder);
        }

        return query.setParameter("userId", userId).getResultList();
    }


    @Override
    public List<Content> getSearchList(int userId, String whatSearch) {
        return entityManager.createQuery("SELECT new com.gmail.dzhivchik.web.dto.Content(f.id, f.name, f.size, f.user.login, f.type, f.starred, f.shareInFolder, f.inbin) FROM File f " +
                "WHERE f.user.id = :userId AND UPPER(f.name) LIKE :whatSearch AND f.inbin <> 1", Content.class)
                .setParameter("userId", userId)
                .setParameter("whatSearch", "%" + whatSearch.toUpperCase() + "%")
                .getResultList();
    }


    @Override
    public List<File> getListFilesById(List<Integer> listOfId) {
        return entityManager.createQuery("SELECT f FROM File f WHERE f.id in (:list)", File.class)
                .setParameter("list", listOfId)
                .getResultList();
    }

    @Override
    public void changeStar(List<Integer> checkedFilesId, boolean stateOfStar) {
        entityManager.createQuery("UPDATE File f SET f.starred = :stateOfStar WHERE f.id in (:list)")
                .setParameter("stateOfStar", stateOfStar)
                .setParameter("list", checkedFilesId)
                .executeUpdate();
    }


    @Override
    public void renameFile(int userId, int fileId, String newName) {
        entityManager.createQuery("UPDATE File f SET f.name = :newName WHERE f.id = :id AND f.user.id = :userId")
                .setParameter("newName", newName)
                .setParameter("id", fileId)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    public void changeShare(List<File> targets) {
        for (File file : targets) {
            entityManager.merge(file);
        }
    }

    public List<Content> getBinList(int userId) {
        return entityManager.createQuery("SELECT new com.gmail.dzhivchik.web.dto.Content(f.id, f.name, f.size, f.user.login, f.type, f.starred, f.shareInFolder, f.inbin) FROM File f " +
                "WHERE f.user.id = :userId AND f.inbin = 1", Content.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public void changeInBin(List<Integer> checkedFilesId, boolean stateOfInBinStatus) {
        entityManager.createQuery("UPDATE File f SET f.inbin = :stateOfInBinStatus WHERE f.id in (:list)")
                .setParameter("stateOfInBinStatus", stateOfInBinStatus)
                .setParameter("list", checkedFilesId)
                .executeUpdate();
    }

    @Override
    public void moveTo(List<Integer> checkedFilesId, Folder target) {
        entityManager.createQuery("UPDATE File f SET f.parentFolder = :target WHERE f.id in (:list)")
                .setParameter("target", target)
                .setParameter("list", checkedFilesId)
                .executeUpdate();
    }

    @Override
    public long getMemoryBusySize(int userId) {
        Object sum = entityManager.createQuery("SELECT sum(f.size) FROM File f WHERE f.user.id = :userId")
                .setParameter("userId", userId)
                .getSingleResult();
        return sum != null ? (Long) sum : 0;
    }
}
