package com.gmail.dzhivchik.service;

import com.gmail.dzhivchik.dao.FileDAO;
import com.gmail.dzhivchik.domain.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by User on 31.01.2017.
 */

@Service
public class ContentService {
    @Autowired
    private FileDAO fileDAO;

    @Transactional
    public  void uploadFile(File file){
        fileDAO.upload(file);
    }

    @Transactional(readOnly=true)
    public List<File> listOfFiles() {
        return fileDAO.getList();
    }
}
