
package com.jwdnd.cloudstorage.services;

import com.jwdnd.cloudstorage.Mappers.NotesMapper;
import com.jwdnd.cloudstorage.Mappers.UserMapper;
import com.jwdnd.cloudstorage.Model.Notes;
import com.jwdnd.cloudstorage.Model.User;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

    @Autowired
    private NotesMapper NotesMapper;
    
    @Autowired
    private UserMapper UserMapper;
     
    public List<Notes> getAllNotes(String username) {

        Integer user = UserMapper.getUserIdByUserName(username);
        List<Notes> allNotes = NotesMapper.getAllNotes(user);

        return allNotes;
    }

    public void addNote(Notes Note, String name) {
 
        Integer userByUserName = UserMapper.getUserIdByUserName(name);

        Notes build = Notes.builder()
                           .noteDescription(Note.getNoteDescription())
//                                   .replaceAll("[\\n\\r]", "")
//                                  .replaceAll("[\\s]{2,}", " ")
//                                  .replace("'", "\'")
//                                  .trim())
                           .noteTitle(Note.getNoteTitle())
                           .userId(userByUserName)
                           .build();

       NotesMapper.addNote(build); 
    }  
    
    public void updateNote(Notes Notes) {
        
//        String replaceAll = Notes.getNoteDescription().replaceAll("[\\n\\r]", "")
//                                                      .replaceAll("[\\s]{2,}", " ")
//                                                      .replace("'", "\\'")
//                                                      .trim();
//        Notes.setNoteDescription(replaceAll);
        
        NotesMapper.updateNote(Notes);
    }  

    public boolean isValidFileId(String username, Integer noteId) {

        List<Notes> allNotes = getAllNotes(username);

        List<Integer> noteIds = allNotes.stream()
                                        .map(note-> note.getNoteId())
                                        .collect(Collectors.toList());

        boolean contains = noteIds.contains(noteId);
        
        return contains;
    }

    public Notes deleteNote(Integer noteId) {
        
        Notes note = NotesMapper.getNote(noteId);
        NotesMapper.deletNote(noteId); 
         
        return note;

    }
    
}
