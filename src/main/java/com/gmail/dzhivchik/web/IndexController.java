package com.gmail.dzhivchik.web;

import com.gmail.dzhivchik.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by User on 26.01.2017.
 */

@Controller
@RequestMapping("/")
public class IndexController {
//
//
//    @RequestMapping(method = RequestMethod.GET)
//    public String start(Model model){
//        return "index";
//    }

    @Autowired
    private ContentService contentService;

    @RequestMapping("/")
    public String onIndex(Model model) {
        model.addAttribute("listOfFiles", contentService.listOfFiles());
        return "index";
    }

    @RequestMapping(value = "/create_folder", method = RequestMethod.POST)
    public String createNewFolder(@RequestParam String nameOfFolder){
        File myPath = new File("/" + nameOfFolder);
        myPath.mkdirs();
        return "index";
    }

    @RequestMapping(value = "/upload_file", method = RequestMethod.POST)
    public String uploadFile(@RequestParam MultipartFile file, Model model){
        if(!file.isEmpty()){
            String fileName = file.getOriginalFilename();
            long size = file.getSize();
            String type = "test";
            com.gmail.dzhivchik.domain.File fileForDAO = new com.gmail.dzhivchik.domain.File(fileName, size, type);
            File convFile = new File(fileName);
            contentService.uploadFile(fileForDAO);
            try {
                //model.addAttribute("file", fileForDAO);
                convFile.createNewFile();
                FileOutputStream fos = new FileOutputStream("c:/DevKit/Temp/test/" + convFile);
                fos.write(file.getBytes());
                fos.close();
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        return "index";
    }

    @RequestMapping(value = "/upload_folder", method = RequestMethod.POST)
    public String uploadFile(@RequestParam MultipartFile[] files){
        System.out.println();
        if(files.length != 0){

            for (MultipartFile file : files) {
                File convFile = new File(file.getOriginalFilename());
                try {
                    convFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream("c:/DevKit/Temp/" + convFile);
                    fos.write(file.getBytes());
                    fos.close();
                }catch(FileNotFoundException e){
                    e.printStackTrace();
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        return "index";
    }
}
