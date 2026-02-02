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
        System.out.println("referer in HomeController.homePage() "+referer);
        if (referer != null && referer.contains("login")) {
            ModelMap.remove("msg");
        }

        if (referer == null || (!referer.contains("home") && !referer.contains("login"))) {

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
        
        return "home";
    }

    @ModelAttribute("authorizedUser")
    public String getAuthorizedUser(Authentication Authentication) {
        String name = Authentication.getName();
        
        return name;
    }
}
