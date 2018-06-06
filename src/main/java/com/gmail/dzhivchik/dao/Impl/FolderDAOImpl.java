package com.gmail.dzhivchik.dao.Impl;

import com.gmail.dzhivchik.AuthorizedUser;
import com.gmail.dzhivchik.dao.FolderDAO;
import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by User on 27.02.2017.
 */

@Repository
public class FolderDAOImpl implements FolderDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Folder save(Folder folder) {
        Folder searchFolder = isFolder(folder.getName(), folder.isInbin(), folder.getParentFolder());
        if (searchFolder != null) {
            return entityManager.merge(folder);
        } else {
            entityManager.persist(folder);
            return folder;
        }
    }


    @Override
    public Folder isFolder(String name, boolean inbin, Folder parentFolder) {
        int user_id = AuthorizedUser.id();
        Query query;
        if (parentFolder == null) {
            query = entityManager.createQuery("SELECT f FROM Folder f WHERE f.name = :name " +
                    "AND f.inbin = :inbin AND f.user.id = :user_id AND f.parentFolder IS NULL", Folder.class);
        } else {
            query = entityManager.createQuery("SELECT f FROM Folder f WHERE f.name = :name " +
                    "AND f.inbin = :inbin AND f.user.id = :user_id AND f.parentFolder = :parentFolder", Folder.class);
            query.setParameter("parentFolder", parentFolder);
        }
        query.setParameter("name", name);
        query.setParameter("inbin", inbin);
        query.setParameter("user_id", user_id);
        List<Folder> resultList = ((List<Folder>) query.getResultList());
        return (resultList.size() != 0) ? resultList.get(0) : null;
    }

    @Override
    public List<Folder> getList(Folder parentFolder) {
        int user_id = AuthorizedUser.id();
        Query query;
        if (parentFolder == null) {
            query = entityManager.createQuery("SELECT f FROM Folder f WHERE f.user.id = :user_id AND f.parentFolder IS NULL AND f.inbin <> 1", Folder.class);
        } else {
            query = entityManager.createQuery("SELECT f FROM Folder f WHERE f.user.id = :user_id AND f.parentFolder = :parent AND f.inbin <> 1", Folder.class);
            query.setParameter("parent", parentFolder);
        }
        query.setParameter("user_id", user_id);
        return (List<Folder>) query.getResultList();
    }

    @Override
    public List<Folder> getList(Folder parentFolder, String[] exceptionFolder) {
        int[] tempList = Arrays.asList(exceptionFolder).stream().mapToInt(Integer::parseInt).toArray();
        List<Integer> list = Arrays.stream(tempList).boxed().collect(Collectors.toList());
        int user_id = AuthorizedUser.id();

        Query query;
        if (parentFolder == null) {
            query = entityManager.createQuery("SELECT c FROM Folder f WHERE f.user.id = :user_id AND f.parentFolder IS NULL AND f.inbin <> 1" +
                    " AND c.id NOT IN :list", Folder.class);
        } else {
            query = entityManager.createQuery("SELECT f FROM Folder f WHERE f.user.id = :user_id AND f.parentFolder = :parent AND f.inbin <> 1 " +
                    " AND c.id NOT IN :list", Folder.class);
            query.setParameter("parent", parentFolder);
        }
        query.setParameter("user_id", user_id);
        query.setParameter("list", list);
        return (List<Folder>) query.getResultList();
    }

    @Override
    public Folder getFolder(int id) {
        Query query;
        query = entityManager.createQuery("SELECT f FROM Folder f WHERE f.id = :id", Folder.class);
        query.setParameter("id", id);
        return (Folder)query.getSingleResult();
    }

    @Override
    public Folder getFolder(String name, Folder parentFolder) {
        int user_id = AuthorizedUser.id();
        Query query;
        if (parentFolder == null) {
            query = entityManager.createQuery("SELECT f FROM Folder f WHERE f.name = :name " +
                    "AND f.user.id = :user_id AND f.parentFolder IS NULL", Folder.class);
        } else {
            query = entityManager.createQuery("SELECT f FROM Folder f WHERE f.name = :name " +
                    "AND f.user.id = :user_id AND f.parentFolder = :parentFolder", Folder.class);
            query.setParameter("parentFolder", parentFolder);
        }
        query.setParameter("name", name);
        query.setParameter("user_id", user_id);
        return (Folder)query.getSingleResult();
    }


    @Override
    public void deleteGroup(List<Folder> folders) {
        for (Folder folder:folders) {
            entityManager.remove(folder);
        }
    }

    @Override
    public List<Folder> getListFoldersById(int[] listOfId) {
        List<Integer> list = Arrays.stream(listOfId).boxed().collect(Collectors.toList());
        Query query = entityManager.createQuery("SELECT f FROM Folder f WHERE f.id IN :list", Folder.class);
        query.setParameter("list", list);
        return (List<Folder>) query.getResultList();
    }


    @Override
    public void changeStar(int[] checked_folders_id, boolean stateOfStar) {
        List<Integer> list = Arrays.stream(checked_folders_id).boxed().collect(Collectors.toList());
        Query query = entityManager.createQuery("UPDATE Folder f SET f.starred = :stateOfStar WHERE f.id IN :list");
        query.setParameter("stateOfStar", stateOfStar);
        query.setParameter("list", list);
        query.executeUpdate();
    }

    @Override
    public List<Folder> getStarredList() {
        int user_id = AuthorizedUser.id();
        Query query = entityManager.createQuery("SELECT f FROM Folder f WHERE f.user.id = :user_id AND f.starred = 1  AND f.inbin <> 1", Folder.class);
        query.setParameter("user_id", user_id);
        Query query2 = entityManager.createQuery("SELECT f FROM Folder f INNER JOIN f.shareFor user " +
                "WHERE user.id = :user_id AND f.inbin <> 1 AND f.starred = 1", Folder.class);
        query2.setParameter("user_id", user_id);
        List<Folder> result = new ArrayList<>();
        result.addAll((List<Folder>) query.getResultList());
        result.addAll((List<Folder>) query2.getResultList());
        return result;
    }

    @Override
    public List<Folder> getSearchList(String whatSearch) {
        int user_id = AuthorizedUser.id();
        Query query = entityManager.createQuery("SELECT f FROM Folder f WHERE f.user.id = :user_id AND UPPER(f.name) LIKE :whatSearch  AND f.inbin <> 1", Folder.class);
        query.setParameter("user_id", user_id);
        query.setParameter("whatSearch", "%" + whatSearch.toUpperCase() + "%");
        return (List<Folder>) query.getResultList();
    }

    @Override
    public void renameFolder(int[] checked_folders_id, String newName) {
        int user_id = AuthorizedUser.id();
        Query query = entityManager.createQuery("UPDATE Folder f SET f.name = :newName WHERE f.id = :id AND f.user.id = :user_id");
        query.setParameter("newName", newName);
        query.setParameter("id", checked_folders_id[0]);
        query.setParameter("user_id", user_id);
        int result = query.executeUpdate();
    }

    @Override
    public void changeShare(List<Folder> targets) {
        for (Folder folder : targets) {
            entityManager.merge(folder);
        }
    }

    @Override
    public List<Folder> getSharedList(Integer targetFolder) {
        int user_id = AuthorizedUser.id();
        Query query;
        if (targetFolder == null) {
            query = entityManager.createQuery("SELECT f FROM Folder f INNER JOIN f.shareFor user " +
                    "WHERE user.id = :user_id AND f.inbin <> 1 AND f.shareInFolder = false", Folder.class);
        } else {
            query = entityManager.createQuery("SELECT f FROM Folder f INNER JOIN f.shareFor user " +
                    "WHERE user.id = :user_id AND f.inbin <> 1 AND f.shareInFolder = true AND f.parentFolder.id = :targetFolder", Folder.class);
            query.setParameter("targetFolder", targetFolder);
        }
        query.setParameter("user_id", user_id);
        return (List<Folder>) query.getResultList();
    }

    @Override
    public void changeInBin(int[] checked_folders_id, boolean stateOfInBinStatus) {
        List<Integer> list = Arrays.stream(checked_folders_id).boxed().collect(Collectors.toList());
        Query query = entityManager.createQuery("UPDATE Folder f SET f.inbin = :stateOfInBinStatus WHERE f.id IN :list");
        query.setParameter("stateOfInBinStatus", stateOfInBinStatus);
        query.setParameter("list", list);
        query.executeUpdate();
    }

    @Override
    public List<Folder> getBinList() {
        int user_id = AuthorizedUser.id();
        Query query = entityManager.createQuery("SELECT f FROM Folder f WHERE f.user.id = :user_id AND f.inbin = 1", Folder.class);
        query.setParameter("user_id", user_id);
        return (List<Folder>) query.getResultList();
    }

    @Override
    public void move_to(int[] checked_folders_id, Folder target) {
        List<Integer> list = Arrays.stream(checked_folders_id).boxed().collect(Collectors.toList());
        Query query = entityManager.createQuery("UPDATE Folder f SET f.parentFolder = :target WHERE f.id IN :list");
        query.setParameter("target", target);
        query.setParameter("list", list);
        query.executeUpdate();
    }
}
