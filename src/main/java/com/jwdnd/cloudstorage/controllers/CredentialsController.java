package com.jwdnd.cloudstorage.controllers;

import com.jwdnd.cloudstorage.Model.Credentials;
import com.jwdnd.cloudstorage.services.CredentialService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/credentials")
@SessionAttributes({"msg", "authorizedUser"})
public class CredentialsController {

    @Autowired
    CredentialService CredentialsService;

    @Autowired
    HomeController HomeController;

    @PostMapping("add-update")
    public String addOrUpdateCredentials(Credentials Credentials, ModelMap ModelMap, @Value("${base.url}") String base) {

        String msg;

        if (Credentials.getCredentialId() == null) {

            String name = (String) ModelMap.getAttribute("authorizedUser");
            CredentialsService.addCredential(Credentials, name);

            msg = "credential is successfully created !";

        } else {

            CredentialsService.updateCredentials(Credentials);
            msg = "credential is successfully edited !";
        }

        ModelMap.addAttribute("msg", msg);
        HomeController.showMsg = 'y';
        
        return "redirect:" + base + "home";

    }

    @GetMapping("deletecredentials/{credentialId}")
    public String deletCredentials(@PathVariable Integer credentialId, ModelMap ModelMap, HttpServletRequest r, @Value("${base.url}") String base) {

        String name = (String) ModelMap.getAttribute("authorizedUser");

        boolean validCredentialId = CredentialsService.isValidCredId(name, credentialId);
        boolean reject = r.getHeader("Referer") == null || !r.getHeader("Referer").contains("home");
        String msg;

        if (validCredentialId && !reject) {

            CredentialsService.deleteCredentials(credentialId);
            msg = "credential is successfully deleted !";
            ModelMap.addAttribute("msg", msg);

        }

        HomeController.showMsg = 'y';

        return "redirect:" + base + "home";
    }
}
