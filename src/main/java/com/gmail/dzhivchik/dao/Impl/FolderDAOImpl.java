package com.gmail.dzhivchik.dao.Impl;

import com.gmail.dzhivchik.dao.FolderDAO;
import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.web.dto.Content;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
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
        return (Folder) query.setParameter("name", name)
                .setParameter("inbin", inbin)
                .setParameter("user", user)
                .getSingleResult();
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
        if (listOfId == null || listOfId.length == 0) {
            return new ArrayList<Folder>();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT f FROM Folder f WHERE ");
        for (int i = 0; i < listOfId.length; i++) {
            if (i == 0) {
                sb.append("f.id = " + listOfId[0]);
            } else {
                sb.append(" OR f.id = " + listOfId[i]);
            }
        }
        return entityManager.createQuery(sb.toString(), Folder.class).getResultList();
    }

    @Override
    public void changeStar(int[] checked_folders_id, boolean stateOfStar) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE Folder f SET f.starred = :stateOfStar WHERE ");
        for (int i = 0; i < checked_folders_id.length; i++) {
            if (i == 0) {
                sb.append("f.id = " + checked_folders_id[0]);
            } else {
                sb.append(" OR f.id = " + checked_folders_id[i]);
            }
        }
        entityManager.createQuery(sb.toString())
                .setParameter("stateOfStar", stateOfStar)
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
    public void changeInBin(int[] checked_folders_id, boolean stateOfInBinStatus) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE Folder f SET f.inbin = :stateOfInBinStatus WHERE ");
        for (int i = 0; i < checked_folders_id.length; i++) {
            if (i == 0) {
                sb.append("f.id = " + checked_folders_id[0]);
            } else {
                sb.append(" OR f.id = " + checked_folders_id[i]);
            }
        }
        entityManager.createQuery(sb.toString())
                .setParameter("stateOfInBinStatus", stateOfInBinStatus)
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
    public void moveTo(int[] checked_folders_id, Folder target) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE Folder f SET f.parentFolder = :target WHERE ");
        for (int i = 0; i < checked_folders_id.length; i++) {
            if (i == 0) {
                sb.append("f.id = " + checked_folders_id[0]);
            } else {
                sb.append(" OR f.id = " + checked_folders_id[i]);
            }
        }
        entityManager.createQuery(sb.toString()).setParameter("target", target).executeUpdate();
    }

    @Override
    public void copyTo(int[] checked_folders_id, Folder target) {

    }

    public List<Folder> getList(User user, Folder parentFolder, String[] exceptionFolder) {
        StringBuilder sb = new StringBuilder();
        if (!exceptionFolder[0].equals("")) {
            for (int i = 0; i < exceptionFolder.length; i++) {
                if (i == 0) {
                    sb.append("AND c.id <>" + exceptionFolder[0]);
                } else {
                    sb.append(" OR c.id <>" + exceptionFolder[i]);
                }
            }
        }
        Query query;
        if (parentFolder == null) {
            query = entityManager.createQuery("SELECT c FROM Folder c WHERE c.user = :user AND c.parentFolder IS NULL AND c.inbin <> 1" + sb.toString(), Folder.class);
        } else {
            query = entityManager.createQuery("SELECT c FROM Folder c WHERE c.user = :user AND c.parentFolder = :parent AND c.inbin <> 1" + sb.toString(), Folder.class);
            query.setParameter("parent", parentFolder);
        }
        query.setParameter("user", user);
        return query.getResultList();
    }
}
