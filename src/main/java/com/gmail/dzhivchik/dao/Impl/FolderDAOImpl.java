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
        query.setParameter("name", name);
        query.setParameter("inbin", inbin);
        query.setParameter("user", user);
        List<Folder> resultList = ((List<Folder>) query.getResultList());
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
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public Folder getFolder(int id) {
        Query query;
        query = entityManager.createQuery("SELECT f FROM Folder f WHERE f.id = :id", Folder.class);
        query.setParameter("id", id);
        List<Folder> temp = (List<Folder>) query.getResultList();
        return temp.get(0);
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
        query.setParameter("name", name);
        query.setParameter("user", user);
        List<Folder> temp = (List<Folder>) query.getResultList();
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
        if(listOfId == null || listOfId.length == 0){
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
        Query query = entityManager.createQuery(sb.toString(), Folder.class);
        return (List<Folder>) query.getResultList();
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
        Query query = entityManager.createQuery(sb.toString());
        query.setParameter("stateOfStar", stateOfStar);
        int result = query.executeUpdate();
    }

    @Override
    public List<Content> getStarredList(User user) {
        int user_id = user.getId();
        Query query = entityManager.createQuery("SELECT new com.gmail.dzhivchik.web.dto.Content(f.id, f.name, f.user.login, f.starred, f.shareInFolder, f.inbin) FROM Folder f " +
                "WHERE f.user.id = :user_id AND f.starred = 1  AND f.inbin <> 1", Content.class);
        query.setParameter("user_id", user_id);
        Query query2 = entityManager.createQuery("SELECT new com.gmail.dzhivchik.web.dto.Content(f.id, f.name, f.user.login, f.starred, f.shareInFolder, f.inbin) FROM Folder f INNER JOIN f.shareFor user " +
                "WHERE user = :user AND f.inbin <> 1 AND f.starred = 1", Content.class);
        query2.setParameter("user", user);
        List<Content> result = new ArrayList<>();
        result.addAll(query.getResultList());
        result.addAll(query2.getResultList());
        return result;
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
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public List<Content> getSearchList(int userId, String whatSearch) {
        Query query = entityManager.createQuery("SELECT new com.gmail.dzhivchik.web.dto.Content(f.id, f.name, f.user.login, f.starred, f.shareInFolder, f.inbin) FROM Folder f " +
                "WHERE f.user.id = :userId AND UPPER(f.name) LIKE :whatSearch  AND f.inbin <> 1", Content.class);
        query.setParameter("userId", userId);
        query.setParameter("whatSearch", "%" + whatSearch.toUpperCase() + "%");
        return query.getResultList();
    }

    @Override
    public void renameFolder(int userId, int folderId, String newName) {
        Query query = entityManager.createQuery("UPDATE Folder f SET f.name = :newName WHERE f.id = :id AND f.user.id = :userId");
        query.setParameter("newName", newName);
        query.setParameter("id", folderId);
        query.setParameter("userId", userId);
        int result = query.executeUpdate();
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
        Query query = entityManager.createQuery(sb.toString());
        query.setParameter("stateOfInBinStatus", stateOfInBinStatus);
        query.executeUpdate();
    }

    @Override
    public List<Content> getBinList(int userId) {
        Query query = entityManager.createQuery("SELECT new com.gmail.dzhivchik.web.dto.Content(f.id, f.name, f.user.login, f.starred, f.shareInFolder, f.inbin) FROM Folder f " +
                "WHERE f.user.id = :userId AND f.inbin = 1", Content.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public void move_to(int[] checked_folders_id, Folder target) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE Folder f SET f.parentFolder = :target WHERE ");
        for (int i = 0; i < checked_folders_id.length; i++) {
            if (i == 0) {
                sb.append("f.id = " + checked_folders_id[0]);
            } else {
                sb.append(" OR f.id = " + checked_folders_id[i]);
            }
        }
        Query query = entityManager.createQuery(sb.toString());
        query.setParameter("target", target);
        query.executeUpdate();
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
        return (List<Folder>) query.getResultList();
    }
}
