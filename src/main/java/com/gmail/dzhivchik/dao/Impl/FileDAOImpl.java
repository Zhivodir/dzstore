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
    public void upload(File file) {
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
    public List<File> getListFilesById(int[] listOfId) {
        if (listOfId == null || listOfId.length == 0) {
            return new ArrayList<File>();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT f FROM File f WHERE ");
        for (int i = 0; i < listOfId.length; i++) {
            if (i == 0) {
                sb.append("f.id = " + listOfId[0]);
            } else {
                sb.append(" OR f.id = " + listOfId[i]);
            }
        }
        return entityManager.createQuery(sb.toString(), File.class).getResultList();
    }

    @Override
    public void changeStar(int[] checked_files_id, boolean stateOfStar) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE File f SET f.starred = :stateOfStar WHERE ");
        for (int i = 0; i < checked_files_id.length; i++) {
            if (i == 0) {
                sb.append("f.id = " + checked_files_id[0]);
            } else {
                sb.append(" OR f.id = " + checked_files_id[i]);
            }
        }
        entityManager.createQuery(sb.toString())
                .setParameter("stateOfStar", stateOfStar)
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


    @Override
    public List<File> getAllList(User user) {
        int user_id = user.getId();
        return entityManager.createQuery("SELECT f FROM File f WHERE f.user.id = :user_id", File.class)
                .setParameter("user_id", user_id)
                .getResultList();
    }

    public List<Content> getBinList(int userId) {
        return entityManager.createQuery("SELECT new com.gmail.dzhivchik.web.dto.Content(f.id, f.name, f.size, f.user.login, f.type, f.starred, f.shareInFolder, f.inbin) FROM File f " +
                "WHERE f.user.id = :userId AND f.inbin = 1", Content.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public void changeInBin(int[] checked_files_id, boolean stateOfInBinStatus) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE File f SET f.inbin = :stateOfInBinStatus WHERE ");
        for (int i = 0; i < checked_files_id.length; i++) {
            if (i == 0) {
                sb.append("f.id = " + checked_files_id[0]);
            } else {
                sb.append(" OR f.id = " + checked_files_id[i]);
            }
        }
        entityManager.createQuery(sb.toString())
                .setParameter("stateOfInBinStatus", stateOfInBinStatus)
                .executeUpdate();
    }


    public File getFile(User user, String name, Folder parentFolder) {
        Query query;
        if (parentFolder == null) {
            query = entityManager.createQuery("SELECT f FROM File f WHERE f.name = :name " +
                    "AND f.user = :user " +
                    "AND f.parentFolder IS NULL", File.class);
        } else {
            query = entityManager.createQuery("SELECT f FROM File f WHERE f.name = :name " +
                    "AND f.user = :user " +
                    "AND f.parentFolder = :parentFolder", File.class);
            query.setParameter("parentFolder", parentFolder);
        }
        query.setParameter("name", name);
        query.setParameter("user", user);
        List<File> temp = (List<File>) query.getResultList();
        return temp.get(0);
    }

    @Override
    public void moveTo(int[] checked_files_id, Folder target) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE File f SET f.parentFolder = :target WHERE ");
        for (int i = 0; i < checked_files_id.length; i++) {
            if (i == 0) {
                sb.append("f.id = " + checked_files_id[0]);
            } else {
                sb.append(" OR f.id = " + checked_files_id[i]);
            }
        }
        entityManager.createQuery(sb.toString()).setParameter("target", target).executeUpdate();
    }

    @Override
    public void copyTo(int[] checked_files_id, Folder target) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE File f SET f.parentFolder = :target WHERE ");
        for (int i = 0; i < checked_files_id.length; i++) {
            if (i == 0) {
                sb.append("f.id = " + checked_files_id[0]);
            } else {
                sb.append(" OR f.id = " + checked_files_id[i]);
            }
        }
        entityManager.createQuery(sb.toString()).setParameter("target", target).executeUpdate();
    }

    @Override
    public long getMemoryBusySize(int userId) {
        return (Long) entityManager.createQuery("SELECT sum(f.size) FROM File f WHERE f.user.id = :userId")
                .setParameter("userId", userId)
                .getSingleResult();
    }
}
