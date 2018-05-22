package com.gmail.dzhivchik.web.ajax;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.service.Impl.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;



@Controller
@RequestMapping("/")
public class AjaxControllerForLoadAccount {

    @Autowired
    private ContentService contentService;

    @ResponseBody
    @RequestMapping(value = "/ajax/load_share_account_for_content", method = RequestMethod.POST)
    public String loadListOfAccount(Model model,
                           @RequestParam(value = "folders", required = false) String folder_id,
                           @RequestParam(value = "files", required = false) String file_id){
        List<User> users = null;

        if(folder_id != ""){
            users = contentService.getFolder(Integer.valueOf(folder_id.trim())).getShareFor();
        }else if(file_id != ""){
            users = contentService.getFile(Integer.valueOf(file_id.trim())).getShareFor();
        }

        StringWriter writer = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(writer, users);
        } catch (IOException e){e.printStackTrace();}
        return writer.toString();
    }
}
