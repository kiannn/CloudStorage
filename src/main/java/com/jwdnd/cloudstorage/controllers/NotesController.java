package com.jwdnd.cloudstorage.controllers;

import com.jwdnd.cloudstorage.Model.Notes;
import com.jwdnd.cloudstorage.services.NoteService;
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
@RequestMapping("/notes")
@SessionAttributes({"msg", "authorizedUser"})
public class NotesController {

    @Autowired
    NoteService NoteService;

    @Autowired
    HomeController HomeController;

    @Value("${base.url}") String base;
    
    @PostMapping("/add-update")
    public String addOrUpdateNotes(Notes Notes, ModelMap ModelMap) {

        String msg;

        if (Notes.getNoteId() == null) {

            String name = (String) ModelMap.getAttribute("authorizedUser");
            NoteService.addNote(Notes, name);

            msg = "note title  " + Notes.getNoteTitle() + "  is successfully created !";

        } else {

            NoteService.updateNote(Notes);
            msg = "note title  " + Notes.getNoteTitle() + "  is successfully edited !";
        }
        ModelMap.addAttribute("msg", msg);
        HomeController.showMsg = 'y';
        
        return "redirect:" + base + "home";

    }

    @GetMapping("deletenote/{noteId}")
    public String deletNotes(@PathVariable Integer noteId, ModelMap ModelMap, HttpServletRequest r) {

        String name = (String) ModelMap.getAttribute("authorizedUser");
        boolean validFileId = NoteService.isValidFileId(name, noteId);

        boolean reject = r.getHeader("Referer") == null || !r.getHeader("Referer").contains("home");

        if (validFileId && !reject) {
            String msg;
            Notes deleteNotes = NoteService.deleteNote(noteId);
            msg = "note title  " + deleteNotes.getNoteTitle() + "  is successfully deleted !";
            ModelMap.addAttribute("msg", msg);

        }
        HomeController.showMsg = 'y';
        
        return "redirect:" + base + "home";
    }

}
