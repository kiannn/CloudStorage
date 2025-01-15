package com.jwdnd.cloudstorage.controllers;

import com.jwdnd.cloudstorage.Model.User;
import com.jwdnd.cloudstorage.services.UserService;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class signUpController {

    
    @Autowired
    UserService UserService;

    @GetMapping
    public String signUpPage(User user) {

        return "signup";

    }

    @PostMapping
    public String signUpProcess(User User, ModelMap ModelMap) {
     
        boolean userExist = UserService.userExist(User.getUserName());

        if (!userExist) {

            ModelMap.addAttribute("sigUpErrorUsername", "'" + User.getUserName() + "'");

            return "signup";
        }
        
        if(User.getPassword().length()<8){
            
            ModelMap.addAttribute("sigUpErrorPass", "Password must be at least 8 character.");

            return "signup";
        
        }
        if(Pattern.matches(".*[\\s]+.*", User.getPassword())){
            
            ModelMap.addAttribute("sigUpErrorPass", "Passwords can not contain spaces");

            return "signup";
        
        }
        
        if(User.getFirstName().isBlank() || User.getLastName().isBlank() || User.getUserName().isBlank()){
            
            ModelMap.addAttribute("sigUpErrorInvalidValue", "Empty or only white spaces value is not valid");

            return "signup";
        
        }

        UserService.createNewUSer(User);

        return "redirect:login?sigUpSuccess";

    }

}