package com.gmail.dzhivchik.dao.Impl;

import com.gmail.dzhivchik.dao.FolderDAO;
import com.gmail.dzhivchik.domain.File;
import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.web.dto.Content;
import com.gmail.dzhivchik.web.dto.PathElement;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Repository
public class FolderDAOImpl implements FolderDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Folder save(Folder folder) {
        Folder searchFolder = isFolder(folder.getName(), folder.isInbin(), folder.getUser(), folder.getParentFolder());
        if (searchFolder != null) {
            return entityManager.merge(folder);
        } else {
            entityManager.persist(folder);
            return folder;
        }
    }

    @Override
    public Folder isFolder(String name, boolean inbin, User user, Folder parentFolder) {
        Query query;
        if (parentFolder == null) {
            query = entityManager.createQuery("SELECT f FROM Folder f WHERE f.name = :name " +
                    "AND f.inbin = :inbin AND f.user = :user AND f.parentFolder IS NULL", Folder.class);
        } else {
            query = entityManager.createQuery("SELECT f FROM Folder f WHERE f.name = :name " +
                    "AND f.inbin = :inbin AND f.user = :user AND f.parentFolder = :parentFolder", Folder.class);
            query.setParameter("parentFolder", parentFolder);
        }
        List<Folder> resultList = query.setParameter("name", name)
                .setParameter("inbin", inbin)
                .setParameter("user", user).getResultList();
        if (resultList.size() != 0) {
            return resultList.get(0);
        }
        return null;
    }

    @Override
    public List<Content> getList(int userId, Folder parentFolder) {
        Query query;
        if (parentFolder == null) {
            query = entityManager.createQuery("SELECT new com.gmail.dzhivchik.web.dto.Content(c.id, c.name, c.user.login, c.starred, c.shareInFolder, c.inbin) FROM Folder c " +
                    "WHERE c.user.id = :userId AND c.parentFolder IS NULL AND c.inbin <> 1", Content.class);
        } else {
            query = entityManager.createQuery("SELECT new com.gmail.dzhivchik.web.dto.Content(c.id, c.name, c.user.login, c.starred, c.shareInFolder, c.inbin) FROM Folder c " +
                    "WHERE c.user.id = :userId AND c.parentFolder = :parent AND c.inbin <> 1", Content.class);
            query.setParameter("parent", parentFolder);
        }
        return query.setParameter("userId", userId).getResultList();
    }

    @Override
    public Folder getFolder(int id) {
        return entityManager.createQuery("SELECT f FROM Folder f WHERE f.id = :id", Folder.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public Folder getFolder(User user, String name, Folder parentFolder) {
        Query query;
        if (parentFolder == null) {
            query = entityManager.createQuery("SELECT f FROM Folder f WHERE f.name = :name " +
                    "AND f.user = :user " +
                    "AND f.parentFolder IS NULL", Folder.class);
        } else {
            query = entityManager.createQuery("SELECT f FROM Folder f WHERE f.name = :name " +
                    "AND f.user = :user " +
                    "AND f.parentFolder = :parentFolder", Folder.class);
            query.setParameter("parentFolder", parentFolder);
        }
        return (Folder) query.setParameter("name", name)
                .setParameter("user", user)
                .getSingleResult();
    }

    @Override
    public Folder getReferenceFolder(int id) {
        return entityManager.getReference(Folder.class, id);
    }

    @Override
    public Folder[] deleteGroup(List<Integer> checkedFoldersId) {
        Folder[] folders = new Folder[checkedFoldersId.size()];
        int num = 0;
        for (Integer id : checkedFoldersId) {
            Folder folder = entityManager.find(Folder.class, id);
            folders[num] = folder;
            entityManager.remove(folder);
            num++;
        }
        return folders;
    }

    @Override
    public List<Folder> getListFoldersById(List<Integer> listOfId) {
        if (listOfId == null || listOfId.size() == 0) {
            return new ArrayList<Folder>();
        }

        return entityManager.createQuery("SELECT f FROM Folder f WHERE f.id in (:list)", Folder.class)
                .setParameter("list", listOfId)
                .getResultList();
    }

    @Override
    public void changeStar(List<Integer> checkedFoldersId, boolean stateOfStar) {
        entityManager.createQuery("UPDATE Folder f SET f.starred = :stateOfStar WHERE f.id in (:list)")
                .setParameter("stateOfStar", stateOfStar)
                .setParameter("list", checkedFoldersId)
                .executeUpdate();
    }

    @Override
    public List<Content> getStarredList(int userId) {
        Query query = entityManager.createQuery("SELECT new com.gmail.dzhivchik.web.dto.Content(f.id, f.name, f.user.login, f.starred, f.shareInFolder, f.inbin) FROM Folder f " +
                "WHERE f.user.id = :userId AND f.starred = 1  AND f.inbin <> 1", Content.class);
        query.setParameter("userId", userId);

        Query query2 = entityManager.createQuery("SELECT new com.gmail.dzhivchik.web.dto.Content(f.id, f.name, f.user.login, f.starred, f.shareInFolder, f.inbin) FROM Folder f " +
                "INNER JOIN f.shareFor user " +
                "WHERE f.user.id = :userId AND f.starred = 1 AND f.inbin <> 1", Content.class);
        query2.setParameter("userId", userId);
        List<Content> result = new ArrayList<>();
        result.addAll(query.getResultList());
        result.addAll(query2.getResultList());
        return result.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<Content> getSharedList(int userId, Integer targetFolder) {
        Query query;
        if (targetFolder == null) {
            query = entityManager.createQuery("SELECT new com.gmail.dzhivchik.web.dto.Content(f.id, f.name, f.user.login, f.starred, f.shareInFolder, f.inbin) FROM Folder f " +
                    "INNER JOIN f.shareFor user " +
                    "WHERE user.id = :userId AND f.inbin <> 1 AND f.shareInFolder = false", Content.class);
        } else {
            query = entityManager.createQuery("SELECT new com.gmail.dzhivchik.web.dto.Content(f.id, f.name, f.user.login, f.starred, f.shareInFolder, f.inbin) FROM Folder f " +
                    "INNER JOIN f.shareFor user " +
                    "WHERE user.id = :userId AND f.inbin <> 1 AND f.shareInFolder = true AND f.parentFolder.id = :targetFolder", Content.class);
            query.setParameter("targetFolder", targetFolder);
        }
        return query.setParameter("userId", userId).getResultList();
    }

    @Override
    public List<Content> getSearchList(int userId, String whatSearch) {
        return entityManager.createQuery("SELECT new com.gmail.dzhivchik.web.dto.Content(f.id, f.name, f.user.login, f.starred, f.shareInFolder, f.inbin) FROM Folder f " +
                "WHERE f.user.id = :userId AND UPPER(f.name) LIKE :whatSearch  AND f.inbin <> 1", Content.class)
                .setParameter("userId", userId)
                .setParameter("whatSearch", "%" + whatSearch.toUpperCase() + "%")
                .getResultList();
    }

    @Override
    public void renameFolder(int userId, int folderId, String newName) {
        entityManager.createQuery("UPDATE Folder f SET f.name = :newName WHERE f.id = :id AND f.user.id = :userId")
                .setParameter("newName", newName)
                .setParameter("id", folderId)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    public void changeShare(List<Folder> targets) {
        for (Folder folder : targets) {
            entityManager.merge(folder);
        }
    }

    @Override
    public void changeInBin(List<Integer> checkedFoldersId, boolean stateOfInBinStatus) {
        entityManager.createQuery("UPDATE Folder f SET f.inbin = :stateOfInBinStatus WHERE f.id in (:list)")
                .setParameter("stateOfInBinStatus", stateOfInBinStatus)
                .setParameter("list", checkedFoldersId)
                .executeUpdate();
    }

    @Override
    public List<Content> getBinList(int userId) {
        return entityManager.createQuery("SELECT new com.gmail.dzhivchik.web.dto.Content(f.id, f.name, f.user.login, f.starred, f.shareInFolder, f.inbin) FROM Folder f " +
                "WHERE f.user.id = :userId AND f.inbin = 1", Content.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public void moveTo(List<Integer> checkedFoldersId, Folder target) {
        entityManager.createQuery("UPDATE Folder f SET f.parentFolder = :target WHERE f.id in (:list)")
                .setParameter("target", target)
                .setParameter("list", checkedFoldersId)
                .executeUpdate();
    }

    @Override
    public List<PathElement> getPathElements(List<String> pathes, int userId) {
        return entityManager.createQuery("SELECT new com.gmail.dzhivchik.web.dto.PathElement(f.name, f.id) FROM Folder f WHERE f.user.id =:userId AND f.path in (:pathes)")
                .setParameter("userId", userId)
                .setParameter("pathes", pathes)
                .getResultList();
    }
}
