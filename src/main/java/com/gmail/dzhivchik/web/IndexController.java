package com.gmail.dzhivchik.web;

import org.springframework.stereotype.Controller;
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

    @RequestMapping("/")
    public String onIndex() {
        return "index";
    }

    @RequestMapping(value = "/create_folder", method = RequestMethod.POST)
    public String createNewFolder(@RequestParam String nameOfFolder){
        File myPath = new File("/" + nameOfFolder);
        myPath.mkdirs();
        return "index";
    }

    @RequestMapping(value = "/upload_file", method = RequestMethod.POST)
    public String uploadFile(@RequestParam MultipartFile file){
        if(!file.isEmpty()){
            File convFile = new File(file.getOriginalFilename());
            System.out.println(convFile);
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
        return "index";
    }
}
