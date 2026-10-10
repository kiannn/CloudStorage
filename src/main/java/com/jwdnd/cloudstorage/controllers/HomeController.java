package com.jwdnd.cloudstorage.controllers;

import com.jwdnd.cloudstorage.services.NoteService;
import com.jwdnd.cloudstorage.services.CredentialService;
import com.jwdnd.cloudstorage.Model.Credentials;
import com.jwdnd.cloudstorage.Model.FileMetadata;
import com.jwdnd.cloudstorage.Model.Files;
import com.jwdnd.cloudstorage.Model.Notes;
import com.jwdnd.cloudstorage.services.FileService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/home")
@SessionAttributes({"msg","authorizedUser"})
public class HomeController {

    @Autowired
    FileService FileService;

    @Autowired
    NoteService NoteService;

    @Autowired
    CredentialService CredentialsService;
    
    Character showMsg;


    @GetMapping
    public String homePage(ModelMap ModelMap, HttpServletRequest HttpServletRequest) {

        String referer = HttpServletRequest.getHeader("Referer");
        System.out.println("referer in HomeController.homePage() "+referer+ "\nshowMsg="+showMsg);


        /**
         * showMsg = 'i' happens when the 'referer' URL is not null and might
         * 'contain' home and 'login', but the access is still unauthorized
         */
        if (referer == null || (!referer.contains("home") && !referer.contains("login")) || Objects.equals('i', showMsg) ) {

            ModelMap.addAttribute("msg", "Direct access denied !");
        }

        if (showMsg != null && showMsg == 'n') {
            ModelMap.remove("msg");
        }

        String user = (String) ModelMap.getAttribute("authorizedUser");

        List<FileMetadata> allFiles = FileService.getAllFiles(user);
        List<Notes> allNotes = NoteService.getAllNotes(user);
        Collection<Credentials> allCredentials = CredentialsService.getAllCredentials(user);

        Map<String, Collection<? extends Object>> attributes = Map.of("userFiles", allFiles,
                                                                      "userNotes", allNotes,
                                                                      "userCredentials", allCredentials);

        ModelMap.addAllAttributes(attributes);

        showMsg = 'n'; 
        /**
         * The feedback message (success and error, etc) should be disappeared on
         * Home page reload. 'showMsg = 'n'' will remove 'msg' attribute from
         * the model by calling: ModelMap.remove("msg");
         */
        
        return "home";
    }

    @ModelAttribute("authorizedUser")
    public String getAuthorizedUser(Authentication Authentication) {
        String name = Authentication.getName();
        
        return name;
    }
}
