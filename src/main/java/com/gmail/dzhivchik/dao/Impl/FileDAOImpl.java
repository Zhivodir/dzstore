package com.gmail.dzhivchik.dao.Impl;

import com.gmail.dzhivchik.AuthorizedUser;
import com.gmail.dzhivchik.dao.FileDAO;
import com.gmail.dzhivchik.domain.File;
import com.gmail.dzhivchik.domain.Folder;
import com.gmail.dzhivchik.domain.User;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
        Query query = entityManager.createQuery("SELECT f FROM File f WHERE f.id = :id", File.class);
        query.setParameter("id", id);
        return (File) query.getSingleResult();
    }

    @Override
    public void upload(File file) {
        File searchFile = isFile(file.getName(), file.isInbin(), file.getParentFolder());
        if (searchFile != null) {
            Query query = entityManager.createQuery("UPDATE File f SET f.size = :size, f.inbin = :inbin WHERE f.id = :id");
            query.setParameter("size", file.getSize());
            query.setParameter("inbin", false);
            query.setParameter("id", searchFile.getId());
            query.executeUpdate();
        } else {
            entityManager.persist(file);
            AuthorizedUser.getUserTo().changeBusySize(file.getSize());
        }

    }

    @Override
    public void uploadGroup(File[] files) {
        for (File file : files) {
            entityManager.merge(file);
        }
    }

    public File isFile(String name, boolean inbin, Folder parentFolder) {
        int user_id = AuthorizedUser.id();
        Query query = entityManager.createQuery("SELECT f FROM File f WHERE f.name = :name " +
                "AND f.inbin = :inbin AND f.user.id = :user_id AND ( f.parentFolder = :parentFolder OR ( f.parentFolder IS NULL AND :parentFolder IS NULL ) )", File.class);
        query.setParameter("parentFolder", parentFolder);
        query.setParameter("name", name);
        query.setParameter("inbin", inbin);
        query.setParameter("user_id", user_id);
        List<File> resultList = ((List<File>) query.getResultList());
        return (resultList.size() != 0) ? resultList.get(0) : null;
    }

    @Override
    public File[] deleteGroup(int[] checked_files_id) {
        File[] files = new File[checked_files_id.length];
        int num = 0;
        long addFreeMemmory = 0;
        for (Integer id : checked_files_id) {
            File file = entityManager.find(File.class, id);
            addFreeMemmory += file.getSize();
            files[num] = file;
            entityManager.remove(file);
            num++;
        }
        AuthorizedUser.getUserTo().changeBusySize(-1 * addFreeMemmory);
        return files;
    }

    @Override
    public List<File> getList(Folder parentFolder) {
        int user_id = AuthorizedUser.id();
        Query query;
        if (parentFolder == null) {
            query = entityManager.createQuery("SELECT f FROM File f WHERE f.user.id = :user_id AND f.parentFolder IS NULL AND f.inbin <> 1", File.class);
        } else {
            query = entityManager.createQuery("SELECT f FROM File f WHERE f.user.id = :user_id AND f.parentFolder = :parentFolder AND f.inbin <> 1", File.class);
            query.setParameter("parentFolder", parentFolder);
        }
        query.setParameter("user_id", user_id);
        return (List<File>) query.getResultList();
    }

    @Override
    public List<File> getListFilesById(int[] listOfId) {
        List<Integer> list = Arrays.stream(listOfId).boxed().collect(Collectors.toList());
        Query query = entityManager.createQuery("SELECT f FROM File f WHERE f.id IN :list", File.class);
        query.setParameter("list", list);
        return (List<File>) query.getResultList();
    }

    @Override
    public void changeStar(int[] checked_files_id, boolean stateOfStar) {
        List<Integer> list = Arrays.stream(checked_files_id).boxed().collect(Collectors.toList());
        Query query = entityManager.createQuery("UPDATE File f SET f.starred = :stateOfStar WHERE f.id IN :list");
        query.setParameter("stateOfStar", stateOfStar);
        query.setParameter("list", list);
        query.executeUpdate();
    }

    @Override
    public List<File> getStarredList() {
        int user_id = AuthorizedUser.id();
        Query query = entityManager.createQuery("SELECT f FROM File f WHERE f.user.id = :user_id AND f.inbin <> 1 AND f.starred = 1", File.class);
        query.setParameter("user_id", user_id);
        Query query2 = entityManager.createQuery("SELECT f FROM File f INNER JOIN f.shareFor user WHERE user.id = :user_id AND f.inbin <> 1 AND f.starred = 1", File.class);
        query2.setParameter("user_id", user_id);
        List<File> result = new ArrayList<>();
        result.addAll((List<File>) query.getResultList());
        result.addAll((List<File>) query2.getResultList());
        return result;
    }

    @Override
    public List<File> getSearchList(String whatSearch) {
        int user_id = AuthorizedUser.id();
        Query query = entityManager.createQuery("SELECT f FROM File f WHERE f.user.id = :user_id AND UPPER(f.name) LIKE :whatSearch AND f.inbin <> 1", File.class);
        query.setParameter("user_id", user_id);
        query.setParameter("whatSearch", "%" + whatSearch.toUpperCase() + "%");
        return (List<File>) query.getResultList();
    }

    @Override
    public void renameFile(int[] checked_files_id, String newName) {
        int owner_id = AuthorizedUser.id();
        Query query = entityManager.createQuery("UPDATE File f SET f.name = :newName WHERE f.id = :id AND f.user.id = :owner_id");
        query.setParameter("newName", newName);
        query.setParameter("id", checked_files_id[0]);
        query.setParameter("owner_id", owner_id);
        query.executeUpdate();
    }

    @Override
    public void changeShare(List<File> targets) {
        for (File file : targets) {
            entityManager.merge(file);
        }
    }

    @Override
    public List<File> getSharedList(Integer targetFolder) {
        int user_id = AuthorizedUser.id();
        Query query;
        if (targetFolder == null) {
            query = entityManager.createQuery("SELECT f FROM File f INNER JOIN f.shareFor user " +
                    "WHERE user.id = :user_id AND f.inbin <> 1 AND f.shareInFolder = false", File.class);
        } else {
            query = entityManager.createQuery("SELECT f FROM File f INNER JOIN f.shareFor user " +
                    "WHERE user.id = :user_id AND f.inbin <> 1 AND f.shareInFolder = true AND f.parentFolder.id = :targetFolder", File.class);
            query.setParameter("targetFolder", targetFolder);
        }
        query.setParameter("user_id", user_id);
        return (List<File>) query.getResultList();
    }

    @Override
    public List<File> getAllList() {
        int user_id = AuthorizedUser.id();
        Query query = entityManager.createQuery("SELECT f FROM File f WHERE f.user.id = :user_id", File.class);
        query.setParameter("user_id", user_id);
        return (List<File>) query.getResultList();
    }

    public List<File> getBinList() {
        int user_id = AuthorizedUser.id();
        Query query = entityManager.createQuery("SELECT f FROM File f WHERE f.user.id = :user_id AND f.inbin = 1", File.class);
        query.setParameter("user_id", user_id);
        return (List<File>) query.getResultList();
    }

    @Override
    public void changeInBin(int[] checked_files_id, boolean stateOfInBinStatus) {
        List<Integer> list = Arrays.stream(checked_files_id).boxed().collect(Collectors.toList());
        Query query = entityManager.createQuery("UPDATE File f SET f.inbin = :stateOfInBinStatus WHERE f.id IN :list");
        query.setParameter("stateOfInBinStatus", stateOfInBinStatus);
        query.setParameter("list", list);
        query.executeUpdate();
    }

    @Override
    public File getFile(String name, Folder parentFolder) {
        int user_id = AuthorizedUser.id();
        Query query;
        if (parentFolder == null) {
            query = entityManager.createQuery("SELECT f FROM File f WHERE f.name = :name " +
                    "AND f.user.id = :user_id AND f.parentFolder IS NULL", File.class);
        } else {
            query = entityManager.createQuery("SELECT f FROM File f WHERE f.name = :name " +
                    "AND f.user.id = :user_id AND f.parentFolder = :parentFolder", File.class);
            query.setParameter("parentFolder", parentFolder);
        }
        query.setParameter("name", name);
        query.setParameter("user_id", user_id);
        return (File) query.getSingleResult();
    }

    @Override
    public void move_to(int[] checked_files_id, Folder target) {
        List<Integer> list = Arrays.stream(checked_files_id).boxed().collect(Collectors.toList());
        Query query = entityManager.createQuery("UPDATE File f SET f.parentFolder = :target WHERE f.id IN :list");
        query.setParameter("target", target);
        query.setParameter("list", list);
        query.executeUpdate();
    }
}


//       Restrictions.eqOrIsNull("status", status)      instead |
//        c.add(status == null ? Restrictions.isNull("status") : Restrictions.eq("status", status));

//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<File> query = cb.createQuery(File.class);
//        Root<File> c = query.from(File.class);
//        query.select(c).where(cb.equal(c.get("user"), user),
//                              cb.equal(c.get("parentFolder"), parentFolder),
//                              cb.notEqual(c.get("inbin"), 1));
//        List<File> result = entityManager.createQuery(query).getResultList();
//        return result;